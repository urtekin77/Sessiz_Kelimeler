package com.example.tidtanima.Data;

import java.util.ArrayList;

public class kullanici {
    String K_ad;
    String K_ID;
    String K_mail;
    int K_puan;
    ArrayList<String> M_ID;
    String L_ID;
    int L_puan;
    int can;
    String U_ID;
    String U_adim;

    public kullanici(){
        //boş yapııcı
    }
    public kullanici(String K_ad, String K_ID, String K_mail, int K_puan, ArrayList<String> M_ID, String L_ID, int L_puan, int can, String U_ID, String U_adim){
        this.can = can;
        this.K_ad = K_ad;
        this.K_ID = K_ID;
        this.K_mail = K_mail;
        this.L_ID = L_ID;
        this.L_puan = L_puan;
        this.U_ID = U_ID;
        this.U_adim = U_adim;
        this.M_ID = M_ID;
        this.K_puan = K_puan;

    }

    public String getU_ID() {
        return U_ID;
    }

    public String getL_ID() {
        return L_ID;
    }

    public int getCan() {
        return can;
    }

    public int getK_puan() {
        return K_puan;
    }

    public int getL_puan() {
        return L_puan;
    }


    public String getK_ad() {
        return K_ad;
    }

    public String getK_ID() {
        return K_ID;
    }

    public String getK_mail() {
        return K_mail;
    }

    public ArrayList<String> getM_ID() {
        return M_ID;
    }

    public String getU_adim() {
        return U_adim;
    }

    public void setL_ID(String l_ID) {
        L_ID = l_ID;
    }

    public void setU_ID(String u_ID) {
        U_ID = u_ID;
    }


    public void setCan(int can) {
        this.can = can;
    }

    public void setK_ad(String k_ad) {
        K_ad = k_ad;
    }

    public void setK_ID(String k_ID) {
        K_ID = k_ID;
    }

    public void setK_mail(String k_mail) {
        K_mail = k_mail;
    }

    public void setK_puan(int k_puan) {
        K_puan = k_puan;
    }


    public void setM_ID(ArrayList<String> m_ID) {
        M_ID = m_ID;
    }

    public void setL_puan(int l_puan) {
        L_puan = l_puan;
    }

    public void setU_adim(String u_adim) {
        U_adim = u_adim;
    }
}
