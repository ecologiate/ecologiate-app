package com.app.ecologiate.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MaterialConObjetivos {

    private Material material;
    private int cantReciclada = 0;
    private List<Objetivo> objetivos = new ArrayList<>();


    public MaterialConObjetivos(Material material, int cantReciclada, List<Objetivo> objetivosDelMaterial) {
        this.material = material;
        this.cantReciclada = cantReciclada;
        this.objetivos = objetivosDelMaterial;

        //ordeno de menor meta a mayor
        Collections.sort(this.objetivos, new Comparator<Objetivo>() {
            @Override
            public int compare(Objetivo o1, Objetivo o2) {
                return o1.getCantMeta().compareTo(o2.getCantMeta());
            }
        });
    }

    public List<Objetivo> getObjetivosCumplidos(){
        List<Objetivo> result = new ArrayList<>();
        if(this.objetivos != null && !this.objetivos.isEmpty()){
            Iterator<Objetivo> it = this.objetivos.iterator();
            while (it.hasNext()){
                Objetivo o = it.next();
                if(o.getCumplido()){
                    result.add(o);
                }
            }
        }
        return result;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getCantReciclada() {
        return cantReciclada;
    }

    public void setCantReciclada(int cantReciclada) {
        this.cantReciclada = cantReciclada;
    }

    public List<Objetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }
}
