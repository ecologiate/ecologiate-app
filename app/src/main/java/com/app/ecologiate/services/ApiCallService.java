package com.app.ecologiate.services;

import android.content.Context;
import android.text.TextUtils;

import com.app.ecologiate.utils.EcologiateConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;


public class ApiCallService {

    static final String SERVER_URL = EcologiateConstants.SERVER_URL;

    AsyncHttpClient client = new AsyncHttpClient();

    /**
        ***************************************************************
        ACÁ VAN TODAS LAS LLAMADAS A LAS APIS DEL BACKEND CON SUS URLS
        ***************************************************************
     */

    public void getProductoPorCodigo(String codigo, JsonHttpResponseHandler responseHandler){
        //armo la url del get
        String url = SERVER_URL + "/api/producto/codigo/" + codigo;
        get(url, responseHandler);
    }

    public void getProductoPorId(Long id, JsonHttpResponseHandler responseHandler){
        //armo la url del get
        String url = SERVER_URL + "/api/producto/" + id;
        get(url, responseHandler);
    }

    public void getProductoPorTitulo(String titulo, JsonHttpResponseHandler responseHandler){
        //armo la url del get
        String url = SERVER_URL + "/api/producto/nombre/" + titulo;
        get(url, responseHandler);
    }

    public void getPuntosDeRecoleccion(List<String> materiales, String pais, String area, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/pdr";
        List<String> parametros = new ArrayList<>();
        if(materiales != null && !materiales.isEmpty()){
            //agrego ids de materiales separados por coma
            parametros.add("materiales=" + TextUtils.join(",", materiales));
        }
        if(pais != null){
            parametros.add("pais=" + pais);
        }
        if(area != null){
            parametros.add("area=" + area);
        }
        if(!parametros.isEmpty()){
            //agrego los parámetros separados por &
            //ejemplo /api/pdr?materiales=1,2,3&area=Misiones&pais=Argentina
            url += "?"+TextUtils.join("&", parametros);
        }
        get(url, responseHandler);
    }

    public void postPuntoDeRecoleccion(Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/pdr";
        post(url, context, entityBody, responseHandler);
    }

    public void postProducto(Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/alta_producto";
        post(url, context, entityBody, responseHandler);
    }

    public void postReciclaje(Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/reciclar/reciclar_producto";
        post(url, context, entityBody, responseHandler);
    }

    public void login(Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/usuario/login";
        put(url, context, entityBody, responseHandler);
    }

    public void getGruposDelUsuario(Context context, Long idUsuario, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/grupos/" + idUsuario;
        get(url, responseHandler);
    }


    /**
     ***************************************************************
     Métodos genéricos
     ***************************************************************
     */

    public void get(String url, JsonHttpResponseHandler responseHandler){
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ApiCallException("Error formateando URL "+url, e);
        }
        client = new AsyncHttpClient();
        client.get(url, responseHandler);
    }

    public void post(String url, Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        client.post(context, url, entityBody, "application/json", responseHandler);
    }

    public void put(String url, Context context, HttpEntity entityBody, JsonHttpResponseHandler responseHandler){
        client.put(context, url, entityBody, "application/json", responseHandler);
    }


    public class ApiCallException extends RuntimeException{
        public ApiCallException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
