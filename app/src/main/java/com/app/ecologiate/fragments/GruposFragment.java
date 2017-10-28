package com.app.ecologiate.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.ecologiate.R;
import com.app.ecologiate.models.GrupoAdapter;
import com.app.ecologiate.models.Grupos;
import com.app.ecologiate.models.Tip;
import com.app.ecologiate.models.TipAdapter;
import com.app.ecologiate.models.Usuario;

import java.util.ArrayList;
import java.util.List;


public class GruposFragment extends AbstractEcologiateFragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;

    Button crearGrupo;


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);    }

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

        List<Grupos> myDataset = new ArrayList<>();
        List<Usuario> integrantesGrupo1 = new ArrayList<>();
        integrantesGrupo1.add(new Usuario("Juan","Perez",150L));
        integrantesGrupo1.add(new Usuario("Manuel","Chota",450L));
        integrantesGrupo1.add(new Usuario("Riquelme","Pechofrio",10L));
        List<Usuario> integrantesGrupo2 = new ArrayList<>();
        integrantesGrupo2.add(new Usuario("Gonza","Albarracin",1150L));
        integrantesGrupo2.add(new Usuario("Diego","Fernandez",1750L));
        myDataset.add(new Grupos("Proyecto", integrantesGrupo1, "1000","50","20","100"));
        myDataset.add(new Grupos("lalala", integrantesGrupo2, "1000","2000","4","5000"));
        mAdapter = new GrupoAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


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
