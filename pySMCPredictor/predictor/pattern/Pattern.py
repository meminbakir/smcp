'''
Created on Aug 29, 2016
@author: Mehmet Emin
'''
from enum import Enum
# import sys

# from predictor import config
from predictor.io.DataProcess import toCSV, writeToFile
from predictor.io.Features import getDPandNG4MC
from predictor.io.FeaturesEnum import FeaturesEnum
# from predictor.io.Logger import Logger
from predictor.io.Response import getMCBestResponse, getMCAllResponse
# from predictor.ml.Classifiers import Classifiers
import pandas as pd

class PatternEnum(Enum):
    EVENTUALLY = (1, 'Eventually')
    ALWAYS = (2, 'Always')
    FOLLOWS = (3, 'Follows')
    PRECEDES = (4, 'Precedes')
    NEVER = (5, 'Never')
    STEADY_STATE = (6, 'Steady State')
    UNTIL = (7, 'Until')
    INFINITELY_OFTEN = (8, 'Infinitely Often')
    NEXT = (9, 'Next')
    RELEASE = (10, 'Release')
    WEAK_UNTIL = (11, 'Weak Until')

    def __init__(self, order, prettyName="Eventually"):
        self.order = order
        self.prettyName = prettyName
        self.directory = None
        self.response465 = str(self.order) + "_" + self.getFullName() + "_465.csv"
        self.response210 = str(self.order) + "_" + self.getFullName() + "_210.csv"

    def __str__(self):
        return self.name.replace("_", " ").title()
    def getOrderName(self):
        return str(self.order) + "_" + self.name
    def setDirectory(self, directory):
        '''Add pattern directory to pattern file name'''
        if self.directory is None:
            self.directory = directory
            self.response465 = directory + self.response465
            self.response210 = directory + self.response210

    def getFullName(self):
        '''Returns proper name for pattern, e.g., WithPropEVENTUALLY'''
        return "WithProb" + self.name

class Pattern(object):
    ''' Patterns and their response series'''
    _response = None
    _features = None
    _X = None  # The features which are used as final predictors, ready to be used by classifier
    _y = None  # The response only, ready to be used by classifier

    def __init__(self, pattern, which="both", singleFile=True, directory="/data/"):
        '''Parameters: 
            pattern is PatternEnum 
            Which is, which response file we interested in, either 465 or 210 or both
            singleFile indicates whether DP and NG features are in different files or they are in single file
        '''
        self.pattern = pattern

        # update pattern file location with directory
        self.pattern.setDirectory(directory)
        if which == "465" or "210" or "both":
            self.which = which
        else:
            print("The response of 465 or 210, the which parameter {} is not valid.".format(self.which))
        self.singleFile = singleFile
        # directory is the path which are under the main project
        self.fileDP_465 = directory + "7_465GraphsOnly.csv"
        self.fileNG_465 = directory + "7_465NONGraphPropsWithProbEVENTUALLY.csv"
        self.fileDP_210 = directory + "DP_210.csv"
        self.fileNG_210 = directory + "NG_210.csv"
        self.fileDPandNG_465 = directory + "DPandNG_465.csv"  # if DP and NG are in single file
        self.fileDPandNG_210 = directory + "DPandNG_210.csv"  # if DP and NG are in single file

        # Output file
        self.outLogFile = self.pattern.getOrderName() + "Log.txt"

    def getMCAllResponse(self):
        '''Get responses of ALL Model Checkers, not just the fastest one'''
        df1 = getMCAllResponse(self.pattern.response465)
        df2 = getMCAllResponse(self.pattern.response210)
        assert df1.shape[1] == df2.shape[1], "Number of columns are not same"
        return df1.append(df2, ignore_index=True)
    @property
    def response(self):
        '''Get fastest Model Checkers name'''
        if self._response is None:  # if it is first time accessing to features by any instance
            if self.which == "465":
                self._response = getMCBestResponse(self.pattern.response465)
            elif self.which == "210":
                self._response = getMCBestResponse(self.pattern.response210)
            elif self.which == "both":
                df1 = getMCBestResponse(self.pattern.response465)
                df2 = getMCBestResponse(self.pattern.response210)
                assert df1.shape[1] == df2.shape[1], "Number of columns are not same"
                self._response = df1.append(df2, ignore_index=True)  # Append on row basis

        return self._response
    @property
    def features(self):
        ''' Features will be concatenation of DP and NG properties'''
        if self._features is None:  # if it is first time accessing to features by any instance
            if self.which == "465":
                if self.singleFile:  # if NG and DP are in one file
                    self._features = getDPandNG4MC(singleFile=self.singleFile, fileDPandNG=self.fileDPandNG_465)
                else:  # if NG and DP in two different files
                    self._features = getDPandNG4MC(singleFile=self.singleFile, fileDP=Pattern.fileDP_465, fileNG=Pattern.fileNG_465)
            elif self.which == "210":
                if self.singleFile:
                    self._features = getDPandNG4MC(singleFile=self.singleFile, fileDPandNG=self.fileDPandNG_210)
                else:
                    self._features = getDPandNG4MC(singleFile=self.singleFile, fileDP=Pattern.fileDP_210, fileNG=Pattern.fileNG_210)
            elif self.which == "both":
                if self.singleFile:
                    df1 = getDPandNG4MC(singleFile=self.singleFile, fileDPandNG=self.fileDPandNG_465)
                    df2 = getDPandNG4MC(singleFile=self.singleFile, fileDPandNG=self.fileDPandNG_210)
                    assert df1.shape[1] == df2.shape[1], "Number of columns are not same"
                    self._features = df1.append(df2, ignore_index=True)  # Append on row basis
                else:
                    df1 = getDPandNG4MC(singleFile=self.singleFile, fileDP=Pattern.fileDP_465, fileNG=Pattern.fileNG_465)
                    df2 = getDPandNG4MC(singleFile=self.singleFile, fileDP=Pattern.fileDP_210, fileNG=Pattern.fileNG_210)
                    assert df1.shape[1] == df2.shape[1], "Number of columns are not same"
                    self._features = df1.append(df2, ignore_index=True)  # Append on row basis

        return self._features

    @property
    def feature(self):
        if self._X is None:
            self._X = self.features.ix[:, (FeaturesEnum.DP.features + FeaturesEnum.NG.features)]  # @UndefinedVariable
        return self._X
    @property
    def y(self):
        if self._y is None:
            self._y = self.response.ix[:, "ModelChecker"].values.reshape(-1)  # y is now 'numpy.ndarray'
        return self._y

    def report(self):
        allDF = [classifier.report() for classifier in self.classifiers]
        result = pd.concat(allDF, ignore_index=True)
        result.insert(0, "Pattern", self.pattern.getOrderName())
        result.sort_values(by="cvScore")
        return result
    def out(self, content):
        print(content)
        writeToFile(self.outLogFile, content + "\n", directory="results")

if __name__ == '__main__':
    directory = "/data/"
    pattern = Pattern(PatternEnum.EVENTUALLY, which="both", singleFile=True, directory=directory)
    toCSV(pattern.response, directory=directory, fileName=(pattern.pattern.name + "Response.csv"))
    toCSV(pattern.features, directory=directory, fileName=(pattern.pattern.name + "Features.csv"))
    toCSV(pattern.getMCAllResponse(), fileName=(pattern.pattern.name + "AllFeatures.csv"))



