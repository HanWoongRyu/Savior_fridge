# �̹��� ��ó��

import cv2
import numpy as np

def resize_img(image, detected_location):
    tmp = []
    height, width = image.shape[:2]

    for l in detected_location:
        if l[2]>100 and l[3]<70 : #가로길이
            tmp.append(l[3])
    avg = np.mean(tmp)        

    for l in detected_location:
        if l[0] > width*0.2: #C
            image[l[1]: (l[1] + l[3]), l[0]: (l[0] + l[2])] = 255

    dst = cv2.resize(image, (int(width*45/avg), int(height*45/avg)), interpolation=cv2.INTER_AREA)

    return dst

def preprocess_word(image):
    dst = cv2.GaussianBlur(image, (5, 5), 1.0)

    sharpening = np.array([[-1, -1, -1, -1, -1],
                           [-1, 2, 2, 2, -1],
                           [-1, 2, 9, 2, -1],
                           [-1, 2, 2, 2, -1],
                           [-1, -1, -1, -1, -1]]) / 9.0
    dst = cv2.filter2D(dst, -1, sharpening)

    ret, dst = cv2.threshold(dst, 147, 255, cv2.THRESH_BINARY)

    closing = np.ones((3, 3), np.uint8)  # dilation=>erosion
    dst = cv2.morphologyEx(dst, cv2.MORPH_CLOSE, closing)

    conv = np.array([[0, 1, 1], [0, 1, 0], [1, 1, 0]])
    dst = cv2.filter2D(dst, -1, conv)

    return dst


def preprocess_line(image):
    img = image.copy()
    dst = image.copy()
    img = cv2.copyMakeBorder(img, 1, 1, 1, 1, cv2.BORDER_CONSTANT, value=[
        255, 255, 255])  # ?��?��추�??
    dst = cv2.copyMakeBorder(img, 1, 1, 1, 1, cv2.BORDER_CONSTANT, value=[
        255, 255, 255])  # ?��?��추�??
    img[img == 0] = 1
    img[img == 255] = 0

    h, w = img.shape
    projection = np.sum(img, axis=1)  # ?���? ?��?��
    avg = np.mean(projection)
    for row in range(h):
        if projection[row] < avg*0.6:
           dst[row] = 255

    return dst

