package com.example.tidtanima.Data;

import java.util.List;

public class unite {

    private String U_Ad;
    private String U_ID;
    private String U_Icerik;
    private String U_background;
    public unite(){
        //boş yapıcı
    }
    public unite( String U_Ad, String U_ID, String U_Icerik, String U_background){

        this.U_Ad = U_Ad;
        this.U_ID = U_ID;
        this.U_Icerik = U_Icerik;
        this.U_background = U_background;
    }

    public String getU_Ad() {
        return U_Ad;
    }

    public String getU_Icerik() {
        return U_Icerik;
    }
    public String getU_ID() {
        return U_ID;
    }



    public String getU_background() {
        return U_background;
    }



    public void setU_Ad(String u_Ad) {
        U_Ad = u_Ad;
    }

    public void setU_Icerik(String u_Icerik) {
        U_Icerik = u_Icerik;
    }

    public void setU_ID(String u_ID) {
        U_ID = u_ID;
    }

    public void setU_background(String u_background) {
        U_background = u_background;
    }
}
