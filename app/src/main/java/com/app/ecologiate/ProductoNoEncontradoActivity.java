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
                Intent scanIntent = new Intent(ProductoNoEncontradoActivity.this, WelcomeMenuActivity.class);
                scanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(scanIntent);
            }
        });

        tvNoEncontrado = (TextView) findViewById(R.id.textViewNoEncontrado);
        String codigoNoEncontrado = getIntent().getStringExtra("codigo");
        tvNoEncontrado.setText("Producto "+codigoNoEncontrado+" no encontrado");
    }
}
