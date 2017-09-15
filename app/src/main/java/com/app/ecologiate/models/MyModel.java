package com.app.ecologiate.models;

import com.app.ecologiate.R;




public class MyModel {
    public String titulo;
    public String descripcion;
    public Integer foto;


    public MyModel(String titulo, String descripcion, Integer foto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.foto = foto;
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

    public Integer getFoto() {
        return foto;
    }

    public void setFoto(Integer foto) {
        this.foto = foto;
    }


}
