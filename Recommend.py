import pandas as pd
import numpy as np
from surprise import SVD
from surprise import Dataset
from surprise import Reader
from surprise.model_selection import GridSearchCV
from surprise.dataset import DatasetAutoFolds
import joblib
import os
import sys
import warnings; warnings.filterwarnings('ignore')

model_file = 'model.pkl'
svd = joblib.load(model_file)


def recomm_camp_by_surprise(svd, member, camp_list, top_n):
    # svd 객체에 unseen_movies 를 입력하여 예측평점 계산
    predictions = [svd.predict(str(member), str(camp)) for camp in camp_list]
    
    def sortkey_est(pred):
        return pred.est
    
    #sortkey_est() : 변환값의 내림차순으로 정렬 수행
    predictions.sort(key=sortkey_est, reverse=True)
    
    if top_n == 0 :
        top_n = len(camp_list)
    top_predictions = predictions[:top_n]
    
    # top_n 으로 추출된 영화의 정보 추출 : iid = cseq
    top_camp_cseq = [int(pred.iid) for pred in top_predictions]
    top_camp_rate = [pred.est for pred in top_predictions]
    
    top_camp_preds = pd.DataFrame({'cseq':top_camp_cseq, 'rate':top_camp_rate})
    
    return top_camp_preds


member = int(sys.argv[1])

target_camp_file = 'tmp_filtered.csv'
camp_list_raw = pd.read_csv(target_camp_file, sep=';', encoding='utf-8')

camp_list = list(camp_list_raw['cseq'])
result = recomm_camp_by_surprise(svd, member, camp_list, 0)
result.set_index('cseq', inplace=True)

review_file = 'Review.csv'
if not os.path.exists(review_file):
    tmp_review = pd.DataFrame({'vseq':[], 'member':[], 'camp':[], 'rate':[]})
    tmp_review.to_csv(review_file, encoding='utf-8', index=False)
    while True:
        if os.path.exists(review_file):
            break
    
review_data_raw = pd.read_csv(review_file, encoding='utf-8', sep=',')
review_data_raw.rename(columns={'member':'mseq', 'camp':'cseq'}, inplace=True)
review_data = review_data_raw.copy()
review_data.set_index(['mseq', 'cseq'], inplace=True)
review_data.sort_values(by='vseq', ascending=True, inplace=True)


# 기록된 평점이 있을 경우 가장 최신 평점을 가져온다.
predict = []
for i in result.index:
    try:
        if type(review_data.loc[member, i]['rate']) == np.float64:
            result.loc[i]['rate'] = review_data.loc[member, i]['rate']
        else:
            result.loc[i]['rate'] = review_data.loc[member, i]['rate'].iloc[-1]
        predict.append('n')
    except:
        predict.append('y')

result['predict'] = predict
result.sort_values(by='rate', ascending=False, inplace=True)

result_file = 'recommend.csv'
result.to_csv(result_file, sep=',', encoding='utf-8-sig')

while True:
    if os.path.exists(result_file):
        break
        
print('success')