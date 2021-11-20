package com.example.myRefri02;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Item_Client extends AppCompatActivity{

    // 소켓통신에 필요한것
//   private String html = "";
    private Handler mHandler;
    public static final String UTF8_CHARSET= "UTF-8";
    private Socket socket;
    private static String STOP_MSG = "stop";

    public String teststring;

//    private BufferedReader networkReader;
//    private PrintWriter networkWriter;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.55.16";            // IP 번호
    private int port = 8080;                          // port 번호

    private Bitmap bitmap;
//    private String img_path;
//    private String mark;
//    private String shape;

    public String readString (DataInputStream dis) throws IOException{
        int length = dis.readInt();
        byte[] data = new byte[length];
        dis.readFully(data,0,length);
        String text = new String(data, UTF8_CHARSET);
        return text;
    }

    public Item_Client(Bitmap asd){
        bitmap=asd;
    }

    // 로그인 정보 db에 넣어주고 연결시켜야 함.
    void connect(){
        mHandler = new Handler();
        Log.w("connect","연결 하는중");
        // 받아오는거
        Thread checkUpdate = new Thread() {
            public void run() {
                // ip받기
                String newip = String.valueOf(ip);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,70, byteArray);
                byte[] bytes= byteArray.toByteArray();
                // 서버 접속
                try {
                    socket = new Socket(newip, port);
                    Log.w("서버 접속 유무", "서버 접속");
                } catch (IOException e) {
                    Log.w("서버 접속 유무", "서버 접속 불가");
                    e.printStackTrace();
                }

                try {
                    dos = new DataOutputStream(socket.getOutputStream());   // output에 보낼꺼 넣음
                    dis = new DataInputStream(socket.getInputStream());     // input에 받을꺼 넣어짐

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }

                try{
                    dos.writeUTF(Integer.toString(bytes.length));
                    dos.flush();

                    dos.write(bytes);
                    dos.flush();

                }
                catch(Exception e){
                    Log.w("error","error occur");
                }

                while(true) {
                    // 서버에서 받아옴
                    String input_message;
                    try {
                        byte[] buf = new byte[2000];
                        int read_Byte  = dis.read(buf);
                        input_message = new String(buf, 0, read_Byte);
                        teststring = input_message;
                        Log.w("받아오는 값", input_message);
                        if (!input_message.equals(STOP_MSG)){
//                            publishProgress(input_message);
                        }
                        else{
                            break;
                        }
                        Thread.sleep(2);
                    }
                    catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        };
        // 소켓 접속 시도, 버퍼생성
        checkUpdate.start();
        try {
            checkUpdate.join();  // thread 종료를 기다렸다가 진행
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}