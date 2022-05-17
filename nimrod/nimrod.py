import os
from os.path import isdir
import shutil
import time
import threading
import tempfile

from time import time
from nimrod.tools.randoop import Randoop
from nimrod.tools.mujava import MuJava
from nimrod.tools.major import Major
from nimrod.mutant import Mutant
from nimrod.tools.safira import Safira
from nimrod.tools.tce_detector import Tce
from nimrod.tools.junit import JUnit, JUnitResult
from nimrod.coverage import JMockit, Coverage
from nimrod.tools.evosuite import Evosuite
from nimrod.utils import package_to_dir, copy_dir, remove_dir, get_java_files

from collections import namedtuple


OUTPUT_DIR = 'nimrod_output'


NimrodResult = namedtuple('NimrodResult', ['mutant', 
                                            'mutant_operator',
                                            'mutant_line_number',
                                            'mutant_method',
                                            'mutant_transformation',
                                            'tce_equivalent',
                                            'killed_by_auto_test', 
                                            'num_killer_test_cases', 
                                            'killer_test_cases', 
                                            'timeout',
                                            'executions', 
                                            'equal_line_coverage', 
                                            'num_different_lines'])


class Nimrod:

    def __init__(self, java, maven):
        self.java = java
        self.maven = maven        
        self.suite_evosuite_diff = None
        self.suite_evosuite = None
        self.suite_randoop = None
        #New fields
        self.junit = None
        self.tce = None
        self.testsuite_combined = None
        self.testsuite_killer = None
        self.mutants_surviving = None


    def _print_output(self, nimrod_results):
        print("")
        print("")
        print("********************************************* RESULTS *********************************")
        print("")
        print("")
        print("mutant|tce_equivalent|killed_by_auto_test|num_test_cases|test_cases|timeout|executions|equal_line_coverage|num_different_lines")
        for mid in nimrod_results:            
            print("{0}|{1}|{2}|{3}|{4}|{5}|{6}|{7}|{8}".format(nimrod_results[mid][0],nimrod_results[mid][1],nimrod_results[mid][2],nimrod_results[mid][3],nimrod_results[mid][4],nimrod_results[mid][5],nimrod_results[mid][6],nimrod_results[mid][7],nimrod_results[mid][8]))           


    def _check_output_dir(self, output_dir):
        output_dir = os.path.abspath(os.path.join('.', output_dir))
        if os.path.exists(output_dir):
            shutil.rmtree(output_dir)
        os.makedirs(output_dir)

        return output_dir    


    def _try_tce(self, project_dir, classes_dir, mutants_dir, sut_class):
        try:      
            #Create a temp directory to execute TCE   
            tce_temp_dir = tempfile.TemporaryDirectory()            
            self.tce = Tce(self.java, tce_temp_dir.name + "/ted", tce_temp_dir.name + "/result")                  
            print('Creating TCE structure in the temp directory: {0}'.format(tce_temp_dir.name))
            self.tce.setup_tce_structure(project_dir, mutants_dir, tce_temp_dir.name, sut_class)            
            start_time = time()                
            print ('TCE optimisation phase started')
            self.tce.optimize()
            print ('Finished optimisation phase: ', round((time() - start_time), 2), ' secs')             
            #Save TCE equivalents and duplicates
            print ('TCE equivalent detection via Soot started')
            start_time = time()                
            eqvs = self.tce.equivalents()
            print ('Finished equivalent detection: ', round((time() - start_time), 2), ' secs')
            print('Equivalents by TCE: {0}'.format(eqvs))
            # Deteccao de duplicados pronta. Mas sem uso por enquanto.
            # print ('TCE duplicate detection via Soot started')
            # start_time = time()                
            dups = self.tce.duplicates() 
            print('Duplicates by TCE: {0}'.format(dups))
            # dups = []
            # print ('Finished duplicate detection: ', round((time() - start_time), 2), ' secs')        
            #Delete the temp directory
            tce_temp_dir.cleanup()
            return (eqvs, dups)
        except Exception as e:
            print("**ERROR: TCE NOT EXECUTED - REASON: " + str(e))


    def _del_mutants(self, mutants_dir, mutants):
        for mutant in mutants:
            print(mutants_dir + "/" + mutant)

    def _gen_automatic_tests(self, classes_dir, mutants_dir, nimrod_output_dir, sut_class, randoop_params, evosuite_params):        
        #Set up the test suite dir for the mutant...
        auto_generated_tests_dir = os.path.join(nimrod_output_dir, 'suites')

        thread_evosuite = threading.Thread(target=self._gen_evosuite, args=(classes_dir, evosuite_params, sut_class, auto_generated_tests_dir))
        thread_randoop = threading.Thread(target=self._gen_randoop, args=(classes_dir, randoop_params, sut_class, auto_generated_tests_dir))

        print('Generating automatic tests...')
        thread_evosuite.start()
        thread_randoop.start()

        # Wait the generation of the Suite in the thread
        thread_evosuite.join()
        # Wait the generation of the Suite in the thread
        thread_randoop.join()
        
        return auto_generated_tests_dir
    
    def _gen_evosuite_diff(self, sut_class, evosuite_diff_params, orig_classes_dir, auto_generated_tests_dir, mutant):

        evosuite = Evosuite(
            java=self.java,
            classpath=orig_classes_dir,
            tests_src=auto_generated_tests_dir,
            sut_class=sut_class,
            params=evosuite_diff_params
        )
        try:
            self.suite_evosuite_diff = evosuite.generate_differential(mutant.dir)        
        except Exception as e:
            print("**WARNING: EvoSuite Diff Test Creation - REASON: " + str(e))

        return self.suite_evosuite_diff

    def _gen_evosuite(self, classes_dir, evosuite_params, sut_class, tests_src):
        evosuite = Evosuite(
            java=self.java,
            classpath=classes_dir,
            tests_src=tests_src,
            sut_class=sut_class,
            params=evosuite_params
        )
        try:
            self.suite_evosuite = evosuite.generate()
        except Exception as e:
            print("**WARNING: EvoSuite Test Creation - REASON: " + str(e))

        return self.suite_evosuite

    def _gen_randoop(self, classes_dir, randoop_params, sut_class, tests_src):
        randoop = Randoop(
            java=self.java,
            classpath=classes_dir,
            tests_src=tests_src,
            sut_class=sut_class,
            params=randoop_params
        )
        # try:   
        #     self.suite_randoop = randoop.generate()
        # except Exception as e:
        #     print("**WARNING: Randoop Test Generation - REASON: " + str(e))    
        # if "Bisect" in sut_class:
        #     self.suite_randoop = randoop.generate()
        # else:
        #     self.suite_randoop = randoop.generate_with_impact_analysis(safira)
        #     if "Simulator" in sut_class:
        #         import distutils.dir_util
        #         distutils.dir_util.copy_tree("./config/", randoop.suite_dir + "/config/")
        return self.suite_randoop


    def _exec_junit(self, test_classes_dir, sut_class, mutant): 
        try:       
            return self.junit.run_with_mutant(test_classes_dir, sut_class, mutant)
        except Exception as e:
            print("**ERROR: JUnit Test Execution. Reason: {0}".format(str(e)))
            return None     


    def _combine_tests(self, orig_test_classes_dir, auto_generated_tests_dir):
        for test_dir in os.listdir(auto_generated_tests_dir):
            try:
                copy_dir(auto_generated_tests_dir + '/' + test_dir + '/classes/', orig_test_classes_dir)
            except Exception as e:
                print("**WARNING: Directory {0} does not exist.".format(str(e)))        

    def _remove_wrong_test(self, auto_generated_tests_dir):
        try:
            remove_dir(auto_generated_tests_dir)
        except Exception as e:
            print("**WARNING: Could not remove directory: {0}. Reason: {1}.".format(auto_generated_tests_dir, str(e)))

    def _get_coverage(self, orig_project_dir, sut_class):
        jmockit = JMockit(orig_project_dir + '/target', sut_class)
        class_coverage = jmockit.coverage()  
        # mutated_line_info = jmockit.get_mutated_line_info(class_coverage, mutated_line)
        return class_coverage

    def _get_diff_coverage_lines(self, orig_coverage, mut_coverage):
        diff_lines = {k: orig_coverage[k] for k in orig_coverage if k in mut_coverage and orig_coverage[k] != mut_coverage[k]}
        if(len(orig_coverage) != len(mut_coverage)):
            lines_1 = orig_coverage.keys() - mut_coverage.keys()
            lines_2 = mut_coverage.keys() - orig_coverage.keys()
            for l in lines_1:
                diff_lines[l] = orig_coverage[l]
            for l in lines_2:
                diff_lines[l] = mut_coverage[l]
        return diff_lines

    def _get_mutation_line_info(self, class_coverage, mutation_line):        
        _, executions, test_cases_cps = JMockit._get_mutation_line_info(class_coverage, mutation_line)          
        return executions, test_cases_cps    
        
    def _get_test_files(self, dir):
        tests = set()
        for java_file in get_java_files(dir):
            tests.add(java_file[java_file.rindex('/')+1:java_file.index('.java')])
        return tests

    #Configure mutant tool, compile mutants dir, and return the mutants set
    def get_mutants(self, classpath, mutants_dir):        
        if('mujava' in mutants_dir):
            mujava = MuJava(self.java, mutants_dir)
            mutants = mujava.read_log()
            mujava.compile_mutants(classpath, mutants)
            return mutants
        elif('major' in mutants_dir):
            major = Major(self.java, mutants_dir)
            mutants = major.read_log()
            major.compile_mutants(classpath, mutants)
            return mutants
        return []    

    

    def try_evosuite_diff(self, classes_dir, tests_src, sut_class, mutant,
                     evosuite_diff_params=None):
        junit = JUnit(java=self.java, classpath=classes_dir)
        return junit.run_with_mutant(self.suite_evosuite_diff, sut_class, mutant)


    def try_evosuite(self, classes_dir, tests_src, sut_class, mutant=None,
                     evosuite_params=None):
        junit = JUnit(java=self.java, classpath=classes_dir)
        if mutant is None:
            return (junit.run_with_original(self.suite_evosuite, sut_class, classes_dir)
                if self.suite_evosuite else None)
        else:
            return (junit.run_with_mutant(self.suite_evosuite, sut_class, mutant)
                if self.suite_evosuite else None)


    def try_randoop(self, classes_dir, tests_src, sut_class, mutant,
                    randoop_params=None):
        junit = JUnit(java=self.java, classpath=classes_dir)
        return (junit.run_with_mutant(self.suite_randoop, sut_class, mutant)
                if self.suite_randoop else None)

    def gen_evosuite_diff(self, classes_dir, evosuite_diff_params, mutant, sut_class, tests_src):
        evosuite = Evosuite(
            java=self.java,
            classpath=classes_dir,
            tests_src=tests_src,
            sut_class=sut_class,
            params=evosuite_diff_params
        )
        self.suite_evosuite_diff = evosuite.generate_differential(mutant.dir)
        return self.suite_evosuite_diff

    def gen_evosuite(self, classes_dir, evosuite_params, mutant, sut_class, tests_src):
        evosuite = Evosuite(
            java=self.java,
            classpath=classes_dir,
            tests_src=tests_src,
            sut_class=sut_class,
            params=evosuite_params
        )
        # suite = evosuite.generate()
        safira = Safira(java=self.java, classes_dir=classes_dir,
                        mutant_dir=mutant.dir)
        self.suite_evosuite = evosuite.generate_with_impact_analysis(safira)
        if "Simulator" in sut_class:
            import distutils.dir_util
            distutils.dir_util.copy_tree("./config/", evosuite.suite_dir + "/config/")

        return self.suite_evosuite

    def gen_randoop(self, classes_dir, mutant, randoop_params, sut_class, tests_src):
        randoop = Randoop(
            java=self.java,
            classpath=classes_dir,
            tests_src=tests_src,
            sut_class=sut_class,
            params=randoop_params
        )
        safira = Safira(java=self.java, classes_dir=classes_dir,
                        mutant_dir=mutant.dir)
        if "Bisect" in sut_class:
            self.suite_randoop = randoop.generate()
        else:
            self.suite_randoop = randoop.generate_with_impact_analysis(safira)
            if "Simulator" in sut_class:
                import distutils.dir_util
                distutils.dir_util.copy_tree("./config/", randoop.suite_dir + "/config/")
        return self.suite_randoop    

    def _copy_coverage_report_to_output_dir(self, orig_project_dir, nimrod_output_dir, mutant_id):
        original_dir = os.path.join(orig_project_dir, 'target','coverage-report')
        output_dir = os.path.join(nimrod_output_dir, 'coverage', mutant_id)
        try:
            copy_dir(original_dir, output_dir)
        except Exception as e:
            print("**WARNING: Could not copy coverage report of {0}.".format(str(mutant_id)))        

        


    # #####  EXPERIMENT ALGORITHM #####
    #
    # 1. Execute the dev tests (through mvn) - IMPORTANT: Skip this line and take the survived mutants (in case of Defects4J)
    # **** Save the survived mutants
    # 2. Take the survived mutants and execute TCE
    # **** Remove TCE equivalents (log results)
    # **** Remove TCE duplicates (log results)
    # 3. Generate automatic tests with EvoSuite and Randoop (based on original)
    # **** Save the automatic tests alongside the dev tests (combined test set)
    # 4. Execute the combined test set to check if they are passing on original (eliminate in case of flaky)
    # 5. Take the survived mutants (minus TCE eqv ) and execute the combined test set
    #    For each mutant:
    #    IF mutant killed: 
    #       save the killer test, and GO TO ...
    #    ELSE mutant not killed: 
    #       save the coverage on mutant
    #       execute EvoSuiteR with the mutant
    #       IF EvoSuiteR find a test and kill the mutant:
    #           Save the killer test
    #           Add up this new test case to the combined test set
    #           Label the mutant as Non-equivalent
    #           Remove from survived set
    # 
    # 7. Take the survived mutant set  and the orignal, execute the combined test set
    # **** Save the coverage (check if any other mutant kill)
    # **** Compare coverage (and other information)
    # 8. Rank the survived mutants according to coverage information
    # ===============================================
    # OUTCOME
    # **** a) Set of equivalent mutants from TCE
    # **** b) Set of duplicate mutants from TCE
    # **** c) Set of non-equivalent mutants and the corresponding killer test-set
    # **** d) A rank of last survived mutants to manual analysis
    def run(self, orig_project_dir, mutants_dir, sut_class, randoop_params=None,
            evosuite_diff_params=None, evosuite_params=None, nimrod_output_dir=None):     
        
        start_time = time()
        
        nimrod_results = dict()

        # Compile Project With Maven       
        print("Compiling the project with maven...")
        _, orig_classes_dir = self.maven.compile(orig_project_dir, clean=True)
        # Compile Tests with Maven
        print("Compiling the tests with maven...")
        _, orig_test_classes_dir = self.maven.test_compile(orig_project_dir)
        # get outcome results
        nimrod_output_dir = self._check_output_dir(nimrod_output_dir if nimrod_output_dir
                                           else os.path.join(orig_project_dir,
                                                             OUTPUT_DIR))      

        
        
        self.junit = JUnit(java=self.java, classpath=orig_classes_dir)

        # 1. Execute the dev tests (through Maven) - IMPORTANT: Skip this line and take the survived mutants (in case of Defects4J)
        # **** Save the survived mutants

        # 2. Take the survived mutants and execute TCE
        tce_eqvs, tce_dups = self._try_tce(orig_project_dir, orig_classes_dir, mutants_dir, sut_class)
        # **** Remove TCE equivalents and duplicates (TODO: need log results before)        
        # self._del_mutants(mutants_dir, set(tce_eqvs+tce_dups))
        # 3. Generate automatic tests with EvoSuite and Randoop (based on original)
        auto_generated_tests_dir = self._gen_automatic_tests(orig_classes_dir, mutants_dir, nimrod_output_dir, sut_class, randoop_params, evosuite_params)
        # Save the automatic tests alongside the dev tests (combined test set)
        self._combine_tests(orig_test_classes_dir, auto_generated_tests_dir)
        # Exec tests on original program
        print("Executing tests on original...")
        num_tests, num_failures, num_errors, num_skipped, failed_tests = self.maven.test(orig_project_dir, sut_class)
        # Stop execution in case of flaky tests
        if(num_failures > 0 or num_errors > 0):
                print("*** ERROR - Flaky tests or error in automatic test generation. Please check executing: mvn test.")
                print("num_tests: {0}, num_failures: {1}, num_errors: {2}, num_skipped: {3}, failed_tests: {4}".format(num_tests, num_failures, num_errors, num_skipped, failed_tests))
                return 
       
        #Configure mutant tool, compile mutants dir, and return the mutants set
        nimrod_non_equivs = set()
        nimrod_survived_muts = set()

        mutants = self.get_mutants(orig_classes_dir, mutants_dir) 
        # print("Total mutants: {0}".format(len(mutants)))  # esse numero nao eh real, pois soh pega os mutantes do arq de log
        print("Mutant analysis phase started")
        for mutant in mutants:
            # Does not exec TCE eqvs and dupl mutants
            if (mutant.mid[mutant.mid.index('_')+1:] in tce_eqvs):
                if('$' not in mutant.method): # ib case of $ means interal class which is a TCE error
                    print("Mutant {0} is equivalent".format(mutant.mid))
                    nimrod_results[mutant.mid] = NimrodResult(mutant.mid, mutant.operator, mutant.line_number, mutant.method, mutant.transformation, True, '', '', 
                        '', '', '', '', '')                        
                    continue

            if(os.path.isdir(mutant.dir)):      
                evosuite_diff_output_dir = self._gen_evosuite_diff(sut_class, evosuite_diff_params, orig_classes_dir, auto_generated_tests_dir, mutant)
                # test_result = self._exec_junit(evosuite_diff_output_dir, sut_class, mutant)
                test_result = self.try_evosuite_diff(orig_classes_dir, evosuite_diff_output_dir, sut_class, mutant,
                                                     evosuite_diff_params)
                if test_result.fail_tests > 0 or test_result.timeout: #If killed by EvoSuiteR Diff
                    print("Mutant {0} is NOT equivalent. Killed by {1}".format(mutant.mid, self._get_test_files(evosuite_diff_output_dir.suite_dir)))
                    nimrod_non_equivs.add(mutant)
                    nimrod_results[mutant.mid] = NimrodResult(mutant.mid,  mutant.operator, mutant.line_number, mutant.method, mutant.transformation, False, True, test_result.fail_tests, 
                        self._get_test_files(evosuite_diff_output_dir.suite_dir), test_result.timeout, 1, False, '')
                else:
                    # print("Mutant {0} was NOT killed by EvoSuiteR".format(mutant.mid)) 
                    # self._remove_wrong_test(evosuite_diff_output_dir.suite_dir)   
                    nimrod_survived_muts.add(mutant)             

        # Junta todos os testes gerados com os testes do desenvolvedor
        self._combine_tests(orig_test_classes_dir, auto_generated_tests_dir)
        # Executa no programa original
        num_tests, num_failures, num_errors, num_skipped, failed_tests = self.maven.test(orig_project_dir, sut_class)
        self._copy_coverage_report_to_output_dir(orig_project_dir, nimrod_output_dir, 'original')
        orig_coverage = self._get_coverage(orig_project_dir, sut_class)
        # Stop execution in case of flaky tests
        if(num_failures > 0 or num_errors > 0):
                print("*** ERROR - Flaky tests or error in automatic test generation. Please check executing: mvn test.")
                print("num_tests: {0}, num_failures: {1}, num_errors: {2}, num_skipped: {3}, failed_tests: {4}".format(num_tests, num_failures, num_errors, num_skipped, failed_tests))
                return 
        # Próximo passo calcular o Coverage e montar o Rank
        for mutant in nimrod_survived_muts:  
            # Copia o mutante para a pasta correta em target/classes
            copy_dir(mutant.dir, orig_classes_dir)         
            try:
                # Executa o os testes combinados com o mutante que nao morreu com EvoSuiteR - Pega Coverage  
                num_tests, num_failures, num_errors, num_skipped, failed_tests = self.maven.test(orig_project_dir, sut_class)
                self._copy_coverage_report_to_output_dir(orig_project_dir, nimrod_output_dir, mutant.mid)
                mut_coverage = self._get_coverage(orig_project_dir, sut_class)            
                executions, test_cases_cps = self._get_mutation_line_info(mut_coverage, mutant.line_number)
                diff_lines = self._get_diff_coverage_lines(orig_coverage, mut_coverage)
                
                if(num_failures > 0 or num_errors > 0):
                    print("Mutant {0} is NOT equivalent. Killed by {1}.".format(mutant.mid, failed_tests))
                    nimrod_results[mutant.mid] = NimrodResult(mutant.mid,  mutant.operator, mutant.line_number, mutant.method, mutant.transformation, False, True, num_failures + num_errors, 
                            failed_tests, False, executions, False if len(diff_lines)>0 else True, len(diff_lines))
                else:
                    print("Mutant {0} may be equivalent. Coverage is {1}.".format(mutant.mid, 'Different' if len(diff_lines)>0 else 'Equal'))
                    nimrod_results[mutant.mid] = NimrodResult(mutant.mid,  mutant.operator, mutant.line_number, mutant.method, mutant.transformation, False, False, '', 
                        '', False, executions, False if len(diff_lines)>0 else True, len(diff_lines))

            except Exception as e:
                print("**ERROR: Test timedout to mutant {0}".format(mutant.mid))
                # Adiciona mutante como diferenete e justifica com Timeout
                print("Mutant {0} is NOT equivalent. Timeout error.".format(mutant.mid))
                nimrod_results[mutant.mid] = NimrodResult(mutant.mid,  mutant.operator, mutant.line_number, mutant.method, mutant.transformation, False, True, '', 
                        '', True, 0, False, '')
            
        
        self._print_output(nimrod_results) 
        self.write_to_csv(nimrod_results, output_dir=nimrod_output_dir, filename='nimrod.csv',
                     exclude_if_exists=True)
        print ('Finished Nimrod analysis of {0} mutants in {1} secs: '.format(len(nimrod_results), round((time() - start_time), 2)))

    
    @staticmethod
    def write_to_csv(nimrod_results, output_dir='.', filename='nimrod.csv',
                     exclude_if_exists=False):
        file = os.path.join(output_dir, filename)

        if exclude_if_exists:
            if os.path.exists(file):
                os.remove(file)

        if not os.path.exists(file):
            with open(file, 'w') as f:
                f.write('mutant,mutant_operator,mutant_line_number,mutant_method,mutant_transformation,tce_equivalent,killed_by_auto_test,num_killer_test_cases,killer_test_cases,timeout,executions,equal_line_coverage,num_different_lines\n')
        
        for mid in nimrod_results:
            with open(file, 'a') as f:
                f.write('{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11},{12}\n'.format(
                    nimrod_results[mid][0],
                    nimrod_results[mid][1],
                    nimrod_results[mid][2],
                    str(nimrod_results[mid][3]).replace(',', ';'),
                    str(nimrod_results[mid][4]).replace(',', ';'),
                    nimrod_results[mid][5],
                    nimrod_results[mid][6],
                    nimrod_results[mid][7],
                    str(nimrod_results[mid][8]).replace(',', ';'),
                    nimrod_results[mid][9],                    
                    nimrod_results[mid][10],
                    nimrod_results[mid][11],
                    nimrod_results[mid][12],
                ))
                



