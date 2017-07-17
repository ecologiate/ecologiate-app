package com.app.ecologiate;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TestActivity extends AppCompatActivity {

    static final String SERVER_URL = "https://ecologiate.herokuapp.com";

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView outputMsg;
    // Code Edit Text View Object
    EditText codeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //asigno la pantalla correspondiente a mi activity
        setContentView(R.layout.activity_test);

        //inicializo componentes
        Toolbar toolbar = (Toolbar) findViewById(R.id.test_toolbar);
        setSupportActionBar(toolbar);

        outputMsg = (TextView)findViewById(R.id.test_output);
        codeET = (EditText)findViewById(R.id.test_input_code);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor espere...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.test_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Más acciones", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    /**
     * método ejecutado cuando el botón Search Item es clickeado
     *
     * @param view
     */
    public void searchItem(View view){
        // Obtengo el código del input
        String code = codeET.getText().toString();
        if(code != null){
            //Si fuera post o put, debería pasar parámetros
            RequestParams params = new RequestParams();
            params.put("code", code);
            invokeWS(params, code);
        }else {
            Toast.makeText(getApplicationContext(), "Complete el código", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params, String code){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
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
                        String msg = "Producto encontrado: "+response.getString("title");
                        outputMsg.setText(msg);
                        //seteo color segun status
                        if(response.has("status") && "PENDING".equals(response.getString("status"))){
                            outputMsg.setTextColor(Color.rgb(204,204,0)); //amarillo oscuro
                        }else{
                            outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgOk));
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
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
                }
            }
        });
    }

}
