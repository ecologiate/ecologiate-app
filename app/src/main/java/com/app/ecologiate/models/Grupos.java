package com.app.ecologiate.models;


import android.widget.ImageView;

public class Grupos {
    public String titulo;
    public String integrante;
    public String puntos;
    public String metricaArboles;
    public String metricaAgua;
    public String metricaEnergia;



    public Grupos(String titulo, String integrante, String puntos, String metricaArboles, String metricaAgua, String metricaEnergia) {
        this.titulo = titulo;
        this.integrante = integrante;
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

    public String getIntegrante() {
        return integrante;
    }

    public void setIntegrante(String integrante) {
        this.integrante = integrante;
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
