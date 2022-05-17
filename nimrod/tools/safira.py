import sys
import time
import subprocess

from abc import ABC, abstractmethod
from collections import namedtuple

from nimrod.utils import generate_classpath
from nimrod.tools.bin import SAFIRA


ImpactAnalysisResult = namedtuple('ImpactAnalysisResult', ['methods',
                                                           'constructors',
                                                           'all_methods_by_class',
                                                           'run_time'])

TIMEOUT = 60 * 1


class ImpactAnalysis(ABC):
    pass

    @abstractmethod
    def run(self):
        """Execute impact analysis."""


class Safira(ImpactAnalysis):

    def __init__(self, java, classes_dir, mutant_dir):
        self.java = java
        self.classes_dir = classes_dir
        self.mutant_dir = mutant_dir

    def run(self, params=None, timeout=TIMEOUT):
        classpath = generate_classpath([
            SAFIRA
        ])
        params = ["--intraclass"]
        start = time.time()
        try:
            output = self.java.exec_java(
                None, self.java.get_env(), timeout,
                '-classpath', classpath,
                'saferefactor.safira.SafiraStart',                
                self.classes_dir, self.mutant_dir,
                *tuple(params if params else [])
            )

            methods, constructors, all_methods_by_class = self._extract_results(output.decode('unicode_escape'))

            return ImpactAnalysisResult(
                methods,
                constructors,
                all_methods_by_class,
                time.time() - start
            )
        except subprocess.TimeoutExpired as e:
            elapsed_time = time.time() - start
            print("# ERROR: [SAFIRA] execution timed out: {0} seconds".format(
                elapsed_time), file=sys.stderr)
            raise e


    def run_javap(self, classfiles, params=None, timeout=TIMEOUT):
        classpath = self.classes_dir

        start = time.time()
        try:
            output = self.java.exec_javap(
                classfiles, None, self.java.get_env(), timeout,
                '-classpath', classpath,
                *tuple(params if params else [])
            )
            return output
        except subprocess.TimeoutExpired as e:
            elapsed_time = time.time() - start
            print("# ERROR: [SAFIRA] execution timed out: {0} seconds".format(
                elapsed_time), file=sys.stderr)
            raise e

    
    def _extract_results(self, output):
        methods = []
        constructors = []
        classes = set()
        
        for res in output.split("|"):
            res = res.split(':')
            if str(res[0]).startswith('method'):
                methods.append(' '.join(res[1].split()).strip())
                classes.add(res[2].strip())
            elif str(res[0]).startswith('cons'):
                constructors.append(' '.join(res[1].replace(
                    '.<init>', '').split()).strip())

        all_methods_by_class = dict()
        for n in classes:
            b_methods_from_n = self.run_javap(classfiles=n.strip())
            methods_from_n = b_methods_from_n.decode('utf-8').split('\n')
            methods_from_n = methods_from_n[2:-2]
            all_methods_by_class[n.strip()] = methods_from_n

        return methods, constructors, all_methods_by_class
