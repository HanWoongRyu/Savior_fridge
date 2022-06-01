#딕셔너리 내의 단어들중 가장일치하는 단어를 반환

import datetime
from jamo import h2j, j2hcj
from hangul_utils import join_jamos
from difflib import SequenceMatcher
import pickle
 
import time

def find_word(text): #단어를 찾고 유통기한을 계산하는 것까지
    word = j2hcj(h2j(text))
    with open('dict.pkl', 'rb') as f:
        mydict = pickle.load(f)

    tmp = 0
    ans = "null"
    mydict["null"] = "noData"
    for key, val in mydict.items():
        similarity = similar(word, key)
        if similarity > 0.67 and similarity > tmp:
            tmp = similarity
            ans = key

    if ans != "null":
        ans_val = calc_date(mydict[ans]).strftime('%Y-%m-%d %H:%M:%S')
        # print(str(tmp) + "% "+ str(join_jamos(str(ans)))+"  time:"+str(ans_val))
        ans = join_jamos(str(ans))
    else:
        ans_val = "noData"
    return ans, ans_val

def similar(a, b):
    return SequenceMatcher(None, a, b).ratio()

def calc_date(val):
    now = datetime.datetime.now()
    if val.endswith('년'):
        return now + datetime.timedelta(days = 365*int(val[:-1]))
    elif val.endswith('월'):
        return now + datetime.timedelta(days = 30*int(val[:-2]))
    elif val.endswith('일'):
        return now + datetime.timedelta(days = int(val[:-1]))
    elif val.endswith('시'):
        return now + datetime.timedelta(hours = int(val[:-1]))
        
    else:
        return print("no time value error!!!")


if __name__ == "__main__": # 테스트용
    start = time.time()

    print(find_word("매일우유"))

    print("time: ", time.time() - start)


