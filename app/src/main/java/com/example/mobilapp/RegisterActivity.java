package com.example.mobilapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonBack;
    private TextInputLayout editTextName;
    private TextInputLayout editTextEmail;
    private TextInputLayout editTextPassword;
    private String requestUrl = "http://10.0.2.2:8000/api/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getEditText().getText().toString().trim();
                String email = editTextEmail.getEditText().getText().toString().trim();
                String password = editTextPassword.getEditText().getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Minden mező kitöltése kötelező", Toast.LENGTH_SHORT).show();
                    editTextName.setError("Kötelező mező");
                    editTextEmail.setError("Kötelező mező");
                    editTextPassword.setError("Kötelező mező");
                    return;
                }

                // További ellenőrzések

                if (name.length() < 5 || name.length() > 50) {
                    editTextName.setError("A névnek 5 és 50 karakter között kell lennie");
                    return;
                } else if (email.length() < 5 || email.length() > 100) {
                    editTextEmail.setError("Az email címnek 5 és 100 karakter között kell lennie");
                    return;
                } else if (!email.contains("@") || !email.contains(".")) {
                    editTextEmail.setError("Helytelen email cím");
                    return;
                } else if (password.length() < 8) {
                    editTextPassword.setError("Adjon meg egy erősebb jelszót");
                    return;
                }

                // User objektum létrehozása

                User user = new User(name, email, password);
                Gson converter = new Gson();
                RequestTask task = new RequestTask(requestUrl, "POST", converter.toJson(user));
                task.execute();


            }
        });

        // Vissza gomb
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBack = findViewById(R.id.buttonBack);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

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
                    response = RequestHandler.post(requestUrl, requestParams);
                }
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        //onPostExecute metódus létrehozása a válasz feldolgozásához
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            Toast.makeText(RegisterActivity.this, "" + response.getResponseCode(), Toast.LENGTH_SHORT).show();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(RegisterActivity.this,
                        "Hiba történt a kérés feldolgozása során", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            }
            if (requestType.equals("POST")) {
                if (response.getResponseCode() == 201) {
                    Toast.makeText(RegisterActivity.this,
                            "Sikeres regisztráció", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}