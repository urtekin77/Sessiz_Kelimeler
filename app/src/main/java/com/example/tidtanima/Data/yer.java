package com.example.tidtanima.Data;

public class yer {
    private String Y_ID;
    private String Y_ad;
    private  String Y_bolge;
    private yer(){
        //boş yapıcı
    }
    public yer(String Y_ID, String Y_ad, String Y_bolge){
        this.Y_ad = Y_ad;
        this.Y_bolge = Y_bolge;
        this.Y_ID = Y_ID;
    }

    public String getY_ad() {
        return Y_ad;
    }

    public String getY_bolge() {
        return Y_bolge;
    }

    public String getY_ID() {
        return Y_ID;
    }

    public void setY_ad(String y_ad) {
        Y_ad = y_ad;
    }

    public void setY_ID(String y_ID) {
        Y_ID = y_ID;
    }

    public void setY_bolge(String y_bolge) {
        Y_bolge = y_bolge;
    }
}

