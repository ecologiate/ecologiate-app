package com.app.ecologiate.service;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;


public class ApiCallService {

    static final String SERVER_URL = "https://ecologiate.herokuapp.com";

    AsyncHttpClient client = new AsyncHttpClient();

    /**
        ***************************************************************
        ACÁ VAN TODAS LAS LLAMADAS A LAS APIS DEL BACKEND CON SUS URLS
        ***************************************************************
     */

    public void getProductoPorCodigo(String codigo, JsonHttpResponseHandler responseHandler){
        //armo la url del get
        String url = SERVER_URL + "/api/producto/" + codigo;
        get(url, responseHandler);
    }

    public void getProductoPorTitulo(String titulo, JsonHttpResponseHandler responseHandler){
        //armo la url del get
        String url = SERVER_URL + "/api/busqueda_manual/" + titulo;
        get(url, responseHandler);
    }

    public void getPuntosDeRecoleccion(List<String> materiales, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/pdr";
        if(materiales != null && !materiales.isEmpty()){
            url += "?materiales=" + TextUtils.join(",", materiales);
        }
        get(url, responseHandler);
    }

    public void postPuntosDeRecoleccion(RequestParams params, JsonHttpResponseHandler responseHandler){
        String url = SERVER_URL + "/api/pdr";
        post(url, params, responseHandler);
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

    public void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler){
        client.post(url, params, responseHandler);
    }


    public class ApiCallException extends RuntimeException{
        public ApiCallException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
