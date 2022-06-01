import socket
import cv2
from tqdm import tqdm
from ko_ocr import ko_ocr
from text_post_processing import find_word


def ocr(img_path):
    data = ""

    img = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)


    for word in tqdm(ko_ocr(img)):  # ocr
        ans = find_word(word)
        print(word, ans)
        if ans[0] != "null":
            data = data + str(ans[0]) + "," + str(ans[1]) + ","
    return data

def main():
    host = "" # 자신의 ip쓰기 !
    port = 8080

    server_sock = socket.socket(socket.AF_INET)
    server_sock.bind((host,port))
    server_sock.listen(10)

    def get_bytes_stream(sock, length) :
        buffer = b''
        try:
            remain = length
            while True :
                data = sock.recv(remain)
                buffer += data
                if len(buffer) == length :
                    break
                elif len(buffer) < length:
                    remain = length - len(buffer) # buf ? 

        except Exception as e :
            print(e)
        return buffer [:length]

    while True : 
        print("기다리는중")
        client_sock, addr = server_sock.accept()
        print("연결완료")

        len_bytes_stirng = bytearray(client_sock.recv(1024))[2:]
        len_bytes = len_bytes_stirng.decode("utf-8")
        length = int(len_bytes)

        img_bytes = get_bytes_stream(client_sock, length)
        img_path = "test1.png"

        with open(img_path, "wb") as writer :
            writer.write(img_bytes)
            print(img_path+" is saved")
        

        # print("보내졌나?")
        # data = "우유,1900-01-01,바나나,2000-01-01,사과,1800-01-01,우유,1900-01-01,바나나,2000-01-01,사과,1800-01-01"

        data = ocr(img_path)

        print(data)
        client_sock.send(data.encode("utf-8"))


if __name__ == "__main__":
    main()
