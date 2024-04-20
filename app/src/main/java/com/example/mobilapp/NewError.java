package com.example.mobilapp;

/**
 * Az új hibát reprezentáló osztály
 */
public class NewError {

    private int id;
    private String hibaMegnevezese;
    private String hibaLeirasa;
    private String hibaHelye;
    private String hibaKepe;

    // Konstruktorok
    public NewError(String hibaMegnevezese, String hibaLeirasa, String hibaHelye, String hibaKepe) {
        this.hibaMegnevezese = hibaMegnevezese;
        this.hibaLeirasa = hibaLeirasa;
        this.hibaHelye = hibaHelye;
        this.hibaKepe = hibaKepe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHibaMegnevezese() {
        return hibaMegnevezese;
    }

    public void setHibaMegnevezese(String hibaMegnevezese) {
        this.hibaMegnevezese = hibaMegnevezese;
    }

    public String getHibaLeirasa() {
        return hibaLeirasa;
    }

    public void setHibaLeirasa(String hibaLeirasa) {
        this.hibaLeirasa = hibaLeirasa;
    }

    public String getHibaHelye() {
        return hibaHelye;
    }

    public void setHibaHelye(String hibaHelye) {
        this.hibaHelye = hibaHelye;
    }

    public String getHibaKepe() {
        return hibaKepe;
    }

    public void setHibaKepe(String hibaKepe) {
        this.hibaKepe = hibaKepe;
    }
}

