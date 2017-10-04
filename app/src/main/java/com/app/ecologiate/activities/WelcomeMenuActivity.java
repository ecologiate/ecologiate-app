package com.app.ecologiate.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.ecologiate.R;
import com.app.ecologiate.fragments.AjustesFragment;
import com.app.ecologiate.fragments.AltaProductoFragment;
import com.app.ecologiate.fragments.AltaPuntoRecoleccionFragment;
import com.app.ecologiate.fragments.EnterateFragment;
import com.app.ecologiate.fragments.EscaneoFragment;
import com.app.ecologiate.fragments.GruposFragment;
import com.app.ecologiate.fragments.InicioFragment;
import com.app.ecologiate.fragments.ManualFragment;
import com.app.ecologiate.fragments.MapaFragment;
import com.app.ecologiate.fragments.MiCuentaFragment;
import com.app.ecologiate.fragments.PerfilFragment;
import com.app.ecologiate.fragments.ProductoNoEncontradoFragment;
import com.app.ecologiate.fragments.ReciclarFragment;
import com.app.ecologiate.fragments.ResultadoFragment;
import com.app.ecologiate.fragments.TipsFragment;
import com.app.ecologiate.fragments.TriviaFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class WelcomeMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AltaProductoFragment.OnFragmentInteractionListener,
        ReciclarFragment.OnFragmentInteractionListener,
        InicioFragment.OnFragmentInteractionListener,
        EscaneoFragment.OnFragmentInteractionListener,
        ManualFragment.OnFragmentInteractionListener,
        EnterateFragment.OnFragmentInteractionListener,
        TriviaFragment.OnFragmentInteractionListener,
        TipsFragment.OnFragmentInteractionListener,
        MapaFragment.OnFragmentInteractionListener ,
        MiCuentaFragment.OnFragmentInteractionListener,
        PerfilFragment.OnFragmentInteractionListener,
        GruposFragment.OnFragmentInteractionListener,
        AjustesFragment.OnFragmentInteractionListener,
        AltaPuntoRecoleccionFragment.OnFragmentInteractionListener,
        ResultadoFragment.OnFragmentInteractionListener,
        ProductoNoEncontradoFragment.OnFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //selecciono el inicioFragment por default
        if(savedInstanceState == null){
            Fragment inicioFragment = new InicioFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFragment, inicioFragment)
                    //.addToBackStack(String.valueOf(inicioFragment.getId()))
                    .commit();
            navigationView.setCheckedItem(R.id.nav_inicio);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int gsResult = googleApi.isGooglePlayServicesAvailable(this);
        if(gsResult == ConnectionResult.SUCCESS){
            //nada, tutto ok
        }else{
            Toast.makeText(this, "Google Services no instalado", Toast.LENGTH_LONG);
            googleApi.getErrorDialog(this, gsResult, 783).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if(id == R.id.nav_inicio){
            fragment = new InicioFragment();

        }else if (id == R.id.nav_reciclar) {
            fragment = new ReciclarFragment();

        } else if (id == R.id.nav_mapa) {
            fragment = new MapaFragment();

        } else if (id == R.id.nav_enterate) {
            fragment = new EnterateFragment();

        } else if (id == R.id.nav_micuenta) {
            fragment = new MiCuentaFragment();

        } else if (id == R.id.nav_ajustes) {
            fragment = new AjustesFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFragment, fragment)
                    .addToBackStack(String.valueOf(fragment.getId()))
                    .commit();
        }

        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}