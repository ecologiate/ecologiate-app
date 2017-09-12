package com.app.ecologiate.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ecologiate.ProductoNoEncontradoFragment;
import com.app.ecologiate.R;
import com.app.ecologiate.ResultadoFragment;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.services.SoundService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoderFactory;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class EscaneoFragment extends Fragment implements
        DecoratedBarcodeView.TorchListener{

    private ApiCallService apiCallService = new ApiCallService();

    private OnFragmentInteractionListener mListener;

    private DecoratedBarcodeView barcodeScannerView;
    private FloatingActionButton switchFlashlightButton;
    Boolean flashLightOn = false;
    private SoundService soundService;
    ProgressDialog prgDialog;
    Boolean scanEnabled = true;
    private static int REQUEST_CAMERA = 2067;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || !scanEnabled) {
                //previene múltiples escaneos si está todavía buscando el producto
                return;
            }

            String code = result.getText();
            //barcodeScannerView.setStatusText(result.getText());
            barcodeScannerView.pause();
            scanEnabled = false;
            soundService.playBastaChicos(true);
            getProducto(code);
            //mensaje emergente
            //Toast.makeText(getContext(), code, Toast.LENGTH_LONG).show();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Buscando");
        prgDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escaneo, container, false);

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
        soundService = new SoundService(getActivity());

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

        String cameraPermission = android.Manifest.permission.CAMERA;
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), cameraPermission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView.resume();
            scanEnabled = true;
        } else {
            requestPermissions(new String[]{cameraPermission}, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(android.Manifest.permission.CAMERA)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    barcodeScannerView.resume();
                    scanEnabled = true;
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
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

    public void getProducto(final String codigo){
        // muestro el mensaje de "Buscando..."
        prgDialog.show();

        //Creo el responseHandler, que va a tener el código a ejecutar cuando vuelve del backend
        //tanto en caso de éxito como en caso de falla
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            //si vuelve sin errores del backend se ejecuta el "onSuccess"
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Dejo de mostrar el cartel de "Buscando"
                prgDialog.hide();
                try {
                    // si me trajo algo
                    if(response.has("producto")){

                        JSONObject productoJson = response.getJSONObject("producto");
                        JSONObject materialJson = response.getJSONObject("material");
                        JSONObject categoriaJson = response.getJSONObject("categoria");

                        String nombreProducto = productoJson.getString("nombre_producto");
                        String categoria = categoriaJson.getString("descripcion");
                        String material = materialJson.getString("descripcion");
                        Long impacto = productoJson.getLong("cant_material"); //TODO HARDCODEADO

                        Producto producto = new Producto(nombreProducto,categoria,material,impacto);
                        Fragment resultadoFragment = ResultadoFragment.newInstance(producto);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFragment, resultadoFragment)
                                .addToBackStack(String.valueOf(resultadoFragment.getId()))
                                .commit();

                    }else{
                        //si no encontró un producto con ese código
                        Fragment noEncontradoFragment = ProductoNoEncontradoFragment.newInstance(codigo);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFragment, noEncontradoFragment)
                                .addToBackStack(String.valueOf(noEncontradoFragment.getId()))
                                .commit();
                    }
                } catch (JSONException e) {
                    //Error parseando el json
                    Toast.makeText(getContext(), "Error en formato de json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            //Cuando no vuelve con un status code "200" del backend, o sea, una falla
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Hide Progress Dialog
                prgDialog.hide();
                scanEnabled = true;
                barcodeScannerView.resume();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getContext(), "Url no encontrada", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getContext(), "Error en el backend", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                }
            }

        };

        apiCallService.getProductoPorCodigo(codigo, responseHandler);

    }
}
