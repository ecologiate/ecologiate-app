package com.app.ecologiate.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecologiate.R;
import com.app.ecologiate.models.CampaniaAdapter;
import com.app.ecologiate.models.Tip;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.UserManager;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InicioFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;



    public InicioFragment() {}

    @BindView(R.id.avatar)
    ImageView imagenAvatar;
    @BindView(R.id.nivelActual)
    TextView txNivelUsuario;

    @BindView(R.id.md_floating_action_reciclar)
    FloatingActionButton fam;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        ButterKnife.bind(this, view);
        imagenAvatar.requestFocus();
        //seteo los datos del usuario
        Usuario usuario = UserManager.getUser();
        completeAppBarInfo(usuario, container);
        Picasso.with(getContext()).load(usuario.getNivel().getImagenLink()).into(imagenAvatar);
        txNivelUsuario.setText(usuario.getNivel().getDescripcion());



        mRecyclerView = (RecyclerView) view.findViewById(R.id.tipFragment);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //creo mis mails
        List<Tip> myDataset = new ArrayList<>();

        myDataset.add(new Tip("Garrahan", "Juntemos las tapitas para ayudar a los chicos ", R.raw.tapitas));
        myDataset.add(new Tip("Fort", "Mantene el aire a 24° para que no se corte la Looz", R.raw.fort));


        // specify an adapter (see also next example)
        mAdapter = new CampaniaAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


        fam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment reciclarFragment = new ReciclarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, reciclarFragment)
                        .commit();

            }
        });


        return view;
    }


    private void completeAppBarInfo(Usuario usuario, ViewGroup container) {
        getActivity().setTitle("Hola "+usuario.getNombre());
        View padre = (View) container.getParent();
        AppBarLayout appBar = (AppBarLayout) padre.findViewById(R.id.appbar);
        //appBar.addView(alguna view creada);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
