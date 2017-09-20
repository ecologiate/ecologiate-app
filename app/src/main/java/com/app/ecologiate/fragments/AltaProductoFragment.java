package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.CategoryManager;
import com.app.ecologiate.services.MaterialsManager;
import com.app.ecologiate.services.UserManager;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class AltaProductoFragment extends Fragment {

    private ApiCallService apiCallService = new ApiCallService();

    private OnFragmentInteractionListener mListener;

    private String codigoDeBarras;

    @BindView(R.id.spinnerCategoria)
    Spinner spCategoria;
    @BindView(R.id.spinnerMaterial)
    Spinner spMaterial;
    @BindView(R.id.nombreProducto)
    AutoCompleteTextView tvNombreProducto;
    @BindView(R.id.pesoGramos)
    AutoCompleteTextView tvPesoGramos;

    //DVP: Defino Barra de progreso.
    ProgressDialog prgDialog;

    public AltaProductoFragment() {
        // Required empty public constructor
    }

    public static AltaProductoFragment newInstance(String codigoDeBarras) {
        AltaProductoFragment fragment = new AltaProductoFragment();
        fragment.codigoDeBarras = codigoDeBarras;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DVP: Inicializo la barra de progreso.
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Creando...");
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alta_producto, container, false);
        ButterKnife.bind(this, view);

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, CategoryManager.array);
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategoria);

        ArrayAdapter<String> adapterMaterial = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, MaterialsManager.array);
        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaterial.setAdapter(adapterMaterial);

        return view;
    }

    @OnClick(R.id.btnSaveProduct)
    public void guardarProductoNuevo(View view){
        String nombreProducto = tvNombreProducto.getText().toString();
        String materialSelected = spMaterial.getSelectedItem().toString();
        String categorySelected = spCategoria.getSelectedItem().toString();
        Integer cantMaterial;
        try{
            cantMaterial = Integer.valueOf(tvPesoGramos.getText().toString());
        }catch (NumberFormatException e){
            cantMaterial = null;
        }
        Long codigoDeBarras = this.codigoDeBarras != null ? Long.valueOf(this.codigoDeBarras) : null;
        Long userId = UserManager.getUser().getId();
        String imgUrl = null;
        Long materialId = null;
        Long categoriaId = null;
        for(int i = 0; i < MaterialsManager.materiales.size(); i++){
            if(MaterialsManager.materiales.get(MaterialsManager.materiales.keyAt(i)).equals(materialSelected)){
                materialId = MaterialsManager.materiales.keyAt(i);
            }
        }
        for(int i = 0; i < CategoryManager.categorias.size(); i++){
            if(CategoryManager.categorias.get(CategoryManager.categorias.keyAt(i)).equals(categorySelected)){
                categoriaId = CategoryManager.categorias.keyAt(i);
            }
        }
        //DVP: Faltaba agregar el show para mostrar el mensaje.
        if(!(nombreProducto.length()>0 && materialId!=null && categoriaId!=null && cantMaterial!=null
                && codigoDeBarras!=null && userId!=null)){
            Toast.makeText(getContext(), "Faltan datos obligatorios", Toast.LENGTH_LONG).show();
        }else{
            JSONObject jsonBody = new JSONObject();
            StringEntity bodyEntity = null;
            try {
                jsonBody.put("nombre", nombreProducto);
                jsonBody.put("tipo_material", materialId);
                jsonBody.put("cant_material", cantMaterial);
                jsonBody.put("codigo_barra", codigoDeBarras);
                jsonBody.put("categoria_id", categoriaId);
                jsonBody.put("usuario_id", userId);
                jsonBody.put("imagen", imgUrl);
                bodyEntity = new StringEntity(jsonBody.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error armando Json", Toast.LENGTH_LONG).show();
            }

            //DVP: muestro "Creando".
            prgDialog.show();
            JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //DVP: oculto el "Creando".
                    prgDialog.hide();
                    if (response != null) {
                        Toast.makeText(getContext(), "Producto creado", Toast.LENGTH_LONG).show();
                        //DVP: voy a resultado
                        getProductoManual(tvNombreProducto.getText().toString());
                    } else {
                        Toast.makeText(getContext(), "Producto no creado", Toast.LENGTH_LONG).show();
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

            apiCallService.postProducto(getContext(), bodyEntity, responseHandler);
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

                        Long productId = productoJson.getLong("id");
                        String nombreProducto = productoJson.getString("nombre_producto");
                        String categoria = categoriaJson.getString("descripcion");
                        String material = materialJson.getString("descripcion");
                        Long impacto = productoJson.getLong("cant_material"); //TODO HARDCODEADO

                        Producto producto = new Producto(productId,nombreProducto,categoria,material,impacto);
                        Fragment resultadoFragment = ResultadoFragment.newInstance(producto);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFragment, resultadoFragment)
                                .addToBackStack(String.valueOf(resultadoFragment.getId()))
                                .commit();
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
}
