package com.app.ecologiate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {

    TextView tvResultado;

    static Intent crearIntentParaResultado(Context context, String nombreProducto,
                                           String categoria, String material, String impacto){
        Intent resutadoIntent = new Intent(context, ResultadoActivity.class);
        resutadoIntent.putExtra("nombre_producto", nombreProducto);
        resutadoIntent.putExtra("categoria", categoria);
        resutadoIntent.putExtra("material", material);
        resutadoIntent.putExtra("impacto", impacto);
        resutadoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return resutadoIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        tvResultado = (TextView) findViewById(R.id.textViewResultado);
        //obtengo los parámetros que vinieron del escaneo
        String nombreProducto = getIntent().getStringExtra("nombre_producto");
        String categoria = getIntent().getStringExtra("categoria");
        String material = getIntent().getStringExtra("material");
        String impacto = getIntent().getStringExtra("impacto");

        String mensajeResultado = "<li><b>Nombre del producto</b><br/> "+ nombreProducto + "<br/><br/>"+
                "<li><b>Categoría</b><br/> "+ categoria + "<br/><br/>"+
                "<li><b>Material</b><br/> "+ material + "<br/><br/>"+
                "<li><b>Impacto</b><br/> "+ impacto;

        tvResultado.setText(Html.fromHtml(mensajeResultado));

    }
}
