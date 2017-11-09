package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.activities.LoginActivity;
import com.app.ecologiate.activities.TriviaPreguntaActivity;
import com.app.ecologiate.models.ProductoEncontradoAdapter;
import com.app.ecologiate.models.TriviaPregunta;
import com.app.ecologiate.services.ApiCallService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class TriviaFragment extends AbstractEcologiateFragment {


    private OnFragmentInteractionListener mListener;
    private static int REQUEST_TRIVIA = 8429;

    private ProgressDialog prgDialog;

    private ApiCallService apiCallService = ApiCallService.getInstance();

    private List<TriviaPregunta> preguntas;
    private int preguntaIndex = 0;
    private int cantCorrectas = 0;
    private int cantIncorrectas = 0;

    public TriviaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //DVP: Manejo el fragment como un view, creo el botón y en el onclick llamo
        //a la función que invocará al servicio del back.
        View view = inflater.inflate(R.layout.fragment_trivia, container, false);
        Button botonEmpezarTrivia = (Button) view.findViewById(R.id.btnEmpezarTrivia);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);

        botonEmpezarTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerPreguntasParaTrivia();
            }
        });
        return view;
    }

    private void obtenerPreguntasParaTrivia(){
        prgDialog.show();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                prgDialog.hide();
                try {
                    if(response.has("trivia_final") && response.getJSONArray("trivia_final").length()>0){
                        JSONArray triviaJsonArray = response.getJSONArray("trivia_final");
                        preguntas = new ArrayList<>();
                        for(int i = 0; i < triviaJsonArray.length(); i++){
                            JSONObject jsonTriviaPregunta = triviaJsonArray.getJSONObject(i);
                            TriviaPregunta preg = TriviaPregunta.getFromJson(jsonTriviaPregunta);
                            preguntas.add(preg);
                        }
                        preguntaIndex = 0; //arranco de cero
                        cantCorrectas = 0;
                        cantIncorrectas = 0;
                        abrirPregunta();
                    }else {
                        Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    Toast.makeText(getContext(), "Error en formato de Json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                prgDialog.hide();
                if (statusCode == 404){
                    Toast.makeText(getContext(), "URL no encontrada", Toast.LENGTH_LONG).show();
                }else if (statusCode == 500){
                    Toast.makeText(getContext(), "Error en el Backend", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Error Inesperado ["+statusCode+"]", throwable);
                }
            }
        };
        //invoco al servicio del Back
        apiCallService.getTriviaPreguntas(responseHandler);
    }

    private void abrirPregunta(){
        TriviaPregunta pregunta = getProximaPregunta();
        if(pregunta != null) {
            Intent i = TriviaPreguntaActivity.createIntent(getContext(), pregunta);
            startActivityForResult(i, REQUEST_TRIVIA);
        }else{
            //ya no hay más preguntas, terminó la trivia
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(cantCorrectas>=3 ? "Muy bien" : "Buen intento");
            alertDialog.setMessage("Respondiste "+cantCorrectas+" respuestas correctas. Seguí concientizándote para reducir la huella");
            alertDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //nada
                }
            });

            alertDialog.show();
        }
    }

    private TriviaPregunta getProximaPregunta(){
        TriviaPregunta pregunta = null;
        if(preguntaIndex < preguntas.size()) {
            pregunta = preguntas.get(preguntaIndex);
            preguntaIndex++; //incremento
        }
        return pregunta;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TRIVIA){
            boolean correcta = (boolean) data.getExtras().get("correcta");
            if(correcta){
                cantCorrectas++;
            }else{
                cantIncorrectas++;
            }
            //abro próxima pregunta, si hay
            abrirPregunta();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.trivia_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
