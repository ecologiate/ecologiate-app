package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class Material {
    private Long id;
    private String descripcion;
    private Integer equArboles;
    private Integer equEnergia;
    private Integer equAgua;
    private Integer tipoMaterialEqu;
    private Integer puntosOtorgados;
    private Categoria categoria;


    public static Material getFromJson(JSONObject jsonObject){
        try {
            return new Material(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null,
                    jsonObject.has("equ_arboles") ? jsonObject.getInt("equ_arboles") : null,
                    jsonObject.has("equ_energia") ? jsonObject.getInt("equ_energia") : null,
                    jsonObject.has("equ_agua") ? jsonObject.getInt("equ_agua") : null,
                    jsonObject.has("tipo_material_equ") ? jsonObject.getInt("tipo_material_equ") : null,
                    jsonObject.has("puntos_otorgados") ? jsonObject.getInt("puntos_otorgados") : null,
                    jsonObject.has("categoria") ? Categoria.getFromJson(jsonObject.getJSONObject("categoria")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Material: "+ e.getMessage());
        }
    }

    public Material(Long id, String descripcion, Integer equArboles, Integer equEnergia,
                    Integer equAgua, Integer tipoMaterialEqu, Integer puntosOtorgados,
                    Categoria categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.equArboles = equArboles;
        this.equEnergia = equEnergia;
        this.equAgua = equAgua;
        this.tipoMaterialEqu = tipoMaterialEqu;
        this.puntosOtorgados = puntosOtorgados;
        this.categoria = categoria;
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

    public Integer getEquArboles() {
        return equArboles;
    }

    public void setEquArboles(Integer equArboles) {
        this.equArboles = equArboles;
    }

    public Integer getEquEnergia() {
        return equEnergia;
    }

    public void setEquEnergia(Integer equEnergia) {
        this.equEnergia = equEnergia;
    }

    public Integer getEquAgua() {
        return equAgua;
    }

    public void setEquAgua(Integer equAgua) {
        this.equAgua = equAgua;
    }

    public Integer getTipoMaterialEqu() {
        return tipoMaterialEqu;
    }

    public void setTipoMaterialEqu(Integer tipoMaterialEqu) {
        this.tipoMaterialEqu = tipoMaterialEqu;
    }

    public Integer getPuntosOtorgados() {
        return puntosOtorgados;
    }

    public void setPuntosOtorgados(Integer puntosOtorgados) {
        this.puntosOtorgados = puntosOtorgados;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
