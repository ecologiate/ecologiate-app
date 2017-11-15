package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Grupo;
import com.app.ecologiate.models.GrupoAdapter;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.models.ProductoEncontradoAdapter;
import com.app.ecologiate.services.ApiCallService;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ManualFragment extends AbstractEcologiateFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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
        ImageView botonBuscaManual = (ImageView) view.findViewById(R.id.btnBuscaManual);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btnAltaManual);
        et = (EditText) view.findViewById(R.id.etProductoBuscado);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.manualFragment);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        botonBuscaManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // EditText et = (EditText) view.findViewById(R.id.etProductoBuscado);
                getProductoManual(et.getText().toString());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment altaProductoFragment = AltaProductoFragment.newInstance(null);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, altaProductoFragment)
                        //.addToBackStack(String.valueOf(altaProductoFragment.getId())) no dejo volver
                        .commit();
            }
        });
        return view;
    }


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
                    if(response.has("productos")){
                        //DVP: Hago el parseo del Json.


                        JSONArray productosJsonArray = response.getJSONArray("productos");
                        List<Producto> productos = new ArrayList<>();
                        for(int i = 0; i < productosJsonArray.length(); i++){
                            JSONObject jsonProducto = productosJsonArray.getJSONObject(i);
                            Producto prod = Producto.getFromJson(jsonProducto);
                            productos.add(prod);
                        }

                        mAdapter = new ProductoEncontradoAdapter(productos);
                        mRecyclerView.setAdapter(mAdapter);


                    }else {
                        Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
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

    @Override
    public String getTitle() {
        return getResources().getString(R.string.manual_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
    }
}
