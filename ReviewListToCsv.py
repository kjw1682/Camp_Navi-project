import sys
import pandas as pd
import os

file_name = 'Review.csv'
new_data = pd.read_csv(sys.argv[1], encoding='utf-8')
new_data.set_index('vseq', inplace=True)
data = pd.DataFrame()

try:
    if os.path.exists(file_name):
        original_data = pd.read_csv(file_name, encoding='utf-8')
        original_data.set_index('vseq', inplace=True)
        for i in new_data.index:
            original_data.loc[i] = new_data.loc[i]
        data = original_data
    else:
        data = new_data
except:
    data = new_data
    
data.to_csv(file_name, sep=',', encoding='utf-8-sig')

while True:
    if os.path.exists(file_name):
        break

print('success')