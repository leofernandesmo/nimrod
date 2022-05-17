from nimrod.tools.suite_generator import SuiteGenerator

import os
import random

from nimrod.utils import generate_classpath
from nimrod.tools.bin import RANDOOP
from nimrod.tools.bin import COMMONS_LANG_24


METHOD_LIST_FILENAME = 'methods_to_test.txt'


class Randoop(SuiteGenerator):

    def _get_tool_name(self):
        return "randoop"

    def _exec_tool(self):
        params = [
            '-classpath', generate_classpath([self.classpath, RANDOOP, COMMONS_LANG_24]),
            'randoop.main.Main',
            'gentests',
            '--randomseed=' + str(random.randint(0, 9999)),
            '--testclass=' + self.sut_class,
            '--junit-output-dir=' + self.suite_dir
        ]

        params += self.parameters
        return self._exec(*tuple(params))

    def _test_classes(self):
        return ['RegressionTest']

    def generate_with_impact_analysis(self, impact_analysis):                
        method_list = ""
        self._make_src_dir()
        impact_analysis_result = impact_analysis.run()
        method_list = self.create_method_list(impact_analysis_result,
                                              self.suite_dir)
        if os.path.exists(method_list):
            elem = [x for x in self.parameters if '--methodlist=' in x]
            if len(elem) > 0:
                self.parameters.remove(elem[0])
            self.parameters.append('--methodlist=' + method_list)

        return super().generate(make_dir=False)

    
    def create_method_list(self, impact_analysis_result, output_dir,
                           filename=METHOD_LIST_FILENAME):
        filename = os.path.join(output_dir, filename)

        with open(filename, 'w') as f:
            methods = (impact_analysis_result.constructors
                       + impact_analysis_result.methods)
            [f.write(l + '\n') for l in methods]
            f.close()

        return filename
