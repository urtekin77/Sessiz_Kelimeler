package com.example.tidtanima.Data;

public class madalya {
    String M_Ad;
    String M_ID;
    String M_Resim;

    public madalya(){}
    public madalya(String M_Ad, String M_ID, String M_Resim){
        this.M_Ad = M_Ad;
        this.M_ID = M_ID;
        this.M_Resim = M_Resim;
    }

    public String getM_ID() {
        return M_ID;
    }

    public String getM_Ad() {
        return M_Ad;
    }

    public String getM_Resim() {
        return M_Resim;
    }
}
