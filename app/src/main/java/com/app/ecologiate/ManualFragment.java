package com.app.ecologiate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecologiate.service.ApiCallService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ManualFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //DVP: Defino Barra de progreso y ApiCall.
    ProgressDialog prgDialog;
    ApiCallService apiCallService = new ApiCallService();
    EditText et;

    public ManualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DVP: Inicializo la barra de progreso.
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Buscando...");
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //DVP: Manejo el fragment como un view, creo el botón y en el onclick llamo
        //a la función que invocará al servicio del back.
        View view = inflater.inflate(R.layout.fragment_manual, container, false);
        Button botonBuscaManual = (Button) view.findViewById(R.id.btnBuscaManual);
        et = (EditText) view.findViewById(R.id.etProductoBuscado);

        botonBuscaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // EditText et = (EditText) view.findViewById(R.id.etProductoBuscado);
                getProductoManual(et.getText().toString());
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method UI event
    public void onButtonPressed(Uri uri){
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

    //DVP: Función para buscar el producto de forma manual.
    public void getProductoManual(final String nombre_producto){
        //DVP: muestro "Buscando".
        prgDialog.show();
        //DVP: agrego el código a ejecutar cuando vuelve del Back.
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                //DVP: oculto el "Buscando".
                prgDialog.hide();
                try {
                    //DVP: Si encuentra el Producto.
                    if(response.has("producto")){
                        //DVP: Hago el parseo del Json.
                        JSONObject productoJson = response.getJSONObject("producto");
                        JSONObject materialJson = response.getJSONObject("material");
                        JSONObject categoriaJson = response.getJSONObject("categoria");
                        String nombreProducto = productoJson.getString("nombre_producto");
                        String categoria = categoriaJson.getString("descripcion");
                        String material = materialJson.getString("descripcion");
                        String impacto = productoJson.getString("cant_material");

                        startActivity(ResultadoActivity.crearIntentParaResultado(
                                getActivity(), nombreProducto, categoria, material, impacto));
                    }else {
                        //DVP: Si no encuentra el Producto.
                        Intent noEncontradoIntent = new Intent(getActivity(), ProductoNoEncontradoActivity.class);
                        noEncontradoIntent.putExtra("nombre_producto", nombre_producto);
                        startActivity(noEncontradoIntent);
                    }
                }catch (JSONException e) {
                    //DVP: Si encuentra algun error parseando el Jason.
                    Toast.makeText(getContext(), "Error en formato de Json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            //DVP: Cuando falla la invocación al Back. (status code != 200).
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
        //DVP: invoco al servicio del Back
        apiCallService.getProductoPorTitulo(nombre_producto, responseHandler);
    }
}
