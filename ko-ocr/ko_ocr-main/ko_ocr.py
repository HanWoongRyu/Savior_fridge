import cv2
from tqdm import tqdm
from image_deskew import deskew #return img
from image_preprocessing import * #return img
from image_detection import detect #return ary
from image_segmentation import * #return ary
from model_apply import model_apply
from text_post_processing import find_word

import time

def ko_ocr(img):
    word_list = []
    img = deskew(img) #전처리
    img = preprocess_word(img)
    img = preprocess_line(img)

    detected_location = detect(img) #영역추출
    img = resize_img(img, detected_location)
    detected_location = detect(img)
    cv2.imwrite('resized.png', img)

    # for l in detected_location:
    #     if l[2]>100 and l[3]<70 : #가로길이
    #         cv2.rectangle(img, (l[0], l[1]), ((l[0]+l[2]-1), (l[1]+l[3]-1)), (0, 255, 0),1)
    # cv2.imshow("teddy-bear", img)
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()

    for l in tqdm(detected_location):
        if l[2] > 100 and l[3] < 70: #한줄씩
            dst = img[l[1]: (l[1] + l[3]), l[0]: (l[0] + l[2])]
            segmentated_location = segmentate_w(dst) #글자단위로
            if segmentated_location == False: #숫자인 ff경우 
                continue
            word = ""

            for l in segmentated_location: 
                    dst2 = dst[l[1]: (l[1] + l[3]), l[0]: (l[0] + l[2])]
                    dst2 = segmentate_h(dst2)
                    word = word + model_apply(dst2)

                    # cv2.imshow("teddy-bear", dst2)
                    # cv2.waitKey(0)
                    # cv2.destroyAllWindows()
                    # print(word)

            word = word + "\n"
            word_list.append(word)
    return word_list


def main():
    start = time.time()

    img_path = r"a6.png"
    img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)

    f = open("word_list.txt", 'w', encoding = 'utf-8')
    for word in tqdm(ko_ocr(img)):  # ocr
        ans = find_word(word)
        if ans[0] != "null":
            f.write(ans[0]+','+str(ans[1])+'\n')
            print(ans)
    f.close()

    print("time: ", time.time() - start)

    # f = open("word_list.txt", 'r', encoding='utf-8')
    # lines = f.readlines()
    # for line in lines:
    #     print(line)
    # f.close()

if __name__ == "__main__":
    main()
