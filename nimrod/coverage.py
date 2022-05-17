import os
import re
import time
import subprocess

from nimrod.utils import package_to_dir
from collections import namedtuple
from bs4 import BeautifulSoup


# CoverageResults = namedtuple('CoverageResults', [
#     'id',     
#     'class',    
#     'method',    
#     'line_number',        
# ]) 

Coverage = namedtuple('Coverage', ['call_points', 'test_cases', 'executions', 'class_coverage'])



class JMockit:

    def __init__(self, suite_dir, sut_class):
        self.suite_dir = suite_dir
        self.sut_class = sut_class
        # self.id = 0
        # self.file = ''
        # self.class = ''
        # self.method = ''
        # self.line_num = 0
        # self.stmt_id = ''
        # self.stmt_desc = ''
        # self.stmt_execs = ''
        # self.test_methods_touch = []
                
        # self.java_home = java_home
        # self._javac = None
        # self._java = None
        # self._set_home()
        # self._check()

    def coverage(self):
        report_file = self.get_coverage_report_file()
        if report_file:
            with open(report_file, 'r') as html:
                soup = BeautifulSoup(html, 'html.parser')
                return JMockit._get_coverage_info(soup)

    
    def get_coverage_report_file(self):
        coverage_report = os.path.join(self.suite_dir, 'coverage-report',
                                       package_to_dir(self.sut_class) + '.html')

        return coverage_report if os.path.exists(coverage_report) else None

    @staticmethod
    def _get_coverage_info(soup):
        class_coverage =  JMockit._get_coverage_info_class2(soup)        
        return class_coverage



    @staticmethod
    def _get_coverage_info_class(soup):
        executions = 0
        class_coverage_line = dict()

        for tr in soup.find_all('tr'):
            td_line = tr.find_all('td', class_='line')
            td_executions = tr.find_all('td', class_='count')
            td_count = tr.find_all('td', class_='callpoints-count')
            if (td_line and td_count and td_executions):
                line = int(td_line[0].string)
                executions = int(td_executions[0].string.strip())
                class_coverage_line[line] = executions
        return class_coverage_line


    @staticmethod
    def _get_coverage_info_class2(soup):  
        class_coverage = dict()
        for tr in soup.find_all('tr'):            
            td_line = tr.find('td').get_text()
            td_executions = tr.find_all('td', class_='ct')
            if(td_line.isnumeric() and td_executions):
                line = int(td_line)                    
                executions = int(td_executions[0].string.strip())                
                class_coverage[line] = (executions, None, None, None)                                            
                call_points = set()
                for li in tr.find_all('li'):
                    info = JMockit._extract_li(li)
                    if info:
                        file, test_case, cps = info                            
                        for cp in cps:
                            call_points.add((file, test_case, cp))
                class_coverage[line] = (executions, call_points)                    
        return class_coverage

    @staticmethod
    def _extract_li(li):
        li = li.string.split(':')
        if len(li) == 2:
            try:
                file = li[0].split('#')[0]
                test_case = li[0].split('#')[1]
                return (file, test_case,
                        [int(cp.split('x')[0].strip())
                         for cp in li[1].split(',')])
            except IndexError:
                return None

    @staticmethod
    def _get_mutation_line_info(class_coverage, mutation_line):
        if(mutation_line in class_coverage):
            executions, call_points = class_coverage[mutation_line]
            return (mutation_line, executions, call_points)
        return (mutation_line, 0, set()) 
