package com.app.ecologiate.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.services.UserManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{


    //Facebook
    CallbackManager mFacebookCallbackManager;

    private ProgressDialog mProgressDialog;

    private static final int GOOGLE_RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);

        //Facebook's binding
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList("email","public_profile","user_friends"));

        fbLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Logueado con Facebook: "+loginResult.getAccessToken().getUserId(),
                        Toast.LENGTH_LONG).show();
                goToNextActivity();
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                mostrarLoginViejo();
            }
            @Override
            public void onError(FacebookException exception) {
                Log.e(LoginActivity.class.getCanonicalName(), exception.getMessage());
                Toast.makeText(getApplicationContext(), "ERROR: "+exception.getMessage(), Toast.LENGTH_LONG).show();
                mostrarLoginViejo();
            }
        });

        //Google's binding
        UserManager.setUpGoogleSignIn(this, this, this);

        SignInButton mGoogleSignInButton = (SignInButton)findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        showProgressDialog();

        UserManager.googleSilentSignIn(new ResultCallback<GoogleSignInResult>() {
            @Override
            public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                hideProgressDialog();
                handleGoogleSignInResult(googleSignInResult, true);
            }
        });

        UserManager.connect();
    }

    private void handleGoogleSignInResult(GoogleSignInResult result, Boolean silent) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            // get profile information
            String name = "";
            String email = "";
            String uriPicture = "";
            if (account.getDisplayName() != null) {
                name = account.getDisplayName();
            }
            if (account.getEmail() != null) {
                email = account.getEmail();
            }
            if (account.getPhotoUrl() != null) {
                uriPicture = account.getPhotoUrl().toString();
            }
            // save profile information to preferences
            SharedPreferences prefs = getSharedPreferences("com.app.ecologiate", Context.MODE_PRIVATE);
            prefs.edit().putString("com.app.ecologiate.nombre", name).apply();
            prefs.edit().putString("com.app.ecologiate.email", email).apply();
            prefs.edit().putString("com.app.ecologiate.uriPicture", uriPicture).apply();
            UserManager.getUser().setNombre(name);

            if(!silent) {
                Toast.makeText(getApplicationContext(), "Logueado con Google: " + name,
                        Toast.LENGTH_LONG).show();
            }
            goToNextActivity();
        } else {
            //failed
            if(!silent) {
                String msg = result.getStatus().getStatusMessage()+" - "+result.getStatus().toString();
                Toast.makeText(getApplicationContext(), "Error: " + msg,
                        Toast.LENGTH_LONG).show();
                mostrarLoginViejo();
            }
        }
    }


    private void signInWithGoogle() {
        final Intent signInIntent = UserManager.createGoogleSignIntent();
        startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            //Google's activity result
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result, false);
        } else {
            //Facebook's activity result
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void goToNextActivity(){
        Intent menuIntent = new Intent(getApplicationContext(),WelcomeMenuActivity.class);
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menuIntent);
        finish();
    }


    public void goToNextActivity(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Copate");
        alertDialog.setMessage("Usá el login con Facebook o Google");
        alertDialog.setPositiveButton("Dale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"Crack", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("No jodas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"Forro", Toast.LENGTH_SHORT).show();
                goToNextActivity();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this,"Error al conectarse con Google", Toast.LENGTH_LONG).show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Obteniendo información del usuario");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        UserManager.disconnect();
    }

    private void mostrarLoginViejo(){
        Button btn = (Button) findViewById(R.id.ecoCustomLogin);
        btn.setVisibility(View.VISIBLE);
    }


    /*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    */


}

