package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONObject;

public class Reciclaje {
    private Integer cantProd;
    private Producto producto;


    public static Reciclaje getFromJson(JSONObject jsonObject){
        try {
            return new Reciclaje(
                    jsonObject.has("cant_prod") ? jsonObject.getInt("cant_prod") : 0 ,
                    jsonObject.has("producto") ? Producto.getFromJson(jsonObject.getJSONObject("producto")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Usuario: "+ e.getMessage());
        }
    }

    public Reciclaje(Integer cantProd, Producto producto) {
        this.cantProd = cantProd;
        this.producto = producto;
    }

    public Integer getCantProd() {
        return cantProd;
    }

    public void setCantProd(Integer cantProd) {
        this.cantProd = cantProd;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
