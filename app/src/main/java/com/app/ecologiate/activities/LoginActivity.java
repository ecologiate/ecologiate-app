package com.app.ecologiate.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


public class LoginActivity extends AppCompatActivity {


    //Facebook
    CallbackManager mFacebookCallbackManager;
    //Google OAuth
    GoogleApiClient mGoogleApiClient;
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
            }
            @Override
            public void onError(FacebookException exception) {
                Log.e(LoginActivity.class.getCanonicalName(), exception.getMessage());
                Toast.makeText(getApplicationContext(), "ERROR: "+exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Google's binding
        SignInButton mGoogleSignInButton = (SignInButton)findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_RC_SIGN_IN) {
            //Google's activity result
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {

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


                Toast.makeText(getApplicationContext(), "Logueado con Google: "+name,
                        Toast.LENGTH_LONG).show();
                goToNextActivity();
            } else {
                //failed
                String msg = result.getStatus().getStatusMessage()+" - "+result.getStatus().toString();
                Toast.makeText(getApplicationContext(), "Error: "+msg,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            //Facebook's activity result
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    public void goToNextActivity(){
        Intent menuIntent = new Intent(getApplicationContext(),WelcomeMenuActivity.class);
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(menuIntent);
    }


    public void goToNextActivity(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Copate");
        alertDialog.setMessage("Usá el login con Facebook o Google");
        alertDialog.setPositiveButton("Dale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"Crack", Toast.LENGTH_SHORT);
            }
        });
        alertDialog.setNegativeButton("No jodas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(LoginActivity.this,"Forro", Toast.LENGTH_SHORT);
                goToNextActivity();
            }
        });
        alertDialog.show();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

