package com.ineedserv.ineed.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ineedserv.ineed.Base_datos;
import com.ineedserv.ineed.Category;
import com.ineedserv.ineed.MainActivity;
import com.ineedserv.ineed.PermissionUtils;
import com.ineedserv.ineed.R;
import com.ineedserv.ineed.Solicitudes;

import static com.ineedserv.ineed.R.id.mapSolicitante;


public class LocalizacionSolicitantes extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    int MY_LOCATION_REQUEST_CODE;
    LocationManager locationManager1;
    public double lat, lon;
    LatLng direccion;

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;
    public int idSolicitudes;
    public Solicitudes[] solicitud = new Solicitudes[150];

    /* variables para el manejo del autocompleteextView*/
    Button actualizar;
    Button historial;
    /* declaracion para manejo de medicos seleccionados*/
    private Context context;

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

    String zipCode;
    String nroCelular;

    String nroSolicitanteAceptado;

    private ProgressDialog progress;

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    String[] cosasPorHacer;

    ListView miLista;
    String despliega;
    ArrayList<Category> category;

    Category cat;

    private static final int NOTIF_ALERTA_ID = 1;
    /* variables para grabar en basse de datos remota*/
    //String direccion ="http://190.129.95.187/aineed/"; //URL del servicio WEB creado en el servidor

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_localizacion_solicitantes, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(mapSolicitante);
        mapFragment.getMapAsync(this);


        context = getActivity().getApplicationContext();

        MainActivity activity = (MainActivity) getActivity();
        zipCode = activity.getZipCode();
        nroCelular = activity.getNroCelular();

        // locationManager1 = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        /* configuracion para trabajar con la base de datos
           esto debe ir en esta parte por que sino no funciona*/
        Base_datos crearBD = new Base_datos(context, VERSION);
        db = crearBD.getWritableDatabase();
/*
        Cursor usuarioExistente=db.rawQuery("SELECT 1 FROM solicitudes", null);
        if(usuarioExistente.moveToFirst())
        {
            Toast.makeText(getActivity().getApplicationContext(), "existe registro previo.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No existe registro previo.", Toast.LENGTH_LONG).show();
        }
*/

        actualizar = (Button) view.findViewById(R.id.actualizar);

        //* cuando se presiona el boton actualizar*/
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarSolicitud();
            }
        });

        //cat = new Category();
        category = new ArrayList<Category>();
        miLista = (ListView) view.findViewById(R.id.lista);
 /*       cat.setTittle("alskddflas");
        cat.setDescription("12121");
        cat.setCategoryId(1);
        cat.setImagen(null);*/
        actualizaLista();
        AdapterItem adapter = new AdapterItem(getActivity(), category); //new AdapterItem(this, category);
        miLista.setAdapter(adapter);
        miLista.setVisibility(View.INVISIBLE);


        despliega = "0";
        historial = (Button) view.findViewById(R.id.historial);
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (despliega.equals("0")) {
                    actualizaLista();
                    miLista.setVisibility(view.VISIBLE);
                    actualizar.setVisibility(view.INVISIBLE);
                    historial.setText("MAPA");
                    despliega = "1";
                } else {
                    miLista.setVisibility(view.INVISIBLE);
                    actualizar.setVisibility(view.VISIBLE);
                    historial.setText("HISTORIAL");
                    despliega = "0";
                }
            }
        });
        return view;
    }
/* fin del programa principal*/



    /* actualiza las solictudes en la base de datos local y actualiza la lista del ListView*/
    public void actualizarSolicitud() {
        Cursor c = db.rawQuery("DELETE FROM solicitudes", null);
        int contador = c.getCount();
        Cursor d = db.rawQuery("SELECT id FROM solicitudes", null);
        int contador2 = d.getCount();
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Carga solicitudes..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        new ListarSolicitudes().execute(Base_datos.direccion + "app_consulta_solicitudes.php?cod=" + zipCode + "&i_ofer=" + nroCelular);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                base(); /* carga los puntos de los solicitantes */
                UbicacionActual(mMap); /* carga la ubicacion del ofertante */
                progress.cancel();
            }
        }, 3000);

    }

    public void actualizaLista() {
        int contador = 0;
        Drawable img;
        category.clear();
        String qry;
        qry="SELECT a.fechaFin,a.instanciaSolicitante,a.distancia,a.servicio," +
                " a.estado,a.instanciaOfertante, a.nombreSolicitante,b.nombre," +
                " a.fechaSolicitud, a.fechaAceptacion, a.fechaConfirmacion, a.fechaHoraCita " +
                "FROM solicitudes a, ofertantes b " +
                " where a.instanciaOfertante=b.instancia " +
                "and a.pais=b.pais " +
                "order by strftime('%dd-%mm-%yyyy %HH:%mm:%ss',a.fechaFin)";
        Cursor solicitudes_existentes = db.rawQuery(qry, null);//where servicio='" + textView.getText().toString() + "'", null);
        if (solicitudes_existentes.moveToFirst()) {   /* saca la especialidad en aux*/
            //aux=quantityTextView.getText().toString();
            do {
                cat = new Category();
                cat.setDescription("Realizada por "+solicitudes_existentes.getString(6));
                cat.setCategoryId(contador);
                if (solicitudes_existentes.getString(4).equals("ATENDIDA")) {
                    cat.setTittle("Estado "+solicitudes_existentes.getString(4)+"\nen fecha:"+ solicitudes_existentes.getString(9));
                    //img = R.drawable.alfiler;
                    cat.setImagen(null);
                } else if (solicitudes_existentes.getString(4).equals("CONFIRMADA")) {
                    cat.setTittle("Estado "+solicitudes_existentes.getString(4)+" para hrs:"+ solicitudes_existentes.getString(11));
                    cat.setImagen(null);
                } else if (solicitudes_existentes.getString(4).equals("CANCELADA")) {
                    cat.setTittle("Estado "+solicitudes_existentes.getString(4));
                    cat.setImagen(null);
                } else if (solicitudes_existentes.getString(4).equals("ELABORADA")) {
                    cat.setTittle("Estado "+solicitudes_existentes.getString(4)+"\nen fecha:"+ solicitudes_existentes.getString(8));
                    cat.setImagen(null);
                }
                category.add(cat);
                contador++;
            } while (solicitudes_existentes.moveToNext());
        }

    }

    private class ActualizarSolicitudes extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to return webpage";
            }
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "Se aceptó la solicitud del " + nroSolicitanteAceptado, Toast.LENGTH_LONG).show();
        }
    }


    public class ListarSolicitudes extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);

            } catch (IOException e) {
                return "Unable to return webpage";
            }
        }

    protected void onPostExecute(String result) {
            JSONArray ja, je = null;
            try {
                //JSONObject object = new JSONObject(result);
                //JSONArray ja  = object.getJSONArray("ofertantes");
                ja = new JSONArray(result);
                je = new JSONArray(result);
                Integer num_reg = ja.length();
                // Aca cargamos los datos en el listview
                if (ja != null) {
                    for (int i = 0; i < ja.length(); i++) {
                        String cadena;
                        je = ja.getJSONArray(i);
                        ContentValues values = new ContentValues();
                        values = new ContentValues();
                        values.put("pais", je.getString(1));
                        values.put("fechaFin", je.getString(2));
                        values.put("instanciaSolicitante", je.getString(3));
                        values.put("instanciaOfertante", je.getString(4));
                        values.put("latSolicitante", je.getString(5));
                        values.put("lonSolicitante", je.getString(6));
                        values.put("distancia", je.getString(7));
                        values.put("servicio", je.getString(8));
                        values.put("estado", je.getString(9));
                        values.put("fechaSolicitud", je.getString(10));
                        values.put("fechaAceptacion", je.getString(11));
                        values.put("fechaConfirmacion", je.getString(12));
                        values.put("fechaHoraCita", je.getString(13));
                        values.put("nombreSolicitante", je.getString(14));
                        db.insert("solicitudes", null, values);
                        // Toast.makeText(getApplicationContext(), ja.getString(1), Toast.LENGTH_LONG).show();
                        //Cursor usuarioExistente=db.rawQuery("select count(*) from solicitudes", null);
                    }
                }
                //Toast.makeText(getActivity(), "Se cargaron: " + num_reg + " solicitudes", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "No se encontró información de solicitudes: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        //LOG.i("URL",""+myurl);
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;
        int len = 500000;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            conn.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            conn.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            conn.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            conn.setDoInput(true);
            // Open communications link (network traffic occurs here).
            conn.connect();
            //////publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
            int responseCode = conn.getResponseCode();
            Log.d("respuesta", "The response is " + responseCode);
            is = conn.getInputStream();
            String contentAsString = readStream(is, len);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readStream(InputStream stream, int maxReadSize)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] rawBuffer = new char[maxReadSize];
        int readSize;
        StringBuffer buffer = new StringBuffer();
        while (((readSize = reader.read(rawBuffer)) != -1) && maxReadSize > 0) {
            if (readSize > maxReadSize) {
                readSize = maxReadSize;
            }
            buffer.append(rawBuffer, 0, readSize);
            maxReadSize -= readSize;
        }
        return buffer.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation(mMap);
        // determina el punto por defecto o la ubicacion actual del solicitante
        // Acquire a reference to the system Location Manager
        //LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager1 = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                lat = location.getLatitude();
                lon = location.getLongitude();
                //UbicacionActual(lat,lon,mMap);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };


// Register the listener with the Location Manager to receive location updates
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager1.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

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

        Location location = locationManager1.getLastKnownLocation(locationManager1.getBestProvider(criteria, false));
       // if (location.getLatitude()!=null)
        lat = location.getLatitude();
        lon = location.getLongitude();
        LatLng latLng = new LatLng(lat, lon);
        float zoom = 10;//13 es optimo
        //lat=-16.510886515734978; // OAS eliminar estas dos lineas
        //lon=-68.137675524898430;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (!marker.getSnippet().equals("Solicitud Aceptada")) {
                    nroSolicitanteAceptado = marker.getTitle();
                    nroSolicitanteAceptado= nroSolicitanteAceptado.substring(0,nroSolicitanteAceptado.indexOf("\n"));
                    direccion = marker.getPosition();
                    String fecha = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                    new ActualizarSolicitudes().execute(Base_datos.direccion + "app_actualiza_fecha_aceptacion.php?cod=" + zipCode + "&i_sol=" + nroSolicitanteAceptado + "&i_ofe=" + nroCelular + "&est=ATENDIDA&f_acep="+fecha);
                    Toast.makeText(getActivity().getApplicationContext(), "Se aceptó la solicitud del " + nroSolicitanteAceptado, Toast.LENGTH_LONG).show();
                    marker.setSnippet("Solicitud Aceptada");
                    //marker.
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker

                            (BitmapDescriptorFactory.HUE_YELLOW));
                }
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        actualizarSolicitud();
        //base(); /* carga los puntos de los solicitantes */
        //UbicacionActual(mMap); /* carga la ubicacion del ofertante */
    }

    /**
     * fin onmapready
     */

     /* carga los puntos de las solictudes relacionadas con la especialidad del ofertante */
    public void base() {
        mMap.clear();
        idSolicitudes = 0;
        LatLng destino;
        String fechaActual;

        /* obtienes la fecha para poder calcular las atenciones*/
        Calendar calendar = Calendar.getInstance();
        fechaActual = simpleDateFormat.format(calendar.getTime());

        //pais,fechaFin text,instanciaSolicitante,instanciaOfertante,latSolicitante,lonSolicitante,distancia,servicio,estado

        Cursor solicitudes_existentes = db.rawQuery("SELECT pais,fechaFin,instanciaSolicitante,instanciaOfertante,latSolicitante,lonSolicitante," +
                                                    "distancia,servicio,estado,fechaSolicitud,fechaAceptacion,fechaConfirmacion,fechaHoraCita " +
                                                    "FROM solicitudes where estado in ('ELABORADA','ATENDIDA','CONFIRMADA')", null);//where servicio='" + textView.getText().toString() + "'", null);
        if (solicitudes_existentes.moveToFirst()) {   /* saca la especialidad en aux*/
            //aux=quantityTextView.getText().toString();
            do {
                Location location2 = new Location("localizacion 2");
                location2.setLatitude(solicitudes_existentes.getDouble(4));  //latitud
                location2.setLongitude(solicitudes_existentes.getDouble(5)); //longitud
                destino = new LatLng(solicitudes_existentes.getDouble(4), solicitudes_existentes.getDouble(5));
                // mMap.addMarker(new MarkerOptions().position(destino).title(
                //         "Fono:" + solicitudes_existentes.getString(2) + " a "+ solicitudes_existentes.getString(6) +" km."));
                if (solicitudes_existentes.getString(8).equals("ATENDIDA")) {
                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudes_existentes.getString(7))
                            .snippet("Solicitud Aceptada se espera\nconfirmación del solicitante")
                            .icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_YELLOW)));
                } else if (solicitudes_existentes.getString(8).equals("CONFIRMADA")) {
                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudes_existentes.getString(7))
                            .snippet("Solicitud CONFIRMADA \nProgramada para hrs:"+solicitudes_existentes.getString(12))
                            .icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_GREEN)));
                } else {
                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudes_existentes.getString(2)+"\n"+solicitudes_existentes.getString(7))
                            .snippet("Presione AQUI para \n ACEPTAR la solicitud"));
                }
            } while (solicitudes_existentes.moveToNext());
            //Toast.makeText(getActivity().getApplicationContext(), "termino ", Toast.LENGTH_SHORT).show();
        }
        // Posicionar el mapa en una localización y con un nivel de zoom
        // LatLng latLng = new LatLng(lat, lon);
        // Un zoom mayor que 13 hace que el emulador falle, pero un valor deseado para
        // callejero es 17 aprox.
        // float zoom = 13;//13 es optimo
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // Colocar un marcador en la misma posición
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicación"));
        // mMap.addMarker(new MarkerOptions().position(latLng));
    }


    /* carga la ubicacion del ofertante */
    public void UbicacionActual(GoogleMap mMap) {
        // Más opciones para el marcador en:
        // https://developers.google.com/maps/documentation/android/marker

        // Otras configuraciones pueden realizarse a través de UiSettings
        // UiSettings settings = getMap().getUiSettings();
        String consultaDBInterna = "SELECT pais, instancia, latitud, longitud FROM ofertantes where pais=" + zipCode + " and instancia=" + nroCelular + ";";
        Cursor c = db.rawQuery(consultaDBInterna, null);
        int nServicios = c.getCount();
        int x = 0;
        if (c.moveToFirst()) {
            do {
                lat = c.getDouble(2);
                lon = c.getDouble(3);
                x++;
            } while (c.moveToNext());
        }
        LatLng latLng = new LatLng(lat, lon); // Un zoom mayor que 13 hace que el emulador falle, pero un valor deseado para
        // callejero es 17 aprox.
        float zoom = 11;//13 es optimo
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        // mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicación"));
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Consultorio")
                .snippet("")
                .icon(BitmapDescriptorFactory.defaultMarker
                        (BitmapDescriptorFactory.HUE_AZURE)));
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


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
            //map.setMyLocationEnabled(true);
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
            enableMyLocation(mMap);
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

}