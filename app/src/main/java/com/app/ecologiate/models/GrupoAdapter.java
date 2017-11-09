package com.app.ecologiate.models;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.UserManager;
import com.app.ecologiate.utils.NumberUtils;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.ViewHolder>{

    private List<Grupo> gruposDataset = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private ResultCallback resultCallback;
    private ApiCallService apiCallService = new ApiCallService();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txTitulo;
        public TextView txIntegrantes;
        public TextView txMetricaArboles;
        public TextView txMetricaAgua;
        public TextView txMetricaEnergia;
        public TextView txMetricaEmisiones;
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
            txMetricaEmisiones = (TextView) v.findViewById(R.id.grupoContenidoEmisiones);
            btnInvitar = (Button) v.findViewById(R.id.btnInvitarGrupo);
            btnSalir = (Button) v.findViewById(R.id.btnSalirGrupo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GrupoAdapter(Context context, List<Grupo> myDataset, ResultCallback resultCallback) {
        this.gruposDataset = myDataset;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.resultCallback = resultCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = mInflater.inflate(R.layout.list_grupos,parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Grupo selectedGrupo = gruposDataset.get(position);
        //armo string de integrantes
        String htmlIntegrantes = "";
        List<Usuario> integrantes = selectedGrupo.getUsuarios();
        for(int i=0; i< integrantes.size(); i++){
            Usuario usuario = integrantes.get(i);
            htmlIntegrantes += usuario.getNombreCompleto()+" ("+NumberUtils.format(usuario.getPuntos())+") <br/>";
        }
        holder.txTitulo.setText(selectedGrupo.getNombre());
        holder.txIntegrantes.setText(Html.fromHtml(htmlIntegrantes));
        Impacto impactoDelGrupo = selectedGrupo.getImpacto();

        holder.txMetricaArboles.setText(NumberUtils.format(impactoDelGrupo.getArboles()));
        holder.txMetricaAgua.setText(NumberUtils.format(impactoDelGrupo.getAgua()));
        holder.txMetricaEnergia.setText(NumberUtils.format(impactoDelGrupo.getEnergia()));
        holder.txMetricaEmisiones.setText(NumberUtils.format(impactoDelGrupo.getEmisiones()));

        holder.btnInvitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.dialogo_invitargrupo,null);
                final EditText tvAmigo = (EditText) view1.findViewById(R.id.txtAmigo);
                builder.setView(view1);
                builder.setTitle("Invitar amigo a "+selectedGrupo.getNombre());
                builder.setPositiveButton("Invitar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //invitar
                                String emailAmigo = tvAmigo.getText().toString();
                                if(emailAmigo.length()>0){
                                    invitarAmigo(emailAmigo, selectedGrupo);
                                }else{
                                    Toast.makeText(context, "Debe ingresar un mail", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                builder.setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //no hacer nada supongo
                            }
                        });
                final AlertDialog dialog = builder.create();

                //este quilombo para poner un iconito
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        // if you do the following it will be left aligned, doesn't look
                        // correct
                        // button.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play,
                        // 0, 0, 0);
                        Drawable drawable = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_send);
                        // set the bounds to place the drawable a bit right
                        drawable.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
                                0, (int) (drawable.getIntrinsicWidth() * 1.5),
                                drawable.getIntrinsicHeight());
                        button.setCompoundDrawables(drawable, null, null, null);
                        // could modify the placement more here if desired
                        button.setCompoundDrawablePadding(25);
                    }
                });
                dialog.show();
            }
        });

        holder.btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.dialogo_salirgrupo,null);
                builder.setView(view1);
                builder.setTitle("Seguro que deseas abandonar el grupo "+selectedGrupo.getNombre());
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //salir
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

    private void invitarAmigo(String emailAmigo, Grupo grupo){
        Long usuarioId = UserManager.getUser().getId();
        Long grupoId = grupo.getId();

        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("usuario_id", usuarioId);
            jsonBody.put("email_invitado", emailAmigo);
            jsonBody.put("grupo_id", grupoId);
            bodyEntity = new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error armando Json", Toast.LENGTH_LONG).show();
        }
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response != null && response.has("status_code")) {
                        if (response.getInt("status_code")==200 || response.getInt("status_code")==201) {
                            //ok
                            if(resultCallback!=null){
                                resultCallback.onResult(new Status(statusCode));
                            }
                        } else {
                            if(response.getInt("status_code")==404)
                                Toast.makeText(context, "No se encontró un usuario con ese email", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(context, "Error desconocido: "+response.getInt("status_code"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Error en respuesta", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    //DVP: Si encuentra algun error parseando el Jason.
                    Toast.makeText(context, "Error en formato de Json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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

        apiCallService.postGrupoAmigo(context, bodyEntity, responseHandler);
    }


    @Override
    public int getItemCount() {
        return gruposDataset.size();
    }



}
