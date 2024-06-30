package com.example.tidtanima.Data;

public class el {
    private String E_ID;
    private String E_hareketi;
    public el(){
        //boş yapıcı
    }
    public el(String E_ID, String E_hareketi){
        this.E_ID = E_ID;
        this.E_hareketi = E_hareketi;
    }

    public String getE_hareketi() {
        return E_hareketi;
    }

    public String getE_ID() {
        return E_ID;
    }

    public void setE_hareketi(String e_hareketi) {
        E_hareketi = e_hareketi;
    }

    public void setE_ID(String e_ID) {
        E_ID = e_ID;
    }
}
