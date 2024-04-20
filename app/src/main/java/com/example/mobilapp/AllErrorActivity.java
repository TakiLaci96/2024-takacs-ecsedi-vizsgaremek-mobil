package com.example.mobilapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Az összes hibát megjelenítő Activity
 */
public class AllErrorActivity extends AppCompatActivity {

    private ListView listViewErrors;
    private List<Error> errors = new ArrayList<>();
    private Button buttonBack;
    private String url = "http://10.0.2.2:8000/api/indexAll";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        /**
         * Az activity teljes képernyős módban való futtatásához
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_all_error);
        init();

        // Hibaadatok lekérdezése
        RequestTask task = new RequestTask(url, "GET");
        task.execute();

        // Vissza gombra kattintás
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllErrorActivity.this, OptionActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Inicializálás metódus
    private void init() {
        listViewErrors = findViewById(R.id.listViewErrors);
        listViewErrors.setAdapter(new ErrorAdapter());
        buttonBack = findViewById(R.id.buttonBack);
    }

    /**
     * Az ErrorAdapter osztály a hibák listájának megjelenítésére szolgáló adapter
     */
    private class ErrorAdapter extends ArrayAdapter<Error> {
        public ErrorAdapter() {
            super(AllErrorActivity.this, R.layout.errors_list_items, errors);
        }

        /**
         * A getView metódus a listaelemek megjelenítésére szolgál
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            //view létrehozása az activity_my_errors.xml-ből
            View view = inflater.inflate(R.layout.errors_list_items, parent, false);
            //activity_my_errors.xml-ben lévő elemek inicializálása
            TextView textViewErrorTitle = view.findViewById(R.id.textViewErrorTitle);
            TextView textViewErrorDescription = view.findViewById(R.id.textViewErrorDescription);
            TextView textViewErrorLocation = view.findViewById(R.id.textViewErrorLocation);
            TextView textViewErrorStatus = view.findViewById(R.id.textViewErrorStatus);
            ImageView imageViewError = view.findViewById(R.id.imageViewError);
            //CardView inicializálása
            androidx.cardview.widget.CardView cardView = view.findViewById(R.id.cardViewError);
            //aktuális hiba létrehozása az errors listából
            Error error = errors.get(position);
            //adatok beállítása az aktuális hiba alapján
            textViewErrorTitle.setText(error.getHibaMegnevezese());
            textViewErrorDescription.setText(error.getHibaLeirasa());
            textViewErrorLocation.setText(error.getHibaHelye());
            textViewErrorStatus.setText(error.getHibaAllapota());
            Bitmap bitmap = convertBase64ToImage(error.getHibaKepe());
            imageViewError.setImageBitmap(bitmap);

            // Háttérszín beállítása az állapot szerint
            switch (error.getHibaAllapota()) {
                case "bejelentés alatt":
                    cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.status_bejelentes_alatt));
                    break;
                case "folyamatban":
                    cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.status_feldolgozas_alatt));
                    break;
                case "kész":
                    cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.status_kesz));
                    break;
                default:
                    cardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                    break;
            }

            return view;
        }
    }

    /**
     * A convertBase64ToImage metódus a base64 stringet képpé alakítja
     */
    private Bitmap convertBase64ToImage(String base64String) {
        // A base64 string átalakítása képpé
        // A base64 string átalakítása byte tömbbé
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        // A byte tömb átalakítása képpé
        // A kép visszaadása
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    /**
     * A RequestTask osztály a hibák lekérdezésére szolgáló aszinkron művelet
     */
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

        /**
         * Az doInBackground metódus a hálózati kérés elküldésére szolgál
         */
        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            SharedPreferences preferences = getSharedPreferences("datas", MODE_PRIVATE);
            String token = preferences.getString("token", null);
            try {
                if (requestType.equals("GET")) {
                    response = RequestHandler.get(requestUrl, token);
                }
            } catch (IOException e) {
                Toast.makeText(AllErrorActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }

        /**
         * Az onPostExecute metódus a válasz feldolgozására és megjelenítésére szolgál
         */
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            //progressBar.setVisibility(View.GONE);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(AllErrorActivity.this,
                        "Hiba történt a kérés feldolgozása során" + response.getResponseCode(), Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            } else {
                Error[] errorArray = converter.fromJson(response.getContent(), Error[].class);
                errors.clear();
                errors.addAll(Arrays.asList(errorArray));
                ((ErrorAdapter) listViewErrors.getAdapter()).notifyDataSetChanged();
                Toast.makeText(AllErrorActivity.this,
                        "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();
            }
        }
    }
}