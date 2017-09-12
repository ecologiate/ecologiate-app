package com.app.ecologiate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ecologiate.models.Producto;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultadoFragment extends Fragment {

    private Producto producto;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.textViewResultado)
    TextView tvResultado;

    public ResultadoFragment() {}


    public static ResultadoFragment newInstance(Producto producto) {
        ResultadoFragment fragment = new ResultadoFragment();
        fragment.producto = producto;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);
        ButterKnife.bind(this, view);

        String mensajeResultado = "<b>Nombre del producto</b><br/> "+ producto.getNombreProducto() + "<br/><br/>"+
                "<b>Categoría</b><br/> "+ producto.getNombreCategoria() + "<br/><br/>"+
                "<b>Material</b><br/> "+ producto.getNombreMaterial() + "<br/><br/>"+
                "<b>Impacto</b><br/> "+ producto.getImpacto();

        tvResultado.setText(Html.fromHtml(mensajeResultado));

        return view;
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
