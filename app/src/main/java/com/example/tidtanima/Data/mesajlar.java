package com.example.tidtanima.Data;

import java.util.ArrayList;

public class mesajlar {
    String k_ID;
    ArrayList<String> k_mesaj;
    public mesajlar(){}
    public mesajlar(String k_ID, ArrayList<String> k_mesaj){
        this.k_mesaj = k_mesaj;
        this.k_ID = k_ID;
    }

    public String getK_ID() {
        return k_ID;
    }

    public ArrayList<String> getK_mesaj() {
        return k_mesaj;
    }

    public void setK_ID(String k_ID) {
        this.k_ID = k_ID;
    }

    public void setK_mesaj(ArrayList<String> k_mesaj) {
        this.k_mesaj = k_mesaj;
    }
}
