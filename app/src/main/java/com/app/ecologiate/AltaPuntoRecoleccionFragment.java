package com.app.ecologiate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecologiate.service.ApiCallService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class AltaPuntoRecoleccionFragment extends Fragment {

    ApiCallService apiCallService = new ApiCallService();

    private String direccionSugerida;
    private double latitud;
    private double longitud;

    private OnFragmentInteractionListener mListener;

    private View myView;


    public AltaPuntoRecoleccionFragment() {}


    public static AltaPuntoRecoleccionFragment newInstance(double latitud, double longitud, String direccion) {
        AltaPuntoRecoleccionFragment fragment = new AltaPuntoRecoleccionFragment();
        fragment.latitud = latitud;
        fragment.longitud = longitud;
        fragment.direccionSugerida = direccion;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_alta_punto_recoleccion, container, false);

        Button botonEnviar = (Button) myView.findViewById(R.id.btnEnviar);
        botonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPuntoRecoleccion();
            }
        });

        if(direccionSugerida != null && direccionSugerida.length() > 0){
            ((EditText) myView.findViewById(R.id.etDireccion)).setText(direccionSugerida);
        }

        return myView;
    }

    private void guardarPuntoRecoleccion(){
        String descripcion = ((EditText) myView.findViewById(R.id.etDescripcion)).getText().toString();
        String direccion = ((EditText) myView.findViewById(R.id.etDireccion)).getText().toString();
        double latitud = this.latitud;
        double longitud = this.longitud;
        long usuarioId = 1L; //TODO hardcodeado
        List<Long> materialIds = new ArrayList<>();
        CheckBox checkPapelYCarton = (CheckBox) myView.findViewById(R.id.checkPapel);
        if(checkPapelYCarton.isChecked()){
            materialIds.add(1L);
        }
        CheckBox checkVidrio = (CheckBox) myView.findViewById(R.id.checkVidrio);
        if(checkVidrio.isChecked()){
            materialIds.add(2L);
        }
        CheckBox checkPlastico = (CheckBox) myView.findViewById(R.id.checkPlastico);
        if(checkPlastico.isChecked()){
            materialIds.add(3L);
        }

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try{
            jsonBody.put("descripcion", descripcion);
            jsonBody.put("direccion", direccion);
            jsonBody.put("latitud", latitud);
            jsonBody.put("longitud", longitud);
            jsonBody.put("usuario", usuarioId);
            JSONArray arrayMateriales = new JSONArray(materialIds);
            jsonBody.put("materiales", arrayMateriales);
            bodyEntity = new StringEntity(jsonBody.toString());
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Error armando Json", Toast.LENGTH_LONG).show();
        }


        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    if(response != null){
                        Toast.makeText(getContext(), "Punto creado", Toast.LENGTH_LONG).show();
                        //volver al mapa
                    }else {
                        Toast.makeText(getContext(), "Punto no creado", Toast.LENGTH_LONG).show();
                    }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
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

        apiCallService.postPuntosDeRecoleccion(getContext(), bodyEntity, responseHandler);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
