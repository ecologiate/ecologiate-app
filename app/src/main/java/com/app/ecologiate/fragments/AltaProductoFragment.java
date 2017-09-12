package com.app.ecologiate.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.ecologiate.R;


public class AltaProductoFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private String codigoDeBarras;

    Spinner categoria;
    Spinner material;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alta_producto, container, false);

        categoria = (Spinner) view.findViewById(R.id.spinnerCategoria);
        material = (Spinner) view.findViewById(R.id.spinnerMaterial);

        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(getContext(), R.array.Categoria, android.R.layout.simple_spinner_item);
        categoria.setAdapter(adapterCategoria);

        ArrayAdapter <CharSequence> adapterMaterial = ArrayAdapter.createFromResource(getContext(), R.array.Materiales, android.R.layout.simple_spinner_item);
        material.setAdapter(adapterMaterial);
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
