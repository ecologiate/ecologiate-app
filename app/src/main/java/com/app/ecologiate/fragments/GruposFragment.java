package com.app.ecologiate.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Grupo;
import com.app.ecologiate.models.GrupoAdapter;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.UserManager;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class GruposFragment extends AbstractEcologiateFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog prgDialog;
    private ResultCallback resultCallback;

    private ApiCallService apiCallService = new ApiCallService();



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

        //cada vez que vuelve de una apicall
        resultCallback = new ResultCallback() {
            @Override
            public void onResult(@NonNull Result result) {
                obtenerGruposDelUsuario();
            }
        };

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true); //para que me agregue un menu, tiene que ir lo más al final posible
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_superior_grupos, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_crear_grupo:
                //crear grupo
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialogo_creargrupo,null);
                final EditText tvNombreGrupo = (EditText) view1.findViewById(R.id.nombreGrupo);
                builder.setView(view1);
                builder.setPositiveButton("Crear",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nombreGrupo = tvNombreGrupo.getText().toString();
                                if(nombreGrupo!= null && nombreGrupo.length()>0){
                                    crearGrupo(nombreGrupo);
                                }else{
                                    Toast.makeText(getContext(), "Debe insertar un nombre", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do nothing Jon Snow
                                    }
                                });
                Dialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                        mAdapter = new GrupoAdapter(getContext(), grupos, resultCallback);
                        mAdapter.notifyDataSetChanged();
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

    private void crearGrupo(String nombreGrupo){
        Long usuarioId = UserManager.getUser().getId();
        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("usuario_id", usuarioId);
            jsonBody.put("nombre_grupo", nombreGrupo);
            bodyEntity = new StringEntity(jsonBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error armando Json", Toast.LENGTH_LONG).show();
        }
        prgDialog.show();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                try {
                    if (response != null && response.has("status_code")) {
                        if (response.getInt("status_code")==200 || response.getInt("status_code")==201) {
                            //creado ok
                            obtenerGruposDelUsuario();
                        } else {
                            if(response.getInt("status_code")==500)
                                Toast.makeText(getContext(), "Ya existe un grupo con ese nombre", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getContext(), "Error creando grupo: "+response.getInt("status_code"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error en respuesta", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    //DVP: Si encuentra algun error parseando el Jason.
                    Toast.makeText(getContext(), "Error en formato de Json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (statusCode == 404) {
                    Toast.makeText(getContext(), "URL no encontrada", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getContext(), "Error en el Backend", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Error Inesperado [" + statusCode + "]", throwable);
                }
            }
        };

        apiCallService.postGrupo(getContext(), bodyEntity, responseHandler);

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
