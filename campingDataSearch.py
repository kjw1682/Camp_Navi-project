# 페이지번호를 받아서 해당 페이지의 캠핑장 데이터를 csv 파일로 생성한다.

# 준비 작업
import pandas as pd
import json
import numpy as np
import urllib.request
import sys
import os
import warnings; warnings.filterwarnings('ignore')

url_base = 'http://apis.data.go.kr/B551011/GoCamping'
url_scanType = '/basedList?'
url_serviceKey = 'serviceKey='
url_mobileOS = '&MobileOS=WIN'
url_mobileApp = '&MobileApp=CampingNavi'
url_numOfRows = '&numOfRows='
url_pageNo = '&pageNo='
url_dataType = '&_type=json'

serviceKey = 'cSqs0wGkiNNILoYdHrUMd4ydoaavnQUkUF7ahPxZnrAQCj%2FtBUlv%2B4kmdxzncn7Dk%2BJcy8IadGRdrFJ2yje%2B6A%3D%3D'


# 100개 단위로 읽어서 csv 파일로 만들기
numOfRows = 100
pageNo = int(sys.argv[1])
url = url_base + url_scanType + url_serviceKey + serviceKey + url_numOfRows + str(numOfRows) + url_pageNo + str(pageNo) + url_mobileOS + url_mobileApp + url_dataType
result_obj = urllib.request.urlopen(url)
result_json = result_obj.read().decode('utf-8')
result_dict = json.loads(result_json)

# 컨텐츠 아이디
content_id = []
# 캠핑장 이름
camp_name = []
# 설립일
prmisnDe = []
# 주소1
addr1 = []
# 주소2(상세주소)
addr2 = []
# 위도
mapY = []
# 경도
mapX = []
# 캠핑장 종류
induty = []
# 도
doNm = []
# 시군구
sigunguNm = []

for tmp in result_dict['response']['body']['items']['item']:
    content_id.append(tmp['contentId'])
    camp_name.append(tmp['facltNm'])
    prmisnDe.append(tmp['prmisnDe'])
    addr1.append(tmp['addr1'])
    addr2.append(tmp['addr2'])
    mapY.append(tmp['mapY'])
    mapX.append(tmp['mapX'])
    induty.append(tmp['induty'])
    doNm.append(tmp['doNm'])
    sigunguNm.append(tmp['sigunguNm'])

df = pd.DataFrame({
'컨텐츠아이디' : content_id,
'캠핑장이름' : camp_name,
'인허가일자' : prmisnDe,
'주소1' : addr1,
'주소2' : addr2,
'위도' : mapY,
'경도' : mapX,
'캠핑장종류' : induty,
'도' : doNm,
'시군구' : sigunguNm
})

try:
    pageNo = str(pageNo)
except:
    pass
    
tmp_filename = 'temp/tmp_campingData' + pageNo + '.csv'    
df.to_csv(tmp_filename, encoding='utf-8-sig', index=False, sep=";")

while True:
    if os.path.exists(tmp_filename):
        break

print('success')