package com.example.xrossprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AutoLogin extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);

        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword("rhtech558@gmail.com","rahul2002").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful())
                {
                    Toast.makeText(AutoLogin.this,"Login successful",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AutoLogin.this,MainActivity.class));
                }
                else
                {
                    Toast.makeText(AutoLogin.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}