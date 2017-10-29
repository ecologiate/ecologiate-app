package com.app.ecologiate.utils;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtils {

    private static NumberFormat nf = new DecimalFormat("###.##");

    public static String format(Double numero){
        return nf.format(numero);
    }
}
