import os
import re
import time
import subprocess

from collections import namedtuple

from nimrod.tools.bin import JUNIT, HAMCREST, JMOCKIT, EVOSUITE_RUNTIME, COMMONS_LANG_24
from nimrod.utils import generate_classpath, package_to_dir
from nimrod.mutant import Mutant
from nimrod.coverage import JMockit, Coverage

TIMEOUT = 40


JUnitResult = namedtuple('JUnitResult', ['ok_tests', 'fail_tests', 
                                         'fail_test_set', 'run_time',
                                         'coverage', 'timeout'])


class JUnit:

    def __init__(self, java, classpath):
        self.java = java
        self.classpath = classpath
        
    def exec(self, suite_dir, suite_classes_dir, sut_class, test_class,
             timeout=TIMEOUT):
        classpath = generate_classpath([
            JMOCKIT, JUNIT, HAMCREST, EVOSUITE_RUNTIME,
            suite_classes_dir,
            self.classpath
        ])

        return self._exec(suite_dir, sut_class, test_class, classpath, '.',
                          timeout)

    def exec_with_mutant(self, suite_dir, suite_classes_dir, sut_class,
                         test_class, mutant, timeout=TIMEOUT):
        classpath = generate_classpath([
            JMOCKIT, JUNIT, HAMCREST, EVOSUITE_RUNTIME,
            suite_classes_dir,
            mutant.dir,
            self.classpath
        ])

        return self._exec(suite_dir, sut_class, test_class, classpath,
                          mutant.dir, timeout)

    def _exec(self, suite_dir, sut_class, test_class, classpath,
              cov_src_dirs='.', timeout=TIMEOUT):

        params = (
            '-javaagent:'+str(JMOCKIT), 
            '-classpath', classpath,
            '-Dcoverage-classes=' + sut_class,
            '-Dcoverage-output=html',
            '-Dcoverage-metrics=line',
            '-Dcoverage-srcDirs=' + cov_src_dirs,
            'org.junit.runner.JUnitCore', test_class
        )

        start = time.time()
        try:            
            output = self.java.exec_java(suite_dir, self.java.get_env(),
                                         timeout, *params)            
            return JUnitResult(
                *JUnit._extract_results_ok(output.decode('unicode_escape')),
                time.time() - start, None, False
            )
        except subprocess.CalledProcessError as e:
            return JUnitResult(
                *JUnit._extract_results(e.output.decode('unicode_escape')),
                time.time() - start, None, False
            )
        except subprocess.TimeoutExpired as e:
            elapsed_time = time.time() - start
            print("# [WARNING] Run JUnit tests timed out. {0} seconds".format(
                elapsed_time))            
            return JUnitResult(0, 0, set(), 0, None, True)

    @staticmethod
    def _extract_results_ok(output):
        result = re.findall(r'OK \([0-9]* tests?\)', output)
        if len(result) > 0:
            result = result[0].replace('(', '')
            r = [int(s) for s in result.split() if s.isdigit()]
            return r[0], 0, set()

        return 0, 0, set()

    @staticmethod
    def _extract_results(output):
        if len(re.findall(r'initializationError', output)) == 0:
            result = re.findall(r'Tests run: [0-9]*,[ ]{2}Failures: [0-9]*',
                                output)
            if len(result) > 0:
                result = result[0].replace(',', ' ')
                r = [int(s) for s in result.split() if s.isdigit()]
                return r[0], r[1], JUnit._extract_test_id(output)

        return 0, 0, set()

    @staticmethod
    def _extract_test_id(output):
        tests_fail = set()
        for test in re.findall(r'\.test[0-9]+\([A-Za-z0-9_]+\.java:[0-9]+\)',
                               output):
            i = re.findall('\d+', test)
            file = re.findall(r'\(.+?(?=\.)', test)[0][1:]
            test_case = re.findall(r'\..+?(?=\()', test)[0][1:]

            if len(i) > 0:
                tests_fail.add('{0}#{1}'.format(file, test_case, int(i[-1])))
            else:
                print("*** ERROR: Error in regex of junit output.")

        return tests_fail

    def run_with_mutant(self, suite, sut_class, mutant, cov_original=False,
                        original_dir=None):
        ok_tests = 0
        fail_tests = 0
        fail_test_set = set()
        run_time = 0
        call_points = set()
        test_cases = set()
        class_coverage = dict()
        executions = 0
        timeout = False

        for test_class in suite.test_classes:

            result = self.exec_with_mutant(suite.suite_dir,
                                           suite.suite_classes_dir, sut_class,
                                           test_class, mutant)
            ok_tests += result.ok_tests
            fail_tests += result.fail_tests
            fail_test_set = fail_test_set.union(result.fail_test_set)
            run_time += run_time
            timeout = timeout or result.timeout

            if not timeout: #Se nao ocorreu timeout...
                if cov_original:
                    if original_dir is None:
                        original_dir = os.path.join(
                            mutant.dir[:mutant.dir.rfind(os.sep)], 'ORIGINAL')
                    if os.path.exists(original_dir):                        
                        self.java.compile_all(self.classpath, original_dir)
                        result_original = self.exec_with_mutant(suite.suite_dir,
                                              suite.suite_classes_dir,
                                              sut_class, test_class,
                                              self.get_original(original_dir))
                        if result_original.fail_tests > 0:
                            #Se os testes tb falham no original e são os mesmos teste que falahm no mutante. 
                            #Logo devem ser Equivalentes.
                            if result_original.fail_test_set == result.fail_test_set:
                                fail_tests = 0
                                fail_test_set = set()
                    else:
                        print('[WARNING] ORIGINAL class not found in {0}, using'
                              ' mutant in coverage.'.format(original_dir))

                cov = self.run_coverage(suite.suite_dir, sut_class,
                                        mutant.line_number)
                if cov:
                    call_points = call_points.union(cov.call_points)
                    test_cases = test_cases.union(cov.test_cases)
                    executions += cov.executions
                    class_coverage[test_class] = cov.class_coverage
            else:
                # Se o teste teve timeout no mutante, executamos contra o original para averiguar se nao ocorre timeout
                r = self.exec(suite.suite_dir, suite.suite_classes_dir,
                              sut_class, test_class)                
                if r.timeout: #Se ocorre timeout tb no original, entao retorna, indicando q nao houve diferença na execução.
                    return None

        return JUnitResult(ok_tests, fail_tests, fail_test_set, run_time,
                           Coverage(call_points, test_cases, executions,class_coverage),
                           timeout)


    def run_with_original(self, suite, sut_class, original, cov_original=False,
                        original_dir=None):
        ok_tests = 0
        fail_tests = 0
        fail_test_set = set()
        run_time = 0
        call_points = set()
        test_cases = set()
        class_coverage = dict()
        executions = 0
        timeout = False

        #Navigate through all test classes generated...
        for test_class in suite.test_classes:
            #Execute the test suite
            result = self.exec(suite.suite_dir,suite.suite_classes_dir, 
                                sut_class,test_class)
            #Increment the results (in case of previous tests executed)
            ok_tests += result.ok_tests
            fail_tests += result.fail_tests
            fail_test_set = fail_test_set.union(result.fail_test_set)
            run_time += run_time
            timeout = timeout or result.timeout

            cov = self.run_coverage(suite.suite_dir, sut_class,
                                    original.line_number)
            if cov:
                call_points = call_points.union(cov.call_points)
                test_cases = test_cases.union(cov.test_cases)
                executions += cov.executions
                class_coverage[test_class] = cov.class_coverage
            

        return JUnitResult(ok_tests, fail_tests, fail_test_set, run_time,
                           Coverage(call_points, test_cases, executions,class_coverage),
                           timeout)                       

    @staticmethod
    def get_original(original_dir):
        return Mutant(mid='ORIGINAL', operator=None, line_number=None,
                      method=None, transformation=None, dir=original_dir)

    @staticmethod
    def run_coverage(suite_dir, sut_class, mutation_line):
        jmockit = JMockit(suite_dir, sut_class)
        return jmockit.coverage()







