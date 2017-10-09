package com.ineedserv.ineed;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistraSolicitante extends Activity {

    String zipCode;
    String nroCelular;
    String tipoUsuario;
    String nroVerificacion;
    Button siguiente;
    String nombre;
    String correo;
    EditText etNombre;
    EditText etCorreo;

    /* variables para grabar en basse de datos remota*/
    //String direccion ="http://190.129.95.187/aineed/"; //URL del servicio WEB creado en el servidor

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;

    /* declaracion para manejo de medicos seleccionados*/
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_solicitante);
        /* configuracion para trabajar con la base de datos
           esto debe ir en esta parte por que sino no funciona*/
        context=this;
        Base_datos crearBD = new Base_datos(context,VERSION);
        db = crearBD.getWritableDatabase();

        Intent intent = getIntent();
        zipCode = getIntent().getStringExtra("zipCode");
        nroCelular = getIntent().getStringExtra("nroCelular");
        nroVerificacion = getIntent().getStringExtra("nroVerificacion");
        tipoUsuario = getIntent().getStringExtra("tipoUsuario");
        nombre = getIntent().getStringExtra("nombre");
        correo = getIntent().getStringExtra("correo");

        etNombre = (EditText) findViewById(R.id.nombre);
        etCorreo = (EditText) findViewById(R.id.correo);

        nombre= etNombre.getText().toString();
        correo= etCorreo.getText().toString();

        TextView textoVerificacion =  (TextView) findViewById(R.id.nroClave);
        textoVerificacion.setText(nroVerificacion);
        textoVerificacion.setVisibility(View.INVISIBLE);

        siguiente = (Button) findViewById(R.id.btnConfirmacion);

        /* boton siguiente confirma nro de verificacion*/
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificaConfirmacion(nroVerificacion)) {

                    registraUsuario(zipCode, nroCelular, etCorreo.getText().toString(), etNombre.getText().toString(), tipoUsuario);
                    /* activa la pagina de mapas para solicitante*/
                    Intent actividadMapa = new Intent(RegistraSolicitante.this, MainActivity.class);
                    actividadMapa.putExtra("zipCode", zipCode);
                    actividadMapa.putExtra("nroCelular", nroCelular);
                    actividadMapa.putExtra("tipoUsuario", tipoUsuario);
                    actividadMapa.putExtra("nombre",etNombre.getText().toString());
                    actividadMapa.putExtra("correo",etCorreo.getText().toString());
                    //Intent actividadMapa = new Intent(RegistraSolicitante.this, MainActivity.class);
                    //startActivity(actividadMapa);
                    startActivityForResult(actividadMapa, 0);
                } else {
                    Toast.makeText(RegistraSolicitante.this, "El número no corresponde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
    }

    /*verifica la clave.. confirma el numero y graba si corresponde*/
    public boolean verificaConfirmacion(String nroVerificacion){
        TextView confirmacion =  (TextView) findViewById(R.id.nroConfirmacion);
        if (nroVerificacion.equals(confirmacion.getText().toString())){
            return true;
        } else{
            return false;
        }
    }

    /* registra un usuario de tipo solicitante*/
    public void registraUsuario(String zip, String fono, String correo, String nombre, String tipoUsr){

        ContentValues values = new ContentValues();
        values = new ContentValues();
        values.put("pais", zip);
        values.put("instancia", fono);
        values.put("correo", correo);
        values.put("tipoUsuario", "1");
        values.put("estado", "AC");
        values.put("nombre",nombre);
        values.put("contrasenia", "");
        db.insert("usuario", null, values);
        //http://190.129.95.187/aineed/inserta_usuario.php?pais=591&ins=70676031&cor=gonzalo.murillo@gmail.com&tip=1
        //new GrabarDatos().execute(Base_datos.direccion+"app_inserta_usuario.php?pais="+zip+"&ins="+fono+"&cor="+correo+"&nom="+nombre+"&tip="+tipoUsr);
        new GrabarDatos().execute(Base_datos.direccion+"app_inserta_usuario.php?pais="+zip+"&ins="+fono+"&cor="+correo+"&nom="+nombre+"&tip="+tipoUsr);
    }

    private class GrabarDatos extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls){
            try{
                return downloadUrl(urls[0]);
            }catch(IOException e){
                return "Unable to return webpage";
            }
        }
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(), "Se grabó correctamente el registro!!", Toast.LENGTH_LONG).show();

        }
    }
    private String downloadUrl(String myurl) throws IOException {
        //LOG.i("URL",""+myurl);
        myurl = myurl.replace(" ","%20");
        InputStream is = null;
        int len = 500;
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
}
