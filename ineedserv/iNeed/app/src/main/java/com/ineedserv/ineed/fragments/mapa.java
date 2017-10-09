package com.ineedserv.ineed.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.ineedserv.ineed.Base_datos;
import com.ineedserv.ineed.PermissionUtils;
import com.ineedserv.ineed.R;

import static com.ineedserv.ineed.R.id.map;


public class mapa extends Fragment implements
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    int MY_LOCATION_REQUEST_CODE;
    LocationManager locationManager1;
    public double lat, lon;

    /* variables para el manejo del autocompleteextView*/
    Button limpiar;
    AutoCompleteTextView textView;
    View view;

    /* variables para el manejo de la barra de distancia*/
    SeekBar seek;
    TextView valor;
    int distanciaDeterminada;
    //private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;
    public int idSolicitudes;
    //public Solicitudes[] solicitud = new Solicitudes[50];

    /* declaracion para manejo de medicos seleccionados*/
    private Context context;
    private GoogleMap googleMap;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_localizacion_ofertantes, null, false);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        context = getActivity().getApplicationContext();

        locationManager1 = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        /* configuracion para trabajar con la base de datos
           esto debe ir en esta parte por que sino no funciona*/
        Base_datos crearBD = new Base_datos(context, VERSION);
        db = crearBD.getWritableDatabase();

// Get a reference to the AutoCompleteTextView in the layout
        textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
// Get the string array carga la lista
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter1);
/* pone invisible el boton limpiar*/
        limpiar = (Button) view.findViewById(R.id.limpiar);
        limpiar.setVisibility(View.INVISIBLE);

/*prepara el listener para las acciones con la lista*/
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something when the user clicks
                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                base();  /* carga los puntos de los doctores con la especialidad solicitada*/
            }
        });

        //close button visibility for manual typing
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    limpiar.setVisibility(View.VISIBLE);

                } else {
                    limpiar.setVisibility(View.GONE);
                }
            }
        });
/* muestra la lista al presionar en el campo*/
        textView.setThreshold(1);       //will start working from first character

        textView.setAdapter(adapter1);   //setting the adapter data into the AutoCompleteTextView

        //Shows drop down list on touch
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.showDropDown();
                return false;
            }
        });

        //close button visibility for autocomplete text view selection
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                limpiar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                limpiar.setVisibility(View.GONE);
            }

        });

        /* configuracion de la barra de busqueda que sirve para definir la distancia de busqueda de medicos*/
        seek = (SeekBar) view.findViewById(R.id.seek);
        valor = (TextView) view.findViewById(R.id.valor);
        seek.setOnSeekBarChangeListener(this);

         /* coloca el foco en el boton solicitar*/
        Button myBtn = (Button) view.findViewById(R.id.botonSolocitar);
        myBtn.setFocusableInTouchMode(true);
        myBtn.requestFocus();

        return view;
    }
/* fin del programa principal*/


    @Override
    public void onMapReady(GoogleMap map) {
        // Posicionar el mapa en una localización y con un nivel de zoom
        LatLng latLng = new LatLng(36.679582, -5.444791);
        // Un zoom mayor que 13 hace que el emulador falle, pero un valor deseado para
        // callejero es 17 aprox.
        float zoom = 13;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // Colocar un marcador en la misma posición
        map.addMarker(new MarkerOptions().position(latLng));
        // Más opciones para el marcador en:
        // https://developers.google.com/maps/documentation/android/marker

        // Otras configuraciones pueden realizarse a través de UiSettings
        // UiSettings settings = getMap().getUiSettings();


        map.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        // determina el punto por defecto o la ubicacion actual del solicitante
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                lat=location.getLatitude();
                lon=location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (!checkLocation())
            return;
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager1.removeUpdates(locationListener);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager1.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager1.requestLocationUpdates(provider, 2 * 20 * 1000, 10, locationListener);
            // button.setText(R.string.pause);
            // Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        lat = location.getLatitude();
        lon = location.getLongitude();

        //lat lon -16.529219010345596,-68.05958382995748
        //  LatLng origen = new LatLng(lat,lon);
        //  mMap.addMarker(new MarkerOptions().position(origen).title("La casa de Oscar"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(origen));

        // Toast.makeText(MapsActivity.this, "MAPS 2", Toast.LENGTH_SHORT).show();

    }




    /* obtiene el zip code del pais*/
    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    /*reacciona al movimiento del seekbar*/
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        valor.setText("Buscar en " + new Integer(progress).toString() + " km");
        distanciaDeterminada = progress;
        //base();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // Hacer algo
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // Hacer algo
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getApplicationContext());
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager1.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager1.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void clear(View view) {
        textView.setText("");
        limpiar.setVisibility(View.GONE);
    }


    /* carga los puntos de los doctores desde la base de datos despues de que se selecciona una especialidad */
    public void base() {
        String aux, aux1;
        double distance;
        //TextView quantityTextView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
        //TextView quantityTextView = (TextView) view.findViewById(R.id.autocomplete_country);
//        mMap.clear();
        // idSolicitudes=0;
        LatLng origen;
        LatLng destino;
        String fecha;

        Cursor ubicaciones_existentes = db.rawQuery("SELECT id, pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado FROM ofertantes where servicio='" + textView.getText().toString() + "'", null);
        if (ubicaciones_existentes.moveToFirst()) {   /* saca la especialidad en aux*/
            //aux=quantityTextView.getText().toString();
            aux = textView.getText().toString();
            do {
                /* compara con la especialidad buscada en aux1*/
                aux1 = ubicaciones_existentes.getString(8).toString();
                if (aux.equals(aux1)) {
                    Location location = new Location("localizacion 1");
                    location.setLatitude(lat);  //latitud
                    location.setLongitude(lon); //longitud
                    origen = new LatLng(lat, lon);
                    Location location2 = new Location("localizacion 2");
                    location2.setLatitude(ubicaciones_existentes.getDouble(5));  //latitud
                    location2.setLongitude(ubicaciones_existentes.getDouble(6)); //longitud
                    destino = new LatLng(ubicaciones_existentes.getDouble(5), ubicaciones_existentes.getDouble(6));
                    distance = location.distanceTo(location2);
                    distance = distance / 100000;
                    //distance2=CalculationByDistance(origen,destino);
                    if (distance <= distanciaDeterminada) {
                        //Toast.makeText(this, "distancia " + distance, Toast.LENGTH_SHORT).show();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(ubicaciones_existentes.getDouble(5), ubicaciones_existentes.getDouble(6))).title("Dr:" + ubicaciones_existentes.getString(7)));
                      /*  solicitud[idSolicitudes] = new Solicitudes();
                        solicitud[idSolicitudes].setInstancia(iMei);
                        solicitud[idSolicitudes].setUbicacion(ubicaciones_existentes.getString(0));
                        solicitud[idSolicitudes].setDistancia(distance);
                        solicitud[idSolicitudes].setEstado("Enviado");
                        fecha = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                        solicitud[idSolicitudes].setHora(fecha);
                        idSolicitudes++;*/
                    }
                }
            } while (ubicaciones_existentes.moveToNext());
            Toast.makeText(getActivity().getApplicationContext(), "termino ", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity().getApplicationContext(), "No existen medicos para esa especialidad " + ubicaciones_existentes.getString(0), Toast.LENGTH_SHORT).show();

    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
            //mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

/*
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
*/

}