package com.app.ecologiate.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecologiate.R;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyModel> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivFoto;
        public TextView txTitulo;
        public TextView txDescripcion;
        public ViewHolder(View v) {
            super(v);
            ivFoto = (ImageView) v.findViewById(R.id.tipFoto);
            txTitulo = (TextView) v.findViewById(R.id.tipTitulo);
            txDescripcion = (TextView) v.findViewById(R.id.tipDescripcion);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<MyModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MyModel model = mDataset.get(position);
        holder.ivFoto.setImageResource(model.getFoto());
        holder.txTitulo.setText(model.getTitulo());
        holder.txDescripcion.setText(model.getDescripcion());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
