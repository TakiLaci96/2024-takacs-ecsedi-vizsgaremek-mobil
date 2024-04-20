package com.example.mobilapp;
    /** A válasz osztály */
public class Response {

    /** A válasz kódja */
    private int responseCode;
    /** A válasz tartalma */
    private String content;
    /** A válasz konstruktora */
    public Response(int responseCode, String content) {
        this.responseCode = responseCode;
        this.content = content;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
