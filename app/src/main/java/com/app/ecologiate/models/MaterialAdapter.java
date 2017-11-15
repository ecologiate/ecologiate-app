package com.app.ecologiate.models;


import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecologiate.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private List<Material> materiales = new ArrayList<>();
    private Set<Material> selectedMaterials = new HashSet<>();
    private LayoutInflater mInflater;

    public MaterialAdapter(Context context, List<Material> materiales) {
        this.mInflater = LayoutInflater.from(context);
        this.materiales = materiales;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MaterialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_material_seleccion, parent, false);
        return new MaterialAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(MaterialAdapter.ViewHolder holder, int position) {
        final Material material = materiales.get(position);
        holder.currentMaterial = material;
        holder.materialIcon.setImageResource(material.getImageResourceId());
        holder.materialNombre.setText(material.getDescripcion());
        holder.materialCheck.setChecked(selectedMaterials.contains(material));
    }


    // total number of cells
    @Override
    public int getItemCount() {
        return materiales.size();
    }

    public Set<Material> getSelectedMaterials(){
        return selectedMaterials;
    }

    public void setSelectedMaterials(Set<Material> selected){
        this.selectedMaterials = selected;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView materialIcon;
        TextView materialNombre;
        AppCompatCheckBox materialCheck;
        Material currentMaterial;

        ViewHolder(View itemView) {
            super(itemView);
            materialIcon = (ImageView) itemView.findViewById(R.id.materialImagen);
            materialNombre = (TextView) itemView.findViewById(R.id.materialNombre);
            materialCheck = (AppCompatCheckBox) itemView.findViewById(R.id.materialCheck);
            materialCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        selectedMaterials.add(currentMaterial);
                    }else{
                        if(selectedMaterials.contains(currentMaterial)){
                            selectedMaterials.remove(currentMaterial);
                        }
                    }
                }
            });
        }
    }


}