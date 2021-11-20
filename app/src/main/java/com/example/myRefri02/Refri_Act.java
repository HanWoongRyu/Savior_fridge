package com.example.myRefri02;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Refri_Act extends AppCompatActivity implements View.OnClickListener{

    private Adapter adapter;

    Button gotoMian;
    Button gotoEditor;

    private String teststr;

    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listContent = new ArrayList<>();

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    File f = new File("/data/data/com.example.myRefri02/files/datafile.txt");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refri_layout);

        // 받은 데이터.
//        Intent second = getIntent();
//        teststr = second.getStringExtra("Test");
        //
        gotoMian = (Button) findViewById(R.id.gotomain);
        gotoMian.setOnClickListener(this);
        gotoEditor = (Button) findViewById(R.id.gotoeditor);
        gotoEditor.setOnClickListener(this);


        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();


        init();

        // 파일이 존재 하지 않을시 안함.
        // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함)
        // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함)
        // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함)
        // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함) // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함)
        // 파일이 존재 하지 않을시 안함.// 파일이 존재 하지 않을시 안함.(해야함)


        if(f.exists()){
            teststr = getfile("datafile.txt");
            if(teststr.length() != 0){
                getData();
            }
        }
        else{
            Log.w("test","파일이 존재하지 않음");
            Toast.makeText(getApplicationContext(), "냉장고가 비어있습니다.", Toast.LENGTH_SHORT).show();
        }


        // 파일이 존재 하지 않을시 안함.
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData(){

        String[] testdata = teststr.split(",");

        if(testdata.length != 0) {
            for (int i = 0; i < testdata.length; i++) {
                if (i % 2 == 0) {
                    listTitle.add(testdata[i]);

                } else {
                    listContent.add(testdata[i]);
                }
            }

            if (!listTitle.isEmpty()){
                for (int i = 0; i < listTitle.size(); i++){
                    Data data = new Data(listTitle.get(i), listContent.get(i));
                    adapter.addItem(data);
                }
                adapter.notifyDataSetChanged();
            }

        }

    }

    String getfile(String filename){
        String dataStr = null;

        try{
            FileInputStream fis = openFileInput(filename);
            byte txt[] = new byte[500];
            fis.read(txt);
            fis.close();
            dataStr = (new String(txt)).trim();
        }
        catch(FileNotFoundException e){

        }
        catch (IOException e){

        }
        return dataStr;
    }

    @Override
    public void onClick(View v) {
        if(v == gotoMian){
            startActivity(new Intent(this, Main_Act.class));
            finish();
        }
        if(v == gotoEditor){
            startActivity(new Intent(this, Refri_Editor.class));
            finish();

        }
    }
}