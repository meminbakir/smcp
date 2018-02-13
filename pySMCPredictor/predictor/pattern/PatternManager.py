'''
Created on Aug 28, 2016
@author: Mehmet Emin BAKIR
'''
from predictor.pattern.Pattern import Pattern, PatternEnum
import pandas as pd

class PatternManager(object):
    '''
    Creates and initialize Patterns
    '''
    def __init__(self, gridParams):
        '''
        Constructor
        '''

    @staticmethod
    def getAllPatterns(which="both", singleFile=True, directory="/data/"):
        allPatterns = []
        for patternEnum in PatternEnum:
            pattern = Pattern(patternEnum, which=which, singleFile=singleFile, directory=directory)
            allPatterns.append(pattern)
        return allPatterns

    @staticmethod
    def performExperiments(pattern):
        # search and find best classifiers, dump the best one to a file
        # assignBestClassifier(pattern)
        # perform 10 cross validation with the best classifiers, assign results to the pattern
        # split test and validations and test with the classifiers, assign results to the pattern
        # performance loss, compare best with worst, tabulate for each pattern
        pass

    @staticmethod
    def report(patterns=None):
        if patterns is None:
            patterns = PatternManager.getAllPatterns()
        if isinstance(patterns, list):
            allDF = [pattern.report() for pattern in patterns]
            result = pd.concat(allDF, ignore_index=True)
        else:
            result = patterns.report()
        return result

if __name__ == '__main__':
    allPatterns = PatternManager.getAllPatterns()
    for i, pattern in enumerate(allPatterns):
        print(i + 1, pattern.pattern.name)
