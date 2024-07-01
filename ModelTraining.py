import pandas as pd
import numpy as np
from surprise import SVD
from surprise import Dataset
from surprise import Reader
from surprise.model_selection import GridSearchCV
from surprise.dataset import DatasetAutoFolds
import joblib
import os
import warnings; warnings.filterwarnings('ignore')

# 초기 평점 데이터
rating_init_file = 'ratingList.csv'
rating_init_raw = pd.read_csv(rating_init_file, encoding='utf-8', sep=';')
rating_init = rating_init_raw[['회원', '캠핑장ID', '평점']]
rating_init.rename(columns={'회원':'member', '캠핑장ID':'camp', '평점':'rate'}, inplace=True)
rating_init.dropna(inplace=True)
rating_init['member'] = rating_init['member'].apply(lambda x: 'm'+str(hash(x)))

# camp번호 규격 정리
def refine_camp(x):
    try:
        x = str(x)[:-2]
    except:
        pass
    return int(x)

rating_init['camp'] = rating_init['camp'].apply(refine_camp)

# 평점 규격 정리
def refine_rate(x):
    try:
        x = str(x)
    except:
        pass
    index = x.find('/')
    if index != -1:
        x = x[:index]
    return float(x)

rating_init['rate'] = rating_init['rate'].apply(refine_rate)


# 새로 축적된 리뷰 데이터 (평점 포함)
rating_new_file = 'Review.csv'
if not os.path.exists(rating_new_file):
    tmp_review = pd.DataFrame({'vseq':[], 'member':[], 'camp':[], 'rate':[]})
    tmp_review.to_csv(rating_new_file, encoding='utf-8', index=False)
    while True:
        if os.path.exists(rating_new_file):
            break

rating_new = pd.read_csv(rating_new_file, encoding='utf-8', sep=',')
rating_new.set_index(['member', 'camp'], inplace=True)
rating_new.sort_values(by='vseq', ascending=True, inplace=True)
rating_new['rate'] = rating_new['rate'].apply(refine_rate)

indexes = []
for i in rating_new.index:
    if i not in indexes:
        indexes.append(i)

member_new = []
camp_new = []
rate_new = []

for (i, j) in indexes:
    member_new.append(str(i))
    camp_new.append(j)
    if type(rating_new.loc[(i, j)]['rate']) == np.float64:
        rate_new.append(rating_new.loc[(i, j)]['rate'])
    else :
        rate_new.append(rating_new.loc[(i, j)]['rate'].iloc[-1])

rating_renewal = pd.DataFrame({'member':member_new, 'camp':camp_new, 'rate':rate_new})



# 평점 데이터 통합
rating = pd.concat([rating_init, rating_renewal])
rating.reset_index(inplace=True)
rating.drop('index', axis=1, inplace=True)
rating['camp'] = rating['camp'].apply(lambda x: int(x))



# 최적화
reader = Reader(rating_scale=(0.5, 5.0))
data = Dataset.load_from_df(rating[['member', 'camp', 'rate']], reader)

# 최적화할 파라미터
param_grid = {'n_epochs':[20, 40, 60, 80, 100], 'n_factors':[50, 100, 150, 200]}

# CV를 3개의 폴드로 지정하고 rmse, mse 로 평가 수행
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(data)



# 최고의 RMSE 평가점수 매개변수 입력
best_epochs = gs.best_params['rmse']['n_epochs']
best_factors = gs.best_params['rmse']['n_factors']


# 모델 학습
rating_noh = rating.copy()
rating_noh_file = 'rating_noh.csv'
rating_noh.to_csv(rating_noh_file, sep=';', encoding='utf-8-sig', index=False, header=False)
while True:
    if os.path.exists(rating_noh_file):
        break

reader = Reader(line_format='user item rating', sep=';', rating_scale=(0.5, 5.0))
data_folds = DatasetAutoFolds(ratings_file=rating_noh_file, reader=reader)

# 전체 데이터를 학습데이터로 생성
trainset = data_folds.build_full_trainset()

# 학습
svd = SVD(n_epochs=best_epochs, n_factors=best_factors, random_state=0)
svd.fit(trainset)


# 결과 저장
model_file = 'model.pkl'
joblib.dump(svd, model_file)

while True:
    if os.path.exists(model_file):
        break

if os.path.exists(rating_noh_file):
    os.remove(rating_noh_file)

print('success')