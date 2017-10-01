package com.app.ecologiate.models;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.ecologiate.R;
import java.util.List;


public class CampaniaAdapter extends RecyclerView.Adapter<CampaniaAdapter.ViewHolder>{

    private List<Tip> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView ivFoto;
        public TextView txTitulo;
        public TextView txDescripcion;
        public ViewHolder(View v) {
            super(v);
            ivFoto = (ImageView) v.findViewById(R.id.campaniaFoto);
            txTitulo = (TextView) v.findViewById(R.id.campaniaTitulo);
            txDescripcion = (TextView) v.findViewById(R.id.campaniaDescripcion);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CampaniaAdapter(List<Tip> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CampaniaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_campania,parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tip model = mDataset.get(position);
        holder.ivFoto.setImageResource(model.getFoto());
        holder.txTitulo.setText(model.getTitulo());
        holder.txDescripcion.setText(model.getDescripcion());

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
