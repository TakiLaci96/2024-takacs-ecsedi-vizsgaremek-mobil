package com.example.mobilapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A javított hibákat megjelenítő Activity
 */
public class FixedErrorsActivity extends AppCompatActivity {

    private ListView listViewErrors;
    private List<Error> errors = new ArrayList<>();
    // Az API végpontja, ahol az összes hibát lekérjük, de csak a kész hibákat jelenítjük meg
    private String url = "http://10.0.2.2:8000/api/indexAll";
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_fixed_errors);
        init();

        // Hibaadatok lekérdezése a RequestTask segítségével a megadott URL címre
        RequestTask task = new FixedErrorsActivity.RequestTask(url, "GET");
        task.execute();

         // Vissza gombra kattintás
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FixedErrorsActivity.this, OptionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        buttonBack = findViewById(R.id.buttonBack);
        listViewErrors = findViewById(R.id.listViewErrors);
        listViewErrors.setAdapter(new ErrorAdapter());

    }

    // Az ErrorAdapter osztály létrehozása az Error objektumok megjelenítéséhez
    private class ErrorAdapter extends ArrayAdapter<Error> {
        public ErrorAdapter() {
            super(FixedErrorsActivity.this, R.layout.errors_list_items, errors);
        }

        // getView metódus létrehozása a listaelemek megjelenítéséhez
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //inflater létrehozása
            LayoutInflater inflater = getLayoutInflater();
            //view létrehozása az activity_my_errors.xml-ből
            View view = inflater.inflate(R.layout.errors_list_items, null, false);
            //activity_my_errors.xml-ben lévő elemek inicializálása
            TextView textViewErrorTitle = view.findViewById(R.id.textViewErrorTitle);
            TextView textViewErrorDescription = view.findViewById(R.id.textViewErrorDescription);
            TextView textViewErrorLocation = view.findViewById(R.id.textViewErrorLocation);
            TextView textViewErrorStatus = view.findViewById(R.id.textViewErrorStatus);
            ImageView imageViewError = view.findViewById(R.id.imageViewError);
            //aktuális hiba létrehozása az errors listából
            Error error = errors.get(position);
            //adatok beállítása
            textViewErrorTitle.setText(error.getHibaMegnevezese());
            textViewErrorDescription.setText(error.getHibaLeirasa());
            textViewErrorLocation.setText(error.getHibaHelye());
            textViewErrorStatus.setText(error.getHibaAllapota());
            Bitmap bitmap = convertBase64ToImage(error.getHibaKepe());
            imageViewError.setImageBitmap(bitmap);
            return view;
        }
    }

    /**
     * A base64 string átalakítása képpé
     */
    private Bitmap convertBase64ToImage(String base64String) {
        // A base64 string átalakítása képpé
        // A base64 string átalakítása byte tömbbé
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        // A byte tömb átalakítása képpé
        // A kép visszaadása
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    // RequestTask osztály létrehozása a hálózati kérés elküldéséhez
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

        //doInBackground metódus létrehozása a kérés elküldéséhez a háttérben
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
                Toast.makeText(FixedErrorsActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);

        }

        //onPostExecute metódus létrehozása a kérés eredményének feldolgozásához és megjelenítéséhez
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            //progressBar.setVisibility(View.GONE);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(FixedErrorsActivity.this,
                        "Hiba történt a kérés feldolgozása során" + response.getResponseCode(), Toast.LENGTH_SHORT).show();
                Log.d("onPostExecuteError:", response.getContent());
            } else {
                Error[] errorArray = converter.fromJson(response.getContent(), Error[].class);
                errors.clear(); // Tisztítja a listát
                // Hozzáadja az új elemeket a listához, de csak a kész hibákat
                for (Error error : errorArray) {
                    if (error.getHibaAllapota().equals("kész")) {
                        errors.add(error);
                    }
                }
                // Értesíti az adaptert, hogy a lista tartalma megváltozott
                ((ErrorAdapter) listViewErrors.getAdapter()).notifyDataSetChanged();
                Toast.makeText(FixedErrorsActivity.this,
                        "Sikeres adatlekérdezés", Toast.LENGTH_SHORT).show();

            }
        }
    }
}