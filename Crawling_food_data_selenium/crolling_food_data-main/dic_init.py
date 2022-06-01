# 자음 모음을 분리한 key로 key재생성

from jamo import h2j, j2hcj
import pickle

with open('86000~96000.pkl', 'rb') as f:
    mydict = pickle.load(f)

new_dic = dict()
for key, value in mydict.items():
    if key not in new_dic:
        new_dic[j2hcj(h2j(key))] = value

with open('dict.pkl', 'wb') as f:
    pickle.dump(new_dic, f)

print(new_dic)

