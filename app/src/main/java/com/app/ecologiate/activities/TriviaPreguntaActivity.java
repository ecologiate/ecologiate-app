package com.app.ecologiate.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.app.ecologiate.R;

public class TriviaPreguntaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_preguntas);
        Bundle parametros = getIntent().getExtras();

        Toast.makeText(this, savedInstanceState.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, parametros.toString(), Toast.LENGTH_SHORT).show();

    }
}
