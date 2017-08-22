package com.app.ecologiate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProductoNoEncontradoActivity extends AppCompatActivity {

    Button alta;
    Button volver;
    TextView tvNoEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_no_encontrado);

        alta = (Button)findViewById(R.id.btnAltaProducto);

        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaIntent = new Intent(ProductoNoEncontradoActivity.this, AltaProductoActivity.class);
                startActivity(altaIntent);
            }
        });

        volver = (Button)findViewById(R.id.btnVolverBuscar);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvNoEncontrado = (TextView) findViewById(R.id.textViewNoEncontrado);
        // tvNoEncontrado.setText(getIntent().getStringExtra("codigo")); //TODO MOSTRABA EL CODIGO DE BARRAS
    }
}
