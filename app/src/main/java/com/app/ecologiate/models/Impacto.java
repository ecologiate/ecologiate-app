package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Impacto {

    private Long arboles;
    private Long agua;
    private Long energia;
    private Long emisiones;

    public static Impacto getFromJson(JSONObject jsonObject){
        try {
            return new Impacto(
                    jsonObject.has("arboles") ? jsonObject.getLong("arboles") : 0 ,
                    jsonObject.has("agua") ? jsonObject.getLong("agua") : 0 ,
                    jsonObject.has("energia") ? jsonObject.getLong("energia") : 0 ,
                    jsonObject.has("emisiones") ? jsonObject.getLong("emisiones") : 0
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Impacto: "+ e.getMessage());
        }
    }

    public Impacto(Long arboles, Long agua, Long energia, Long emisiones) {
        this.arboles = arboles;
        this.agua = agua;
        this.energia = energia;
        this.emisiones = emisiones;
    }

    public Long getArboles() {
        return arboles;
    }

    public void setArboles(Long arboles) {
        this.arboles = arboles;
    }

    public Long getAgua() {
        return agua;
    }

    public void setAgua(Long agua) {
        this.agua = agua;
    }

    public Long getEnergia() {
        return energia;
    }

    public void setEnergia(Long energia) {
        this.energia = energia;
    }

    public Long getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(Long emisiones) {
        this.emisiones = emisiones;
    }
}
