package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Impacto {

    private Double arboles;
    private Double agua;
    private Double energia;
    private Double emisiones;

    public static Impacto getFromJson(JSONObject jsonObject){
        try {
            return new Impacto(
                    jsonObject.has("arboles") ? jsonObject.getDouble("arboles") : 0d ,
                    jsonObject.has("agua") ? jsonObject.getDouble("agua") : 0d ,
                    jsonObject.has("energia") ? jsonObject.getDouble("energia") : 0d ,
                    jsonObject.has("emisiones") ? jsonObject.getDouble("emisiones") : 0d
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Impacto: "+ e.getMessage());
        }
    }

    public Impacto(Double arboles, Double agua, Double energia, Double emisiones) {
        this.arboles = arboles;
        this.agua = agua;
        this.energia = energia;
        this.emisiones = emisiones;
    }

    public Double getArboles() {
        return arboles;
    }

    public void setArboles(Double arboles) {
        this.arboles = arboles;
    }

    public Double getAgua() {
        return agua;
    }

    public void setAgua(Double agua) {
        this.agua = agua;
    }

    public Double getEnergia() {
        return energia;
    }

    public void setEnergia(Double energia) {
        this.energia = energia;
    }

    public Double getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(Double emisiones) {
        this.emisiones = emisiones;
    }
}
