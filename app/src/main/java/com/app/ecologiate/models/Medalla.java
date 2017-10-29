package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Medalla {

    private Long id;
    private String nombre;
    private String imagen;

    public static Medalla getFromJson(JSONObject jsonObject){
        try {
            return new Medalla(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("nombre") ? jsonObject.getString("nombre") : null,
                    jsonObject.has("imagen") ? jsonObject.getString("imagen") : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Medalla: "+ e.getMessage());
        }
    }

    public Medalla(Long id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
