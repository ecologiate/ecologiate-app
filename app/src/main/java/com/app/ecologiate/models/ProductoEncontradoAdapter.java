package com.app.ecologiate.models;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.fragments.ResultadoFragment;
import com.app.ecologiate.services.ApiCallService;
import com.google.android.gms.common.api.Api;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

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

                ApiCallService apiCallService = ApiCallService.getInstance();
                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        try {
                            if(response.has("status_code") && response.getInt("status_code") == 200){
                                Producto productoCompleto = Producto.getFromJson(response.getJSONObject("producto"));
                                Fragment resultadoFragment = ResultadoFragment.newInstance(productoCompleto);
                                ((AppCompatActivity) holderContext).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentFragment, resultadoFragment)
                                        .commit();
                            }else{
                                Toast.makeText(holderContext, "No se encontró el producto", Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e) {
                            Toast.makeText(holderContext, "Error en formato de Json", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
                        if (statusCode == 404){
                            Toast.makeText(holderContext, "URL no encontrada", Toast.LENGTH_LONG).show();
                        }else if (statusCode == 500){
                            Toast.makeText(holderContext, "Error en el Backend", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(holderContext, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            Log.e("API_ERROR", "Error Inesperado ["+statusCode+"]", throwable);
                        }
                    }
                };
                apiCallService.getProductoPorId(producto.getId(), responseHandler);
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
