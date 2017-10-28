package com.app.ecologiate.models;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import com.app.ecologiate.R;
import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolder>{

    private List<Grupos> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txTitulo;
        public TextView txIntegrantes;
        public TextView txMetricaArboles;
        public TextView txMetricaAgua;
        public TextView txMetricaEnergia;
        public Button btnInvitar;
        public Button btnSalir;

        public Context holderContext;

        public ViewHolder(View v) {
            super(v);
            txTitulo = (TextView) v.findViewById(R.id.grupoTitulo);
            txIntegrantes = (TextView) v.findViewById(R.id.grupoIntegrantes);
            txMetricaArboles = (TextView) v.findViewById(R.id.grupoContenidoAboles);
            txMetricaAgua = (TextView) v.findViewById(R.id.grupoContenidoAgua);
            txMetricaEnergia = (TextView) v.findViewById(R.id.grupoContenidoEnergia);
            btnInvitar = (Button) v.findViewById(R.id.btnInvitarGrupo);
            btnSalir = (Button) v.findViewById(R.id.btnSalirGrupo);

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
        vh.holderContext = parent.getContext();
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Grupos model = mDataset.get(position);
        //armo string de integrantes
        String htmlIntegrantes = "";
        List<Usuario> integrantes = model.getIntegrantes();
        for(int i=0; i< integrantes.size(); i++){
            Usuario usuario = integrantes.get(i);
            htmlIntegrantes += usuario.getNombreCompleto()+" ("+usuario.getPuntos()+") <br/>";
        }
        holder.txTitulo.setText(model.getTitulo());
        holder.txIntegrantes.setText(Html.fromHtml(htmlIntegrantes));
        holder.txMetricaArboles.setText(model.getMetricaArboles());
        holder.txMetricaAgua.setText(model.getMetricaAgua());
        holder.txMetricaEnergia.setText(model.getMetricaEnergia());

        final Context context = holder.holderContext;

        final String nombreGrupo = model.getTitulo();

        holder.btnInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.dialogo_invitargrupo,null);
                builder.setView(view1);
                builder.setTitle("Invitar amigo a "+nombreGrupo);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //invitar
                            }
                        })
                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //no hacer nada supongo
                                    }
                                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
