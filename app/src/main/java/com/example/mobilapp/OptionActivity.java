package com.example.mobilapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    private Button buttonNewError;
    private Button buttonListMyErrors;
    private Button buttonListFixedErrors;
    private Button buttonContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        init();

        // Új hiba bejelentése gombra kattintás
        buttonNewError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, NewErrorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonNewError = findViewById(R.id.buttonNewError);
        buttonListMyErrors = findViewById(R.id.buttonListMyErrors);
        buttonListFixedErrors = findViewById(R.id.buttonListFixedErrors);
        buttonContacts = findViewById(R.id.buttonContacts);
    }
}