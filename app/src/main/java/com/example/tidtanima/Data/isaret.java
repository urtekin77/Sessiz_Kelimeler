package com.example.tidtanima.Data;

import android.net.Uri;

public class isaret {
    private String A_ID;
    private String E_ID;
    private String I_ID;
    private String I_ad;
    private String I_anlam;
    private String I_resim;
    private String U_ID;
    private String Y_ID;
    private String I_video;
    public isaret (){
        // boş yapıcı
    }
    public isaret (String A_ID, String E_ID, String I_ID, String I_ad, String I_anlam,
                   String I_resim, String U_ID, String Y_ID, String I_video){
        this.A_ID = A_ID;
        this.U_ID = U_ID;
        this.E_ID = E_ID;
        this.I_ad = I_ad;
        this.I_anlam = I_anlam;
        this.I_ID = I_ID;
        this.I_resim = I_resim;
        this.Y_ID = Y_ID;
        this.I_video = I_video;
    }

    public String getA_ID() {
        return A_ID;
    }

    public String getY_ID() {
        return Y_ID;
    }

    public String getE_ID() {
        return E_ID;
    }

    public String getU_ID() {
        return U_ID;
    }

    public String getI_ad() {
        return I_ad;
    }

    public String getI_anlam() {
        return I_anlam;
    }

    public String getI_ID() {
        return I_ID;
    }

    public String getI_resim() {
        return I_resim;
    }

    public String getI_video() {
        return I_video;
    }
}
