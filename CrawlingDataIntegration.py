# 크롤링 데이터 통합하기
import pandas as pd
import os

camp_DB = pd.read_csv('camp.csv', sep=';', encoding='utf-8')
camp_name = list(camp_DB['캠핑장이름'])
camp_cseq = list(camp_DB['cseq'])


# csv 파일 통합 후 ratingList.csv 생성하기
rating_all = pd.DataFrame({'캠핑장':[], '회원':[], '평점':[], '캠핑장ID':[]})
for i in range(len(camp_name)):
    tmp_rating_file = 'temp/rating' + str(i+1) + '.csv'
    if os.path.exists(tmp_rating_file):
        tmp_rating = pd.read_csv(tmp_rating_file, sep=';', encoding='utf-8')
        if len(rating_all) == 0:
            rating_all = tmp_rating
        else:
            rating_all = pd.concat([rating_all, tmp_rating])


if len(rating_all) != 0:
    rating_file = 'ratingList.csv'
    rating_all.reset_index(inplace=True)
    rating_all.drop('index', axis=1, inplace=True)
    rating_all.to_csv(rating_file, sep=';', encoding='utf-8-sig', index=False)

    while True:
        if os.path.exists(rating_file):
            break

    for i in range(len(camp_name)):
        tmp_rating_file = 'temp/rating' + str(i+1) + '.csv'
        if os.path.exists(tmp_rating_file):
            os.remove(tmp_rating_file)
    
    stop_command = '/temp/crawling_stop'
    if os.path.exist(stop_command):
        os.remove(stop_command)
        
    crawling_status = '/temp/crawling_status'
    if os.path.exist(crawling_status):
        os.remove(crawling_status)

print('success')   