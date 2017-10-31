package com.app.ecologiate.models;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.ecologiate.R;

import java.util.ArrayList;
import java.util.List;

public class MedallaAdapter extends RecyclerView.Adapter<MedallaAdapter.ViewHolder> {

    private List<MaterialConObjetivos> materialesConObjetivos = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MedallaAdapter(Context context, List<MaterialConObjetivos> materialesConObjetivos) {
        this.mInflater = LayoutInflater.from(context);
        this.materialesConObjetivos = materialesConObjetivos;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_medalla_material, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MaterialConObjetivos materialConObjetivos = materialesConObjetivos.get(position);
        Material material = materialConObjetivos.getMaterial();
        holder.boton.setImageResource(material.getImageResourceId());
        int colorIcon = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_icon_disabled);
        int colorBackground = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_disabled);

        Objetivo maxObjetivoCumplido = getObjetivoCumplidoMasAlto(materialConObjetivos.getObjetivos());
        if(maxObjetivoCumplido != null){
            colorIcon = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_icon_enabled);
            if(maxObjetivoCumplido.getMedalla().getNombre().toLowerCase().contains("oro")) {
                colorBackground = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_oro);
            }
            if(maxObjetivoCumplido.getMedalla().getNombre().toLowerCase().contains("plata")){
                colorBackground = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_plata);
            }
            if(maxObjetivoCumplido.getMedalla().getNombre().toLowerCase().contains("bronce")){
                colorBackground = ContextCompat.getColor(mInflater.getContext(), R.color.medalla_bronce);
            }
        }
        holder.boton.setColorFilter(colorIcon);
        holder.boton.getBackground().setColorFilter(colorBackground, PorterDuff.Mode.SRC_IN);
    }

    private Objetivo getObjetivoCumplidoMasAlto(List<Objetivo> objetivos){
        Objetivo result = null;
        //ya vienen ordenados de menor a mayor por el sort en el constructor que le puse
        for(int i = 0; i<objetivos.size(); i++){
            Objetivo o = objetivos.get(i);
            if(o.getCumplido()){
                result = o;
            }
        }
        return result;
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return materialesConObjetivos.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton boton;

        ViewHolder(View itemView) {
            super(itemView);
            boton = (ImageButton) itemView.findViewById(R.id.imagenMedalla);
            boton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public MaterialConObjetivos getItem(int index) {
        return materialesConObjetivos.get(index);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}