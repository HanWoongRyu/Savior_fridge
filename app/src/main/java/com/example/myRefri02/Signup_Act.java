package com.example.myRefri02;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Act extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Signup_Act";
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordcheck;
    private Button btn;
    TextView textviewMessage;
    TextView textviewSingin;
    ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), Main_Act.class)); //추가해 줄 ProfileActivity
        }

        editTextEmail = findViewById(R.id.Email);
        editTextPassword = findViewById(R.id.Password);
        editTextPasswordcheck = findViewById(R.id.Passwordcheck);
        btn = findViewById(R.id.Signup);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        textviewSingin= (TextView) findViewById(R.id.textViewSignin);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        btn.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);


    }

    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordcheck = editTextPasswordcheck.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordcheck)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.equals(passwordcheck)) {
            Log.d(TAG, "등록 버튼 " + email + " , " + password);
            final ProgressDialog mDialog = new ProgressDialog(Signup_Act.this);
            mDialog.setMessage("가입중입니다...");
            mDialog.show();

            //email과 password가 제대로 입력되어 있다면 계속 진행된다.
            progressDialog.setMessage("등록중입니다. 기다려 주세요...");
            progressDialog.show();

            //creating a new user
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), Main_Act.class));
                            } else {
                                //에러발생시
                                textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                                Toast.makeText(Signup_Act.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

        }else{

            Toast.makeText(Signup_Act.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //button click event
    @Override
    public void onClick(View view) {
        if(view == btn) {
            //TODO
            registerUser();
        }

        if(view == textviewSingin) {
            //TODO
            startActivity(new Intent(this, Login_Act.class)); //추가해 줄 로그인 액티비티
        }
    }
}