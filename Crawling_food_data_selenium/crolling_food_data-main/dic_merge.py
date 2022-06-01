import pickle

with open('86001~87000.pkl', 'rb') as f:
    dic1 = pickle.load(f)
with open('87001~90000.pkl', 'rb') as f:
    dic2 = pickle.load(f)
with open('90001~93000.pkl', 'rb') as f:
    dic3 = pickle.load(f)
with open('93001~96000.pkl', 'rb') as f:
    dic4 = pickle.load(f)

dic = dic1.copy()
dic.update(dic2)
dic.update(dic3)
dic.update(dic4)

dic_manual = {'깻잎':'4일', '포카칩어니언맛':'5개월', '스프라이트':'1년', '매일우유특별기획저지방':'15일'}
dic.update(dic_manual)

with open('86000~96000.pkl', 'wb') as f: #저장~$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    pickle.dump(dic, f)


print(dic)

