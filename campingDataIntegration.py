# campingData.csv 파일을 생성한다.

import sys
import os
import pandas as pd
import warnings; warnings.filterwarnings('ignore')

# 만들어진 csv 파일을 통합하기 (임시파일은 삭제)
totalPage = int(sys.argv[1])
campingDataAll = None
for i in range(totalPage):
    tmp_filename = 'temp/tmp_campingData' + str(i+1) + '.csv'
    tmp_campingData = pd.read_csv(tmp_filename, sep=';', encoding='utf-8')
    if i == 0:
        campingDataAll = tmp_campingData
    else:
        campingDataAll = pd.concat([campingDataAll, tmp_campingData])
    if os.path.exists(tmp_filename):
        os.remove(tmp_filename)

campingDataAll.reset_index(inplace=True)
campingDataAll.drop(['index'], axis=1, inplace=True)

# campingData.csv 파일 생성하기
filename = 'campingData.csv'
campingDataAll.to_csv(filename, sep=';', index=False, encoding='utf-8-sig')

while True:
    if os.path.exists(filename):
        break

print('success')
