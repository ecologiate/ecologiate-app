package com.app.ecologiate.services;

import android.support.v4.util.SimpleArrayMap;

import com.app.ecologiate.models.Material;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ldzisiuk on 13/9/17.
 */

public class MaterialsManager {

    private static List<Material> materiales;

    public static SimpleArrayMap<Long, String> materialesMap = new SimpleArrayMap<>();
    static {
        //inicializo con hardcodeo, luego se completa desde el backend
        materialesMap.put(1L, "Papel y cartón");
        materialesMap.put(2L, "Vidrio");
        materialesMap.put(3L, "Plástico");
        materialesMap.put(4L, "Tetra-brik");
        materialesMap.put(5L, "Tapitas");
        materialesMap.put(6L, "Pilas");
        materialesMap.put(7L, "Neumáticos");
        materialesMap.put(8L, "Electrónicos");
        materialesMap.put(9L, "Bronce");
        materialesMap.put(10L, "Textiles");
        materialesMap.put(11L, "Aceite");
        materialesMap.put(12L, "Telgopor");
        materialesMap.put(13L, "Metales");
        materialesMap.put(14L, "Orgánicos");
    }

    public static List<String> array = new ArrayList<>();
    static{
        for(int i = 0; i < materialesMap.size(); i++){
            array.add(materialesMap.get(materialesMap.keyAt(i)));
        }
    }

    public static void init(List<Material> listMateriales){
        materiales = listMateriales;

        //limpio
        materialesMap = new SimpleArrayMap<>();
        array = new ArrayList<>();

        //completo
        for(int i = 0; i<listMateriales.size(); i++){
            Material material = listMateriales.get(i);
            materialesMap.put(material.getId(), material.getDescripcion());
        }
        for(int i = 0; i < materialesMap.size(); i++){
            array.add(materialesMap.get(materialesMap.keyAt(i)));
        }
    }

    public static List<Material> getMateriales() {
        return materiales;
    }


}
