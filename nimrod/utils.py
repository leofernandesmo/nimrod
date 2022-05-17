import os
import shutil

from distutils.dir_util import copy_tree


def get_class_files(path):
    return get_files(path, ext='.class')


def get_java_files(path):
    return get_files(path, ext='.java')


def get_files(path, root='', ext=None):
    files = []

    for node in os.listdir(path):
        node_path = os.path.join(path, node)
        if os.path.isdir(node_path):
            files += get_files(node_path, os.path.join(root, node), ext)
        elif ext is None or os.path.splitext(node_path)[1] == ext:
            files.append(os.path.join(root, node))

    return files


def generate_classpath(paths):
    return os.pathsep.join([p for p in paths if p is not None and len(p) > 0])


def package_to_dir(package):
    return package.replace('.', os.sep)


def dir_to_package(directory):
    return directory.replace(os.sep, '.')

def copy_dir(input, output):    
    copy_tree(input, output)    

def remove_dir(directory):    
    shutil.rmtree(directory, ignore_errors=True)       