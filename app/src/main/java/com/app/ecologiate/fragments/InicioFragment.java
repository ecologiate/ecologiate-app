package com.app.ecologiate.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.app.ecologiate.models.Campania;
import com.app.ecologiate.models.CampaniaAdapter;
import com.app.ecologiate.models.Impacto;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Tip;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.MaterialsManager;
import com.app.ecologiate.services.UserManager;
import com.app.ecologiate.utils.NumberUtils;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @BindView(R.id.inicio_impacto)
    ImpactoView impactoView;

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

        //creo mis campanias
        List<Campania> myDataset = new ArrayList<>();
        //todo esto es un mock
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        try {
            fechaInicio = sdf.parse("2017-09-20T00:00:00.000Z");
            fechaFin = sdf.parse("2018-11-11T00:00:00.000Z");
        }catch (Exception e){}
        myDataset.add(new Campania(1L,"Garrahan","Juntemos tapitas para ayudar a los chicos",200,fechaInicio,fechaFin, null,
                new Material(5L,"Tapitas",0d,0d,0d,0d,0d,null), null));


        // specify an adapter (see also next example)
        mAdapter = new CampaniaAdapter(myDataset);
        mRecyclerViewCampania.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true); //para que me agregue un menu, tiene que ir lo más al final posible
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
            impactoView.setImpactoAgua(NumberUtils.format(impacto.getAgua())+" litros");
            impactoView.setImpactoEnergia(NumberUtils.format(impacto.getEnergia())+" kw");
            impactoView.setImpactoArboles(NumberUtils.format(impacto.getArboles())+" árboles");
            impactoView.setImpactoEmisiones(NumberUtils.format(impacto.getEmisiones())+" kg");
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
