package com.app.ecologiate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ecologiate.task.ImageLoadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class TestScanActivity extends AppCompatActivity {

    static final String SERVER_URL = "https://ecologiate.herokuapp.com";

    //output
    TextView outputMsg;

    //foto
    ImageView imgView;

    // Progress Dialog Object
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);

        outputMsg = (TextView)findViewById(R.id.scan_out);
        imgView = (ImageView)findViewById(R.id.scan_img);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Buscando item...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /**
     * método ejecutado cuando el botón Scan es clickeado
     *
     * @param view
     */
    public void scanBarcode(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void goManualInput(View view){
        Intent manualIntent = new Intent(getApplicationContext(),TestActivity.class);
        manualIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(manualIntent);
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String code = result.getContents();
                getItem(code);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getItem(String code){
        // Show Progress Dialog
        prgDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = SERVER_URL + "/api/item/" + code;

        //LLAMO A UN GET!
        client.get(url, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // si me trajo algo
                    if(response.has("title")){
                        String msg = "Title: "+response.getString("title")+
                                "\nDescription: "+ (response.has("description") ? response.getString("description") : "No tiene");
                        outputMsg.setText(msg);
                        //seteo color segun status
                        if(response.has("status") && "PENDING".equals(response.getString("status"))){
                            outputMsg.setTextColor(Color.rgb(204,204,0)); //amarillo oscuro
                        }else{
                            outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgOk));
                        }

                        if(response.has("img_link")){
                            imgView.setVisibility(View.VISIBLE);
                            new ImageLoadTask(response.getString("img_link"), imgView).execute();
                        }else{
                            //ocultar
                            imgView.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        // Navigate to other screen
                        //navigatetoHomeActivity();
                    }
                    // Else display error message
                    else{
                        String errorMsg = "Item no encontrado";
                        if(response.has("message")) {
                            errorMsg = response.getString("message");
                        }
                        outputMsg.setText(errorMsg);
                        //outputMsg.setTextColor(Color.RED);
                        outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgError));
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                    outputMsg.setText(throwable.getLocalizedMessage());
                    outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgError));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                    outputMsg.setText(throwable.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        prgDialog.dismiss();
        super.onDestroy();
    }
}
