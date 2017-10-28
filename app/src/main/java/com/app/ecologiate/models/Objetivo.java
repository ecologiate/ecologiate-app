package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Objetivo {

    private String descripcion;
    private Integer cantMeta;
    private Material material;
    //private Medalla medalla;

    public static Objetivo getFromJson(JSONObject jsonObject){
        try {
            return new Objetivo(
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null ,
                    jsonObject.has("cant_meta") ? jsonObject.getInt("cant_meta") : null,
                    jsonObject.has("material") ? Material.getFromJson(jsonObject.getJSONObject("material")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Usuario: "+ e.getMessage());
        }
    }

    public Objetivo(String descripcion, Integer cantMeta, Material material) {
        this.descripcion = descripcion;
        this.cantMeta = cantMeta;
        this.material = material;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantMeta() {
        return cantMeta;
    }

    public void setCantMeta(Integer cantMeta) {
        this.cantMeta = cantMeta;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
