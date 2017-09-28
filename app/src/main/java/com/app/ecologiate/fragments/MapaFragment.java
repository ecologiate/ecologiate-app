package com.app.ecologiate.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.services.ApiCallService;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MapaFragment extends Fragment implements OnMapReadyCallback{


    private ApiCallService apiCallService = new ApiCallService();

    //si vengo a buscar puntos para un producto
    private Producto producto;

    private OnFragmentInteractionListener mListener;

    private static int REQUEST_GEO = 567;

    private Context context;
    private GoogleMap gMap;
    private MapView mapView;
    private Boolean modoAlta = false;
    private Geocoder geocoder;
    private Address address;

    @BindView(R.id.lyEditMode)
    LinearLayout editMessage;
    @BindView(R.id.md_floating_action_menu)
    FloatingActionMenu fam;


    public MapaFragment() {}

    public static MapaFragment newInstance(Producto producto) {
        MapaFragment fragment = new MapaFragment();
        fragment.producto = producto;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();
        editMessage.setVisibility(View.GONE);
        FloatingActionButton fabBuscar = (FloatingActionButton) view.findViewById(R.id.buscarEnMapa);
        fabBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        FloatingActionButton fabAltaPunto = (FloatingActionButton) view.findViewById(R.id.agregarPuntoRecoleccion);
        fabAltaPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEditMode();
            }
        });

        //pedir al user que encienda el gps
        int off = 0;
        try {
            off = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Opciones de GPS no encontradas", Toast.LENGTH_LONG);
        }
        if(off==0){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Acceso a ubicación");
            alertDialog.setMessage("El GPS no está encendido, ¿te gustaría encenderlo?");
            alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GEO);
        }

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        LatLng utn = new LatLng(-34.598684, -58.419960);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(utn, 12.0f));
        /*
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(utn)
                .zoom(15f)
                .bearing(90f) //orientacion hacia al este
                .tilt(30f) //inclinacion
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        */

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(modoAlta) {
                    try {
                        address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error obteniendo dirección", Toast.LENGTH_LONG).show();
                    }
                    //obtengo dirección, ciudad, país, CP, etc
                    String direcciones = "";
                    for(int i=0; i <= address.getMaxAddressLineIndex(); i++){
                        direcciones += "("+i+") "+address.getAddressLine(i)+"\n";
                    }
                    String infoMarker =
                            "Direcciones: "+direcciones+
                            "Admin Area: "+address.getAdminArea() + "\n" +
                            "Cod Pais: "+address.getCountryCode() + "\n" +
                            "Pais nombre: "+address.getCountryName() + "\n" +
                            "Localidad: "+address.getLocality() + "\n" +
                            "CP: "+address.getPostalCode() + "\n" +
                            "Sub Admin Area: "+address.getSubAdminArea() + "\n" +
                            "Sub Locality: "+address.getSubLocality();
                    Toast.makeText(getContext(), infoMarker, Toast.LENGTH_LONG).show();
                    gMap.addMarker(new MarkerOptions().position(latLng).title("Nuevo").snippet(address.getAddressLine(0)));

                    Fragment fragment = AltaPuntoRecoleccionFragment.newInstance(latLng.latitude, latLng.longitude,
                            address.getAddressLine(0), address.getAdminArea(), address.getCountryName());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFragment, fragment)
                            .addToBackStack(String.valueOf(fragment.getId()))
                            .commit();
                }
            }
        });

        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
            }
        });

        getPuntosDeRecoleccion();
    }

    private void getPuntosDeRecoleccion(){

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // si me trajo algo
                    if(response != null && response.has("puntos")){
                        JSONArray puntosArray = response.getJSONArray("puntos");
                        if(puntosArray.length()>0){
                            for(int i = 0; i < puntosArray.length(); i++){
                                JSONObject pdr = puntosArray.getJSONObject(i);
                                LatLng pdrLatLng = new LatLng(pdr.getDouble("latitud"), pdr.getDouble("longitud"));
                                String title = pdr.getString("descripcion");
                                String direccion = pdr.getString("direccion");
                                gMap.addMarker(new MarkerOptions().position(pdrLatLng).title(title).snippet(direccion));
                            }
                        }else{
                            Toast.makeText(getContext(), "No se encontró ningún punto de recolección", Toast.LENGTH_LONG).show();
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

        List<String> materialIds = null;
        String pais = null;
        String area = null;
        if(producto != null && producto.getMaterial() != null){
            materialIds = new ArrayList<>();
            materialIds.add(producto.getMaterial().getId().toString());
        }
        apiCallService.getPuntosDeRecoleccion(materialIds, pais, area, responseHandler);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_GEO) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    onMapReady(gMap);
                }
            }
        }
    }

    private void handleEditMode(){
        modoAlta = !modoAlta;
        if(modoAlta) {
            editMessage.setVisibility(View.VISIBLE);
            //hacer zoom donde estoy parado
            try {
                Location myLocation = gMap.getMyLocation();
                LatLng myLocationLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, (gMap.getMaxZoomLevel() - 2)));
            }catch (Exception e){
                Toast.makeText(getContext(), "No se pudo obtener ubicación", Toast.LENGTH_LONG);
            }
        }else {
            editMessage.setVisibility(View.GONE);
        }
        fam.close(false);
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

    @Override
    public void onResume() {
        super.onResume();
        modoAlta = false;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
