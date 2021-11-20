package com.example.myRefri02;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main_Act extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    ImageView refribtn;
    Button logoutbtn;
    Button gobillcapture;
    Button goitemcapture;
    Button testserver;


    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    private String asdf;
    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listContent = new ArrayList<>();
    Switch sw;
    List<PendingIntent> mPendingIntentList = new ArrayList<PendingIntent>();
    private SharedPreferences appData;
    private boolean saveSwitchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        appData = getSharedPreferences("appData", MODE_PRIVATE);



        File f = new File("/data/data/com.example.myapplication/files/datafile.txt");
        asdf = getfile(f.getName());
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
        //알람 버튼
        sw = (Switch)findViewById(R.id.sw);
        sw.setOnClickListener(this);
        load();

        refribtn = (ImageView) findViewById(R.id.imageView2);
        refribtn.setOnClickListener(this);
        logoutbtn = (Button) findViewById(R.id.btn_logout);
        logoutbtn.setOnClickListener(this);
        gobillcapture = (Button) findViewById(R.id.btn_bill_capture);
        gobillcapture.setOnClickListener(this);
        goitemcapture = (Button) findViewById(R.id.btn_item_capture);
        goitemcapture.setOnClickListener(this);
    }

    private void load() {
        saveSwitchData = appData.getBoolean("SAVE_SWITCH_DATA", false);
        if (saveSwitchData == true){
            sw.setChecked(true);
        }

    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_SWITCH_DATA", sw.isChecked());
        editor.apply();
    }


    @Override
    public void onClick(View v) {
        if(v == refribtn){
            //TODO
            startActivity(new Intent(this, Refri_Act.class));
        }
        if(v == logoutbtn){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, Login_Act.class));
            finish();
        }
        if(v == gobillcapture){
            startActivity(new Intent(this, Billcapture_Act.class));
        }
        if(v == goitemcapture){
            startActivity(new Intent(this, Itemcapture_Act.class));
        }
        if(v == sw){
            CheckState();
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
    private void CheckState(){
        if(sw.isChecked()) {
            save();
            File f = new File("/data/data/com.example.myRefri02/files/datafile.txt");
            if (f.exists()) {
                Toast.makeText(getApplicationContext(), "알림이 설정 되었습니다.", Toast.LENGTH_SHORT).show();
                startAlarm();
            } else {
                Toast.makeText(getApplicationContext(), "냉장고가 비어있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            save();
            Toast.makeText(getApplicationContext(), "알림이 해제 되었습니다.", Toast.LENGTH_SHORT).show();
            resetAlarm();
        }
        Log.w("save", Boolean.toString(appData.getBoolean("SAVE_SWITCH_DATA", false)));

    }
    private void startAlarm(){
        String[] testdata = asdf.split(",");

        if(testdata.length != 0) {
            for (int i = 0; i < testdata.length; i++) {
                if (i % 2 == 0) {
                    listTitle.add(testdata[i]);
                } else {
                    listContent.add(testdata[i]);
                }

            }
        }
        for(int i = 0; i<50; i++){
            if( i <listTitle.size()){
                setAlarm(listTitle.get(i), listContent.get(i), i);
            }
            else
                setAlarm("null", "2050-01-01 00:00:00", i);
        }

    }
    private void setAlarm(String product, String expDate, int count) {
        //AlarmReceiver에 값 전달


        Intent receiverIntent = new Intent(this, AlarmRecevier.class);

        receiverIntent.putExtra("product", product);
        receiverIntent.putExtra("expDate", expDate);
        receiverIntent.putExtra("count", count);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, count, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mPendingIntentList.add(pendingIntent);

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(expDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }
    private void resetAlarm(){
        for(int idx = 0 ; idx < mPendingIntentList.size() ; idx++){
            alarmManager.cancel(mPendingIntentList.get(idx));
        }
    }

}
