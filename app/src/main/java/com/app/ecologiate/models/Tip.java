package com.app.ecologiate.models;




@SuppressWarnings("unused")
public class Tip {
    public String titulo;
    public String descripcion;
    public Integer foto;


    public Tip(String titulo, String descripcion, Integer foto) {
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
