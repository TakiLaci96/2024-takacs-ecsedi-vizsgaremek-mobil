package com.example.mobilapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonBack;
    private String requestUrl = "http://10.0.2.2:8000/api/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        // Bejelentkezés gombra kattintás
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, OptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Vissza gombra kattintás
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonBack = findViewById(R.id.buttonBack);
    }




}