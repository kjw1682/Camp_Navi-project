import sys
import pandas as pd
import os

file_name = 'Review.csv'
data = pd.read_csv(file_name, encoding='utf-8')
data.set_index('vseq', inplace=True)

tmp_file = sys.argv[1]
delete_review_data_raw = pd.read_csv(tmp_file, encoding='utf-8')
vseq_list = list(delete_review_data_raw['vseq'])
vseq_list

data.drop(vseq_list, inplace=True)

data.to_csv(file_name, sep=',', encoding='utf-8-sig')

while True:
    if os.path.exists(file_name):
        break

print('success')