package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class Nivel {

    private Long id;
    private String descripcion;
    private String imagenLink;
    private Long puntosDesde;
    private Long puntosHasta;

    public static Nivel getFromJson(JSONObject jsonObject){
        try {
            return new Nivel(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null,
                    jsonObject.has("imagen_avatar") ? jsonObject.getString("imagen_avatar") : null,
                    jsonObject.has("puntos_desde") ? jsonObject.getLong("puntos_desde") : null,
                    jsonObject.has("puntos_hasta") ? jsonObject.getLong("puntos_hasta") : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Nivel: "+ e.getMessage());
        }
    }

    public Nivel(Long id, String descripcion, String imagenLink, Long puntosDesde, Long puntosHasta) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagenLink = imagenLink;
        this.puntosDesde = puntosDesde;
        this.puntosHasta = puntosHasta;
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

    public String getImagenLink() {
        return imagenLink;
    }

    public void setImagenLink(String imagenLink) {
        this.imagenLink = imagenLink;
    }

    public Long getPuntosDesde() {
        return puntosDesde;
    }

    public void setPuntosDesde(Long puntosDesde) {
        this.puntosDesde = puntosDesde;
    }

    public Long getPuntosHasta() {
        return puntosHasta;
    }

    public void setPuntosHasta(Long puntosHasta) {
        this.puntosHasta = puntosHasta;
    }
}
