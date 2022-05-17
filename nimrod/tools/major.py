import os
import sys

from nimrod.mutant import Mutant
from nimrod.utils import get_java_files
from nimrod.tools.bin import COMMONS_LANG_24


class Major:

    def __init__(self, java, mutants_dir):
        self.mutants_dir = mutants_dir
        self.java = java

    def read_log(self, log_dir=None):
        mutants = [] 
        try:    
            with open(self._get_log_file(log_dir)) as log:
                for line in log.readlines():
                    mutants.append(self._log_to_mutant(line))

            return mutants
        except FileNotFoundError:
            print('Mutation log not found.', file=sys.stderr)
            raise SystemExit()
        
    def _get_log_file(self, log_dir=None):
        if log_dir is None:
            log_dir = os.sep.join([self.mutants_dir, 'mutants.log'])

        if os.path.exists(log_dir):
            return log_dir
        else:
            raise FileNotFoundError()

    def _log_to_mutant(self, log):
        log_data = log.split(':')
        operator = self._extract_operator(log_data[1])
     
        return Mutant(
            mid=str(log_data[1] + '_' + log_data[0]).strip(),
            operator=operator,
            line_number=self._get_line_number(log_data[5], operator),
            method=str(log_data[4]).strip(),
            transformation=str(log_data[6]).strip(),
            dir=os.path.join(self.mutants_dir, log_data[0])
        )

    def compile_mutants(self, classpath, mutants):        
        for mutant in mutants:
            self.java.compile_all(classpath + ':'  + COMMONS_LANG_24, mutant.dir)

    @staticmethod
    def _get_line_number(number, operator):
        number = int(number)
        return number

    @staticmethod
    def _extract_operator(mid):
        # return mid.split('_')[0] 
        return mid