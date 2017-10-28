package com.app.ecologiate.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Grupo;
import com.app.ecologiate.models.GrupoAdapter;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.UserManager;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class GruposFragment extends AbstractEcologiateFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog prgDialog;

    private ApiCallService apiCallService = new ApiCallService();


    Button crearGrupo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Cargando...");
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.grupoFragment);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        obtenerGruposDelUsuario();

        Button botonCrearGrupo = (Button) view.findViewById(R.id.btnCrearGrupo);

        botonCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialogo_creargrupo,null);
                builder.setView(view1);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {  }
                        })
                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) { }
                                });
                Dialog dialog = builder.create();
                dialog.show();

            }
        });


        return  view;
    }


    private void obtenerGruposDelUsuario(){
        Usuario usuarioLogueado = UserManager.getUser();
        prgDialog.show();
        //DVP: agrego el código a ejecutar cuando vuelve del Back.
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                //DVP: oculto el "Buscando".
                prgDialog.hide();
                //DVP: Si encuentra el Producto.
                if(response.has("grupos")){
                    try {
                        List<Grupo> grupos = new ArrayList<>();
                        JSONArray arrayGruposJson = response.getJSONArray("grupos");
                        for (int i = 0; i < arrayGruposJson.length(); i++) {
                            JSONObject grupoJsonObject = arrayGruposJson.getJSONObject(i);
                            Grupo grupo = Grupo.getFromJson(grupoJsonObject);
                            grupos.add(grupo);
                        }
                        mAdapter = new GrupoAdapter(grupos);
                        mRecyclerView.setAdapter(mAdapter);
                    }catch (JSONException e){
                        //DVP: Si encuentra algun error parseando el Json.
                        Toast.makeText(getContext(), "Error en formato de Json", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "No se encontraron grupos para el usuario", Toast.LENGTH_LONG).show();
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
        apiCallService.getGruposDelUsuario(getContext(), usuarioLogueado.getId(), responseHandler);
    }


    @Override
    public String getTitle() {
        return getResources().getString(R.string.grupos_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
    }


    public interface OnFragmentInteractionListener {
            void onFragmentInteraction(Uri uri);
    }
}
