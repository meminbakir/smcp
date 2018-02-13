'''
Created on Aug 28, 2016
@author: Mehmet Emin BAKIR


Read and Write CSV files
'''
import os

from predictor import config
import pandas as pd


def readCSV(path):
    path = config.PROJECT_ROOT + os.sep + path
    try:
        df = pd.read_csv(path)
        # Strip spaces from header
        df.columns = df.columns.str.strip()
        if (len(pd.isnull(df).any(1).nonzero()[0]) > 0):
            print("There are some NaNs, replaced with 0.0")
            df = df.fillna(0.0)
    except:
        exit(path + " not found")
    return df

def toCSV(df, directory="test/data", fileName="default.csv", sep=",", mode='a'):
    '''Write to CSV without Index column, mode can be write (w) or append(a, default), or any other file modes'''
    csvFilePath = config.PROJECT_ROOT + os.sep + directory + os.sep + fileName
    if not os.path.isfile(csvFilePath):
        df.to_csv(csvFilePath, mode=mode, sep=sep, index=False,)
    elif len(df.columns) != len(pd.read_csv(csvFilePath, nrows=1, sep=sep).columns):
        raise Exception("Columns do not match!! Dataframe has " + str(len(df.columns)) + " columns. CSV file has " + str(len(pd.read_csv(csvFilePath, nrows=1, sep=sep).columns)) + " columns.")
    elif not (df.columns == pd.read_csv(csvFilePath, nrows=1, sep=sep).columns).all():
        raise Exception("Columns and column order of dataframe and csv file do not match!!")
    else:
        header = False if mode == "a" else True
        df.to_csv(csvFilePath, mode=mode, sep=sep, index=False, header=header)

def toNumeric(df, startCol="Avg_TotalT", endCol="Avg_TotalT"):
    ''' Converts object type columns to numeric, replaces NaN if not convertible, e.g. null or empty '''
    # following columns are object and need to be converted to numeric first to replace nulls with NaNs
    df.loc[:, startCol:endCol] = df.loc[:, startCol:endCol].applymap(lambda x: pd.to_numeric(x, errors='coerce'))
    return df

def modifyFileOrder(df):
    ''' Split the Model name and get its order, then fill FileOrder with true order. '''
    import traceback
    try:
        if df['ModelName'].str.contains('_').all():
            fileOrder = df['ModelName'].str.split('_').str.get(0)
            df['FileOrder'] = pd.to_numeric(fileOrder)  # replace new one with numeric values
            # P.S Do not sort them here, it causes some future problems
        else:
            # print('We assume FileOrder is already sorted for this Response')
            pass
    except:
        print("Error while renaming the FileOrder column")
        print(traceback.format_exc())
        exit()
    return df


def writeToFile(file="defaultLogs.txt", content=None, directory="test/data"):
    file = config.PROJECT_ROOT + os.sep + directory + os.sep + file
    with open(file, 'a') as file_:
        file_.write(content)
