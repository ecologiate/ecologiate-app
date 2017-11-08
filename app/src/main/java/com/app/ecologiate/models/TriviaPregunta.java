package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class TriviaPregunta {

    private Long id;
    private String descripcion;
    private String explicacion;
    private String imagen;
    private String respuestaCorrectaTexto;
    private Long respuestaCorrectaId;

    public static TriviaPregunta getFromJson(JSONObject jsonObject){
        try {
            return new TriviaPregunta(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null ,
                    jsonObject.has("explicacion") ? jsonObject.getString("explicacion") : null ,
                    jsonObject.has("imagen") ? jsonObject.getString("imagen") : null ,
                    jsonObject.has("respuesta_correcta") ?
                            jsonObject.getJSONObject("respuesta_correcta").getString("respuesta") : null,
                    jsonObject.has("respuesta_correcta") ?
                            jsonObject.getJSONObject("respuesta_correcta").getLong("id") : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para TriviaPregunta: "+ e.getMessage());
        }
    }


    public TriviaPregunta(Long id, String descripcion, String explicacion, String imagen,
                          String respuestaCorrectaTexto, Long respuestaCorrectaId) {
        this.id = id;
        this.descripcion = descripcion;
        this.explicacion = explicacion;
        this.imagen = imagen;
        this.respuestaCorrectaTexto = respuestaCorrectaTexto;
        this.respuestaCorrectaId = respuestaCorrectaId;
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

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getRespuestaCorrectaTexto() {
        return respuestaCorrectaTexto;
    }

    public void setRespuestaCorrectaTexto(String respuestaCorrectaTexto) {
        this.respuestaCorrectaTexto = respuestaCorrectaTexto;
    }

    public Long getRespuestaCorrectaId() {
        return respuestaCorrectaId;
    }

    public void setRespuestaCorrectaId(Long respuestaCorrectaId) {
        this.respuestaCorrectaId = respuestaCorrectaId;
    }
}
