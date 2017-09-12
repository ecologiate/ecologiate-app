package com.app.ecologiate.models;


public class Producto {

    private String nombreProducto;
    private String nombreCategoria;
    private String nombreMaterial;
    private Long impacto;


    public Producto(String nombreProducto, String nombreCategoria, String nombreMaterial, Long impacto) {
        this.nombreProducto = nombreProducto;
        this.nombreCategoria = nombreCategoria;
        this.nombreMaterial = nombreMaterial;
        this.impacto = impacto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public Long getImpacto() {
        return impacto;
    }

    public void setImpacto(Long impacto) {
        this.impacto = impacto;
    }
}
