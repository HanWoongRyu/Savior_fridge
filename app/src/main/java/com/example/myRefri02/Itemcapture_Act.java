package com.example.myRefri02;


import android.Manifest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.graphics.Matrix;

import android.media.ExifInterface;

import android.net.Uri;

import android.os.Bundle;

import android.os.Environment;

import android.provider.MediaStore;

import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;


import com.gun0912.tedpermission.PermissionListener;

import com.gun0912.tedpermission.TedPermission;


import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Date;

import java.util.List;


public class Itemcapture_Act extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int REQUEST_IMAGE_CROP= 321;
    private static final int PICK_FROM_ALBUM = 354;
    private String camera_imageFilePath;
    private String imageFilePath;
    private File photoFile;
    private Uri photoUri;
    private File tempFile;
    private Uri tempUri;

    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이 것이 필요하다(미디어 스캐닝)


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_capture);


// 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.

        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

// 권한 체크

        TedPermission.with(getApplicationContext())

                .setPermissionListener(permissionListener)

                .setRationaleMessage("카메라 권한이 필요합니다.")

                .setDeniedMessage("거부하셨습니다.")

                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

                .check();


        findViewById(R.id.item_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {

                    }

                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        findViewById(R.id.item_gallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });


    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir

        );

        imageFilePath = image.getAbsolutePath();
        return image;

    }

    public static boolean fileDelete(String filePath){
        //filePath : 파일경로 및 파일명이 포함된 경로입니다.
        try {
            File file = new File(filePath);
        // 파일이 존재 하는지 체크
            if(file.exists()) {
                file.delete();
                return true; // 파일 삭제 성공여부를 리턴값으로 반환해줄 수 도 있습니다.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int exifOrientation;
        int exifDegree;
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);

            } else {
                exifDegree = 0;
            }


            // 이미지 뷰에 비트맵을 set하여 이미지 표현

            ((ImageView) findViewById(R.id.item_iv_result)).setImageBitmap(rotate(bitmap, exifDegree));
            try {
                cropImage(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode == PICK_FROM_ALBUM){
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            try {
                cropImage(photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == REQUEST_IMAGE_CROP ){
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ((ImageView) findViewById(R.id.item_iv_result)).setImageBitmap(bitmap);
            Item_Client AZ = new Item_Client(bitmap);
            AZ.connect();
            try{
//              Thread.sleep(2000);
                File f = new File("/data/data/com.example.myRefri02/files/datafile.txt");
                if(f.exists()){
                    Log.w("파일", "있음");
                    FileOutputStream fos = openFileOutput(f.getName(), Context.MODE_APPEND);

                    String fdata = AZ.teststring;

                    fos.write(',');
                    fos.write(fdata.getBytes());
                    fos.close();
                }
                else{
                    Log.w("파일", "없음");
                    FileOutputStream fos = openFileOutput(f.getName(), Context.MODE_PRIVATE);

                    String fdata = AZ.teststring;

                    fos.write(fdata.getBytes());
                    fos.close();
                }
            } catch(FileNotFoundException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
//            catch (InterruptedException e){
//                e.printStackTrace();
//            }
            Log.w("Test", AZ.teststring);
            startActivity(new Intent(this, Refri_Act.class));
        }
//deleteFile(imageFilePath); 넘어갈때넣기
// deleteFile(camera_imageFilePath);

    }


    private void cropImage(Uri photoUri) throws IOException {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        camera_imageFilePath = imageFilePath;
        tempFile = createImageFile();
        tempUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        Intent i = new Intent(intent);
        ResolveInfo res = list.get(0);
        grantUriPermission(res.activityInfo.packageName, tempUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        Log.v("여기까지2","왔어용");
        startActivityForResult(i, REQUEST_IMAGE_CROP);
    }


    private int exifOrientationToDegress(int exifOrientation) {

        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {

            return 90;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {

            return 180;

        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {

            return 270;

        }

        return 0;

    }


    public Bitmap rotate(Bitmap bitmap, float degree) {

        Matrix matrix = new Matrix();

        matrix.postRotate(degree);


//여기

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }


    PermissionListener permissionListener = new PermissionListener() {

        @Override

        public void onPermissionGranted() {


        }


        @Override

        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            Toast.makeText(getApplicationContext(), "카메라 권한이 거부됨", Toast.LENGTH_SHORT).show();

        }

    };

}