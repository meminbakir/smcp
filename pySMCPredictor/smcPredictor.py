'''
Created on Aug 30, 2016
@author: Mehmet Emin BAKIR

Predicts the fastest SMC based on given property pattern and topological model features
'''
from io import StringIO
import os
import pickle
import sys
import warnings

from sklearn import preprocessing
from sklearn.ensemble.forest import ExtraTreesClassifier
from sklearn.svm import SVC
from sklearn.utils import shuffle
from predictor.pattern.Pattern import PatternEnum, Pattern
import pandas as pd
import predictor.config as config


def warn(*args, **kwargs):
    pass


warnings.warn = warn


def out(content, directory="results", outputFile="PredictionLogs.txt"):
    '''Prints and Logs the content'''
    print(content)
    file = config.PROJECT_ROOT + os.sep + directory + os.sep + outputFile
    with open(file, 'a') as file_:
        file_.write(content)


def processData(X, preProcessName="NONE"):
        processedX = X.copy()
        preprocessor = None;
        assert not len(pd.isnull(processedX).any(1).nonzero()[0]) > 0, "NaN values should have already been removed."  # feature = feature.fillna(0.0)
        if preProcessName is "SCALE":
            preprocessor = preprocessing.StandardScaler().fit(processedX)
            processedX = preprocessor.transform(processedX)
        elif preProcessName is "NORMALIZE":
            preprocessor = preprocessing.Normalizer().fit(processedX)
            processedX = preprocessor.transform(processedX)
        return processedX, preprocessor


class BestClassifiers(object):
    '''
    SVM models of different patterns. It loads model and enable prediction with new data.
    '''

    def __init__(self, patternEnum=PatternEnum.EVENTUALLY):
        '''
        Initialize pattern's object with corresponding model file name and the best SMV classifier and pre-processing method identified before.
        '''
        self.patternEnum = patternEnum;
        self.pattern = Pattern(patternEnum)
        modelFile = str(patternEnum.order) + "_" + patternEnum.getFullName() + ".pkl"
        self.modelFile = config.PROJECT_ROOT + os.sep + "models" + os.sep + modelFile ;
        self.preProcessMethod = "NONE";
        if(patternEnum == PatternEnum.EVENTUALLY):
            self.maxRandState = 196558;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=1000.0, cache_size=200, class_weight=None, coef0=0.0,
                decision_function_shape=None, degree=3, gamma=0.1, kernel='rbf',
                max_iter=-1, probability=False, random_state=None, shrinking=True,
                tol=0.001, verbose=False)
        elif(patternEnum == PatternEnum.ALWAYS):
            self.maxRandState = 124255;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
               max_depth=None, max_features=None, max_leaf_nodes=None,
               min_samples_leaf=1, min_samples_split=2,
               min_weight_fraction_leaf=0.0, n_estimators=50, n_jobs=1,
               oob_score=False, random_state=0, verbose=0, warm_start=False)
        elif(patternEnum == PatternEnum.FOLLOWS):
            self.maxRandState = 196588;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=1000.0, cache_size=200, class_weight=None, coef0=0.0,
              decision_function_shape=None, degree=3, gamma=1.0, kernel='rbf',
              max_iter=-1, probability=False, random_state=None, shrinking=True,
              tol=0.001, verbose=False)
        elif(patternEnum == PatternEnum.PRECEDES):
            self.maxRandState = 187708;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
               max_depth=None, max_features=None, max_leaf_nodes=None,
               min_samples_leaf=1, min_samples_split=2,
               min_weight_fraction_leaf=0.0, n_estimators=100, n_jobs=1,
               oob_score=False, random_state=0, verbose=0, warm_start=False)
        elif(patternEnum == PatternEnum.NEVER):
            self.maxRandState = 182526;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
                max_depth=None, max_features=None, max_leaf_nodes=None,
                min_samples_leaf=1, min_samples_split=2,
                min_weight_fraction_leaf=0.0, n_estimators=200, n_jobs=1,
                oob_score=False, random_state=0, verbose=0, warm_start=False)
        elif(patternEnum == PatternEnum.STEADY_STATE):
            self.maxRandState = 119746;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=10000000.0, cache_size=200, class_weight=None, coef0=0.0,
              decision_function_shape=None, degree=3, gamma=0.0001, kernel='rbf',
              max_iter=-1, probability=False, random_state=None, shrinking=True,
              tol=0.001, verbose=False)
        elif(patternEnum == PatternEnum.UNTIL):
            self.maxRandState = 114007;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
               max_depth=None, max_features=None, max_leaf_nodes=None,
               min_samples_leaf=1, min_samples_split=2,
               min_weight_fraction_leaf=0.0, n_estimators=30, n_jobs=1,
               oob_score=False, random_state=0, verbose=0, warm_start=False)
        elif(patternEnum == PatternEnum.INFINITELY_OFTEN):
            self.maxRandState = 150000;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
               max_depth=None, max_features='log2', max_leaf_nodes=None,
               min_samples_leaf=1, min_samples_split=2,
               min_weight_fraction_leaf=0.0, n_estimators=10, n_jobs=1,
               oob_score=False, random_state=0, verbose=0, warm_start=False)
        elif(patternEnum == PatternEnum.NEXT):
            self.maxRandState = 173977;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=100.0, cache_size=200, class_weight=None, coef0=0.0,
              decision_function_shape=None, degree=3, gamma=1.0, kernel='rbf',
              max_iter=-1, probability=False, random_state=None, shrinking=True,
              tol=0.001, verbose=False)
        elif(patternEnum == PatternEnum.RELEASE):
            self.maxRandState = 105454;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=10000000.0, cache_size=200, class_weight=None, coef0=0.0,
              decision_function_shape=None, degree=3, gamma=0.0001, kernel='rbf',
              max_iter=-1, probability=False, random_state=None, shrinking=True,
              tol=0.001, verbose=False)
        elif(patternEnum == PatternEnum.WEAK_UNTIL):
            self.maxRandState = 163090;  # random seed to shuffle data for training
            self.preProcessMethod = "NONE";
            self.clf = \
            ExtraTreesClassifier(bootstrap=False, class_weight=None, criterion='gini',
               max_depth=None, max_features=None, max_leaf_nodes=None,
               min_samples_leaf=1, min_samples_split=2,
               min_weight_fraction_leaf=0.0, n_estimators=30, n_jobs=1,
               oob_score=False, random_state=0, verbose=0, warm_start=False)

    def getModel(self):
        "Get the classifier, and the unique class labels (the class names)"
        try:
#             print("self.modelFile",self.modelFile)
            clf, preprocessor = pickle.load(open(self.modelFile, "rb"))
#             print("Classifier found. It is loading.")
            return clf, preprocessor;
        except (OSError, IOError):  # Model does not exist, first train then save it
#             print("Classifier not found. New classifier is training.")
            X, preprocessor = processData(self.pattern.feature, self.preProcessMethod);
            # shuffle data
            shuffled_X, shuffled_y = shuffle(X, self.pattern.y, random_state=self.maxRandState)
            self.clf.fit(shuffled_X, shuffled_y)
            # save the model
            pickle.dump((self.clf, preprocessor), open(self.modelFile, "wb"))
            return self.clf, preprocessor;
        except Exception as e:
            print(e)

    def predict(self, properties):
        clf, preprocessor = self.getModel()
        if preprocessor:
            properties = preprocessor.transform(properties)  # apply the pre-processing method done for training date
        targetMC = clf.predict(properties)
        return targetMC
# end of class


if __name__ == '__main__':
    # print("Accessed the main method of the SMC Predictor")
    try:
        patternName = sys.argv[1]
        patternEnum = None
        for tempEnum in PatternEnum:
            if tempEnum.getFullName() == patternName:
                patternEnum = tempEnum
                break;

        properties = sys.argv[2]
        properties = StringIO(properties);
        # data = data.split(",")
        properties = pd.read_csv(properties, header=None)
        # data = pd.Series(data).str.split(",")
        bestClassifier = BestClassifiers(patternEnum);
        targetMC = bestClassifier.predict(properties);
        print(targetMC[0])
    except Exception as e:
        print("Error!\n")
        print(e)
        print("Usage: patternName,properties (as csv string)")
