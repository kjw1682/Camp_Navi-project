# 준비
import pandas as pd
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
import time
import os
import sys

camp_DB = pd.read_csv('camp.csv', sep=';', encoding='utf-8')
camp_name = list(camp_DB['캠핑장이름'])
camp_cseq = list(camp_DB['cseq'])

stop_command = 'temp/crawling_stop'

# 크롤링
status_file = 'temp/crawling_status.csv'
count = 0
countDF = None
if os.path.exists(status_file):
    countDF = pd.read_csv(status_file, encoding='utf-8')
    count = countDF.iloc[0]['count']
else:
    countDF = pd.DataFrame({'count':[count], 'totalCount':[len(camp_name)]})
countDF.to_csv(status_file, encoding='utf-8-sig', index=False)

from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
import time

driver = webdriver.Chrome()
driver.get('https://www.google.co.kr/maps')
time.sleep(2)

search_bar = driver.find_element(By.CSS_SELECTOR, 'input#searchboxinput')
actions = ActionChains(driver)

isStopped = False
for i in range(count, len(camp_name)):
    if os.path.exists(stop_command):
        isStopped = True
        os.remove(stop_command)
        break
        
    camp_list = []
    camp_ids = []
    persons = []
    scores = []
    
    isError = False
    search_bar.send_keys(camp_name[i] + '\n')
    time.sleep(5)

    try:
        if driver.find_element(By.CLASS_NAME, 'Q2vNVc'):
            search_bar.clear()
            time.sleep(3)
            isError = True
    except:
        pass
    
    if not isError:
        if driver.find_elements(By.CLASS_NAME, 'hh2c6'):
                index = 1

        else:
            for c in range(0, 5):
                # 스크롤바 설정
                scroll = driver.find_element(By.XPATH, '//*[@id="QA0Szd"]/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div[1]')
                # 캠핑장 목록에서 이름 리스트 설정
                camps = scroll.find_elements(By.CLASS_NAME, 'hfpxzc')
                index = 0
                for camp in camps:
                    if camp_name[i] in camp.get_attribute('aria-label') or camp.get_attribute('aria-label') in camp_name[i]:
                        camp.click()
                        time.sleep(3)
                        index = 1
                        break
                    else:
                        continue
                if index == 1:
                    break
                else:
                    try:
                        scroll.send_keys(Keys.PAGE_DOWN)
                        time.sleep(3)
                    except:
                        pass
                        
        
        if index == 1:
            buttons = driver.find_elements(By.CLASS_NAME, 'hh2c6')
                
            for button in buttons:
                if '리뷰' in button.get_attribute('aria-label'):
                    actions.move_to_element(button)
                    actions.click().perform()
                    time.sleep(3)
                        
                    reviewer_name = driver.find_elements(By.CLASS_NAME, 'd4r55') # 사람이름이 있는 태그 저장
                    for name in reviewer_name:
                        persons.append(name.text) # 태그에서 사람 이름만 뽑아서 리스트에 저장
                        camp_list.append(camp_name[i])
                        camp_ids.append(camp_cseq[i])
        
                    if driver.find_elements(By.CLASS_NAME, 'fzvQIb'):
                        review_scores = driver.find_elements(By.CLASS_NAME, 'fzvQIb')
                        for score in review_scores:
                            scores.append(score.text)
                    else:
                        review_scores = driver.find_elements(By.CLASS_NAME, 'kvMYJc')
                        for score in review_scores:
                            scores.append(score.get_attribute('aria-label').split(' ')[1][0:1])
                        
                    search_bar.clear()
                    time.sleep(3)
                    break
                else:
                    continue
        
            search_bar.clear()
            time.sleep(3)
        else:
            search_bar.clear()
            time.sleep(3)
    
    rating = pd.DataFrame({'캠핑장':camp_list, '회원':persons, '평점':scores, '캠핑장ID':camp_ids})
    tmp_rating_file = 'temp/rating' + str(i+1) + '.csv'
    rating.to_csv(tmp_rating_file, encoding='utf-8-sig', sep=';', index=False)
    countDF.iloc[0]['count'] = i+1
    countDF.to_csv(status_file, encoding='utf-8-sig', index=False)

driver.close()
if isStopped:
    print('stopped')
else:
    os.remove(status_file)
    print('success')