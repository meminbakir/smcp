'''
Created on 3 Aug 2016

@author: Mehmet Emin BAKIR
'''

class Logger(object):
    '''Custom Logger'''
    def __init__(self, *files):
        self.files = files
    def write(self, obj):
        for f in self.files:
            f.write(obj)
            f.flush()
    def flush(self) :
        for f in self.files:
            f.flush()
