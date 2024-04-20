package com.example.mobilapp;

import java.util.Date;
/**
 * Az Error osztály, amely a hibákat reprezentálja
 */
public class Error {

    private int id;
    private String hibaMegnevezese;
    private String hibaLeirasa;
    private String hibaHelye;
    private String hibaKepe;
    private Date bejelntesIdeje;
    private String hibaAllapota;

    public Error(int id, String hibaMegnevezese, String hibaLeirasa, String hibaHelye, String hibaKepe, Date bejelntesIdeje, String hibaAllapota) {
        this.id = id;
        this.hibaMegnevezese = hibaMegnevezese;
        this.hibaLeirasa = hibaLeirasa;
        this.hibaHelye = hibaHelye;
        this.hibaKepe = hibaKepe;
        this.bejelntesIdeje = bejelntesIdeje;
        this.hibaAllapota = hibaAllapota;
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

    public Date getBejelntesIdeje() {
        return bejelntesIdeje;
    }

    public void setBejelntesIdeje(Date bejelntesIdeje) {
        this.bejelntesIdeje = bejelntesIdeje;
    }

    public String getHibaAllapota() {
        return hibaAllapota;
    }

    public void setHibaAllapota(String hibaAllapota) {
        this.hibaAllapota = hibaAllapota;
    }
}
