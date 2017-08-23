package com.app.ecologiate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class AltaProductoActivity extends AppCompatActivity {

    Spinner categoria;
    Spinner material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_producto);

        categoria = (Spinner) findViewById(R.id.spinnerCategoria);
        material = (Spinner) findViewById(R.id.spinnerMaterial);

        ArrayAdapter <CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this, R.array.Categoria, android.R.layout.simple_spinner_item);
        categoria.setAdapter(adapterCategoria);


        ArrayAdapter <CharSequence> adapterMaterial = ArrayAdapter.createFromResource(this, R.array.Materiales, android.R.layout.simple_spinner_item);
        material.setAdapter(adapterMaterial);

    }
}
