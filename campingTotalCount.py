# 캠핑장 전체 숫자를 반환

# 준비
import json
import urllib.request
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


# 전체 개수 가져오기
numOfRows = 1
pageNo = 1

url = url_base + url_scanType + url_serviceKey + serviceKey + url_numOfRows + str(numOfRows) + url_pageNo + str(pageNo) + url_mobileOS + url_mobileApp + url_dataType

result_obj = urllib.request.urlopen(url)
result_json = result_obj.read().decode('utf-8')
result_dict = json.loads(result_json)

totalCount = int(result_dict['response']['body']['totalCount'])

print(totalCount)