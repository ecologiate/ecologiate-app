package com.app.ecologiate.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.PuntoRecoleccion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;


public class PuntoRecInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mContent;
    private HashMap<String, PuntoRecoleccion> mapaMarkerPdr;
    private final Context context;
    private final LayoutInflater inflater;

    public PuntoRecInfoWindowAdapter(Context context, HashMap<String, PuntoRecoleccion> mapaMarkerPdr) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        this.mContent = inflater.inflate(R.layout.marker_info_window, null);
        this.mapaMarkerPdr = mapaMarkerPdr;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContent);
        return mContent;
    }

    private void render(Marker marker, View view) {
        //la manera de vincular que tengo es poniendo en el title el id del pdr
        PuntoRecoleccion pdr = mapaMarkerPdr.get(marker.getTitle());

        TextView titleUi = ((TextView) view.findViewById(R.id.tvDescripcion));
        titleUi.setText(pdr.getDescripcion());

        TextView direccionUI = ((TextView) view.findViewById(R.id.tvDireccion));
        direccionUI.setText(pdr.getDireccion());

        LinearLayout materialesBar = (LinearLayout) view.findViewById(R.id.layoutMateriales);
        TextView materialesUI = ((TextView) view.findViewById(R.id.tvMateriales));

        String txtMateriales = "Materiales: ";
        for(int i = 0; i<pdr.getMateriales().size(); i++){
            Material m = pdr.getMateriales().get(i);
            //barrita de íconos
            materialesBar.removeAllViewsInLayout();
            ImageView icon = new ImageView(context);
            icon.setImageResource(m.getImageResourceId());
            materialesBar.addView(icon);
            //textview
            txtMateriales += m.getDescripcion();
            if((i+1)<pdr.getMateriales().size()){
                txtMateriales += ", ";
            }
        }
        materialesUI.setText(txtMateriales);

        Button btnMasInfo = (Button) view.findViewById(R.id.linkMasInfo);
        btnMasInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View viewDialog = inflater.inflate(R.layout.dialogo_opiniones,null);
                RecyclerView recyclerView = (RecyclerView) viewDialog.findViewById(R.id.recyclerViewOpiniones);
                builder.setView(viewDialog);
                builder.setTitle("Opiniones de punto de recolección");
                builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO agrego mi review

                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nada
                    }
                });
                builder.create().show();
            }
        });

    }

}
