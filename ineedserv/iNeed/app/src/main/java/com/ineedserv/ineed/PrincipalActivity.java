package com.ineedserv.ineed;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


public class PrincipalActivity extends AppCompatActivity {

    public String zipCode, nroCelular;
    EditText txtPhoneNo;
    String message;

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;
    String banderaConsulta = "0"; // bandera paa ejecucion de consulta remota de ofertantes

    /* declaracion para manejo de medicos seleccionados*/
    private Context context;


    private GoogleMap mMap;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PROFILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    //   private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int MY_PERMISSIONS_READ_GSERVICES = 1;
    private static final int MY_PERMISSIONS_MAPS_RECEIVE = 1;

    public static final String EXTRA_MESSAGE = "com.ineedserv.ineed.MESSAGE";


    //String direccion ="http://190.129.95.187/aineed/";
    String usuarioRegistrado;
    String tipoUsuario;
    String clave;
    String nroVerificacion;
    String nombre;
    String correo;

    private ProgressDialog progress;

    Location location;
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog alert = null;

    /* para manejar el servicio*/
    TimerTask scanTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    private static final int NOTIF_ALERTA_ID = 1;


    int MY_LOCATION_REQUEST_CODE;
    LocationManager locationManager1;

    Button ofrecer;
    Button solicitar;
    TextView enlace;
    LinearLayout grupo;
    Button btn; // boton envio


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
// OAS cambiar para que trabaje con internet colocar !
        if (!isOnlineNet()  ) {
            Intent sinConexion = new Intent(PrincipalActivity.this, SinConexion.class);
            startActivityForResult(sinConexion, 0);
            // Toast.makeText(getApplicationContext(), "Sin conexión. Comprueba que la conexión de Datos o Wi-Fi esté activada y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
        } else
        if (!checkLocation()) {
            Intent sinUbicacion = new Intent(PrincipalActivity.this, SinUbicacion.class);
            startActivityForResult(sinUbicacion, 0);
            //showAlert();
            //Intent sinConexion = new Intent(PrincipalActivity.this, SinConexion.class);
            //startActivityForResult(sinConexion, 0);
            // Toast.makeText(getApplicationContext(), "La ubicación no se encuentra activada. Activa la ubicación y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
        } else if (getPhoneNumber() == 0) {
            Intent sinTarjetaSim = new Intent(PrincipalActivity.this, SinTarjetaSim.class);
            startActivityForResult(sinTarjetaSim, 0);
        } else {

            verificaPermisos();


            // Iniciar_tiempo();

/*

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager nManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("BroadcastReceiver")
                    .setContentText("Se ha conectado el cargador.")
                    .setSound(defaultSound)
                    .setWhen(System.currentTimeMillis());

            Intent targetIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            builder.setAutoCancel(true);

            nManager.notify(123456, builder.build());
*/

 /*desde aqui*/
            // Sonido por defecto de notificaciones, podemos usar otro
/*

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alert_icon)).getBitmap()))
                            .setContentTitle("Tiene una respuesta en INeed")
                            .setContentText("Ejemplo de notificación.")
                            .setContentInfo("4")
                            .setSound(defaultSound)
                            .setTicker("Alerta!");
            Intent notIntent =  new Intent(this, MainActivity.class);
            PendingIntent contIntent = PendingIntent.getActivity(getApplication().getApplicationContext(), 0, notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
*/




/*hasta aqui*/
            context = this;
            enlace = (TextView) findViewById(R.id.enlace);
            grupo = (LinearLayout) findViewById(R.id.grupo);
            btn = (Button) findViewById(R.id.btnEnvio);
            ofrecer = (Button) findViewById(R.id.ofrecer);
            solicitar = (Button) findViewById(R.id.solicitar);

            enlace.setVisibility(View.INVISIBLE);
            grupo.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);

            ofrecer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enlace.setVisibility(View.VISIBLE);
                    grupo.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                }
            });

            solicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enlace.setVisibility(View.INVISIBLE);
                    grupo.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                }
            });


            enlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://www.ineedserv.com/web/index.php?r=ofertante%2Fcreate");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            zipCode = GetCountryZipCode();
            TextView txtzipcode = (TextView) findViewById(R.id.zipCode);
            txtzipcode.setText("+" + zipCode);

            txtPhoneNo = (EditText) findViewById(R.id.celular);
            Drawable warning = (Drawable) getResources().getDrawable(R.drawable.alert_icon);
            //txtPhoneNo.setError( "Faltan "+R.string.nroFono+" digitos", warning);

        /* configuracion para trabajar con la base de datos
           esto debe ir en esta parte por que sino no funciona*/

            Base_datos crearBD = new Base_datos(context, VERSION);
            db = crearBD.getWritableDatabase();

            /* Se verifica que ya se tenga cargada la tabla de ofertantes si no se traen de la base remota para cargarse de manera local */
            Cursor usuarioExistente = db.rawQuery("SELECT 1 FROM ofertantes", null);
            if (usuarioExistente.moveToFirst()) {
                //todo;
            } else {
                new ListarDatos().execute(Base_datos.direccion + "app_listado_ofertantes.php?cod=" + zipCode);
            }

            Intent actividadMapa;

        /* verifica si existe un usuario en la base de datos local*/
            switch (buscaDatos()) {
                case "0":   /// no existe usuario
                    //Toast.makeText(getApplicationContext(), "No existe registro previo.", Toast.LENGTH_LONG).show();
                    break;
                case "1":   // existe un usuario de tipo solicitante
                    /* borra las solicitudes y actualiza las solicitudes
                    //Cursor solictudExistente = db.rawQuery("DELETE FROM solicitudes", null);
                    Cursor f = db.rawQuery("DELETE FROM solicitudes", null);
                    int contador = f.getCount();
                    Cursor e = db.rawQuery("SELECT id FROM solicitudes", null);
                    int contador2 = e.getCount();
                    progress = new ProgressDialog(this);
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
                            progress.cancel();
                 /* activa la pagina de mapas para solicitante*/
                    actividadMapa = new Intent(PrincipalActivity.this, MainActivity.class);
                    actividadMapa.putExtra("zipCode", zipCode);
                    actividadMapa.putExtra("nroCelular", nroCelular);
                    actividadMapa.putExtra("nombre", nombre);
                    actividadMapa.putExtra("correo", correo);
                    actividadMapa.putExtra("tipoUsuario", "1"); // solicitante
                    startActivityForResult(actividadMapa, 0);
                    /*    }
                    }, 3000);*/
                    break;
                case "2":  // existe un usuarios de tipo ofertante

                    /* borra las solicitudes y actualiza las solicitudes */
                    //Cursor solictudExistente = db.rawQuery("DELETE FROM solicitudes", null);
                    /*Cursor c = db.rawQuery("DELETE FROM solicitudes", null);
                    int contador3 = c.getCount();
                    Cursor d = db.rawQuery("SELECT id FROM solicitudes", null);
                    int contador4 = d.getCount();
                    progress = new ProgressDialog(this);
                    progress.setMessage("Carga solicitudes..");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();
                    new ListarSolicitudes().execute(Base_datos.direccion + "app_consulta_solicitudes.php?cod=" + zipCode + "&i_ofer=" + nroCelular);
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.cancel();*/
                    actividadMapa = new Intent(PrincipalActivity.this, MainActivity.class);
                    actividadMapa.putExtra("zipCode", zipCode);
                    actividadMapa.putExtra("nroCelular", nroCelular);
                    actividadMapa.putExtra("nombre", nombre);
                    actividadMapa.putExtra("correo", correo);
                    actividadMapa.putExtra("tipoUsuario", "2"); // solicitante
                    startActivityForResult(actividadMapa, 0);
                    //     }
                    // }, 5000);
                    break;
                default:
                    //Codigo a ejecutar si no se cumple ningún caso anterior
                    break;
            }

            //txtPhoneNo = (EditText) findViewById(R.id.celular);
            txtPhoneNo.addTextChangedListener(new Validaciones(txtPhoneNo) {
                @Override
                public void validate(EditText editText, String text) {
                    //Implementamos la validación que queramos
                    if (text.length() < 8) // saca el tamanio de string =8
                        txtPhoneNo.setError("Faltan " + (8 - text.length()) + " digitos");
                }
            });


            progress = new ProgressDialog(this);


            //* cuando se presiona el boton siguiente
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progress.setMessage("Verificando..");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();

                    txtPhoneNo = (EditText) findViewById(R.id.celular);
                    nroCelular = txtPhoneNo.getText().toString();
                    if (nroCelular.length() == 8) {
                        //http://190.129.95.187/aineed/consulta_usuario.php?cod=591&ins=70676031
                        //verifica si para este numero de telefono ya existe un registro activo

                        //consulta_usuario.php?cod=591&ins=70676031");//
                        new ConsultarDatos().execute(Base_datos.direccion + "app_consulta_usuario.php?cod=" + zipCode.toString() + "&ins=" + nroCelular.toString());
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                if (usuarioRegistrado.equals("AC")) {  // existe usuario en bd remota
                                    if (tipoUsuario.equals("1")) {  // si es de tipo solicitante
                                        generaNumero();
                                        //OAS
                                        //SmsManager smsManager = SmsManager.getDefault();
                                        //smsManager.sendTextMessage(nroCelular, null, nroVerificacion, null, null);
                                        Intent registraSolicitante = new Intent(PrincipalActivity.this, RegistraSolicitante.class);
                                        registraSolicitante.putExtra("zipCode", zipCode);
                                        registraSolicitante.putExtra("nroCelular", nroCelular);
                                        registraSolicitante.putExtra("nroVerificacion", nroVerificacion);
                                        registraSolicitante.putExtra("tipoUsuario", "1"); // solicitante
                                        registraSolicitante.putExtra("nombre", nombre);
                                        registraSolicitante.putExtra("correo", correo);
                                        startActivityForResult(registraSolicitante, 0);
                                    } else if (tipoUsuario.equals("2")) {  // si es de tipo ofertante
                                        Intent intent = new Intent(PrincipalActivity.this, inicioSesion.class);
                                        intent.putExtra("zipCode", zipCode);
                                        intent.putExtra("nroCelular", nroCelular);
                                        intent.putExtra("tipoUsuario", tipoUsuario); // ofertante
                                        intent.putExtra("clave", clave);
                                        intent.putExtra("nombre", nombre);
                                        intent.putExtra("correo", correo);
                                        startActivityForResult(intent, 0);
                                    }
                                } else if (usuarioRegistrado.equals("error")) {
                                    generaNumero();
                                    //OAS
                                    //SmsManager smsManager = SmsManager.getDefault();
                                    //smsManager.sendTextMessage(nroCelular, null, nroVerificacion, null, null);
                                    Intent registraSolicitante = new Intent(PrincipalActivity.this, RegistraSolicitante.class);
                                    registraSolicitante.putExtra("zipCode", zipCode);
                                    registraSolicitante.putExtra("nroCelular", nroCelular);
                                    registraSolicitante.putExtra("nroVerificacion", nroVerificacion);
                                    registraSolicitante.putExtra("tipoUsuario", "1"); // solicitante
                                    startActivityForResult(registraSolicitante, 0);
                                }
                                progress.cancel();
                            }
                        }, 3000);
                    } else {
                        Toast.makeText(getApplicationContext(), "Introduzca el número de celular.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private int getPhoneNumber() {
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getSimState();
    }


/*
    @Override protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

*/


    /* para verificar si existe conexion a internet*/
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkLocation() {
        locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager1.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager1.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void verificaPermisos() {


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            try {
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Lo sentimos iNeed no puede trabajar sin conexión.", Toast.LENGTH_SHORT).show();
                mMap.setMyLocationEnabled(true);
            }
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

            }
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }

       /* permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }*/

/*        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);

            }
        }*/

/*        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);

            }
        }*/

/*        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            }
        }*/

/*        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

            }
        }*/


/*        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }*/

        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            try {
                PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                        Manifest.permission.ACCESS_FINE_LOCATION, true);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sin conexión. Comprueba que la conexión de Datos o Wi-Fi esté activada y vuelve a intentarlo.", Toast.LENGTH_SHORT).show();
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    /* para que se salga de la aplicacion estando en otra actividad*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            //Intent salida=new Intent( Intent.PrincipalActivity); //Llamando a la activity principal
            finish();
            System.exit(0);
            //finish();
        }
    }

    /* verifica si existe un registro de solicitante u ofertante en base de datos local
    *  devuelve 0 si no existe un registro, 1 si es solicitante y 2 si es ofertante*/
    public String buscaDatos() {
        //String vTipoUsuario;
        Cursor usuarioExistente = db.rawQuery("SELECT id, pais, instancia, correo, tipoUsuario, estado, contrasenia, nombre FROM usuario", null);
        if (usuarioExistente.moveToFirst()) {
            tipoUsuario = usuarioExistente.getString(4).toString();
            clave = usuarioExistente.getString(6).toString();
            nroCelular = usuarioExistente.getString(2).toString();
            nombre = usuarioExistente.getString(7).toString();
            correo = usuarioExistente.getString(3).toString();

            return tipoUsuario;
        } else {
            return "0";
        }
    }


    /* procedimiento para obtener el numero randomico y verificar el envio de mensajes*/
    public void generaNumero() {
        double nroRandom;
        nroRandom = Math.random() * (9000) + 1000; //siempre 4 digitos
        BigDecimal bd = new BigDecimal(String.valueOf(nroRandom));
        BigDecimal iPart = new BigDecimal(bd.toBigInteger());
        nroVerificacion = iPart.toString();
        //OAS
        nroVerificacion = "1111";  // quitar para el futuro

    }

    //obtiene el zip code del pais
    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
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
        //CountryZipCode="591"; // OAS eliminar esta linea
        return CountryZipCode;
    }

    private class ListarDatos extends AsyncTask<String, Void, String> {
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
                        values.put("instancia", je.getString(2));
                        values.put("correo", je.getString(3));
                        values.put("nombre", je.getString(4));
                        values.put("servicio", je.getString(5));
                        values.put("latitud", je.getString(6));
                        values.put("longitud", je.getString(7));
                        values.put("direccion", je.getString(8));
                        values.put("datoServicio", je.getString(9));
                        values.put("estado", je.getString(10));
                        db.insert("ofertantes", null, values);
                        // Toast.makeText(getApplicationContext(), ja.getString(1), Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(getApplicationContext(), "Se cargaron: " + num_reg + " ofertantes", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No se encontro informacion: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }


    private class ListarSolicitudes extends AsyncTask<String, Void, String> {
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
                        db.insert("solicitudes", null, values);
                        // Toast.makeText(getApplicationContext(), ja.getString(1), Toast.LENGTH_LONG).show();
                        //Cursor usuarioExistente=db.rawQuery("select count(*) from solicitudes", null);
                    }
                }
                //Toast.makeText(getApplicationContext(), "Se cargaron: " + num_reg + " solicitudes", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "No se encontro informacion de solicitudes: " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ConsultarDatos extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                //Toast.makeText(getApplicationContext(), "Introduzca el número de celular1.", Toast.LENGTH_LONG).show();
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(), "Introduzca el número de celular2.", Toast.LENGTH_LONG).show();
                return "Unable to return webpage";
            }
        }

        protected void onPostExecute(String result) {
            JSONArray ja = null;
            try {
                //JSONObject object = new JSONObject(result);
                //JSONArray ja  = object.getJSONArray("agenda");
                ja = new JSONArray(result);
                // id, pais, instancia, correo, nombre, tipoU?usario, contrasenia, estado
                usuarioRegistrado = ja.getString(7);
                tipoUsuario = ja.getString(5);
                clave = ja.getString(6);
                nombre = ja.getString(4);
                correo = ja.getString(3);
                //Toast.makeText(getApplicationContext(), "Introduzca el número de celular3.", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                //Toast.makeText(getApplicationContext(), "Introduzca el número de celular4.", Toast.LENGTH_LONG).show();
                usuarioRegistrado = "error";
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
}
