package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class Usuario {

    private Long id;
    private String nombre;
    private String apellido;
    private String mail;
    private String token;
    private Long puntos;
    private Nivel nivel;

    public static Usuario getFromJson(JSONObject jsonObject){
        try {
            return new Usuario(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("nombre") ? jsonObject.getString("nombre") : null,
                    jsonObject.has("apellido") ? jsonObject.getString("apellido") : null,
                    jsonObject.has("mail") ? jsonObject.getString("mail") : null,
                    jsonObject.has("token") ? jsonObject.getString("token") : null,
                    jsonObject.has("puntos") ? jsonObject.getLong("puntos") : null,
                    jsonObject.has("nivel") ? Nivel.getFromJson(jsonObject.getJSONObject("nivel")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Usuario: "+ e.getMessage());
        }
    }


    public Usuario(Long id, String nombre, String apellido, String mail, String token, Long puntos, Nivel nivel) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.token = token;
        this.puntos = puntos;
        this.nivel = nivel;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getPuntos() {
        return puntos;
    }

    public void setPuntos(Long puntos) {
        this.puntos = puntos;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
}
