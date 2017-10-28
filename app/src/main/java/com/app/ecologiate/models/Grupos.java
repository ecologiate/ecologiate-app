package com.app.ecologiate.models;




import java.util.List;

public class Grupos {
    public String titulo;
    public List<Usuario> integrantes;
    public String puntos;
    public String metricaArboles;
    public String metricaAgua;
    public String metricaEnergia;



    public Grupos(String titulo, List<Usuario> integrantes, String puntos, String metricaArboles, String metricaAgua, String metricaEnergia) {
        this.titulo = titulo;
        this.integrantes = integrantes;
        this.puntos = puntos;
        this.metricaArboles = metricaArboles;
        this.metricaAgua = metricaAgua;
        this.metricaEnergia = metricaEnergia;

    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Usuario> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Usuario> integrantes) {
        this.integrantes = integrantes;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getMetricaArboles() {
        return metricaArboles;
    }

    public void setMetricaArboles(String metricaArboles) {
        this.metricaArboles = metricaArboles;
    }
    public String getMetricaAgua() {
        return metricaAgua;
    }

    public void setMetricaAgua(String metricaAgua) {
        this.metricaAgua = metricaAgua;
    }
    public String getMetricaEnergia() {
        return metricaEnergia;
    }

    public void setMetricaEnergia(String metricaEnergia) {
        this.metricaEnergia = metricaEnergia;
    }

}
