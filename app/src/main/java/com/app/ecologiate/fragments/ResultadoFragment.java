package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.UserManager;
import com.app.ecologiate.utils.NumberUtils;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class ResultadoFragment extends AbstractEcologiateFragment {

    private Producto producto;

    private OnFragmentInteractionListener mListener;

    private ApiCallService apiCallService = new ApiCallService();

    @BindView(R.id.textViewResultado)
    TextView tvResultado;

    ProgressDialog prgDialog;

    public ResultadoFragment() {}


    public static ResultadoFragment newInstance(Producto producto) {
        ResultadoFragment fragment = new ResultadoFragment();
        fragment.producto = producto;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);
        ButterKnife.bind(this, view);

        double arboles = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquArboles()) : 0d;
        double agua = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquAgua()) : 0d;
        double energia = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquEnergia()) : 0d;
        double emisiones = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquEmisiones()) : 0d;

        String mensajeResultado = "<b>Nombre del producto</b><br/> "+ producto.getNombreProducto() + "<br/><br/>"+
                "<b>Categoría</b><br/> "+ (producto.getCategoria()!=null ? producto.getCategoria().getDescripcion() : "?") + "<br/><br/>"+
                "<b>Material</b><br/> "+ (producto.getMaterial()!=null ? producto.getMaterial().getDescripcion() : "?") + "<br/><br/>"+
                "<b>Arboles por salvar: </b>"+ NumberUtils.format(arboles)+ "<br/><br/>"+
                "<b>Agua por ahorrar: </b>"+ NumberUtils.format(agua)+ "<br/><br/>"+
                "<b>Energia por ahorrar: </b>"+ NumberUtils.format(energia)+ "<br/><br/>"+
                "<b>Emisiones por ahorrar: </b>"+ NumberUtils.format(emisiones);
        tvResultado.setText(Html.fromHtml(mensajeResultado));

        return view;
    }

    @OnClick(R.id.btnResultVerPdR)
    public void searchOnMap(View view){
        Fragment mapaFragment = MapaFragment.newInstance(producto);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, mapaFragment)
                .commit();
    }

    @OnClick(R.id.btnResultReciclar)
    public void reciclar(View view){

        Long userId = UserManager.getUser().getId();
        Long productId = producto.getId();
        Long puntoRecId = null; //TODO falta integrar con el mapa
        Integer cant = 1; //TODO siempre va 1 por ahora, no está en el front

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("product_id", productId);
            jsonBody.put("user", userId);
            jsonBody.put("puntorec", puntoRecId);
            jsonBody.put("cant", cant);
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
                if (response != null) {
                    Toast.makeText(getContext(), "Producto reciclado", Toast.LENGTH_LONG).show();
                    Double puntosSumados = 0d;
                    try{
                        puntosSumados = response.getDouble("puntos_sumados");
                    }catch (JSONException e){
                        Toast.makeText(getContext(), "Error en json de respuesta", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getContext(), "Puntos sumados: "+puntosSumados, Toast.LENGTH_LONG).show();
                    UserManager.updateUser(getContext(), new ResultCallback() {
                        @Override
                        public void onResult(@NonNull Result result) {
                            Fragment inicioFragment = new InicioFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFragment, inicioFragment)
                                    //.addToBackStack(String.valueOf(resultadoFragment.getId())) //no quiero que pueda volver
                                    .commit();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
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

        apiCallService.postReciclaje(getContext(), bodyEntity, responseHandler);

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
        return getResources().getString(R.string.resultado_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
