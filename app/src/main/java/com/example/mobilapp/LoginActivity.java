package com.example.mobilapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * A bejelentkezésért felelős Activity a mobil alkalmazásban

 */
public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonBack;
    private TextInputLayout editTextEmail;
    private TextInputLayout editTextPassword;
    // Az API végpontja, ahol a bejelentkezési adatokat elküldjük
    private String requestUrl = "http://10.0.2.2:8000/api/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        init();

        // Bejelentkezés gombra kattintás
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getEditText().getText().toString().trim();
                String password = editTextPassword.getEditText().getText().toString().trim();

                // Ellenőrizze, hogy a mezők üresek-e
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Minden mező kitöltése kötelező", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Kötelező mező");
                    editTextPassword.setError("Kötelező mező");
                    return;
                }

                // A bejelentkezési adatok elküldése a RequestTask segítségével a megadott URL címre
                User user = new User(email, password);
                Gson converter = new Gson();
                RequestTask task = new RequestTask(requestUrl, "POST", converter.toJson(user));
                task.execute();
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
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    // RequestTask osztály létrehozása a kérés elküldéséhez és a válasz feldolgozásához
    private class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        //doInBackground metódus létrehozása a kérés elküldéséhez
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                if (requestType.equals("POST")) {
                    response = RequestHandler.post(requestUrl, requestParams, null);
                }
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //onPostExecute metódus létrehozása a válasz feldolgozásához
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            // Ellenőrizze a válasz kódját, csak nekünk kell
            //Toast.makeText(LoginActivity.this, "" + response.getResponseCode(), Toast.LENGTH_SHORT).show();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(LoginActivity.this,
                        "Hibás email cím vagy jelszó!", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            if (requestType.equals("POST")) {
                if (response.getResponseCode() == 200) {
                    Toast.makeText(LoginActivity.this,
                            "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                    // A válasz tartalmának feldolgozása és a token mentése a SharedPreferences-be
                    TokenHelper tokenHelper = converter.fromJson(response.getContent(), TokenHelper.class);
                    SharedPreferences preferences = getSharedPreferences("datas", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", tokenHelper.getToken());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, OptionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}