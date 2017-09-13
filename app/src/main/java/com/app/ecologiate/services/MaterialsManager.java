package com.app.ecologiate.services;

import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ldzisiuk on 13/9/17.
 */

public class MaterialsManager {

    public static SimpleArrayMap<Long, String> materiales = new SimpleArrayMap<Long, String>();
    static {
        //inicializo
        materiales.put(1L, "Papel y cartón");
        materiales.put(2L, "Vidrio");
        materiales.put(3L, "Plástico");
        materiales.put(4L, "Tetra-brik");
        materiales.put(5L, "Tapitas");
        materiales.put(6L, "Pilas");
        materiales.put(7L, "Neumáticos");
        materiales.put(8L, "Electrónicos");
        materiales.put(9L, "Bronce");
        materiales.put(10L, "Textiles");
        materiales.put(11L, "Aceite");
        materiales.put(12L, "Telgopor");
        materiales.put(13L, "Metales");
        materiales.put(14L, "Orgánicos");
    }

    public static List<String> array = new ArrayList<String>();
    static{
        for(int i = 0; i < materiales.size(); i++){
            array.add(materiales.get(materiales.keyAt(i)));
        }
    }
}
