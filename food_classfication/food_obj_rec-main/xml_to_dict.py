import os
import xml.etree.ElementTree as ET
import pickle
 
path_dir = '/상품 이미지 데이터/Validation'
 
file_list1 = os.listdir(path_dir)
real_list = []
for file in file_list1:
    real_list.append(os.listdir(path_dir+'/'+file))
    print(len(os.listdir(path_dir+'/'+file)))
z=-1
item = {}
for i in real_list:
    z += 1
    for file in i:
        dir = os.listdir(path_dir + "/" + file_list1[z] + "/" + file)
        for j in dir:
            if "meta" in j:
                na = j
                break

        doc = ET.parse(path_dir +'/'+ file_list1[z] + "/" + file +"/" + j)
        root = doc.getroot()
        div = root.find("div_cd")
        if div is not None:
            item[div.find("item_cd").text] = div.find("img_prod_nm").text

print(len(item))
with open('data_dict.pkl', 'wb') as f:
    pickle.dump(item, f)        



# path_dir = 'C:/Users/MLYU/alsrb/project/mingyu/상품 이미지 데이터/Validation/[라벨]과자/10060_해태포키블루베리41G'

# print(os.listdir(path_dir)[1])
# doc = ET.parse(path_dir +'/'+os.listdir(path_dir)[1])
# root = doc.getroot()
# div = root.find("div_cd")
# item = {}
# item[div.find("item_cd").text] = div.find("img_prod_nm").text