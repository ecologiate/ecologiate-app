package com.app.ecologiate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoderFactory;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class EscaneoFragment extends Fragment implements
        DecoratedBarcodeView.TorchListener{

    static final String SERVER_URL = "https://ecologiate.herokuapp.com";

    private OnFragmentInteractionListener mListener;

    //private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private FloatingActionButton switchFlashlightButton;
    Boolean flashLightOn = false;
    private BeepManager beepManager;
    ProgressDialog prgDialog;
    Boolean buscando = false;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || buscando) {
                //previene múltiples escaneos si está todavía buscando el producto
                return;
            }

            String code = result.getText();
            barcodeScannerView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();
            //mensaje emergente
            Toast.makeText(getContext(), code, Toast.LENGTH_LONG).show();
            getItem(code);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escaneo, container, false);

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Buscando");
        prgDialog.setCancelable(false);

        barcodeScannerView = (DecoratedBarcodeView)view.findViewById(R.id.fr_zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        //códigos de barras productos
        final List<BarcodeFormat> decodeFormats = Arrays.asList(
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,
                BarcodeFormat.RSS_14
                );
        final DecoderFactory factory = new DefaultDecoderFactory(decodeFormats,null , null, false);
        barcodeScannerView.getBarcodeView().setDecoderFactory(factory);
        barcodeScannerView.decodeContinuous(callback);
        beepManager = new BeepManager(getActivity());

        switchFlashlightButton = (FloatingActionButton)view.findViewById(R.id.fr_switch_flashlight);
        switchFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFlashlight(view);
            }
        });
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        //capture = new CaptureManager(getActivity(), barcodeScannerView);
        //capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
        //capture.decode();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeScannerView.resume();
        //capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
        //capture.onPause();
    }

    public void triggerScan(View view) {
        barcodeScannerView.decodeSingle(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //capture.onSaveInstanceState(outState);
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    */


    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getActivity().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (!flashLightOn) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
    }

    @Override
    public void onTorchOn() {
        flashLightOn = true;
        switchFlashlightButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        switchFlashlightButton.setColorFilter(Color.BLACK);
    }

    @Override
    public void onTorchOff() {
        flashLightOn = false;
        switchFlashlightButton.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        switchFlashlightButton.setColorFilter(Color.WHITE);
    }

    public void getItem(final String code){
        // Show Progress Dialog
        prgDialog.show();
        buscando = true;

        AsyncHttpClient client = new AsyncHttpClient();
        String url = SERVER_URL + "/api/producto/" + code;

        //LLAMO A UN GET!
        client.get(url, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Hide Progress Dialog
                prgDialog.hide();
                buscando = false;
                try {
                    // si me trajo algo
                    if(response.has("producto")){
                        JSONObject productoJson = response.getJSONObject("producto");
                        JSONObject materialJson = response.getJSONObject("material");
                        JSONObject categoriaJson = response.getJSONObject("categoria");
                        String nombreProducto = productoJson.getString("nombre_producto");
                        String categoria = categoriaJson.getString("descripcion");
                        String material = materialJson.getString("descripcion");
                        String impacto = String.valueOf(productoJson.getString("cant_material")); //TODO HARDCODEADO

                        startActivity(ResultadoActivity.crearIntentParaResultado(getActivity(),
                                nombreProducto,categoria,material,impacto));

                    }
                    // Else display error message
                    else{

                        Intent noEncontradoIntent = new Intent(getActivity(), ProductoNoEncontradoActivity.class);
                        noEncontradoIntent.putExtra("codigo", code);
                        //noEncontradoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(noEncontradoIntent);



                        /* String errorMsg = "Producto no encontrado";
                        if(response.has("mensaje")) {
                            errorMsg = response.getString("mensaje");
                        }
                        outputMsg.setText(errorMsg);
                        //outputMsg.setTextColor(Color.RED);
                        outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgError));
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();*/


                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Hide Progress Dialog
                prgDialog.hide();
                buscando = false;
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                    //outputMsg.setText(throwable.getLocalizedMessage());
                    //outputMsg.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.msgError));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // Hide Progress Dialog
                prgDialog.hide();
                buscando = false;
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                    //outputMsg.setText(throwable.getLocalizedMessage());
                }
            }
        });
    }
}
