package com.app.ecologiate.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private Long id;
    private String nombre;
    private List<Usuario> usuarios;
    private Impacto impacto;

    public static Grupo getFromJson(JSONObject jsonObject){
        try {
            Grupo grupo = new Grupo(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("nombre") ? jsonObject.getString("nombre") : null,
                    jsonObject.has("impacto") ? Impacto.getFromJson(jsonObject.getJSONObject("impacto")) : null
            );
            if(jsonObject.has("usuarios")){
                JSONArray arrayUsuarios = jsonObject.getJSONArray("usuarios");
                List<Usuario> usuariosFromJson = new ArrayList<>();
                for(int i = 0; i < arrayUsuarios.length(); i++){
                    JSONObject usuarioJsonObject = arrayUsuarios.getJSONObject(i);
                    Usuario u = Usuario.getFromJson(usuarioJsonObject);
                    usuariosFromJson.add(u);
                }
                grupo.setUsuarios(usuariosFromJson);
            }
            return grupo;
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Usuario: "+ e.getMessage());
        }
    }

    public Grupo(Long id, String nombre, Impacto impacto) {
        this.id = id;
        this.nombre = nombre;
        this.impacto = impacto;
    }

    public Grupo(Long id, String nombre, List<Usuario> usuarios, Impacto impacto) {
        this.id = id;
        this.nombre = nombre;
        this.usuarios = usuarios;
        this.impacto = impacto;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public void setImpacto(Impacto impacto) {
        this.impacto = impacto;
    }
}
