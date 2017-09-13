package com.app.ecologiate.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.ecologiate.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductoNoEncontradoFragment extends Fragment {

    @BindView(R.id.btnAltaProducto)
    Button alta;
    @BindView(R.id.btnVolverBuscar)
    Button volver;
    @BindView(R.id.textViewNoEncontrado)
    TextView tvNoEncontrado;

    private String codigoNoEncontrado;

    private OnFragmentInteractionListener mListener;

    public ProductoNoEncontradoFragment() {}


    public static ProductoNoEncontradoFragment newInstance(String codigoNoEncontrado) {
        ProductoNoEncontradoFragment fragment = new ProductoNoEncontradoFragment();
        fragment.codigoNoEncontrado = codigoNoEncontrado;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_no_encontrado, container, false);
        ButterKnife.bind(this, view);

        tvNoEncontrado.setText("Producto "+codigoNoEncontrado+" no encontrado");
        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment altaProductoFragment = AltaProductoFragment.newInstance(codigoNoEncontrado);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, altaProductoFragment)
                        .addToBackStack(String.valueOf(altaProductoFragment.getId()))
                        .commit();
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment scanFragment = new EscaneoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, scanFragment)
                        .commit();
            }
        });

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
