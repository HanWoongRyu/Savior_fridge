# �ѱ��ھ�����

import cv2
import numpy as np
from numpy.lib.shape_base import dstack


def segmentate_w(image):
    img = image.copy()
    img = cv2.copyMakeBorder(img, 1, 1, 1, 1, cv2.BORDER_CONSTANT, value=[
                             255, 255, 255])  # ?��?��추�??
    img[img == 0] = 1
    img[img == 255] = 0

    start_col = []
    end_col = []
    all_col = 0
    tmp_col = 0

    window_location = []
    toggle = False

    h, w = img.shape
    projection = np.sum(img, axis=0)  # �?�? ?��?��
    for col in range(w):  # 범위 추정
        if projection[col] != 0:
            if projection[col-1] == 0:
                all_col += 1
            if toggle == False:
                toggle = True
                start_col.append(col)
        elif projection[col] == 0:
            if toggle == True:
                toggle = False
                end_col.append(col)
                distance = end_col[-1] - start_col[-1]
                if distance < 15: #버림
                    start_col.pop(-1)
                    end_col.pop(-1)
                elif 15 < distance < 35: #보류
                    toggle = True
                    tmp_col = end_col.pop(-1)
                elif distance > 50: #앞에꺼 잘라냄
                    start_col.pop(-1)
                    start_col.append(tmp_col)


    if not_ko(h, w, all_col):  # ?���? ?��?�� 경우
        return False

    count = len(end_col)
    for i in range(count):
        s = start_col.pop(0)
        e = end_col.pop(0)
        window_location.append([s, 0, e-s, h])

    return window_location


def not_ko(h, w, all_col):
  n = w / h
  N = all_col  # 추정 ?��?�� ?��
  if w < 150:
    if N > n + 2:
        return True
    else:
        return False


def segmentate_h(image):
    img = image.copy()
    img = cv2.copyMakeBorder(img, 1, 1, 1, 1, cv2.BORDER_CONSTANT, value=[
        255, 255, 255])  # ?��?��추�??
    img[img == 0] = 1
    img[img == 255] = 0

    start_row = []
    end_row = []
    distance =[]
    toggle = False

    h, w = img.shape
    projection = np.sum(img, axis=1)  # ?���? ?��?��
    for row in range(h):  # 범위 추정
        if projection[row] != 0:
            if toggle == False:
                toggle = True
                start_row.append(row)
        elif projection[row] == 0:
            if toggle == True:
                toggle = False
                end_row.append(row)
                distance.append(end_row[-1] - start_row[-1])
    
    dst = image[start_row[0]:end_row[-1], 0:w]

    avg = np.mean(distance)
    if avg > 40:
        index = distance.index(max(distance))
        dst = image[start_row[index]:end_row[index], 0:w]
        
    
    dst = cv2.copyMakeBorder(dst, 5, 5, 5, 5, cv2.BORDER_CONSTANT, value=[
                             255, 255, 255])  # ?��?��추�??

    return dst
