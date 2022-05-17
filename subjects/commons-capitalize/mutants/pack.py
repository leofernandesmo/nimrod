import os
import shutil

MUTANTS_DIR = os.path.join('.', 'mujava')
PACKAGE = "org.apache.commons.lang"
PACKAGE_DIR = PACKAGE.replace('.', os.sep)


def main():

    for mutant_dir in os.listdir(MUTANTS_DIR):
        mutant_dir = os.path.join(MUTANTS_DIR, mutant_dir)
        if os.path.isdir(mutant_dir):
            with_package = os.path.join(mutant_dir, PACKAGE_DIR)
            os.makedirs(with_package, exist_ok=True)
            for file in os.listdir(mutant_dir):
                file = os.path.join(mutant_dir, file)
                if os.path.isfile(file):
                    shutil.move(file, with_package)


if __name__ == "__main__":
    main()
