package com.app.ecologiate.services;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.MaterialConObjetivos;
import com.app.ecologiate.models.Objetivo;
import com.app.ecologiate.models.Reciclaje;
import com.app.ecologiate.models.Usuario;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class ObjetivosManager {

    private static ApiCallService apiCallService = new ApiCallService();

    private static List<Objetivo> objetivos = new ArrayList<>();
    private static List<MaterialConObjetivos> materialesConObjetivos = new ArrayList<>();

    /**
     * Método que trae todos los objetivos de la base
     **/
    public static void init(final Context context, final ResultCallback callback){
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if(response.length()>0){
                    try {
                        objetivos = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject objetivoJsonObject = response.getJSONObject(i);
                            Objetivo objetivo = Objetivo.getFromJson(objetivoJsonObject);
                            objetivos.add(objetivo);
                        }
                        completarMaterialesConObjetivos();
                    }catch (JSONException e){
                        Log.e("JSON_ERROR", e.getMessage());
                        throw new RuntimeException("Error en formato de json de objetivos: "+ e.getMessage());
                    }
                }else{
                    Toast.makeText(context, "No se encontraron objetivos", Toast.LENGTH_LONG).show();
                }
                if(callback != null){
                    final int status = statusCode;
                    callback.onResult(new Status(status));
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

        apiCallService.getObjetivos(responseHandler);
    }

    private static void completarMaterialesConObjetivos(){
        materialesConObjetivos = new ArrayList<>();
        SimpleArrayMap<Long, List<Objetivo>> objetivosMap = new SimpleArrayMap<>();
        SimpleArrayMap<Long,Material> materialesMap = new SimpleArrayMap<>();
        Set<Material> materiales = new HashSet<>();

        for(int i = 0; i<objetivos.size(); i++){
            Objetivo ob = objetivos.get(i);
            Material material = ob.getMaterial();
            materiales.add(material);
            Long materialId = material.getId();
            materialesMap.put(materialId, material);
            if(objetivosMap.get(materialId) == null){
                objetivosMap.put(materialId, new ArrayList<Objetivo>());
            }
            objetivosMap.get(materialId).add(ob);
        }
        //completo la lista de materiales existentes
        MaterialsManager.init(new ArrayList<>(materiales));

        for(int i = 0; i < objetivosMap.size(); i++){
            Material mat = materialesMap.get(objetivosMap.keyAt(i));
            List<Objetivo> objetivosDelMaterial = objetivosMap.get(mat.getId());
            MaterialConObjetivos materialConObjetivos = new MaterialConObjetivos(mat,0,objetivosDelMaterial);
            materialesConObjetivos.add(materialConObjetivos);
        }
    }

    public static void completarObjetivosDelUser(Usuario usuario){
        completarMaterialesConObjetivos();
        List<Objetivo> objetivosCumplidos = usuario.getObjetivosCumplidos();
        List<Reciclaje> reciclajes = usuario.getReciclajes();

        if(objetivosCumplidos != null && !objetivosCumplidos.isEmpty()){
            Iterator<Objetivo> it = objetivosCumplidos.iterator();
            while(it.hasNext()){
                Objetivo objetivoCumplido = it.next();
                Material material = objetivoCumplido.getMaterial();
                //Producto producto = objetivo.getProducto();
                Iterator<MaterialConObjetivos> matConObjIt = materialesConObjetivos.iterator();
                while(matConObjIt.hasNext()){
                    MaterialConObjetivos matConObjetivos = matConObjIt.next();
                    if(matConObjetivos.getMaterial().equals(material)){
                        //cumplí algún objetivo del material
                        List<Objetivo> objetivosDelMaterial = matConObjetivos.getObjetivos();
                        Iterator<Objetivo> objetivoDelMaterialIt = objetivosDelMaterial.iterator();
                        while(objetivoDelMaterialIt.hasNext()){
                            Objetivo objetivoDelMaterial = objetivoDelMaterialIt.next();
                            if(objetivoDelMaterial.equals(objetivoCumplido)){
                                //finalmente marco como cumplido el material para este usuario
                                objetivoDelMaterial.setCumplido(true);
                            }
                        }

                    }
                }
            }
        }
        if(reciclajes!=null && !reciclajes.isEmpty()){
            Iterator<Reciclaje> reciclajeIterator = reciclajes.iterator();
            while(reciclajeIterator.hasNext()){
                Reciclaje reciclaje = reciclajeIterator.next();
                Material materialReciclado = reciclaje.getProducto().getMaterial();
                Iterator<MaterialConObjetivos> matConObjIt = materialesConObjetivos.iterator();
                while(matConObjIt.hasNext()) {
                    MaterialConObjetivos matConObjetivos = matConObjIt.next();
                    if(matConObjetivos.getMaterial().equals(materialReciclado)){
                        //incremento la cant reciclada
                        matConObjetivos.setCantReciclada(matConObjetivos.getCantReciclada() + reciclaje.getCantProd());
                    }
                }
            }
        }
    }


    public static List<Objetivo> getObjetivos(){
        return objetivos;
    }

    public static List<MaterialConObjetivos> getMaterialesConObjetivos(){
        return materialesConObjetivos;
    }
}
