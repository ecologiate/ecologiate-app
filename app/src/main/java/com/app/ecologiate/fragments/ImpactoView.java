package com.app.ecologiate.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ecologiate.R;


public class ImpactoView extends LinearLayout {
    private String impactoAgua;
    private String impactoEnergia;
    private String impactoArboles;
    private String impactoEmisiones;
    private String style;

    private TextView tvAgua;
    private TextView tvEnergia;
    private TextView tvArboles;
    private TextView tvEmisiones;

    public ImpactoView(Context context) {
        super(context);
        init(null, 0);
    }

    public ImpactoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ImpactoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ImpactoView, defStyle, 0);

        impactoAgua = a.getString(
                R.styleable.ImpactoView_impactoAgua);
        impactoEnergia = a.getString(
                R.styleable.ImpactoView_impactoEnergia);
        impactoArboles = a.getString(
                R.styleable.ImpactoView_impactoArboles);
        impactoEmisiones = a.getString(
                R.styleable.ImpactoView_impactoEmisiones);
        style = a.getString(
                R.styleable.ImpactoView_style);

        a.recycle();

        // Creamos la interfaz a partir del layout
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater)getContext().getSystemService(infService);
        if("small".equals(style)){
            li.inflate(R.layout.impacto_layout_small, this, true);
        }else{
            li.inflate(R.layout.impacto_layout, this, true);
        }

        // Obtenemos las referencias a las vistas hijas
        tvAgua = (TextView) findViewById(R.id.tv_agua);
        tvEnergia = (TextView) findViewById(R.id.tv_energia);
        tvArboles = (TextView) findViewById(R.id.tv_arboles);
        tvEmisiones = (TextView) findViewById(R.id.tv_emisiones);


        refreshUI();
    }

    private void refreshUI(){
        tvAgua.setText(impactoAgua);
        tvEnergia.setText(impactoEnergia);
        tvArboles.setText(impactoArboles);
        tvEmisiones.setText(impactoEmisiones);
    }


    public String getImpactoAgua() {
        return impactoAgua;
    }

    public void setImpactoAgua(String impactoAgua) {
        this.impactoAgua = impactoAgua;
        refreshUI();

    }

    public String getImpactoEnergia() {
        return impactoEnergia;
    }

    public void setImpactoEnergia(String impactoEnergia) {
        this.impactoEnergia = impactoEnergia;
        refreshUI();
    }

    public String getImpactoArboles() {
        return impactoArboles;
    }

    public void setImpactoArboles(String impactoArboles) {
        this.impactoArboles = impactoArboles;
        refreshUI();
    }

    public String getImpactoEmisiones() {
        return impactoEmisiones;
    }

    public void setImpactoEmisiones(String impactoEmisiones) {
        this.impactoEmisiones = impactoEmisiones;
        refreshUI();
    }
}
