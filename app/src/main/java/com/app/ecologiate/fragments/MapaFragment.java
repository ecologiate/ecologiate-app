package com.app.ecologiate.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.adapters.MapWrapperLayout;
import com.app.ecologiate.adapters.PuntoRecInfoWindowAdapter;
import com.app.ecologiate.models.Material;
import com.app.ecologiate.models.Producto;
import com.app.ecologiate.models.PuntoRecoleccion;
import com.app.ecologiate.services.ApiCallService;
import com.app.ecologiate.utils.ImageUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MapaFragment extends AbstractEcologiateFragment implements
        OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener{


    private ApiCallService apiCallService = new ApiCallService();

    //si vengo con un producto
    private Producto producto;
    //materiales a filtrar
    private List<Material> materiales;
    //pdr recién creado para animación
    private PuntoRecoleccion nuevoPuntoCreado;

    private OnFragmentInteractionListener mListener;
    private HashMap<String, PuntoRecoleccion> mapaMarkerPdr;

    private static int REQUEST_GEO = 567;
    private static float INITIAL_ZOOM = 12.0f;

    private Context context;
    private GoogleMap gMap;
    private MapView mapView;
    private Boolean modoAlta = false;
    private Geocoder geocoder;
    private Address address;
    private LocationManager locationManager;


    @BindView(R.id.lyEditMode)
    LinearLayout editMessage;
    @BindView(R.id.md_floating_action_menu)
    FloatingActionMenu fam;
    @BindView(R.id.barraFiltros)
    LinearLayout materialesBar;
    @BindView(R.id.mapWrapperLayout)
    MapWrapperLayout mapWrapperLayout;


    public MapaFragment() {}

    public static MapaFragment newInstance(Producto producto) {
        MapaFragment fragment = new MapaFragment();
        fragment.producto = producto;
        fragment.materiales = new ArrayList<>();
        fragment.materiales.add(producto.getMaterial());
        return fragment;
    }

    public static MapaFragment newInstance(List<Material> materiales) {
        MapaFragment fragment = new MapaFragment();
        fragment.materiales = materiales;
        return fragment;
    }

    public static MapaFragment newInstance(PuntoRecoleccion pdrCreado) {
        MapaFragment fragment = new MapaFragment();
        fragment.nuevoPuntoCreado = pdrCreado;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        modoAlta = false;
        if(!modoAlta) {
            editMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        ButterKnife.bind(this, view);
        context = view.getContext();
        FloatingActionButton fabBuscar = (FloatingActionButton) view.findViewById(R.id.buscarEnMapa);
        fabBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                abrirDialogoMateriales();
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

        updateMaterialesBar();

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
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.setPadding(0, 35, 10, 0); //para cambiar la posición de los controles UI del mapa

        //eventos sobre el marker
        //gMap.setOnMarkerClickListener(this);
        //gMap.setOnInfoWindowClickListener(this);
        //gMap.setOnInfoWindowCloseListener(this);

        //wraper para poder manejar los clicks del infowindow
        int markerHeightInPixels = (int) getResources().getDimension(R.dimen.market_height);
        //le sumo 20dp que es maso la distancia entre el marker y el infowindow
        int offset = markerHeightInPixels + getPixelsFromDp(context, 20f);
        mapWrapperLayout.init(gMap, offset);

        if(!zoomToMyPosition(INITIAL_ZOOM)){
            //si no obtuve la última posición, me subscribo para cuando la tenga
            subscribeToLocationUpdates();
        }

        geocoder = new Geocoder(getContext(), Locale.getDefault());

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
                        Log.d("POSITION", infoMarker);
                        gMap.addMarker(new MarkerOptions().position(latLng).title("Nuevo").snippet(address.getAddressLine(0)));

                        Fragment fragment = AltaPuntoRecoleccionFragment.newInstance(latLng.latitude, latLng.longitude,
                                address.getAddressLine(0), address.getAdminArea(), address.getCountryName());
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentFragment, fragment)
                                //.addToBackStack(String.valueOf(fragment.getId()))
                                .commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error obteniendo dirección", Toast.LENGTH_LONG).show();
                    }
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
        mapaMarkerPdr = new HashMap<>();

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // si me trajo algo
                    if(response != null && response.has("puntos")){
                        JSONArray puntosArray = response.getJSONArray("puntos");
                        if(puntosArray.length()>0){
                            for(int i = 0; i < puntosArray.length(); i++){
                                agregarMarcador(PuntoRecoleccion.getFromJson(puntosArray.getJSONObject(i)));
                            }
                            //adapter de la ventana de los marcadores
                            PuntoRecInfoWindowAdapter adapter = new PuntoRecInfoWindowAdapter(getContext(), mapaMarkerPdr, mapWrapperLayout, producto);
                            gMap.setInfoWindowAdapter(adapter);
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
                            agregarMarcador(PuntoRecoleccion.getFromJson(response.getJSONObject(i)));
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if(statusCode == 404){
                    Toast.makeText(getContext(), "Url no encontrada", Toast.LENGTH_LONG).show();
                }
                else if(statusCode == 500){
                    Toast.makeText(getContext(), "Error en el backend", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR","Error inesperado ["+statusCode+"]", throwable);
                }
            }

        };

        List<String> materialIds = null;
        String pais = null;
        String area = null;
        if(materiales != null && !materiales.isEmpty()){
            materialIds = new ArrayList<>();
            for(int i = 0; i<materiales.size(); i++){
                materialIds.add(materiales.get(i).getId().toString());
            }
        }
        apiCallService.getPuntosDeRecoleccion(materialIds, pais, area, responseHandler);
    }


    private void agregarMarcador(PuntoRecoleccion pdr){
        //IMPORTANTE: la manera en que vinculo el marker con el pdr es poniendole como title el id
        LatLng pdrLatLng = new LatLng(pdr.getLatitud(), pdr.getLongitud());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pdrLatLng)
                .title(pdr.getId().toString())
                //.icon(BitmapDescriptorFactory.fromResource(pdr.getImageResourceId()));
                .icon(ImageUtils.getMarkerIconFromPdr(getContext(), pdr));
        Marker marker = gMap.addMarker(markerOptions);
        mapaMarkerPdr.put(pdr.getId().toString(), pdr);
        //animo el nuevo punto creado
        if(nuevoPuntoCreado != null && nuevoPuntoCreado.equals(pdr)){
            makeMarkerBounce(marker);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_GEO) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    zoomToMyPosition(INITIAL_ZOOM);
                }
            }
        }
    }

    private void handleEditMode(){
        modoAlta = !modoAlta;
        if(modoAlta) {
            editMessage.setVisibility(View.VISIBLE);
            //hacer zoom donde estoy parado
            zoomToMyPosition((gMap.getMaxZoomLevel() - 2));
        }else {
            editMessage.setVisibility(View.GONE);
        }
        fam.close(false);
    }

    private boolean zoomToMyPosition(float zoomLevel){
        boolean zoomed = false;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            if(locationManager == null) {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }
            Location l = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            if(l != null) {
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), zoomLevel));
                zoomed = true;
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GEO);
        }
        return zoomed;
    }

    private void subscribeToLocationUpdates(){
        //recibo actualización de la posición cada 5 minutos y cada 1 km!!!
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(locationManager == null) {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 1000, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 1000, this);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 300000, 1000, this);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GEO);
        }
    }

    private void abrirDialogoMateriales(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialog = inflater.inflate(R.layout.dialogo_materiales,null);
        final SeleccionMateriales seleccionMateriales = (SeleccionMateriales) viewDialog.findViewById(R.id.seleccionMateriales);
        if(materiales != null && !materiales.isEmpty()){
            Set<Material> selectedMaterial = new HashSet<>(materiales);
            seleccionMateriales.setSelectedMaterials(selectedMaterial);
        }
        builder.setView(viewDialog);
        builder.setTitle("Seleccione materiales a filtrar");
        builder.setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //vuelvo a crear el fragment porque tengo sueño
                Set<Material> selected = seleccionMateriales.getSelectedMaterials();
                MapaFragment fragment = MapaFragment.newInstance(new ArrayList<>(selected));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, fragment)
                        .commit();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nada
            }
        });
        builder.create().show();
    }

    private void updateMaterialesBar(){
        materialesBar.removeAllViewsInLayout();
        if(materiales != null && !materiales.isEmpty()){
            //Snackbar.make(materialesBar, "Filtros activos", BaseTransientBottomBar.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"Filtros activos", Toast.LENGTH_LONG).show();
            for(int i = 0; i<materiales.size(); i++){
                Material m = materiales.get(i);
                ImageView icon = new ImageView(getContext());
                icon.setImageResource(m.getImageResourceId());
                icon.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
                materialesBar.addView(icon);
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLang = new LatLng(location.getLatitude(), location.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 12.0f));
    }



    //Animación piola si quiero hacer rebotar un marker recién creado
    private void makeMarkerBounce(final Marker marker){
        // This causes the marker at Perth to bounce into position when it is clicked.
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.mapa_fragment_title);
    }

    @Override
    public String getSubTitle() {
        return null;
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
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
