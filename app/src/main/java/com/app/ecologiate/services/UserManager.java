package com.app.ecologiate.services;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.app.ecologiate.models.Nivel;
import com.app.ecologiate.models.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class UserManager {

    /* *****************************/
    /* **** manejo del dominio *****/
    /* *****************************/

    private static Usuario loggedUser = new Usuario(1L, "Ecologiate", "",
            "ecologiateapp@gmail.com", 0L, true, new Nivel(1L,"Novato",
            "ecologiate.herokuapp.com/images/avatar_novato.png",
            0L,1000L));

    public static Usuario getUser(){
        return loggedUser;
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
        //si está logueado con google
        manager.signOutFromGoogle(callback);
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


    public static void connect(){
        manager.mGoogleApiClient.connect();
    }

    public static void disconnect(){
        manager.mGoogleApiClient.disconnect();
    }



}
