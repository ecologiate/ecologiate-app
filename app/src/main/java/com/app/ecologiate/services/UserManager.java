package com.app.ecologiate.services;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.fragments.InicioFragment;
import com.app.ecologiate.models.Impacto;
import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Usuario;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class UserManager {

    private static ApiCallService apiCallService = new ApiCallService();

    /* *****************************/
    /* **** manejo del dominio *****/
    /* *****************************/

    private static Usuario loggedUser = new Usuario(1L, "Ecologiate", "",
            "ecologiateapp@gmail.com", 0d, true, new Nivel(1L,"Novato",
            "/images/avatar_novato.png",
            0L,1000L), new Impacto(0d,0d,0d,0d));

    public static Usuario getUser(){
        return loggedUser;
    }

    public static void login(final Context context, String email, String nombre, String apellido, String token, final ResultCallback callback){
        JSONObject jsonBody = new JSONObject();
        StringEntity bodyEntity = null;
        try {
            jsonBody.put("email", email);
            jsonBody.put("nombre", nombre);
            jsonBody.put("apellido", apellido);
            jsonBody.put("token", token);
            bodyEntity = new StringEntity(jsonBody.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error armando Json", Toast.LENGTH_LONG).show();
        }
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    final int status = statusCode;
                    try {
                        Usuario usuario = Usuario.getFromJson(response.getJSONObject("usuario"));
                        loggedUser = usuario;
                    }catch (Exception e){
                        Log.e("JSON_ERROR", e.getMessage());
                        throw new RuntimeException("Error en formato de json para login: "+ e.getMessage());
                    }
                    if(callback!=null){
                        callback.onResult(new Result() {
                            @Override
                            public Status getStatus() {
                                return new Status(status);
                            }
                        });
                    }
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

        apiCallService.login(context, bodyEntity, responseHandler);
    }

    public static void updateUser(Context context, ResultCallback callback){
        //medio choto pero más fácil
        Usuario usuario = getUser();
        login(context, usuario.getMail(), usuario.getNombre(), usuario.getApellido(), "", callback);
    }


    /* *****************************/
    /* ***** manejo del login ******/
    /* *****************************/

    private static UserManager manager;

    //Google OAuth api client
    private GoogleApiClient mGoogleApiClient;

    public static void setUpGoogleSignIn(@NonNull Context context, FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener onFailListener){
        if(manager == null) manager = new UserManager();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        manager.mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, onFailListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public static void googleSilentSignIn(@NonNull final ResultCallback<GoogleSignInResult> resultHandler){
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(manager.mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("LOGIN", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            resultHandler.onResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    resultHandler.onResult(googleSignInResult);
                }
            });
        }
    }

    public static Intent createGoogleSignIntent(){
        return Auth.GoogleSignInApi.getSignInIntent(manager.mGoogleApiClient);
    }

    public static void logOut(ResultCallback<Status> callback){
        manager.signOutFromGoogle(callback);
        manager.signOutFromFacebook(callback);
    }

    private void signOutFromGoogle(final ResultCallback<Status> callback) {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {
                //FirebaseAuth.getInstance().signOut();
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(callback);
                }
            }
            @Override
            public void onConnectionSuspended(int i) {
                Log.d("UserManager", "Google API Client Connection Suspended");
            }
        });
    }

    private void signOutFromFacebook(final ResultCallback<Status> callback){
        LoginManager.getInstance().logOut();
        callback.onResult(null);
    }


    public static void connect(){
        manager.mGoogleApiClient.connect();
    }

    public static void disconnect(){
        manager.mGoogleApiClient.disconnect();
    }



}
