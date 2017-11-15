package com.app.ecologiate.fragments;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.MaterialAdapter;
import com.app.ecologiate.services.MaterialsManager;

import java.util.List;
import java.util.Set;

public class SeleccionMateriales extends LinearLayout {


    private RecyclerView grillaMateriales;
    private MaterialAdapter materialAdapter;

    public SeleccionMateriales(Context context) {
        super(context);
        init();
    }

    public SeleccionMateriales(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeleccionMateriales(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Creamos la interfaz a partir del layout
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.seleccion_materiales, this, true);

        // Obtenemos las referencias a las vistas hijas
        grillaMateriales = (RecyclerView) findViewById(R.id.grillaMateriales);
        List<Material> materiales = MaterialsManager.getMateriales();
        int numberOfColumns = 2;
        grillaMateriales.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        materialAdapter = new MaterialAdapter(getContext(), materiales);
        grillaMateriales.setAdapter(materialAdapter);

    }


    public Set<Material> getSelectedMaterials() {
        return materialAdapter.getSelectedMaterials();
    }

    public void setSelectedMaterials(Set<Material> selectedMaterials) {
        materialAdapter.setSelectedMaterials(selectedMaterials);
    }
}