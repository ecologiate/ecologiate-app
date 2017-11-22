package com.app.ecologiate.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.fragments.InicioFragment;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.Opinion;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.models.PuntoRecoleccion;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.SoundService;
import com.app.ecologiate.services.UserManager;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;


public class PuntoRecInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mContent;
    private HashMap<String, PuntoRecoleccion> mapaMarkerPdr;
    private final Context context;
    private final LayoutInflater inflater;
    private final MapWrapperLayout mapWrapperLayout;
    private final Producto producto;
    private final ApiCallService apiCallService = ApiCallService.getInstance();
    private final ProgressDialog prgDialog;
    private HashMap<Long, Opinion> mapaOpinionPdr;

    public PuntoRecInfoWindowAdapter(Context context, HashMap<String, PuntoRecoleccion> mapaMarkerPdr,
                                     MapWrapperLayout wrapperLayout, Producto producto) {

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContent = inflater.inflate(R.layout.marker_info_window, null);
        this.mapaMarkerPdr = mapaMarkerPdr;
        this.context = context;
        this.mapWrapperLayout = wrapperLayout;
        this.producto = producto;

        prgDialog = new ProgressDialog(context);
        prgDialog.setCancelable(false);
        mapaOpinionPdr = new HashMap<>();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContent);
        mapWrapperLayout.setMarkerWithInfoWindow(marker, mContent);
        return mContent;
    }

    private void render(Marker marker, View view) {
        //la manera de vincular que tengo es poniendo en el title el id del pdr
        final PuntoRecoleccion pdr = mapaMarkerPdr.get(marker.getTitle());

        TextView titleUi = ((TextView) view.findViewById(R.id.tvDescripcion));
        titleUi.setText(pdr.getDescripcion());

        TextView direccionUI = ((TextView) view.findViewById(R.id.tvDireccion));
        direccionUI.setText(pdr.getDireccion());

        TextView creadorUI = ((TextView) view.findViewById(R.id.tvOwner));
        creadorUI.setText("Registrado por "+pdr.getUsuarioAlta().getNombreCompleto());

        LinearLayout materialesBar = (LinearLayout) view.findViewById(R.id.layoutMateriales);
        TextView materialesUI = ((TextView) view.findViewById(R.id.tvMateriales));

        String txtMateriales = "Materiales: ";
        materialesBar.removeAllViewsInLayout();
        for(int i = 0; i<pdr.getMateriales().size(); i++){
            Material m = pdr.getMateriales().get(i);
            //barrita de íconos
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

        final TextView btnMasInfo = (TextView) view.findViewById(R.id.linkMasInfo);
        btnMasInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action){
                    case MotionEvent.ACTION_UP:
                        SoundService.vibrateShort(context);
                        abrirOpiniones(pdr);
                        break;
                }
                return true;
            }
        });

        final ImageView btnReciclar = (ImageView) view.findViewById(R.id.btnReciclar);
        if(producto == null){
            btnReciclar.setVisibility(View.GONE);
        }
        btnReciclar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action){
                    case MotionEvent.ACTION_UP:
                        SoundService.vibrateShort(context);
                        reciclarProducto(producto, pdr);
                        break;
                }
                return true;
            }
        });
    }

    private void abrirOpiniones(final PuntoRecoleccion pdr){
        prgDialog.show();
        final Opinion currentOpinion = new Opinion();
        mapaOpinionPdr.put(pdr.getId(), currentOpinion);

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                try {
                    if (response != null && response.has("punto")) {
                        List<Opinion> opiniones = new ArrayList<>();
                        PuntoRecoleccion puntoConOpiniones = PuntoRecoleccion.getFromJson(response.getJSONObject("punto"));

                        if(puntoConOpiniones.getOpiniones() != null && !puntoConOpiniones.getOpiniones().isEmpty()){
                            opiniones = puntoConOpiniones.getOpiniones();
                            for(int i = 0; i<opiniones.size(); i++){
                                Opinion o = opiniones.get(i);
                                if(UserManager.getUser().getId().equals(o.getUsuario().getId())){
                                    mapaOpinionPdr.put(pdr.getId(), o);
                                }
                            }
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View viewDialog = inflater.inflate(R.layout.dialogo_opiniones,null);
                        RecyclerView recyclerView = (RecyclerView) viewDialog.findViewById(R.id.recyclerViewOpiniones);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        OpinionesAdapter adapter = new OpinionesAdapter(opiniones);
                        recyclerView.setAdapter(adapter);

                        final ImageView ivUp = (ImageView) viewDialog.findViewById(R.id.ivUp);
                        final ImageView ivDown = (ImageView) viewDialog.findViewById(R.id.ivDown);
                        ivUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentOpinion.setPuntuacion(true);
                                ivUp.setImageResource(currentOpinion.getIconResourceId());
                                ivDown.setImageResource(R.drawable.thumb_down_off);
                            }
                        });
                        ivDown.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentOpinion.setPuntuacion(false);
                                ivDown.setImageResource(currentOpinion.getIconResourceId());
                                ivUp.setImageResource(R.drawable.thumb_up_off);
                            }
                        });
                        final EditText etComentario = (EditText) viewDialog.findViewById(R.id.tvComentario);

                        if(currentOpinion != null){
                            etComentario.setText(currentOpinion.getComentario());
                            if(currentOpinion.getPuntuacion() != null){
                                if(currentOpinion.getPuntuacion()){
                                    ivUp.setImageResource(currentOpinion.getIconResourceId());
                                    ivDown.setImageResource(R.drawable.thumb_down_off);
                                }else{
                                    ivDown.setImageResource(currentOpinion.getIconResourceId());
                                    ivUp.setImageResource(R.drawable.thumb_up_off);
                                }
                            }
                        }

                        builder.setView(viewDialog);
                        builder.setTitle("Opiniones de punto de recolección");
                        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(etComentario.getText().length()>0 && currentOpinion.getPuntuacion()!=null){
                                    String comment = etComentario.getText().toString();
                                    Boolean puntuacion = currentOpinion.getPuntuacion();
                                    Long userId = UserManager.getUser().getId();
                                    Long pdrId = pdr.getId();
                                    saveOpinion(comment, puntuacion, userId, pdrId, pdr);
                                }else{
                                    Toast.makeText(context,"Ingrese contenido a su opinión",Toast.LENGTH_LONG).show();
                                }
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
                }catch (JSONException e){
                    Toast.makeText(context,"Error en formato de respuesta", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(context, "URL no encontrada", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Error en el Backend", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Error Inesperado [" + statusCode + "]", throwable);
                }
            }
        };

        apiCallService.getOpinionesDePdr(pdr.getId(), responseHandler);
    }

    private void saveOpinion(String comment, Boolean puntuacion, Long userId, Long pdrId, final PuntoRecoleccion pdr) {
        prgDialog.show();

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("usuario", userId);
            jsonBody.put("punto_rec_id", pdrId);
            jsonBody.put("puntuacion", puntuacion);
            jsonBody.put("comentario", comment);
            bodyEntity = new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error armando Json", Toast.LENGTH_LONG).show();
        }

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                abrirOpiniones(pdr);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(context, "URL no encontrada", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Error en el Backend", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Error Inesperado [" + statusCode + "]", throwable);
                }
            }
        };

        apiCallService.postOpinionPuntoDeRecoleccion(context, bodyEntity, responseHandler);
    }

    private void reciclarProducto(Producto producto, PuntoRecoleccion pdr){
        Long userId = UserManager.getUser().getId();
        Long productId = producto.getId();
        Long puntoRecId = pdr.getId();
        Integer cant = 1; //TODO siempre va 1 por ahora

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("product_id", productId);
            jsonBody.put("user", userId);
            jsonBody.put("puntorec", puntoRecId);
            jsonBody.put("cant", cant);
            bodyEntity = new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error armando Json", Toast.LENGTH_LONG).show();
        }

        prgDialog.show();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                prgDialog.hide();
                if (response != null) {
                    Double puntosSumados = 0d;
                    try{
                        puntosSumados = response.getDouble("puntos_sumados");
                    }catch (JSONException e){
                        Toast.makeText(context, "Error en json de respuesta", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(context, "¡Sumaste "+puntosSumados+ " puntos!", Toast.LENGTH_LONG).show();
                    UserManager.updateUser(context, new ResultCallback() {
                        @Override
                        public void onResult(@NonNull Result result) {
                            Fragment inicioFragment = new InicioFragment();
                            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentFragment, inicioFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(context, "URL no encontrada", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(context, "Error en el Backend", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Error Inesperado [" + statusCode + "]", throwable);
                }
            }
        };

        apiCallService.postReciclaje(context, bodyEntity, responseHandler);
    }

}
