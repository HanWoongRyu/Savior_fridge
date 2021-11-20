package com.example.myRefri02;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Edit_Client {

    private Handler mHandler;
    public static final String UTF8_CHARSET= "UTF-8";
    private Socket socket;
    private static String STOP_MSG = "stop";

//    private BufferedReader networkReader;
//    private PrintWriter networkWriter;

    private DataOutputStream dos;
    private DataInputStream dis;

    private String ip = "192.168.55.16";
    private int port = 8060;


    // 로그인 정보 db에 넣어주고 연결시켜야 함.
    void connect(String test){
        mHandler = new Handler();
        Log.w("connect","연결 하는중");

        Thread checkUpdate = new Thread() {
            public void run() {

                try{
                    socket = new Socket(ip, port);
                }catch (IOException e){
                    e.printStackTrace();
                }

                try{
                    BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream()); //output stream
                    out.write(test.getBytes("EUC_KR"));
                    out.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }


                }
            };
        checkUpdate.start();
        }

}