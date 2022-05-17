#!/usr/bin/python3

import sys, getopt
import os
from pathlib import Path
import shutil
import subprocess
from distutils.dir_util import copy_tree


def copy_major_mutation_result(input, output):
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

def copy_class(input, output):
    src_dir = input + '/target/classes/'
    dest_dir = output + '/classes/'
    print('Copying class files...')
    copy_tree(src_dir, dest_dir)
    # Copiando p/ pasta ted/bin
    dest_dir = output + '/ted/bin/'    
    copy_tree(src_dir, dest_dir)    

def copy_src(input, output):
    src_dir = input + '/src/main/java'
   #  src_dir = input + '/src/java'
    dest_dir = output + '/src/'
   #  print('Copying source files...')
    copy_tree(src_dir, dest_dir)

def copy_mutants(input, output, class_name):
    src_mutants_log = input + '/mutants.log'
    src_mutants_dir = input + '/livemutants/'
    dest1 = output + '/result/' + class_name + '/traditional_mutants'
    dest2 = output + '/result/' + class_name + '/traditional_mutants/method'    
    destination1 = shutil.copy2(src_mutants_log, dest1) 
    copy_tree(src_mutants_dir, dest2 )
    # Copia para pasta original
    src_dir = input + '/target/classes/'
    dest3 = output + '/result/' + class_name + '/original'
    copy_tree(src_dir, dest3 )

def compile_mutants(input):
    src_mutants_dir = input + '/livemutants/'
    classpath = input + '/target/classes/'
   #  classpath += ':' + input + '/lib/*.jar'
    classpath += ':/home/leofernandesmo/workspace/easylab/defects4j/subjects/jsoup_1_fixed/lib/commons-lang-2.4.jar'
    for path in Path(src_mutants_dir).rglob('*.java'):
        # command = '/home/leofernandesmo/workspace/easylab/defects4j/major/bin/javac -cp "%s" %s' % (classpath, path) # Compila como major
        command = 'javac -cp "%s" %s' % (classpath, path) #Compila com Java normal
        print('Compiling: ===> ', command)
        subprocess.call(command, shell=True)

def makeup_struct(output, class_name):
    if not os.path.exists(output):
        # os.makedirs(directory)
        print('Creating structure...')
        Path(output).mkdir(parents=True, exist_ok=True) 
        Path(output + '/src').mkdir(parents=True, exist_ok=True)    
        Path(output + '/classes').mkdir(parents=True, exist_ok=True)    
        Path(output + '/result').mkdir(parents=True, exist_ok=True)    
        Path(output + '/result/' + class_name).mkdir(parents=True, exist_ok=True)    
        Path(output + '/result/' + class_name + '/class_mutants').mkdir(parents=True, exist_ok=True)    
        Path(output + '/result/' + class_name + '/original').mkdir(parents=True, exist_ok=True)            
        Path(output + '/result/' + class_name + '/traditional_mutants/method').mkdir(parents=True, exist_ok=True)    
        Path(output + '/ted/bin').mkdir(parents=True, exist_ok=True)    
        Path(output + '/ted/optimisations').mkdir(parents=True, exist_ok=True)    
        Path(output + '/mutants-info').mkdir(parents=True, exist_ok=True)    

def main(argv):
   input_dir = ''
   output_dir = ''
   try:
      opts, args = getopt.getopt(argv,"hi:o:c:",["idir=","odir=", "classname="])
   except getopt.GetoptError:
      print('subject_analysis_setup.py -i <inputdirectory> -o <outputdirectory>')
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print('subject_analysis_setup.py -i <inputdirectory> -o <outputdirectory>')
         sys.exit()
      elif opt in ("-i", "--idir"):
         input_dir = arg
      elif opt in ("-o", "--odir"):
         output_dir = arg
      elif opt in ("-c", "--classname"):
         class_name = arg   
   
   makeup_struct(output_dir, class_name)   
   compile_mutants(input_dir)
   copy_mutants(input_dir, output_dir, class_name)
   copy_src(input_dir, output_dir)
   copy_class(input_dir, output_dir)
   copy_major_mutation_result(input_dir, output_dir)

   print('It seems everything is ok.')

if __name__ == "__main__":
   main(sys.argv[1:])