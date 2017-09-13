package com.app.ecologiate.services;

import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldzisiuk on 13/9/17.
 */

public class CategoryManager {

    public static SimpleArrayMap<Long, String> categorias = new SimpleArrayMap<>();
    static {
        //inicializo
        categorias.put(1L, "Reciclable");
        categorias.put(2L, "Orgánico");
        categorias.put(3L, "Manejo especial");
        categorias.put(4L, "No tratable");
    }
    public static List<String> array = new ArrayList<String>();
    static{
        for(int i = 0; i < categorias.size(); i++){
            array.add(categorias.get(categorias.keyAt(i)));
        }
    }
}
