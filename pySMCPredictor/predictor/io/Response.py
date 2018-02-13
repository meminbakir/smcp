'''
Created on Aug 26, 2016
@author: Mehmet Emin BAKIR

Loads and Manipulates Response from csv file
'''

from predictor.io.DataProcess import readCSV, modifyFileOrder
import pandas as pd

def getMCResponses(filePath):
    ''' Returns all model checkers from the filePath, which have 'FileOrder', 'ModelChecker', 'Avg_TotalT' '''

    df = readCSV(filePath)
    # make sure all nulls replaced with NaNs in average total time column
    df.loc[:, 'Avg_TotalT'] = df.loc[:, 'Avg_TotalT'].apply(lambda x: pd.to_numeric(x, errors='coerce'))
    # get the prefix of files as file order
    df = modifyFileOrder(df)
    # return only interested columns
    df = df.loc[:, ['FileOrder', 'ModelName', 'ModelChecker', 'Avg_TotalT']]
    return df


def getMCBestResponse(filePath):
    ''' Extracts fastest model checkers data which are 'FileOrder', 'ModelChecker', 'Avg_TotalT' from the filePath '''
    df = getMCResponses(filePath)
    df = df.ix[df.groupby(['FileOrder'], as_index=False)['Avg_TotalT'].idxmin()]
    # df = df.sort_values(by=['FileOrder', 'ModelName', 'Avg_TotalT']).head(1)

    return df

def getMCAllResponse(filePath):
    ''' Groups with model '''
    df = getMCResponses(filePath)
    df = df.sort_values(by=['FileOrder', 'ModelName', 'Avg_TotalT'])
    return df


if __name__ == '__main__':
    filePath = "/data/test.csv"
    df = getMCBestResponse(filePath)
    print(df)
    df = getMCAllResponse(filePath)
    print(df)
    print("Print YMER")
    mc = 'YMER'
    ymerTime = df[df.groupby('FileOrder')['ModelChecker'].apply(lambda x: x == mc)]
    print(ymerTime)
    print("Print groups")
    grouped = df.groupby('FileOrder')
    for name, group in grouped:
        print(name)
        print(type(group))
        print(group)
        print("Print YMER")
        print(group[group["ModelChecker"] == mc].Avg_TotalT)