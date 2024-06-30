package com.example.tidtanima.Data;

public class alfabe {
    private String A_ID;
    private String A_ad;
    public alfabe(String A_ID, String A_ad){
        this.A_ad = A_ad;
        this.A_ID = A_ID;
    }
    public alfabe(){
        // boş yapıcı
    }

    public String getA_ad() {
        return A_ad;
    }

    public String getA_ID() {
        return A_ID;
    }

    public void setA_ad(String a_ad) {
        A_ad = a_ad;
    }

    public void setA_ID(String a_ID) {
        A_ID = a_ID;
    }
}
