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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Refri_Editor extends AppCompatActivity implements View.OnClickListener{

    private Editor_Adapter editorAdapter;

    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listContent = new ArrayList<>();

    private String teststr;
    private File editfile = new File("/data/data/com.example.myRefri02/files/editfile.txt");
    File datafile = new File("/data/data/com.example.myRefri02/files/datafile.txt");

    Button finishEdit;

//    private AlarmManager alarmManager;
//    private GregorianCalendar mCalender;
//
//    private NotificationManager notificationManager;
//    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refri_editor_layout);
        setTitle("냉장고 수정중....");

//        finishEdit = (Button) findViewById(R.id.finishedit);
//        finishEdit.setOnClickListener(this);

        finishEdit = (Button) findViewById(R.id.finishedit);
        finishEdit.setOnClickListener(this);

        init();

        if(datafile.exists()){
            teststr = getfile("datafile.txt");
            if(teststr.length() != 0){
                getData();
            }
        }
        else{
            Log.w("test","파일이 존재하지 않음");
            Toast.makeText(getApplicationContext(), "냉장고가 비어있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        RecyclerView recyclerView = findViewById(R.id.editer_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        editorAdapter = new Editor_Adapter(this);
        recyclerView.setAdapter(editorAdapter);
    }
    private void getData(){

        String[] testdata = teststr.split(",");

        for (int i = 0; i < testdata.length; i++) {
            if (i % 2 == 0) {
                listTitle.add(testdata[i]);
            } else {
                listContent.add(testdata[i]);
            }
        }


        for (int i = 0; i < listTitle.size(); i++){
            Data data = new Data(listTitle.get(i), listContent.get(i));
            editorAdapter.addItem(data);
        }
        editorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onClick(View view) {
        if(view == finishEdit){
            startActivity(new Intent(this, Refri_Act.class));

            // 서버로 수정 텍스트파일 넘기기
            Edit_Client AZ = new Edit_Client();
            AZ.connect(getfile(editfile.getName()));
            if(editfile.exists()) {
                editfile.delete();
            }


            finish();
        }
    }
}
