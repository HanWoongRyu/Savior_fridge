package com.example.myRefri02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Act extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail;
    EditText editTextPassword;
    Button loginBt;
    TextView textviewSingin;
    TextView textviewFindPs;
    TextView textviewMessage;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), Main_Act.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        loginBt = (Button) findViewById(R.id.btnlogin);
        textviewSingin = (TextView) findViewById(R.id.textViewSignin);
        textviewFindPs = (TextView) findViewById(R.id.Findpassword);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        progressDialog = new ProgressDialog(this);

        loginBt.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
        textviewFindPs.setOnClickListener(this);
    }

    private void Login(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password??? ????????? ?????????.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("?????????????????????. ?????? ????????? ?????????...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        /* if(task.isSuccessful()&&editTextEmail.getText().toString().equals(Ad_id)){
                            finish();
                            startActivity(new Intent(getApplicationContext(), AdProfileActivity.class));
                        } */
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), Main_Act.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("????????? ?????? ??????\n - password??? ?????? ????????????.\n -????????????");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == loginBt){
            Login();
        }
        if(v == textviewSingin){
            //TODO
            startActivity(new Intent(this, Signup_Act.class));
        }
        if(v == textviewFindPs){
            startActivity(new Intent(this, FindPs_Act.class));
        }
    }
}
