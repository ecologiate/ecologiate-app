package com.app.ecologiate.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Impacto;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.MaterialConObjetivos;
import com.app.ecologiate.models.Medalla;
import com.app.ecologiate.models.MedallaAdapter;
import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Objetivo;
import com.app.ecologiate.models.Usuario;
import com.app.ecologiate.services.ObjetivosManager;
import com.app.ecologiate.services.UserManager;
import com.app.ecologiate.utils.MapReducer;
import com.app.ecologiate.utils.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.ecologiate.R.string.nivel;


public class PerfilFragment extends AbstractEcologiateFragment implements MedallaAdapter.ItemClickListener {


    private OnFragmentInteractionListener mListener;

    public PerfilFragment() {}

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

    private MedallaAdapter medallaAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //DVP: Manejo el fragment como un view.
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(this, view);
        imagenAvatar.requestFocus();

        //seteo los datos del usuario
        Usuario usuario = UserManager.getUser();
        //Recupero avatar y nivel del usuario
        Nivel nivel = usuario.getNivel();
        Picasso.with(getContext()).load(usuario.getNivel().getImagenLink()).into(imagenAvatar);
        txNivelUsuario.setText(usuario.getNivel().getDescripcion());
        progressBarNivel.setMax((int ) (nivel.getPuntosHasta() - nivel.getPuntosDesde()));
        progressBarNivel.setProgress((int) (usuario.getPuntos() - nivel.getPuntosDesde()));

        Impacto impacto = usuario.getImpacto();
        if(impacto != null){
            contenidoAgua.setText(NumberUtils.format(impacto.getAgua()));
            contenidoArboles.setText(NumberUtils.format(impacto.getArboles()));
            contenidoEmisiones.setText(NumberUtils.format(impacto.getEmisiones()));
            contenidoEnergia.setText(NumberUtils.format(impacto.getEnergia()));
        }

        //grilla de medallas
        ObjetivosManager.completarObjetivosDelUser(usuario);
        recyclerView = (RecyclerView) view.findViewById(R.id.grillaMedallas);
        int numberOfColumns = 5;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        List<MaterialConObjetivos> materialesConObjetivos = ObjetivosManager.getMaterialesConObjetivos();

        medallaAdapter = new MedallaAdapter(getContext(), materialesConObjetivos);
        medallaAdapter.setClickListener(this);
        recyclerView.setAdapter(medallaAdapter);


        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        MaterialConObjetivos selected = medallaAdapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialogo_medallas,null);
        TextView titulo = (TextView) view1.findViewById(R.id.titMed);
        TextView cantReciclada = (TextView) view1.findViewById(R.id.nroReciclado);
        titulo.setText("Medallas por "+selected.getMaterial().getDescripcion().toLowerCase());
        cantReciclada.setText(String.valueOf(selected.getCantReciclada()));
        //TODO Recorrer los objetivos y pintar los que estén con el boolean cumplido en true
        //falta terminar
        builder.setView(view1);
        Dialog dialog = builder.create();
        dialog.show();
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
        return getResources().getString(R.string.perfil_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
