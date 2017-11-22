package com.app.ecologiate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Opinion;

import java.util.List;


public class OpinionesAdapter extends RecyclerView.Adapter<OpinionesAdapter.ViewHolder>{

    private List<Opinion> mDataset;
    //SimpleDateFormat formatter =  new SimpleDateFormat("dd/MM/yy");

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivPuntuacion;
        public TextView txComentario;

        public ViewHolder(View v) {
            super(v);
            ivPuntuacion = (ImageView) v.findViewById(R.id.ivPuntuacion);
            txComentario = (TextView) v.findViewById(R.id.tvComentario);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OpinionesAdapter(List<Opinion> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OpinionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opinion,parent, false);
        // set the view's size, margins, paddings and layout parameters

        OpinionesAdapter.ViewHolder vh = new OpinionesAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(OpinionesAdapter.ViewHolder holder, int position) {
        Opinion model = mDataset.get(position);
        holder.txComentario.setText(model.getUsuario().getNombreCompleto()+": "+model.getComentario());
        holder.ivPuntuacion.setImageResource(model.getIconResourceId());
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
