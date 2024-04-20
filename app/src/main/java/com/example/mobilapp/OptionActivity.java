package com.example.mobilapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Az alkalmazásban elérhető opciókat megjelenítő Activity
 */
public class OptionActivity extends AppCompatActivity {

    private Button buttonNewError;
    private Button buttonListAllError;
    private Button buttonListFixedErrors;
    private Button buttonContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

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

        // Összes hiba megtekintése gombra kattintás
        buttonListAllError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, AllErrorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Javított hibák megtekintése gombra kattintás
        buttonListFixedErrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, FixedErrorsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Kapcsolatok gombra kattintás
        buttonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonNewError = findViewById(R.id.buttonNewError);
        buttonListAllError = findViewById(R.id.buttonListAllError);
        buttonListFixedErrors = findViewById(R.id.buttonListFixedErrors);
        buttonContacts = findViewById(R.id.buttonContacts);
    }
}