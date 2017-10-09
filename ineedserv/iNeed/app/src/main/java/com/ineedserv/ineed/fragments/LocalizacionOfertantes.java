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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import java.util.Calendar;
import java.util.Date;

import com.ineedserv.ineed.Base_datos;
import com.ineedserv.ineed.MainActivity;
import com.ineedserv.ineed.PermissionUtils;
import com.ineedserv.ineed.R;
import com.ineedserv.ineed.Solicitudes;

import static java.lang.Math.round;
import static com.ineedserv.ineed.R.id.map;


public class LocalizacionOfertantes extends Fragment implements
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    int MY_LOCATION_REQUEST_CODE;
    LocationManager locationManager1;
    public double lat, lon;
    LatLng direccion;

    /* variables para el manejo del autocompleteextView*/
    Button limpiar;
    Button solicitalo;  // boton de solicitud
    Button verSolicitudes;
    AutoCompleteTextView textView;
    //private AutoCompleteTextView editGermen;
    ArrayAdapter<String> adaptadorServicios;
    String[] servicios;


    //View view;

    /* variables para el manejo de la barra de distancia*/
    SeekBar seek;
    TextView valor;
    int distanciaDeterminada;
    //private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;
    public int idSolicitudes;
    public Solicitudes[] solicitud = new Solicitudes[150];

    /* declaracion para manejo de medicos seleccionados*/
    private Context context;
    //private GoogleMap googleMap;

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
    String code;
    String nroCelular;
    String nombre;
    String nroOfertanteConfirmado;

    private ProgressDialog progress;

    /* variables para grabar en basse de datos remota*/
    //String direccion ="http://190.129.95.187/aineed/"; //URL del servicio WEB creado en e l servidor

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_localizacion_ofertantes, container, false);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        context = getActivity().getApplicationContext();

        //locationManager1 = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        /* configuracion para trabajar con la base de datos
           esto debe ir en esta parte por que sino no funciona*/
        Base_datos crearBD = new Base_datos(context, VERSION);
        db = crearBD.getWritableDatabase();

        rellenaServicios();

// Get a reference to the AutoCompleteTextView in the layout
        textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
// Get the string array carga la lista
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, servicios);
        textView.setThreshold(3);
        textView.setAdapter(adapter1);
/*
        Cursor solicitudeExistente=db.rawQuery("SELECT 1 FROM solicitudes", null);
        if(solicitudeExistente.moveToFirst())
        {
            Toast.makeText(getActivity().getApplicationContext(), "existe registro previo.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No existe registro previo.", Toast.LENGTH_LONG).show();
        }
*/
        MainActivity activity = (MainActivity) getActivity();
        zipCode = activity.getZipCode();
        nombre = activity.getNombre();
        nroCelular = activity.getNroCelular();
        code=zipCode;


        /* pone invisible el boton limpiar y el boton solicitar*/
        limpiar = (Button) view.findViewById(R.id.limpiar);
        limpiar.setVisibility(View.INVISIBLE);
        solicitalo = (Button) view.findViewById(R.id.botonSolocitar);
        solicitalo.setVisibility(View.INVISIBLE);
        verSolicitudes= (Button) view.findViewById(R.id.botonVerSolicitudes);

        /* boton de limpiar*/
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                            }
        });

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
                    solicitalo.setVisibility(View.VISIBLE);

                } else {
                    limpiar.setVisibility(View.INVISIBLE);
                    solicitalo.setVisibility(View.INVISIBLE);
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
                solicitalo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                limpiar.setVisibility(View.INVISIBLE);
                solicitalo.setVisibility(View.INVISIBLE);
            }

        });

        /* configuracion de la barra de busqueda que sirve para definir la distancia de busqueda de medicos*/
        seek = (SeekBar) view.findViewById(R.id.seek);
        valor = (TextView) view.findViewById(R.id.valor);
        seek.setOnSeekBarChangeListener(this);

         /* coloca el foco en el boton solicitar
        Button myBtn = (Button) view.findViewById(R.id.botonSolocitar);
        myBtn.setFocusableInTouchMode(true);
        myBtn.requestFocus();*/

        /* accion para la presion del boton solicita*/
        solicitalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solicitar(view);
            }
        });
        /* accion para la presion del boton ver solicitudes*/
        verSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar.setVisibility(View.INVISIBLE);
                solicitalo.setVisibility(View.INVISIBLE);
                textView.setText("");
                cargaSolicitudes();
            }
        });
        //PARA INSERTAR UNA SOLICITUD
        //190.129.95.187/aineed/inserta_solicitud.php?pais=BOL&f_fin=25JUL2017&i_sol=70676031&lat=-17.803019&lon=-63.205912&dis=500&ser=OFTALMOLOGIA

        return view;
    }
/* fin del programa principal*/


        public void rellenaServicios() {
            String consultaDBInterna="SELECT DISTINCT servicio FROM ofertantes ORDER BY servicio;";
            Cursor c=db.rawQuery(consultaDBInterna, null);
            int nServicios=c.getCount();
            servicios=new String[nServicios];
            int x=0;
            if (c.moveToFirst()) {
                do {
                    servicios[x]=c.getString(0);
                    x++;
                } while (c.moveToNext());
            }
        }



    /* carga en base de datos cuando se realiza una solicitud de atencion cuando se presiona el boton solicitar*/
    public void solicitar(View view){
        int x;
        String cadena;
        String fecha = (DateFormat.format("hh:mm:ss", new java.util.Date().getTime()).toString());
        for (x=0;x<idSolicitudes;x++) {
            ContentValues values = new ContentValues();
            values = new ContentValues();
            values.put("pais", solicitud[x].getPais());
            values.put("fechaFinSolicitud", solicitud[x].getFechaFinSolicitud());
            values.put("fechaSolicitud", solicitud[x].getFechaFinSolicitud());
            values.put("fechaAceptacion", solicitud[x].getFechaAceptacion());
            values.put("fechaConfirmacion", solicitud[x].getFechaConfirmacion());
            values.put("fechaHoraCita", solicitud[x].getFechaHoraCita());
            values.put("instanciaSolicitante", solicitud[x].getInstanciaSolicitante());
            values.put("nombreSolicitante", solicitud[x].getNombreSolicitante());
            values.put("instanciaOfertante", solicitud[x].getInstanciaOfertante());
            values.put("latSolicitante", lat);
            values.put("lonSolicitante", lon);
            values.put("distancia", solicitud[x].getDistancia());
            values.put("servicio", solicitud[x].getServicio());
            values.put("estado", solicitud[x].getEstado());
            db.insert("solicitudes", null, values);

            String qry ="app_inserta_solicitud.php?pais="+solicitud[x].getPais()+
                    "&f_fin="+solicitud[x].getFechaFinSolicitud()+
                    "&i_sol="+solicitud[x].getInstanciaSolicitante()+
                    "&i_ofe="+solicitud[x].getInstanciaOfertante()+
                    "&lat="+lat+"&lon="+lon+
                    "&dis="+solicitud[x].getDistancia()+"&ser="+solicitud[x].getServicio()+
                    "&n_sol="+solicitud[x].getNombreSolicitante()+
                    "&f_sol="+solicitud[x].getFechaSolicitud();
            //"app_inserta_solicitud.php?pais=591&f_fin=10-08-2017 07:27:17&i_sol=67151010&i_ofe=70676033&lat=-16.529036737282123&lon=-68.05959106489796&dis=3.0&ser=Cardiología"

            new GrabarDatos().execute(Base_datos.direccion+qry);
        }
        Toast.makeText(getActivity().getApplicationContext(), "Se enviarón "+x+" solicitudes", Toast.LENGTH_SHORT).show();
    }

    private class GrabarDatos extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch(IOException e){
                return "No puede volver a la página Web";
            }
        }
        protected void onPostExecute(String result){
            //Toast.makeText(getActivity().getApplicationContext(), R.string.ofertantegrb, Toast.LENGTH_LONG).show();

        }
    }
    private String downloadUrl(String myurl) throws IOException {
        //LOG.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
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
            Log.d("respuesta","The response is "+responseCode);
            is = conn.getInputStream();
            String contentAsString = readStream(is,len);
            return contentAsString;
        } finally {
            if(is != null){
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

        mMap=googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation(mMap);


        //LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager1 = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                lat=location.getLatitude();
                lon=location.getLongitude();
                UbicacionActual(lat,lon,mMap);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
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
        if (location!=null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
            float zoom = 10;//13 es optimo
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }
/*        lat=-16.510886515734978;  // OAS eliminar estas 5 lineas
        lon=-68.137675524898430;
        LatLng latLng = new LatLng(lat, lon);
        float zoom = 10;//13 es optimo
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));*/

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String nroOfertante;
                String fecha;
                String fechahora;
                code=GetCountryZipCode();
                if (marker.getSnippet() != null) {
                    if (marker.getSnippet().indexOf("ATENDIDA") != -1) {
                        nroOfertanteConfirmado = marker.getTitle();
                        direccion = marker.getPosition();
                        nroOfertante = nroOfertanteConfirmado.substring(0, nroOfertanteConfirmado.indexOf("\nDr"));
                        fecha = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                        fechahora = (DateFormat.format("hh:mm:ss", sumarRestarHorasFecha(new java.util.Date(), 1)).toString());
                        new ActualizarSolicitudes().execute(Base_datos.direccion + "app_actualiza_fecha_confirmacion.php?cod=" + code + "&i_sol=" + nroCelular + "&i_ofe=" + nroOfertante + "&est=CONFIRMADA&f_conf=" + fecha + "&fh_cita=" + fechahora);
                        Toast.makeText(getActivity().getApplicationContext(), "Cita confirmada.", Toast.LENGTH_LONG).show();
                        marker.setSnippet("Cita Confirmada");
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_GREEN));
                        cargaSolicitudes();
                    }
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


        //UbicacionActual(lat,lon,mMap);
        cargaSolicitudes();

    }

    // Suma o resta las horas recibidos a la fecha

    public Date sumarRestarHorasFecha(Date fecha, int horas){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }


    /* si el solicitante tiene solicitudes elaboradas en el dia se muestran primero*/
    public void cargaSolicitudes () {
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
        new ListarSolicitudes().execute(Base_datos.direccion + "app_consulta_solicitantes.php?cod=" + zipCode + "&i_sol=" + nroCelular);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cargaMarcasMapa(); /* carga los puntos de los solicitantes */
                progress.cancel();
            }
        }, 3000);

    }

    public void cargaMarcasMapa(){
        mMap.clear();
        LatLng destino;
        String fecha1;

        Cursor solicitudeExistente=db.rawQuery("SELECT a.pais, a.fechaFin, a.instanciaSolicitante, a.instanciaOfertante, a.estado, " +
                                               "b.latitud, b.longitud, b.nombre, a.servicio, a.distancia, b.direccion," +
                                               "a.fechaAceptacion,a.fechaConfirmacion,a.fechaHoraCita from solicitudes a, " +
                                               "ofertantes b WHERE a.pais=b.pais and a.instanciaOfertante=b.instancia " +
                                               "and a.estado in ('ELABORADA','ATENDIDA','CONFIRMADA')", null);
        //Cursor solicitudeExistente=db.rawQuery("SELECT pais,fechaFin,instanciaSolicitante,instanciaOfertante,latSolicitante,lonSolicitante,distancia,servicio,estado FROM solicitudes where estado in ('AC','11  ')", null);
        if(solicitudeExistente.moveToFirst())
        {  /* si existe una solictud enviada en el dia*/
            do {
                Location location2 = new Location("localizacion 2");
               destino = new LatLng(solicitudeExistente.getDouble(5), solicitudeExistente.getDouble(6));
                // mMap.addMarker(new MarkerOptions().position(destino).title(
                //         "Fono:" + solicitudes_existentes.getString(2) + " a "+ solicitudes_existentes.getString(6) +" km."));
                if(solicitudeExistente.getString(4).equals("ATENDIDA")) {
                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudeExistente.getString(3) + "\nDr." + solicitudeExistente.getString(7))
                            .snippet("Solicitud ATENDIDA, a hrs:"+solicitudeExistente.getString(11)+"\npresione AQUI para CONFIRMAR.")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                } else if (solicitudeExistente.getString(4).equals("CONFIRMADA")) {
                    fecha1 = (DateFormat.format("hh:mm:ss", new java.util.Date().getTime()).toString());

                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudeExistente.getString(7))
                            //"My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text"
                            .snippet("La solicitud fue CONFIRMADA a hrs:\n"+solicitudeExistente.getString(12)+"\n Ud. tiene una cita a hrs:"+solicitudeExistente.getString(13))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                } else if (solicitudeExistente.getString(4).equals("CANCELADA")) {
                    fecha1 = (DateFormat.format("hh:mm:ss", new java.util.Date().getTime()).toString());

                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(solicitudeExistente.getString(7))
                            //"My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text"
                            .snippet("Solicitud CANCELADA.a hrs:"+solicitudeExistente.getString(12))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }else if (solicitudeExistente.getString(4).equals("ELABORADA")) {
                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title("Solicitud enviada.")
                            .snippet("Dr:" +solicitudeExistente.getString(7)+" a "+ solicitudeExistente.getString(9) +" km.\n Dirección:"+solicitudeExistente.getString(10)));
                }
            } while (solicitudeExistente.moveToNext());
            float zoom = 10;//13 es optimo
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destino, zoom));
        }else{
            Toast.makeText(getActivity(), "No existen solicitudes pendientes.", Toast.LENGTH_LONG).show();
        }
    }





    /* coloca el foco del mapa en la ubicacion actual*/
    public void UbicacionActual(double lat, double lon, GoogleMap mMap){
        LatLng latLng = new LatLng(lat, lon);
        float zoom = 10;//13 es optimo
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
            // todo Toast.makeText(getActivity(), "Se aceptó la solicitud del " + nroSolicitanteAceptado, Toast.LENGTH_LONG).show();
        }
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
        if (!textView.getText().toString().equals("")){
            base();
        }
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
        limpiar.setVisibility(View.INVISIBLE);
    }


    /* carga los puntos de los doctores desde la base de datos despues de que se selecciona una especialidad */
    public void base() {
        String aux, aux1;
        double distance;
        //TextView quantityTextView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
        //TextView quantityTextView = (TextView) view.findViewById(R.id.autocomplete_country);
        mMap.clear();
        idSolicitudes=0;
        LatLng origen;
        LatLng destino;
        String fecha;

        Cursor ubicaciones_existentes = db.rawQuery("SELECT id,pais,instancia,correo,nombre,servicio,latitud,longitud,direccion,datoServicio,estado FROM ofertantes", null);//where servicio='" + textView.getText().toString() + "'", null);
        if (ubicaciones_existentes.moveToFirst()) {   /* saca la especialidad en aux*/
            //aux=quantityTextView.getText().toString();
            aux = textView.getText().toString();
            do {
                /* compara con la especialidad buscada en aux1*/
                aux1 = ubicaciones_existentes.getString(5).toString();
                if (aux.equals(aux1)) {
                    Location location = new Location("localizacion 1");
                    location.setLatitude(lat);  //latitud
                    location.setLongitude(lon); //longitud
                    origen = new LatLng(lat, lon);
                    Location location2 = new Location("localizacion 2");
                    location2.setLatitude(ubicaciones_existentes.getDouble(6));  //latitud
                    location2.setLongitude(ubicaciones_existentes.getDouble(7)); //longitud
                    destino = new LatLng(ubicaciones_existentes.getDouble(6), ubicaciones_existentes.getDouble(7));
                    distance = location.distanceTo(location2);
                    distance = distance / 1000;
                    distance = round(distance);
                    //distance2=CalculationByDistance(origen,destino);
                    if (distance <= distanciaDeterminada) {
                        //Toast.makeText(this, "distancia " + distance, Toast.LENGTH_SHORT).show();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(ubicaciones_existentes.getDouble(6), ubicaciones_existentes.getDouble(7)))
                                .title("Dr:" + ubicaciones_existentes.getString(4)+" a "+ distance +" km.\n Dirección:"+ubicaciones_existentes.getString(8)));
                        solicitud[idSolicitudes] = new Solicitudes();
                        solicitud[idSolicitudes].setPais(zipCode);
                        fecha = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                        solicitud[idSolicitudes].setFechaFinSolicitud(fecha); // sumar  la fecha un tiempo estimado
                        solicitud[idSolicitudes].setFechaSolicitud(fecha);
                        solicitud[idSolicitudes].setFechaAceptacion(null);
                        solicitud[idSolicitudes].setFechaConfirmacion(null);
                        solicitud[idSolicitudes].setFechaHoraCita(null);
                        solicitud[idSolicitudes].setInstanciaSolicitante(nroCelular);
                        solicitud[idSolicitudes].setNombreSolicitante(nombre);
                        solicitud[idSolicitudes].setInstanciaOfertante(ubicaciones_existentes.getString(2));
                        solicitud[idSolicitudes].setLatSolicitante(ubicaciones_existentes.getDouble(6));
                        solicitud[idSolicitudes].setLonSolicitante(ubicaciones_existentes.getDouble(7));
                        solicitud[idSolicitudes].setDistancia(distance);
                        solicitud[idSolicitudes].setServicio(textView.getText().toString());
                        solicitud[idSolicitudes].setEstado("ELABORADA");
                        idSolicitudes++;
                    }
                }
            } while (ubicaciones_existentes.moveToNext());
            //Toast.makeText(getActivity().getApplicationContext(), "termino ", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity().getApplicationContext(), R.string.ofertantesser + ubicaciones_existentes.getString(0), Toast.LENGTH_SHORT).show();

        // Posicionar el mapa en una localización y con un nivel de zoom
         LatLng latLng = new LatLng(lat, lon);
        // Un zoom mayor que 13 hace que el emulador falle, pero un valor deseado para
        // callejero es 17 aprox.
        float zoom = 11;//13 es optimo
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // Colocar un marcador en la misma posición
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Mi ubicación"));
       // mMap.addMarker(new MarkerOptions().position(latLng));
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