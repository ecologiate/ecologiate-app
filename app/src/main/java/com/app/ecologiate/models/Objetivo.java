package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Objetivo{

    private Long id;
    private String descripcion;
    private Integer cantMeta;
    private Material material;
    private Medalla medalla;
    private Boolean cumplido = false;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Objetivo objetivo = (Objetivo) o;

        if (id != null ? !id.equals(objetivo.id) : objetivo.id != null) return false;
        if (descripcion != null ? !descripcion.equals(objetivo.descripcion) : objetivo.descripcion != null)
            return false;
        if (cantMeta != null ? !cantMeta.equals(objetivo.cantMeta) : objetivo.cantMeta != null)
            return false;
        return material != null ? material.equals(objetivo.material) : objetivo.material == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (cantMeta != null ? cantMeta.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        return result;
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

    public Boolean getCumplido() {
        return cumplido;
    }

    public void setCumplido(Boolean cumplido) {
        this.cumplido = cumplido;
    }

}
