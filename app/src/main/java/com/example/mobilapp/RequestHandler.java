package com.example.mobilapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * A RequestHandler osztály felelős a backend és a frontend közötti kommunikáció megvalósításáért
 */
public class RequestHandler {

    private RequestHandler() {
    }

    // setupConnection metódus létrehozása a connection beállításához
    /**
     * A kapcsolat beállítása
     * @param url a kapcsolódni kívánt URL
     * @param token a kapcsolódáshoz szükséges token
     * @return a kapcsolat
     * @throws IOException ha hiba történik a kapcsolat beállítása során
     */
    private static HttpURLConnection setupConnection(String url, String token) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        Log.d("Token", "setupConnection: " + token);
        if (token != null) {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }
        return conn;
    }


    //getResponse metódus létrehozása a responseCode és a content lekérdezéséhez
    /**
     * A válasz lekérdezése
     * @param connection a kapcsolat
     * @return a válasz
     * @throws IOException ha hiba történik a válasz lekérdezése során
     */
    private static Response getResponse(HttpURLConnection connection) throws IOException {
        //responseCode lekérdezése
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        //ha a responseCode kisebb, mint 400, akkor az inputStream a connection-ből olvas
        if (responseCode < 400) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }
        //content létrehozása
        StringBuilder content = new StringBuilder();
        //inputStream-ből olvasás és hozzáadás a contenthez
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //sorok beolvasása
        String line = reader.readLine();
        //ha nem üres a sor, akkor hozzáadja a contenthez
        while (line != null) {
            content.append(line);
            line = reader.readLine();
        }
        //bezárás
        reader.close();
        inputStream.close();
        //responseCode és egy content
        return new Response(responseCode, content.toString());
    }

    //addRequestBody metódus létrehozása a requestBody hozzáadásához
    private static void addRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        //outputStream létrehozása
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        //írás a outputStream-be
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        //requestBody írása a writer-be
        writer.write(requestBody);
        //véglegesítés
        writer.flush();
        //bezárás
        writer.close();
        outputStream.close();
    }

    //get metódus létrehozása a GET kéréshez
    public static Response get(String url, String token) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url, token);
        //connection típusának beállítása
        connection.setRequestMethod("GET");
        //response visszaadása
        return getResponse(connection);
    }

    //post metódus létrehozása a POST kéréshez
    public static Response post(String url, String requestBody, String token) throws IOException {
        //connection létrehozása
        HttpURLConnection connection = setupConnection(url, token);
        //connection típusának beállítása
        connection.setRequestMethod("POST");
        //requestBody hozzáadása
        addRequestBody(connection, requestBody);
        //response visszaadása
        return getResponse(connection);
    }
}
