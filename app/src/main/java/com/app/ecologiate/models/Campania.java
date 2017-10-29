package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Campania {

    private Long id;
    private String titulo;
    private String descripcion;
    private Integer cantMeta;
    private Date fechaInicio;
    private Date fechaFin;
    private Medalla medalla;

    public static Campania getFromJson(JSONObject jsonObject){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
            return new Campania(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("titulo") ? jsonObject.getString("titulo") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null,
                    jsonObject.has("cant_meta") ? jsonObject.getInt("cant_meta") : 0,
                    jsonObject.has("fecha_inicio") ? sdf.parse(jsonObject.getString("fecha_inicio")) : null,
                    jsonObject.has("fecha_fin") ? sdf.parse(jsonObject.getString("fecha_fin")) : null,
                    jsonObject.has("medalla") ? Medalla.getFromJson(jsonObject.getJSONObject("medalla")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Campania: "+ e.getMessage());
        }
    }

    public Campania(Long id, String titulo, String descripcion, Integer cantMeta,
                    Date fechaInicio, Date fechaFin, Medalla medalla) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cantMeta = cantMeta;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.medalla = medalla;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Medalla getMedalla() {
        return medalla;
    }

    public void setMedalla(Medalla medalla) {
        this.medalla = medalla;
    }
}
