package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Categoria {
    private Long id;
    private String descripcion;
    private List<Material> materiales;

    public static Categoria getFromJson(JSONObject jsonObject){
        try {
            Categoria categoria =  new Categoria(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null
            );
            if(jsonObject.has("materiales")){
                JSONArray arrayMateriales = jsonObject.getJSONArray("materiales");
                List<Material> materialesFromJson = new ArrayList<>();
                for(int i = 0; i < arrayMateriales.length(); i++){
                    JSONObject materialJsonObject = arrayMateriales.getJSONObject(i);
                    Material mat = Material.getFromJson(materialJsonObject);
                    materialesFromJson.add(mat);
                }
                categoria.setMateriales(materialesFromJson);
            }
            return categoria;
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Categoria: "+ e.getMessage());
        }
    }

    public Categoria() {}

    public Categoria(Long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
        this.materiales = materiales;
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

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
}
