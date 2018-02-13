'''
Created on Aug 28, 2016
@author: Mehmet Emin BAKIR

Loads and Manipulates features from csv files
'''

from predictor.io.DataProcess import readCSV, modifyFileOrder, toCSV
from predictor.io.FeaturesEnum import FeaturesEnum
import pandas as pd


# The following methods does not belongs to Features Enum class
def getFeatures(filePath, features):
    ''' Extracts Directed Pseudo Graph features from the file'''
    # Read all features from file and clean the data, reorder the File Order
    df = readCSV(filePath)
    # get the prefix of files as file order
    df = modifyFileOrder(df)
    # then sort with true ascending order.
    df = df.sort_values(by="FileOrder")
    # make sure all null valued features are replaced with NaNs, and they are numeric
    df.loc[:, features] = df.loc[:, features].applymap(lambda x: pd.to_numeric(x, errors='coerce'))  # df.loc[:, features]

    df = df.loc[:, (['FileOrder', 'ModelName'] + features)]  # Always include FileOrder and ModelName, exclude only in classifiers
    return df

def getDPFeatures(filePath, feautes=None):
    # Extract DP Only properties from all features.
    if feautes is None:
        features = FeaturesEnum.DP.features  # @UndefinedVariable

    return getFeatures(filePath, features)

def getNGFeatures(filePath, feautes=None):
    # Extract NG Only properties from all features, by default it EXCLUDES number of species, reactions, stoichiometric updates and queried vertices
    if feautes is None:
        features = FeaturesEnum.NG.features  # @UndefinedVariable

    return getFeatures(filePath, features)

def getDPandNG4MC(singleFile=True, fileDPandNG="mcpExtra210_DPandNG.csv", fileDP="/data/7_465GraphsOnly.csv", fileNG="/data/7_465NONGraphPropsWithProbEVENTUALLY.csv"):
    '''Get DP and NG in single Data frame
    Parameters: 
        singleFile indicates whether DP and NG are in single file or different files,
        if in single File and fileDPandNG should be set, otherwise fileDP and fileNG should be set separately.
    '''
    if singleFile:
        features = FeaturesEnum.DP.features + FeaturesEnum.NG.features  # @UndefinedVariable
        df = getFeatures(filePath=fileDPandNG, features=features)
    else:  # DP and NG are in different files
        dfDP = getDPFeatures(fileDP)
        dfNG = getNGFeatures(fileNG)
        # Number of rows should match
        assert len(dfDP) == len(dfNG), "Number of DP and NG rows should match"
        # remove the FileOrder and ModelName from second df, since it is already in the first one
        dfNG = dfNG.drop(['FileOrder', 'ModelName'], axis=1)
        df = pd.concat([dfDP, dfNG], axis=1)

    return df

if __name__ == '__main__':
    toCSV(getDPandNG4MC() , fileName="testDPNGConcat.csv")




