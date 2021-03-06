package com.example.xrossprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView imgProfile,btnEdit;
    private TextView txtUsername,txtAbout,txtPopularity,txtConnections,txtTag;
    private String userid;
    private FirebaseAuth auth;
    private ColorGetter colorGetter;
    private String tagColor;

    private FirebaseUser loggedUser;
    private FirebaseDatabase mdb;
    private DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        imgProfile = findViewById(R.id.imgProfile);
        btnEdit = findViewById(R.id.btnEdit);
        txtUsername = findViewById(R.id.txtUsername);
        txtTag = findViewById(R.id.txtTagname);
        txtAbout = findViewById(R.id.txtAbout);
        txtConnections = findViewById(R.id.txtConnections);
        txtPopularity = findViewById(R.id.txtPopularity);

        colorGetter = new ColorGetter();
        mdb = FirebaseDatabase.getInstance();
        mreference = mdb.getReference();

        loggedUser = auth.getCurrentUser();
        userid = loggedUser.getUid();

        mreference.child("users").child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
                else {
                    String about = task.getResult().child("About").getValue().toString();
                    String username = task.getResult().child("username").getValue().toString();
                    String tagname = task.getResult().child("tag").getValue().toString();
                    String connections = task.getResult().child("connections").getValue().toString();
                    String image = task.getResult().child("imageurl").getValue().toString();


                    txtUsername.setText(username);
                    txtTag.setText(tagname);
                    tagColor = colorGetter.getColor(tagname);
                    setColor();
                    txtConnections.setText(connections);
                    txtAbout.setText(about);
                    Picasso.with(MainActivity.this).load(image).resize(160,160).into(imgProfile);

                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EditProfile.class));
            }
        });

    }

    private void setColor() {
        txtTag.setTextColor(Color.parseColor(tagColor));
        GradientDrawable myGrad = (GradientDrawable)txtTag.getBackground();
        myGrad.setStroke(convertDpToPx(2), Color.parseColor(tagColor));
    }

    private int convertDpToPx(int dp){
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
