import os
import re
import sys
import subprocess

from collections import namedtuple


TIMEOUT = 10 * 40


MavenResults = namedtuple('MavenResults', ['source_files', 'classes_dir'])


class Maven:

    def __init__(self, java, maven_home=None, skip_compile=False):
        self.maven_home = maven_home
        self.java = java
        self.skip_compile = skip_compile

        self._set_home()
        self._check()

    def _check(self):
        try:
            self._version()
        except FileNotFoundError:
            raise SystemExit()

    def _set_home(self):
        if not self.maven_home:
            if 'M2_HOME' in os.environ and os.environ['M2_HOME']:
                self.maven_home = os.environ['M2_HOME']
            elif 'MAVEN_HOME' in os.environ and os.environ['MAVEN_HOME']:
                self.maven_home = os.environ['MAVEN_HOME']
            elif 'MVN_HOME' in os.environ and os.environ['MVN_HOME']:
                self.maven_home = os.environ['MVN_HOME']
            else:
                print("MAVEN_HOME undefined.", file=sys.stderr)
                raise SystemExit()

    def _version(self):
        return self.simple_exec('-version')

    def simple_exec(self, *args):
        return self._exec_mvn(None, self.java.get_env(), TIMEOUT, *args)

    def exec(self, timeout, *args):
        return self._exec_mvn(None, self.java.get_env(), timeout, *args)

    def _exec_mvn(self, cwd, env, timeout, *args):
        try:
            command = [os.path.join(self.maven_home,
                                    os.sep.join(['bin', 'mvn']))]
            command = command + list(args)

            return subprocess.check_output(command, cwd=cwd, env=env,
                                           timeout=timeout,
                                           stderr=subprocess.STDOUT)
        except subprocess.CalledProcessError as e:
            print('MAVEN: call process error with arguments {0}.'.format(args),
                  file=sys.stderr)            
            return e.output
        except subprocess.TimeoutExpired as e:
            print('MAVEN: timeout with arguments {0}.'.format(args),
                  file=sys.stderr)
            raise e
        except FileNotFoundError as e:
            print('MAVEN: not found.', file=sys.stderr)
            raise e

    def clean(self, project_dir, timeout):
        return self._exec_mvn(project_dir, self.java.get_env(), timeout,
                              'clean').decode('unicode_escape')

    def compile(self, project_dir, timeout=TIMEOUT, clean=False):
        if clean:            
            self.clean(project_dir, TIMEOUT)
        return self.extract_results(
            self._exec_mvn(project_dir, self.java.get_env(), timeout,
                           'compile').decode('unicode_escape')
        )

    def test_compile(self, project_dir, timeout=TIMEOUT, clean=False):
        if clean:            
            self.clean(project_dir, TIMEOUT)
        extraction_result = self.extract_results(
            self._exec_mvn(project_dir, self.java.get_env(), timeout,
                           'test-compile').decode('unicode_escape'), True)
        if extraction_result is None: #means there is no test directory
            return MavenResults(None, None)
        else:
            return extraction_result    


    def test(self, project_dir, sut_class, coverage_output_dir='target/coverage-report', timeout=TIMEOUT, clean=False):
        if clean:            
            self.clean(project_dir, TIMEOUT)
        
        return self.extract_test_results(
            self._exec_mvn(project_dir, self.java.get_env(), timeout,
                           'surefire:test', '-Dmaven.test.failure.ignore=true', '-Dcoverage-classes=' + sut_class,
                            '-Dcoverage-outputDir=' + coverage_output_dir).decode('unicode_escape')
        )

    # def test_with_coverage(self, project_dir, cov_output_dir, timeout=TIMEOUT, clean=False):
    #     if clean:
    #         print("Cleaning up project with maven...")
    #         self.clean(project_dir, TIMEOUT)

    #     print("Testing the project with maven...")
    #     return self.extract_test_results(
    #         self._exec_mvn(project_dir, self.java.get_env(), timeout,
    #                        'test -Dcoverage-outputDir=' + cov_output_dir).decode('unicode_escape')
    #     )    

    @staticmethod
    def extract_results(output, is_test_code=False):
        output_result = re.findall('Compiling [0-9]* source files? to .*\n', output)
        if output_result:
            if(is_test_code):
                output_result = [result for result in output_result if "test" in result]            
            output_result = output_result[0].replace('\n', '').split()
            return MavenResults(int(output_result[1]), output_result[-1])
        return None

    @staticmethod
    def extract_test_results(output, is_test_code=False):
        failed_tests = []
        num_tests = 0
        num_failures = 0
        num_errors = 0
        num_skipped = 0
        output_by_line = output.split("\n")        
        is_results = False
        for line in output_by_line:
            if("Results :" in line):
                is_results = True
            elif("Finished at:" in line):
                is_results = False
            
            if(is_results and "expected:" in line):
                failed_tests.append(line.replace('\n', '').strip())        
            elif(is_results and "Failed tests:" in line):
                failed_tests.append(line.replace('\n', '').strip())            
            elif(is_results and "_ESTest" in line):
                failed_tests.append(line.replace('\n', '').strip())                
            elif(is_results and "[ERROR] There was a timeout or other error in the fork" in line):
                raise Exception("[ERROR] There was a timeout or other error in the fork")
            elif(is_results and "Tests run:" in line):  
                temp = line.split(",") 
                num_tests = int(temp[0][temp[0].find(":")+1:].strip())
                num_failures = int(temp[1][temp[1].find(":")+1:].strip())
                num_errors = int(temp[2][temp[2].find(":")+1:].strip())
                num_skipped = int(temp[3][temp[3].find(":")+1:].strip())

        return (num_tests, num_failures, num_errors, num_skipped, failed_tests)
