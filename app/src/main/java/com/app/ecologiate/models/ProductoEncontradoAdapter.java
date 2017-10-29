package com.app.ecologiate.models;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.ecologiate.R;
import com.app.ecologiate.fragments.ResultadoFragment;

import java.util.List;

public class ProductoEncontradoAdapter extends RecyclerView.Adapter<ProductoEncontradoAdapter.ViewHolder> {

private List<Producto> productosDataset;


public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView txNombreProducto;
    public Context holderContext;
    private Producto producto;
    public ViewHolder(View v) {
        super(v);
        txNombreProducto = (TextView) v.findViewById(R.id.productoEncontrado);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment resultadoFragment = ResultadoFragment.newInstance(producto);
                ((AppCompatActivity) holderContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, resultadoFragment)
                        //.addToBackStack(String.valueOf(resultadoFragment.getId()))
                        .commit();
            }
        });

    }
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductoEncontradoAdapter(List<Producto> productosDataset) {
        this.productosDataset = productosDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductoEncontradoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_busquedamanual,parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        vh.holderContext = parent.getContext();

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.producto = productosDataset.get(position);
        holder.txNombreProducto.setText(holder.producto.getNombreProducto());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productosDataset.size();
    }
}
