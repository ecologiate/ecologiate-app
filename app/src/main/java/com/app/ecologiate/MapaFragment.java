package com.app.ecologiate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ecologiate.service.ApiCallService;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class MapaFragment extends Fragment implements OnMapReadyCallback{


    private ApiCallService apiCallService = new ApiCallService();

    private OnFragmentInteractionListener mListener;

    private Context context;
    private GoogleMap gMap;
    private MapView mapView;

    private Boolean modoAlta = false;

    public MapaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        context = view.getContext();
        FloatingActionButton fabBuscar = (FloatingActionButton) view.findViewById(R.id.buscarEnMapa);
        fabBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.mapa);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        LatLng utn = new LatLng(-34.598684, -58.419960);
        //gMap.addMarker(new MarkerOptions().position(utn).title("UTN"));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(utn, 11.0f));
        /*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(utn)
                .zoom(15f)
                .bearing(90f) //orientacion hacia al este
                .tilt(30f) //inclinacion
                .build();
        */
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(modoAlta) {
                    gMap.addMarker(new MarkerOptions().position(latLng).title("Nuevo"));
                }
            }
        });

        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(modoAlta) {
                    gMap.addMarker(new MarkerOptions().position(latLng).title("Nuevo"));
                }
            }
        });

        getPuntosDeRecoleccion();
    }

    private void getPuntosDeRecoleccion(){

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    // si me trajo algo
                    if(response != null){
                        for(int i = 0; i < response.length(); i++){
                            JSONObject pdr = response.getJSONObject(i);
                            LatLng pdrLatLng = new LatLng(pdr.getDouble("latitud"), pdr.getDouble("longitud"));
                            String title = pdr.getString("descripcion");
                            String direccion = pdr.getString("direccion");
                            gMap.addMarker(new MarkerOptions().position(pdrLatLng).title(title).snippet(direccion));
                        }
                    }else{
                        Toast.makeText(getContext(), "No se encontró ningún punto de recolección", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //Error parseando el json
                    Toast.makeText(getContext(), "Error en formato de json", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<JSONObject> list = new ArrayList<>();
                list.add(response);
                this.onSuccess(statusCode,headers,new JSONArray(list));
            }

            //Cuando no vuelve con un status code "200" del backend, o sea, una falla
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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

        apiCallService.getPuntosDeRecoleccion(null, responseHandler);
    }

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
        void onFragmentInteraction(Uri uri);
    }

}
