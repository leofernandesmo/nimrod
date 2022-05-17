import os

from nimrod.nimrod import Nimrod
from nimrod.tools.java import Java
from nimrod.tools.maven import Maven
from nimrod.tests.utils import get_config

PATH = os.path.dirname(os.path.abspath(__file__))

PROJECT_DIR = PATH
MUTANTS_DIR = os.sep.join([PATH, 'mutants', 'mujava'])


def main():
    java = Java(get_config()['java_home'])
    maven = Maven(java, get_config()['maven_home'])

    nimrod = Nimrod(java, maven)
    nimrod.run(
        PROJECT_DIR, MUTANTS_DIR,
        'org.apache.commons.lang.WordUtils',
        randoop_params=['--time-limit=60'],
        evosuite_params=['-Dsearch_budget=60'],
        evosuite_diff_params=['-Dsearch_budget=60']
    )


if __name__ == "__main__":
    main()
