package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Usuario {

    private Long id;
    private String nombre;
    private String apellido;
    private String mail;
    private Double puntos;
    private Boolean admin;
    private Nivel nivel;
    private String fotoUri;
    private List<Reciclaje> reciclajes;
    private List<Objetivo> objetivosCumplidos;
    //private List<Campania> campaniasCumplidas;
    private Impacto impacto;

    public static Usuario getFromJson(JSONObject jsonObject){
        try {
            Usuario usuario = new Usuario(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("nombre") ? jsonObject.getString("nombre") : null,
                    jsonObject.has("apellido") ? jsonObject.getString("apellido") : null,
                    jsonObject.has("mail") ? jsonObject.getString("mail") : null,
                    jsonObject.has("puntos") ? jsonObject.getDouble("puntos") : null,
                    jsonObject.has("admin") ? jsonObject.getBoolean("admin") : false,
                    jsonObject.has("nivel") ? Nivel.getFromJson(jsonObject.getJSONObject("nivel")) : null,
                    jsonObject.has("impacto") ? Impacto.getFromJson(jsonObject.getJSONObject("impacto")) : null
            );
            if(jsonObject.has("reciclajes")){
                JSONArray arrayReciclajesJson = jsonObject.getJSONArray("reciclajes");
                List<Reciclaje> reciclajesFromJson = new ArrayList<>();
                for(int i = 0; i < arrayReciclajesJson.length(); i++){
                    JSONObject reciclajeJsonObject = arrayReciclajesJson.getJSONObject(i);
                    Reciclaje reciclaje = Reciclaje.getFromJson(reciclajeJsonObject);
                    reciclajesFromJson.add(reciclaje);
                }
                usuario.setReciclajes(reciclajesFromJson);
            }
            if(jsonObject.has("objetivos_cumplidos")){
                JSONArray arrayObjetivosJson = jsonObject.getJSONArray("objetivos_cumplidos");
                List<Objetivo> objetivos = new ArrayList<>();
                for(int i = 0; i < arrayObjetivosJson.length(); i++){
                    JSONObject objetivoJsonObject = arrayObjetivosJson.getJSONObject(i);
                    Objetivo objetivo = Objetivo.getFromJson(objetivoJsonObject);
                    objetivos.add(objetivo);
                }
                usuario.setObjetivosCumplidos(objetivos);
            }

            return usuario;
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Usuario: "+ e.getMessage());
        }
    }


    public Usuario(Long id, String nombre, String apellido, String mail, Double puntos,
                   Boolean admin, Nivel nivel, String fotoUri, List<Reciclaje> reciclajes,
                   List<Objetivo> objetivosCumplidos, Impacto impacto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.puntos = puntos;
        this.admin = admin;
        this.nivel = nivel;
        this.fotoUri = fotoUri;
        this.reciclajes = reciclajes;
        this.objetivosCumplidos = objetivosCumplidos;
        this.impacto = impacto;
    }

    public Usuario(Long id, String nombre, String apellido, String mail, Double puntos, Boolean admin, Nivel nivel, Impacto impacto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.puntos = puntos;
        this.admin = admin;
        this.nivel = nivel;
        this.impacto = impacto;
    }

    public Usuario(String nombre, String apellido, Double puntos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntos = puntos;
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Double getPuntos() {
        return puntos;
    }

    public void setPuntos(Double puntos) {
        this.puntos = puntos;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public void setImpacto(Impacto impacto) {
        this.impacto = impacto;
    }

    public String getNombreCompleto(){
        return this.nombre+" "+this.apellido;
    }

    public List<Reciclaje> getReciclajes() {
        return reciclajes;
    }

    public void setReciclajes(List<Reciclaje> reciclajes) {
        this.reciclajes = reciclajes;
    }

    public List<Objetivo> getObjetivosCumplidos() {
        return objetivosCumplidos;
    }

    public void setObjetivosCumplidos(List<Objetivo> objetivosCumplidos) {
        this.objetivosCumplidos = objetivosCumplidos;
    }
}
