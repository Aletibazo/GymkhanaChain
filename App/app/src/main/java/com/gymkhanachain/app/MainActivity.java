package com.gymkhanachain.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentInteractionListener, ListGymkFragment.OnListGymkFragmentInteractionListener, GymkInfoFragment.OnGymkInfoFragmentInteractionListener {

    // Tags para identificar los distintos fragmentos de la Actividad
    private static final String MAP_FRAGMENT_TAG = "MapFragment";
    private static final String LIST_GYMK_FRAGMENT_TAG = "ListGymkFragment";
    private static final String INFO_GYMK_FRAGMENT_TAG = "GymkInfoFragment";

    // Elementos del NavigationDrawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Lista de los fragmentos creados que gestiona esta actividad
    List<Fragment> fragments;
    // La gestionamos con este FragmentManager, mediante transacciones
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        String[] drawerOptions = getResources().getStringArray(R.array.drawer_items);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, drawerOptions));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Drawer");
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Al iniciar la Aplicación, cargamos el primer fragmento visible: MapFragment
        fragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        MapFragment mapFragment = new MapFragment();
        fragments.add(mapFragment);

        fragmentManager.beginTransaction()
                .add(R.id.placeholder_main, mapFragment, MAP_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // Implementación de las interfaces de los fragmentos
    // TODO Hay que definir las interfaces de cada fragmento, en función de las interacciones con MainActivity
    @Override
    public void onMapFragmentInteraction() {
        Fragment fragment = fragmentManager.findFragmentByTag(INFO_GYMK_FRAGMENT_TAG);

        if (fragment != null && fragment.getTag().equals(INFO_GYMK_FRAGMENT_TAG)) {
            fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.placeholder_main, GymkInfoFragment.newInstance(), INFO_GYMK_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onCreateGymkInteraction() {
        Intent intent = new Intent(this, CreateGymkActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGymkInfoFragmentInteraction(Uri uri) {

    }

    // Listener para el NavigationDrawer, aquí hacemos la transición entre los distintos fragmentos
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            String[] drawerOptions = getResources().getStringArray(R.array.drawer_items);

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment;

            // TODO Los elementos del switch deberían estar codificados en el archivo de strings
            switch (drawerOptions[position]) {
                case "Inicio": // MapFragment
                    fragment = fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);

                    if (fragment != null) {
                        if (fragment.getTag().equals(MAP_FRAGMENT_TAG))
                            break;
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
                    } else
                        fragmentManager.beginTransaction()
                                .replace(R.id.placeholder_main, MapFragment.newInstance(), MAP_FRAGMENT_TAG)
                                .commit();
                    break;
                case "Mis gymkhanas": // Listar gymkhanas
                    fragment = fragmentManager.findFragmentByTag(LIST_GYMK_FRAGMENT_TAG);

                    if (fragment != null) {
                        if (fragment.getTag().equals(LIST_GYMK_FRAGMENT_TAG))
                            break;
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
                    } else
                        fragmentManager.beginTransaction()
                            .replace(R.id.placeholder_main, ListGymkFragment.newInstance(), LIST_GYMK_FRAGMENT_TAG)
                            .commit();
                    break;
                case "Ajustes":
                    // TODO Aquí lanzamos SettingsActivity
                    break;
                //TODO:ilopez: Poner esto en la cabecera del drawer con fotico,
                case "Perfil":
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(intent);
                    break;
            }

            mDrawerList.setItemChecked(position, true);
            setTitle(drawerOptions[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}


