package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Objetivo {

    private Long id;
    private String descripcion;
    private Integer cantMeta;
    private Material material;
    private Medalla medalla;

    public static Objetivo getFromJson(JSONObject jsonObject){
        try {
            return new Objetivo(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null ,
                    jsonObject.has("cant_meta") ? jsonObject.getInt("cant_meta") : null,
                    jsonObject.has("material") ? Material.getFromJson(jsonObject.getJSONObject("material")) : null,
                    jsonObject.has("medalla") ? Medalla.getFromJson(jsonObject.getJSONObject("medalla")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Objetivo: "+ e.getMessage());
        }
    }

    public Objetivo(Long id, String descripcion, Integer cantMeta, Material material, Medalla medalla) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantMeta = cantMeta;
        this.material = material;
        this.medalla = medalla;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Medalla getMedalla() {
        return medalla;
    }

    public void setMedalla(Medalla medalla) {
        this.medalla = medalla;
    }
}
