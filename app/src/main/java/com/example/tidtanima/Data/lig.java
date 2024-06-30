package com.example.tidtanima.Data;

public class lig {
    private String L_Ad;
    private String L_ID;
    private String L_Resim;
    public lig(){
        //boş yapıcı
    }
    public lig(String L_Ad, String L_ID, String L_Resim){
        this.L_Ad = L_Ad;
        this.L_ID = L_ID;
        this.L_Resim = L_Resim;
    }

    public String getL_Ad() {
        return L_Ad;
    }

    public String getL_ID() {
        return L_ID;
    }

    public String getL_Resim() {
        return L_Resim;
    }

    public void setL_Ad(String l_Ad) {
        L_Ad = l_Ad;
    }

    public void setL_ID(String l_ID) {
        L_ID = l_ID;
    }

    public void setL_Resim(String l_Resim) {
        L_Resim = l_Resim;
    }
}
