package com.app.ecologiate.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.ecologiate.R;
import com.app.ecologiate.models.CampaniaAdapter;
import com.app.ecologiate.models.Impacto;
import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Tip;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.UserManager;
import com.app.ecologiate.utils.NumberUtils;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InicioFragment extends AbstractEcologiateFragment {

    private RecyclerView mRecyclerViewCampania;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;



    public InicioFragment() {}

    @BindView(R.id.avatar)
    ImageView imagenAvatar;
    @BindView(R.id.nivelActual)
    TextView txNivelUsuario;
    @BindView(R.id.simpleProgressBar)
    ProgressBar progressBarNivel;
    @BindView(R.id.contenidoAboles)
    TextView contenidoArboles;
    @BindView(R.id.contenidoAgua)
    TextView contenidoAgua;
    @BindView(R.id.contenidoEnergia)
    TextView contenidoEnergia;
    @BindView(R.id.contenidoEmisiones)
    TextView contenidoEmisiones;

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

        mRecyclerViewCampania = (RecyclerView) view.findViewById(R.id.campaniasFragment);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewCampania.setHasFixedSize(true);

        mRecyclerViewCampania.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerViewCampania.setLayoutManager(mLayoutManager);

        //creo mis mails
        List<Tip> myDataset = new ArrayList<>();

        myDataset.add(new Tip("Garrahan", "Juntemos las tapitas para ayudar a los chicos ", R.raw.tapitas));
        myDataset.add(new Tip("Fort", "Mantene el aire a 24° para que no se corte la Looz", R.raw.fort));


        // specify an adapter (see also next example)
        mAdapter = new CampaniaAdapter(myDataset);
        mRecyclerViewCampania.setAdapter(mAdapter);


        setHasOptionsMenu(true); //para que me agregue un menu, tiene que ir al final
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //seteo los datos del usuario
        Usuario usuario = UserManager.getUser();
        Nivel nivel = usuario.getNivel();
        Picasso.with(getContext()).load(usuario.getNivel().getImagenLink()).into(imagenAvatar);
        txNivelUsuario.setText(nivel.getDescripcion());
        progressBarNivel.setMax((int ) (nivel.getPuntosHasta() - nivel.getPuntosDesde()));
        progressBarNivel.setProgress((int) (usuario.getPuntos() - nivel.getPuntosDesde()));
        Impacto impacto = usuario.getImpacto();
        if(impacto != null){
            contenidoAgua.setText(NumberUtils.format(impacto.getAgua()));
            contenidoArboles.setText(NumberUtils.format(impacto.getArboles()));
            contenidoEmisiones.setText(NumberUtils.format(impacto.getEmisiones()));
            contenidoEnergia.setText(NumberUtils.format(impacto.getEnergia()));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_superior_inicio, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reciclar:
                Fragment reciclarFragment = new ReciclarFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, reciclarFragment)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public String getTitle() {
        return "Hola "+UserManager.getUser().getNombre();
    }

    @Override
    public String getSubTitle() {
        return null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
