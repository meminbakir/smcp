'''
Created on Aug 10, 2016

@author: Mehmet Emin BAKIR
'''
from io import StringIO
import os
import pickle
import sys

from sklearn import preprocessing
from sklearn.svm import SVC
from sklearn.utils import shuffle

import config
import numpy as np
import pandas as pd


def read_data(path):
    "csv can be either a csv file or csv formatted StringIO variable"
    try:
        #  print('start to read')
        path = config.PROJECT_ROOT + os.sep + path;
        df = pd.read_csv(path)
        # print("data read")
    except:
        print("-- Unable to read " + path)
        exit("-- Unable to read " + path)
    return df

def encode_response(df, response_column):
    """Add column to df with integers for the response.

    Args
    ----
    df -- pandas DataFrame.
    response_column -- column name to map to int, producing
                     new Response column.

    Returns
    -------
    df_mod -- modified DataFrame.
    responses -- list of response names.
    """
    df_mod = df.copy()
    responses = df_mod[response_column].unique()
    map_to_int = {name: n for n, name in enumerate(responses)}
    df_mod["Response"] = df_mod[response_column].replace(map_to_int)

    return (df_mod, responses)


def processData(X, processIt="NONE"):
    preprocessor = None;
    if (len(pd.isnull(X).any(1).nonzero()[0]) > 0):
        X = X.fillna(0.0)
    if processIt is "SCALE":
        preprocessor = preprocessing.StandardScaler().fit(X)
        X = preprocessing.scale(X)
    elif processIt is "NORMALIZE":
        preprocessor = preprocessing.Normalizer().fit(X)
        X = preprocessing.normalize(X)
    return X, preprocessor

def getMC(path="data", fileName="1_3_WithProbEVENTUALLY_minPD_minNG4.csv", processIt="NONE"):
    """ It reads csv file and converts response to numeric, and returns
    X as predictors (features) and
    y as response (target)
    responses is the unique name of response variables
    """
    path = path + os.sep + fileName
    df = read_data(path)

    response_column = "ModelChecker"  # original response column name
    response = "Response"  # the numeric presentation response_column
    predictors_columns = list(df.columns[:(df.shape[1] - 1)])  # df.shape[1] gives number of columns, we exclude response from predictors
    df2, responses = encode_response(df, response_column)
    y = df2[response]  # numeric Target or Response
    X = df2[predictors_columns]  # predictors or features
    X, preprocessor = processData(X, processIt);
        
    return (X, y, responses, preprocessor)

class SMVModels(object):
    '''
    SVM models of different patterns. It loads model and enable prediction with new data.
    '''
    def __init__(self, pattern="WithProbEVENTUALLY"):
        '''
        Initialize pattern's object with corresponding model file name and the best SMV classifier and pre-processing method identified before.
        '''
        self.pattern = pattern;
        self.modelFile = config.PROJECT_ROOT + os.sep + "models" + os.sep + pattern + ".pkl";
        self.preProcessMethod = "NONE";
        if(pattern == "WithProbEVENTUALLY"):
            self.trainingFile = "1_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 150425;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=10.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=10.0, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbALWAYS"):
            self.trainingFile = "2_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 195596;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=100000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=0.0001, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbFOLLOWS"):
            self.trainingFile = "3_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 169670;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=1000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=0.01, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbPRECEDES"):
            self.trainingFile = "4_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 193109;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=1000000000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=1e-06, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)            
        elif(pattern == "WithProbNEVER"):
            self.trainingFile = "5_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 182304;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=10000000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=1e-05, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbSTEADY_STATE"):
            self.trainingFile = "6_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 159178;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=1.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=1.0, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbUNTIL"):
            self.trainingFile = "7_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 167978;  # random seed to shuffle data for training
            self.preProcessMethod = "NORMALIZE";
            self.clf = \
            SVC(C=100000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=100.0, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)
        elif(pattern == "WithProbINFINITELY_OFTEN"):
            self.trainingFile = "8_" + "3_" + pattern + "_minPD_minNG4.csv";  # topologies of PD and NG
            self.randSeed = 175023;  # random seed to shuffle data for training
            self.preProcessMethod = "SCALE";
            self.clf = \
            SVC(C=1000.0, cache_size=200, class_weight=None, coef0=0.0,
            decision_function_shape=None, degree=3, gamma=0.1, kernel='rbf',
            max_iter=-1, probability=False, random_state=None, shrinking=True,
            tol=0.001, verbose=False)



    def getModel(self):
        "Get the classifier, and the unique class labels (the class names)"
        try:
            clf, responses, preprocessor = pickle.load(open(self.modelFile, "rb"))
            # print(responses)
            # print("Classifier found. It is loading.")
            return clf, responses, preprocessor;
        except (OSError, IOError):  # Model does not exist, first train then save it
            # print("Classifier not found. New classifier is training.")
            X, y, responses, preprocessor = getMC(fileName=self.trainingFile, processIt=self.preProcessMethod)  # read data
            # print(responses)
            # shuffle data
            indices = np.arange(len(X))
            shuffled_indices, shuffled_X, shuffled_y = shuffle(indices, X, y, random_state=self.randSeed)
            self.clf.fit(shuffled_X, shuffled_y)
            # save the model
            pickle.dump((self.clf, responses, preprocessor), open(self.modelFile, "wb"))
            # joblib.dump(self.clf, self.modelFile)
            # print("Classifier created")
            return self.clf, responses, preprocessor;
        except Exception as e:
            print(e)

    def predict(self, properties):
        # properties = processData(properties, processIt=self.preProcessMethod)
        clf, responses, preprocessor = self.getModel();
        properties = preprocessor.transform(properties)  # apply the pre-processing method done for training date
        index = clf.predict(properties);
        return responses[index], index;  # clf.predict(properties) return the index of predicted class, hence retrieves its name from response
        # return targetMC
# end of class
# def testAccuracy():
#     patternName = "WithProbALWAYS"
#     svmModel = SMVModels(patternName);
#     X, y, responses, _ = getMC();
#     X = X.as_matrix();
#     rows, cols = X.shape
#     prediction = [];
#     for i in range(rows):
#         properties = X[i, :];
#         targetMC, index = svmModel.predict(properties);
#         print("{0}. Prediction index {1} is {2}".format(i, index, targetMC))
#         prediction.append(index)
#     
#     from sklearn.metrics import accuracy_score
#     print("Accuracy score {0} with {1} correct prediction.".format(accuracy_score(y, prediction), accuracy_score(y, prediction, normalize=False)))
          
if __name__ == '__main__':
    # str="NaN,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,2,0,1,0.5,0,1,0.5,1,1,1,1,0,0,0.25,0,2,2,2,2,2,2,2,2,2";#ymer
    # str = "1849,43,43,43,43,43,43,43,85,85,85,1,0,0,1,1,43,44,0,43,0.977272727,0,1,0.977272727,1,43,1.954545455,1,1,0,0.022210744,0,1892,2,2,2,86,2,2,2,86"  # mc2
    # str = "503237,2539,1,1401,198.2028358,1,2309,198.2028358,1,3688,394.0803466,8,41,0,0.078063346,0.879784277,16502,1420,0,1102,11.62112676,0,1102,11.62112676,1,1144,23.24225352,2,71,0,0.008183892,0.20942916,3605380,2,4.719574636,27,11983,2,4.719574636,27,11983";
    try:
        patternName = sys.argv[1]
        properties = sys.argv[2]
        properties = StringIO(properties);
        # data = data.split(",")
        properties = pd.read_csv(properties, header=None)
        # data = pd.Series(data).str.split(",")
        svmModel = SMVModels(patternName);
        targetMC, _ = svmModel.predict(properties);
        print(targetMC[0])       
        # testAccuracy();
    except Exception as e:
        print("Error!\n")
        print(e)
        print("Usage: patternName,properties (as csv string)")
        
        
