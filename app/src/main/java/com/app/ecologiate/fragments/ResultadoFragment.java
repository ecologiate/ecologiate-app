package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;


public class ResultadoFragment extends AbstractEcologiateFragment {

    private Producto producto;

    private OnFragmentInteractionListener mListener;

    private ApiCallService apiCallService = new ApiCallService();

    @BindView(R.id.textViewResultado)
    TextView tvResultado;
    @BindView(R.id.impactoLayout)
    LinearLayout impactoLayout;
    @BindView(R.id.impactoView)
    ImpactoView impactoView;
    @BindView(R.id.layoutReciclable)
    LinearLayout layoutReciclable;
    @BindView(R.id.tvCategoria)
    TextView tvCategoria;
    @BindView(R.id.tvMaterial)
    TextView tvMaterial;
    @BindView(R.id.ivTacho)
    ImageView ivTacho;

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

        if(producto != null){
            tvCategoria.setText(producto.getCategoria().getDescripcion());
            ivTacho.setImageResource(producto.getCategoria().getImageTachoResourceId());

            if(producto.getCategoria() != null && producto.getCategoria().getDescripcion().toLowerCase().contains("no ")){
                tvResultado.setText("Lamentáblemente el producto no es reciclable");
                layoutReciclable.setVisibility(View.GONE);
                tvMaterial.setVisibility(View.GONE);
                tvCategoria.setVisibility(View.GONE);
            }else{
                tvMaterial.setText(producto.getMaterial().getDescripcion());

                double arboles = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquArboles()) : 0d;
                double agua = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquAgua()) : 0d;
                double energia = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquEnergia()) : 0d;
                double emisiones = producto.getMaterial()!=null ? (producto.getCantMaterial() * producto.getMaterial().getEquEmisiones()) : 0d;
                tvResultado.setText(producto.getNombreProducto());
                tvResultado.setTypeface(tvResultado.getTypeface(), Typeface.BOLD);

                impactoView.setImpactoAgua(NumberUtils.format(agua)+" litros");
                impactoView.setImpactoEnergia(NumberUtils.format(energia)+" kw");
                impactoView.setImpactoArboles(NumberUtils.format(arboles)+" árboles");
                impactoView.setImpactoEmisiones(NumberUtils.format(emisiones)+" kg");
            }
        }


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
        Long puntoRecId = null;
        Integer cant = 1; //TODO siempre va 1 por ahora, no está en el front

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("product_id", productId);
            jsonBody.put("user", userId);
            jsonBody.put("puntorec", puntoRecId);
            jsonBody.put("cant", cant);
            bodyEntity = new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON);
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
                    Double puntosSumados = 0d;
                    try{
                        puntosSumados = response.getDouble("puntos_sumados");
                    }catch (JSONException e){
                        Toast.makeText(getContext(), "Error en json de respuesta", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getContext(), "¡Sumaste "+puntosSumados+ " puntos!", Toast.LENGTH_LONG).show();
                    UserManager.updateUser(getContext(), new ResultCallback() {
                        @Override
                        public void onResult(@NonNull Result result) {
                            Fragment inicioFragment = new InicioFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFragment, inicioFragment)
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
