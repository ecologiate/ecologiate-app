package com.app.ecologiate.models;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.app.ecologiate.R;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolder>{

    private List<Grupos> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txTitulo;
        public TextView txIntegrante;
        public TextView txPuntos;
        public TextView txMetricaArboles;
        public TextView txMetricaAgua;
        public TextView txMetricaEnergia;
        public ViewHolder(View v) {
            super(v);
            txTitulo = (TextView) v.findViewById(R.id.grupoTitulo);
            txIntegrante = (TextView) v.findViewById(R.id.grupoIntegrante);
            txPuntos = (TextView) v.findViewById(R.id.grupoIntegrantePuntos);
            txMetricaArboles = (TextView) v.findViewById(R.id.grupoContenidoAboles);
            txMetricaAgua = (TextView) v.findViewById(R.id.grupoContenidoAgua);
            txMetricaEnergia = (TextView) v.findViewById(R.id.grupoContenidoEnergia);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GrupoAdapter(List<Grupos> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grupos,parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grupos model = mDataset.get(position);
        holder.txTitulo.setText(model.getTitulo());
        holder.txIntegrante.setText(model.getIntegrante());
        holder.txPuntos.setText(model.getPuntos());
        holder.txMetricaArboles.setText(model.getMetricaArboles());
        holder.txMetricaAgua.setText(model.getMetricaAgua());
        holder.txMetricaEnergia.setText(model.getMetricaEnergia());

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
