package com.app.ecologiate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button reciclarOld;
    Button reciclar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        reciclar = (Button)findViewById(R.id.btnMenuReciclar);

        reciclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reciclarIntent = new Intent(MenuActivity.this, ScanHandlerActivity.class);
                startActivity(reciclarIntent);
            }
        });
    }
}
