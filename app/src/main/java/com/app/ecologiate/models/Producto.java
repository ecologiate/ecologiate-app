package com.app.ecologiate.models;

import android.util.Log;

import org.json.JSONObject;

@SuppressWarnings("unused")
public class Producto {

    private Long id;
    private String nombreProducto;
    private Integer cantMaterial;
    private Long codigoBarra;
    private String estado;
    private Categoria categoria;
    private Material material;
    private Usuario usuarioAlta;


    public static Producto getFromJson(JSONObject jsonObject){
        try {
            return new Producto(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("nombre_producto") ? jsonObject.getString("nombre_producto") : null,
                    jsonObject.has("cant_material") ? jsonObject.getInt("cant_material") : null,
                    jsonObject.has("codigo_barra") ? jsonObject.getLong("codigo_barra") : null,
                    jsonObject.has("estado") ? jsonObject.getString("estado") : null,
                    jsonObject.has("categoria") ? Categoria.getFromJson(jsonObject.getJSONObject("categoria")) : null,
                    jsonObject.has("material") ? Material.getFromJson(jsonObject.getJSONObject("material")) : null,
                    jsonObject.has("usuario_alta") ? Usuario.getFromJson(jsonObject.getJSONObject("usuario_alta")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Producto: "+ e.getMessage());
        }
    }


    public Producto(Long id, String nombreProducto, Integer cantMaterial, Long codigoBarra,
                    String estado, Categoria categoria, Material material, Usuario usuarioAlta) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.cantMaterial = cantMaterial;
        this.codigoBarra = codigoBarra;
        this.estado = estado;
        this.categoria = categoria;
        this.material = material;
        this.usuarioAlta = usuarioAlta;
    }

    public boolean isPendiente(){
        return ("PENDIENTE".equals(this.getEstado()));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantMaterial() {
        return cantMaterial;
    }

    public void setCantMaterial(Integer cantMaterial) {
        this.cantMaterial = cantMaterial;
    }

    public Long getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(Long codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }
}
