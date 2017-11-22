package com.app.ecologiate.models;


import android.util.Log;

import com.app.ecologiate.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PuntoRecoleccion {
    private Long id;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Usuario usuarioAlta;
    private List<Material> materiales;
    private List<Opinion> opiniones;

    public static PuntoRecoleccion getFromJson(JSONObject jsonObject){
        try {
            PuntoRecoleccion punto =  new PuntoRecoleccion(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null,
                    jsonObject.has("direccion") ? jsonObject.getString("direccion") : null,
                    jsonObject.has("latitud") ? jsonObject.getDouble("latitud") : null,
                    jsonObject.has("longitud") ? jsonObject.getDouble("longitud") : null,
                    jsonObject.has("usuario_alta") ? Usuario.getFromJson(jsonObject.getJSONObject("usuario_alta")) : null
            );

            if(jsonObject.has("materiales")){
                JSONArray arrayMateriales = jsonObject.getJSONArray("materiales");
                List<Material> materialesFromJson = new ArrayList<>();
                for(int i = 0; i < arrayMateriales.length(); i++){
                    JSONObject materialJsonObject = arrayMateriales.getJSONObject(i);
                    Material mat = Material.getFromJson(materialJsonObject);
                    materialesFromJson.add(mat);
                }
                punto.setMateriales(materialesFromJson);
            }
            if(jsonObject.has("opiniones")){
                JSONArray arrayOpiniones = jsonObject.getJSONArray("opiniones");
                List<Opinion> opinionesFromJson = new ArrayList<>();
                for(int i = 0; i < arrayOpiniones.length(); i++){
                    JSONObject opinionJsonObject = arrayOpiniones.getJSONObject(i);
                    Opinion o = Opinion.getFromJson(opinionJsonObject);
                    opinionesFromJson.add(o);
                }
                punto.setOpiniones(opinionesFromJson);
            }
            return punto;
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para PuntoRecoleccion: "+ e.getMessage());
        }
    }


    public PuntoRecoleccion(Long id, String descripcion, String direccion,
                            Double latitud, Double longitud, Usuario usuarioAlta) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.usuarioAlta = usuarioAlta;
    }

    public PuntoRecoleccion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PuntoRecoleccion that = (PuntoRecoleccion) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null)
            return false;
        return direccion != null ? direccion.equals(that.direccion) : that.direccion == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    public List<Opinion> getOpiniones() {
        return opiniones;
    }

    public void setOpiniones(List<Opinion> opiniones) {
        this.opiniones = opiniones;
    }

    public int getImageResourceId(){
        if(this.materiales.size()==1){
            Material material = this.materiales.get(0);
            return material.getImageResourceId();
        }else{
            return R.drawable.ic_reciclaje;
        }
    }

    public int getIconColor(){
        if(this.materiales.size()==1){
            Material material = this.materiales.get(0);
            return material.getIconColor();
        }else{
            return R.color.black;
        }
    }

    public int getBackgroundColor(){
        if(this.materiales.size()==1){
            Material material = this.materiales.get(0);
            return material.getBackgroundColor();
        }else{
            return R.color.marker_multiple;
        }
    }
}
