package com.app.ecologiate.models;


import android.util.Log;

import com.app.ecologiate.R;

import org.json.JSONObject;

public class Opinion {

    private Long id;
    private Boolean puntuacion;
    private String comentario;
    private Usuario usuario;

    public static Opinion getFromJson(JSONObject jsonObject){
        try {
            return  new Opinion(
                    jsonObject.has("id") ? jsonObject.getLong("id") : null ,
                    jsonObject.has("puntuacion") ? jsonObject.getBoolean("puntuacion") : null,
                    jsonObject.has("comentario") ? jsonObject.getString("comentario") : null,
                    jsonObject.has("usuario") ? Usuario.getFromJson(jsonObject.getJSONObject("usuario")) : null
            );
        }catch (Exception e){
            Log.e("JSON_ERROR", e.getMessage());
            throw new RuntimeException("Error en formato de json para Opinion: "+ e.getMessage());
        }
    }

    public Opinion(Long id, Boolean puntuacion, String comentario, Usuario usuario) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.usuario = usuario;
    }

    public Opinion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Boolean puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getIconResourceId() {
        if(puntuacion){
            return R.drawable.thumb_up_on;
        }else{
            return R.drawable.thumb_down_on;
        }
    }
}
