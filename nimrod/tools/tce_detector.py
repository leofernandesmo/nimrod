import os
import re
import time
import subprocess
import shutil
import tempfile

from distutils.dir_util import copy_tree
from pathlib import Path
from nimrod.tools.tce.src import main
from nimrod.tools.bin import SOOT, RT_JAR, COMMONS_LANG_24


TIMEOUT = 40

# JUnitResult = namedtuple('JUnitResult', ['ok_tests', 'fail_tests', 
#                                          'fail_test_set', 'run_time',
#                                          'coverage', 'timeout'])


class Tce:

    def __init__(self, java, exp_dir="", mujava_res=""):
        self.java = java
        self.mujava_res = mujava_res
        self.exp_dir = exp_dir
        self.equiv_mutants = []
        self.dupl_mutants = []
        
    def exec(self, action, timeout=TIMEOUT):        
        # 'method(s) to be considered; if not provided, consider all methods. Note that the names of the methods are the ones returned by mujava'
        methods = ''
        # 'the maximum number of mutants to optimise; if not provided, optimise them all'
        mutants = ''

        return self._exec(str(RT_JAR), str(SOOT), self.mujava_res, self.exp_dir, action, methods, mutants)
    

    def _exec(self, rt_jar, soot, mujava_res, exp_dir, action, methods, mutants):

        # params = (
        #     '-javaagent:'+str(JMOCKIT), 
        #     '-classpath', classpath,
        #     '-Dcoverage-classes=' + sut_class,
        #     '-Dcoverage-output=html',
        #     '-Dcoverage-metrics=line',
        #     '-Dcoverage-srcDirs=' + cov_src_dirs,
        #     'org.junit.runner.JUnitCore', test_class
        # )

        start = time.time()
        try:            
            output = main.exec(rt_jar, soot, mujava_res, exp_dir, action, methods, mutants)
            return Tce._extract_results_succ(action, output)
            
        except Exception as e:
            print("### ERROR #####")
            print(e)
            # return JUnitResult(
            #     *Tce._extract_results(e.output.decode('unicode_escape')),
            #     time.time() - start, None, False
            # )
        

    @staticmethod
    def _extract_results_succ(action, output):
        if(action == 'Opt'):
            return ''
        elif(action == 'Ted'):
            equivalents = []
            if(output is not None):
                classes = output.keys()
                for classe in classes:
                    methods = output[classe]
                    for method in methods.keys():
                        mutants = methods[method]
                        equivalents.extend(mutants)
            return equivalents
        elif(action == "Ted-dupes"):
            duplicates = []
            if(output is not None):
                classes = output.keys()
                for classe in classes:
                    methods = output[classe]
                    for method in methods.keys():
                        mutants = methods[method]
                        flat_list = [item for sublist in mutants.values() for item in sublist]
                        duplicates.extend(flat_list)
            return duplicates
        else:
            raise Exception("No valid action selected to execute TCE") 

    @staticmethod
    def _extract_results_fail(output):
        pass
        # if len(re.findall(r'initializationError', output)) == 0:
        #     result = re.findall(r'Tests run: [0-9]*,[ ]{2}Failures: [0-9]*',
        #                         output)
        #     if len(result) > 0:
        #         result = result[0].replace(',', ' ')
        #         r = [int(s) for s in result.split() if s.isdigit()]
        #         return r[0], r[1], JUnit._extract_test_id(output)

        # return 0, 0, set()
    
    def optimize(self):
        action = "Opt"
        results = self.exec(action)
        return results
    
    def equivalents(self):                
        action = "Ted"
        results = self.exec(action)       
        return results

    def duplicates(self):
        action = "Ted-dupes"
        results = self.exec(action)       
        return results

    #python3 src/major-adapter/subject_analysis_setup_tce.py 
      # -i /home/leofernandesmo/workspace/easylab/defects4j/subjects/cli_2_fixed 
      # -o /home/leofernandesmo/workspace/easylab/tce/subjects/commons-cli2 
      # -c org.apache.commons.cli.PosixParser
    #Create a temp folder to execute TCE. 
    #TCE strictly wait a MuJava-like folder structure.
    def setup_tce_structure(self, original_project_dir, original_mutants_dir, temp_dir, sut_class):        
        
        # print('Creating TCE structure in a temp diectory: ')        
        Tce._makeup_struct(temp_dir, sut_class)                   
        # print('Copying source files to TCE...')
        Tce._copy_src(original_project_dir, temp_dir)
        # print('Copying class files to TCE...')
        Tce._copy_class(original_project_dir, temp_dir)        
        # print('Copying mutant files to TCE...')
        Tce._copy_mutants(original_mutants_dir, temp_dir)
        Tce._compile_mutants(self.java, original_project_dir, temp_dir, sut_class)
        Tce._organize_in_dirs(original_project_dir, original_mutants_dir, temp_dir, sut_class)
        #Tce._copy_major_mutation_result(input_dir, output_dir)        
                
        
    
    @staticmethod
    def _makeup_struct(temp_dir, class_name):
        # if os.path.exists(temp_dir) and os.path.isdir(temp_dir):
        #     shutil.rmtree(temp_dir)                
        Path(temp_dir + '/src').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/classes').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/result').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/result/' + class_name).mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/result/' + class_name + '/class_mutants').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/result/' + class_name + '/original').mkdir(parents=True, exist_ok=True)            
        Path(temp_dir + '/result/' + class_name + '/traditional_mutants/method').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/ted/bin').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/ted/optimisations').mkdir(parents=True, exist_ok=True)    
        Path(temp_dir + '/mutants-info').mkdir(parents=True, exist_ok=True)
        Path(temp_dir + '/livemutants').mkdir(parents=True, exist_ok=True)

    @staticmethod
    def _copy_class(input, output):
        src_dir = input + '/target/classes/'
        dest_dir = output + '/classes/'        
        copy_tree(src_dir, dest_dir)
        # Copiando p/ pasta ted/bin
        dest_dir = output + '/ted/bin/'    
        copy_tree(src_dir, dest_dir)    

    @staticmethod
    def _copy_src(input, output):
        src_dir = input + '/src/main/java'
        #  src_dir = input + '/src/java'
        dest_dir = output + '/src/'        
        copy_tree(src_dir, dest_dir)

    @staticmethod
    def _copy_mutants(mutants_dir, temp_dir):
        src_dir = mutants_dir 
        dest_dir = temp_dir + '/livemutants'        
        copy_tree(src_dir, dest_dir)
    
    
    @staticmethod
    def _compile_mutants(java, project_dir, temp_dir, class_name):
        src_mutants_dir = temp_dir + '/livemutants/'
        classpath = project_dir + '/target/classes/'
        classpath += ':'+ COMMONS_LANG_24
        need_compile = True
        for path in Path(src_mutants_dir).rglob('*.class'):
            need_compile = False
        if(need_compile):
            java.compile_all(classpath, src_mutants_dir)
        # for path in Path(src_mutants_dir).rglob('*.java'):
        #     # command = '/home/leofernandesmo/workspace/easylab/defects4j/major/bin/javac -cp "%s" %s' % (classpath, path) # Compila como major
        #     command = 'javac -cp "%s" %s' % (classpath, path) #Compila com Java normal
        #     print('Compiling: ===> ', command)
        #     subprocess.call(command, shell=True)
        

    @staticmethod
    def _organize_in_dirs(project_dir, mutants_dir, temp_dir, class_name):
        src_mutants_dir = temp_dir + '/livemutants/'
        src_mutants_log = ''
        if('mujava' in mutants_dir):
            src_mutants_log = src_mutants_dir + '/mutation_log'
        elif('major' in mutants_dir):
            src_mutants_log = src_mutants_dir + '/mutants.log'
        dest1 = temp_dir + '/result/' + class_name + '/traditional_mutants'
        dest2 = temp_dir + '/result/' + class_name + '/traditional_mutants/method'        
        destination1 = shutil.copy2(src_mutants_log, dest1) 
        copy_tree(src_mutants_dir, dest2 )
        # Copia para pasta original
        src_dir = project_dir + '/target/classes/'
        dest3 = temp_dir + '/result/' + class_name + '/original'
        copy_tree(src_dir, dest3 )     

    @staticmethod
    def _copy_major_mutation_result(self, input, output):
        # Copia os logs gerados pelo major
        print('Copying major results...')
        src_mutants_log = input + '/.mutation.log'
        src_kill_log = input + '/kill.csv'
        src_summary_log = input + '/summary.csv'
        src_testMap_log = input + '/testMap.csv'
        dest = output + '/mutants-info'
        shutil.copy2(src_mutants_log, dest) 
        shutil.copy2(src_kill_log, dest) 
        shutil.copy2(src_summary_log, dest) 
        shutil.copy2(src_testMap_log, dest)   