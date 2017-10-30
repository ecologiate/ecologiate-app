package com.app.ecologiate.models;


import android.util.Log;

import com.app.ecologiate.R;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class Material {
    private Long id;
    private String descripcion;
    private Double equArboles;
    private Double equEnergia;
    private Double equAgua;
    private Double equEmisiones;
    private Double puntosOtorgados;
    private Categoria categoria;


    public static Material getFromJson(JSONObject jsonObject){
        try {
            return new Material(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("descripcion") ? jsonObject.getString("descripcion") : null,
                    jsonObject.has("equ_arboles") ? jsonObject.getDouble("equ_arboles") : 0d,
                    jsonObject.has("equ_energia") ? jsonObject.getDouble("equ_energia") : 0d,
                    jsonObject.has("equ_agua") ? jsonObject.getDouble("equ_agua") : 0d,
                    jsonObject.has("equ_emisiones") ? jsonObject.getDouble("equ_emisiones") : 0d,
                    jsonObject.has("puntos_otorgados") ? jsonObject.getDouble("puntos_otorgados") : 0d,
                    jsonObject.has("categoria") ? Categoria.getFromJson(jsonObject.getJSONObject("categoria")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Material: "+ e.getMessage());
        }
    }

    public Material(Long id, String descripcion, Double equArboles, Double equEnergia,
                    Double equAgua, Double equEmisiones, Double puntosOtorgados,
                    Categoria categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.equArboles = equArboles;
        this.equEnergia = equEnergia;
        this.equAgua = equAgua;
        this.equEmisiones = equEmisiones;
        this.puntosOtorgados = puntosOtorgados;
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        if (!id.equals(material.id)) return false;
        return descripcion.equals(material.descripcion);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + descripcion.hashCode();
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

    public Double getEquArboles() {
        return equArboles;
    }

    public void setEquArboles(Double equArboles) {
        this.equArboles = equArboles;
    }

    public Double getEquEnergia() {
        return equEnergia;
    }

    public void setEquEnergia(Double equEnergia) {
        this.equEnergia = equEnergia;
    }

    public Double getEquAgua() {
        return equAgua;
    }

    public void setEquAgua(Double equAgua) {
        this.equAgua = equAgua;
    }

    public Double getEquEmisiones() {
        return equEmisiones;
    }

    public void setEquEmisiones(Double equEmisiones) {
        this.equEmisiones = equEmisiones;
    }

    public Double getPuntosOtorgados() {
        return puntosOtorgados;
    }

    public void setPuntosOtorgados(Double puntosOtorgados) {
        this.puntosOtorgados = puntosOtorgados;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getImageResourceId(){
        switch (descripcion){
            case "Papel y cartón": return R.drawable.ic_cartonypapel;
            case "Vidrio": return R.drawable.ic_vidrio;
            case "Plástico": return R.drawable.ic_plastico;
            case "Tetra-brik": return R.drawable.ic_tetra;
            case "Tapitas": return R.drawable.ic_tapitas;
            case "Pilas": return R.drawable.ic_pilas;
            case "Neumáticos": return R.drawable.ic_neumaticos;
            case "Electrónicos": return R.drawable.ic_electronicos;
            case "Bronce": return R.drawable.ic_bronce;
            case "Textiles": return R.drawable.ic_textil;
            case "Aceite": return R.drawable.ic_aceite;
            case "Telgopor": return R.drawable.ic_telgopor;
            case "Metales": return R.drawable.ic_metales;
            case "Orgánicos": return R.drawable.ic_organico;
            default: return R.drawable.ic_reciclaje;
        }
    }
}
