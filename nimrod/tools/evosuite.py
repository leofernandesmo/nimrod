import os

from nimrod.tools.suite_generator import SuiteGenerator
from nimrod.utils import get_class_files, get_java_files
from nimrod.tools.bin import EVOSUITE, EVOSUITE_RUNTIME, COMMONS_LANG_24
from nimrod.tools.suite_generator import Suite

METHOD_LIST_FILENAME = 'methods_to_test.txt'

class Evosuite(SuiteGenerator):

    def _get_tool_name(self):
        return "evosuite"

    def _exec_tool(self):
        params = [
            '-jar', EVOSUITE,
            '-projectCP', self.classpath + ':' + COMMONS_LANG_24,
            '-class', self.sut_class,
            '-Dtimeout', '10000',
            '-DOUTPUT_DIR=' + self.suite_dir
        ]

        params += self.parameters

        return self._exec(*tuple(params))

    def _test_classes(self):
        classes = []

        for class_file in sorted(get_class_files(self.suite_classes_dir)):
            filename, _ = os.path.splitext(class_file)
            if not filename.endswith('_scaffolding'):
                classes.append(filename.replace(os.sep, '.'))

        return classes

    def _get_suite_dir(self):
        return os.path.join(self.suite_dir, 'evosuite-tests')

    @staticmethod
    def _extra_classpath():
        return [EVOSUITE_RUNTIME, COMMONS_LANG_24]

    def _get_java_files(self):
        ordered_files = []

        for file in sorted(get_java_files(self.suite_dir)):
            if '_scaffolding' in file:
                ordered_files.insert(0, file)
            else:
                ordered_files.append(file)

        return ordered_files

    def _exec_differential(self, mutants_classpath):
        params = [
            '-jar', EVOSUITE,
            '-regressionSuite',
            '-projectCP', self.classpath + ':' + COMMONS_LANG_24,
            '-Dregressioncp=' + mutants_classpath,
            '-class', self.sut_class,
            '-DOUTPUT_DIR=' + self.suite_dir,
            '-Djunit_suffix=' + '_' + self.suite_name + '_ESTest'
        ]

        params += self.parameters

        return self._exec(*tuple(params))

    def generate_differential(self, mutant_classpath, make_dir=True):
        if make_dir:
            self._make_src_dir()
        output = self._exec_differential(mutant_classpath)
        self._compile()

        return Suite(suite_name=self.suite_name, suite_dir=self.suite_dir,
                     suite_classes_dir=self.suite_classes_dir,
                     test_classes=self._test_classes())

    # Argument for using only specific methods to test
    # Eg.: -Dtarget_method_list="append(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
    def generate_with_impact_analysis(self, impact_analysis):
        self._make_src_dir()
        impact_analysis_result = impact_analysis.run()
        method_list = self.create_method_list(impact_analysis_result)
        if method_list.strip() != '':
            self.parameters.append('-Dtarget_method_list=\"' + method_list + '\"')

        return super().generate(make_dir=False) 

    def create_method_list(self, impact_analysis_result):   
        method_list = '' 
        for meth in impact_analysis_result.methods:
            if self.sut_class in meth:
                meth_temp = meth.replace(self.sut_class, "")[1:]
                all_methods = impact_analysis_result.all_methods_by_class[self.sut_class]
                method_with_return = [m for m in all_methods if meth_temp in m]
                if len(method_with_return) == 1:
                    m = method_with_return[0]                
                    ret = m[0:m.index(meth_temp)].strip()
                    ret = ret[ret.index(" "):].strip() 
                    meth_name = meth_temp[meth_temp.find(".")+1:meth_temp.rfind("(")]
                    meth_args = meth_temp[meth_temp.find("(")+1:meth_temp.rfind(")")].split(",")
                    asm_meth_format = self.asm_based_method_method_descriptor(meth_args, ret)
                    method_list = method_list + "{0}{1}".format(meth_name, asm_meth_format) + ":"
                    
        return method_list[:-1]

    #See at: https://asm.ow2.io/asm4-guide.pdf -- Section 2.1.3 and 2.1.4
    # Java type Type descriptor
    # boolean Z
    # char C
    # byte B
    # short S
    # int I
    # float F
    # long J
    # double D
    # Object Ljava/lang/Object;
    # int[] [I
    # Object[][] [[Ljava/lang/Object;
    def asm_based_method_method_descriptor(self, args, ret):
        result = '('
        for arg in args:
            arg = arg.strip()
            result = result + self._asm_based_type_descriptor(arg)
        result = result + ')'
        result = result + self._asm_based_type_descriptor(ret)
        return result

    def _asm_based_type_descriptor(self, arg):
        result = ''
        if '[]' in arg:    
            result = result + '['
            arg = arg.replace('[]', '')

        if arg == '':
            result = result + ''
        elif arg == 'int':
            result = result + 'I'
        elif arg == 'float':
            result = result + 'F'
        elif arg == 'boolean':
            result = result + 'Z'
        elif arg == 'char':
            result = result + 'C'
        elif arg == 'byte':
            result = result + 'B'
        elif arg == 'short':
            result = result + 'S'
        elif arg == 'long':
            result = result + 'J'
        elif arg == 'double':
            result = result + 'D'
        elif arg == 'void':
            result = result + 'V'     
        else:
            temp = "L" + arg.replace('.', '/') + ';'
            result = result + temp

        return result            