package com.app.ecologiate.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.TriviaPregunta;
import com.app.ecologiate.utils.EcologiateConstants;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TriviaPreguntaActivity extends AppCompatActivity {

    private TriviaPregunta preguntaActual;

    @BindView(R.id.ivImagenTrivia)
    ImageView imgPregunta;
    @BindView(R.id.tvTituloProducto)
    TextView tvTituloProducto;
    @BindView(R.id.btnTachoVerde)
    Button btnTachoVerde;
    @BindView(R.id.btnCompostera)
    Button btnCompostera;
    @BindView(R.id.btnTachoNegro)
    Button btnTachoNegro;
    @BindView(R.id.btnManejoEspecial)
    Button btnManejoEspecial;

    private boolean respondioCorrectamente = false;

    private String[] titulosPositivos = {"Sos groso", "Bien ahí", "Capo capo", "Estás a full papá"};
    private String[] titulosNegativos = {"Andamos flojos", "Casi pero no", "Falta estudiar", "Tiraste fruta"};

    public static Intent createIntent(Context context, TriviaPregunta pregunta){
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putParcelable("pregunta", pregunta);
        i.putExtras(b);
        i.setClass(context, TriviaPreguntaActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_preguntas);
        setTitle("Trivia");
        ButterKnife.bind(this);
        Bundle b = this.getIntent().getExtras();
        if (b != null)
            preguntaActual = b.getParcelable("pregunta");
        if(preguntaActual!=null){
            //cargo la foto
            Picasso.with(this).load(EcologiateConstants.SERVER_URL + preguntaActual.getImagen()).into(imgPregunta);
            tvTituloProducto.setText(preguntaActual.getDescripcion());
            btnTachoVerde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkearRespuesta("verde");
                }
            });
            btnCompostera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkearRespuesta("compostera");
                }
            });
            btnTachoNegro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkearRespuesta("negro");
                }
            });
            btnManejoEspecial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkearRespuesta("especial");
                }
            });
        }else{
            Toast.makeText(this, "No se encontró la pregunta", Toast.LENGTH_LONG).show();
        }
    }

    private void checkearRespuesta(String respuestaSeleccionadaText){
        if(preguntaActual.getRespuestaCorrectaTexto().toLowerCase().contains(respuestaSeleccionadaText)){
            //respuesta correcta
            respondioCorrectamente = true;
        }else{
            //respuesta incorrecta
            respondioCorrectamente = false;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //LayoutInflater inflater = LayoutInflater.from(this);
        //View contenido = inflater.inflate(R.layout.dialogo_trivia_respuesta,null);
        //builder.setView(contenido);
        builder.setTitle(getRandomTitle(respondioCorrectamente));
        builder.setMessage(preguntaActual.getExplicacion());
        builder.setPositiveButton("Siguiente",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cierro y vuelvo a la activity anterior
                        Intent intent = new Intent();
                        intent.putExtra("correcta", respondioCorrectamente);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
        builder.create().show();
    }

    private String getRandomTitle(boolean rtaCorrecta){
        Random rand = new Random();
        if(rtaCorrecta){
            return titulosPositivos[rand.nextInt(titulosPositivos.length)];
        }else{
            return titulosNegativos[rand.nextInt(titulosNegativos.length)];
        }
    }
}
