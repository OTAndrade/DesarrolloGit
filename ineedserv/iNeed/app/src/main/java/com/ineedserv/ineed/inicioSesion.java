package com.ineedserv.ineed;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static com.ineedserv.ineed.R.layout.activity_inicio_sesion;

public class inicioSesion extends Activity {

    String zipCode;
    String nroCelular;
    String tipoUsuario;
    String correo;
    String password;
    String nroVerificacion;
    String message;
    Button sendBtn;
    TextView pwd;
    String clave;
    String nombre;


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static final String EXTRA_MESSAGE2 = "com.ineedserv.ineed.MESSAGE";

    /* declaraciones para el manejo de base de datos*/
    public SQLiteDatabase db;
    public static final int VERSION = 1;

    /* declaracion para manejo de medicos seleccionados*/
    private Context context;

    /* variables para grabar en basse de datos remota*/
    //                  http://190.129.95.187/aineed/consulta_medico.php?cod=591&num=70676031
    //String direccion ="http://190.129.95.187/aineed/"; //URL del servicio WEB creado en el servidor

    TextView sendLink;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        context = this;
        Base_datos crearBD = new Base_datos(context, VERSION);
        db = crearBD.getWritableDatabase();

        //message = intent.getStringExtra(EXTRA_MESSAGE);
        zipCode = getIntent().getStringExtra("zipCode");
        nroCelular = getIntent().getStringExtra("nroCelular");
        tipoUsuario = getIntent().getStringExtra("tipoUsuario");
        clave = getIntent().getStringExtra("clave");
        nombre = getIntent().getStringExtra("nombre");
        correo = getIntent().getStringExtra("correo");
        //nroCelular = message;
        context = this;
        //   Toast.makeText(inicioSesion.this, phoneNo, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_inicio_sesion);
        //final TextView txtSub = (TextView) findViewById(R.id.textoSubrayado);
        sendBtn = (Button) findViewById(R.id.btnEnvio);
        pwd = (EditText) findViewById(R.id.clave);
        progress = new ProgressDialog(this);
        //clave = pwd.getText().toString();

        /* boton de envio de contrasenia ofertante*/
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pwd.getText().toString().length() != 0) {
                    //new ConsultarDatos().execute(Base_datos.direccion + "consulta_medico.php?cod=" + zipCode + "&num=" + nroCelular);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (clave.equals(pwd.getText().toString())) { /* si el pwd coincide va directamente a la pantalla de ofertante*/
                                progress.setMessage("Verificando..");
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.setIndeterminate(true);
                                progress.setProgress(0);
                                progress.show();
                                registraUsuario(zipCode, nroCelular, correo, nombre);
                                Toast.makeText(getApplicationContext(), "Se registro el ofertante correctamente. ", Toast.LENGTH_LONG).show();
                                Intent actividadOfertante = new Intent(inicioSesion.this, MainActivity.class);
                                // Intent actividadOfertante = new Intent(inicioSesion.this, MainActivity.class);
                                actividadOfertante.putExtra("zipCode", zipCode);
                                actividadOfertante.putExtra("nroCelular", nroCelular);
                                actividadOfertante.putExtra("tipoUsuario", tipoUsuario);// usuario ofertante
                                //startActivity(actividadOfertante);
                                startActivityForResult(actividadOfertante, 0);
                                //return true;
                            } else {
                                Toast.makeText(inicioSesion.this, "La contraseña no corresponde al número de celular", Toast.LENGTH_SHORT).show();
                                //return false;
                            }
                            progress.cancel();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(inicioSesion.this, "Debes introducir la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* link para la creacion de un usuario nuevo
        txtSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
               /* se genera el numero randomico y envia por sms
                generaNumero();
                //SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage(nroCelular, null, nroVerificacion, null, null);
                Intent registraSolicitante = new Intent(inicioSesion.this, RegistraSolicitante.class);
                registraSolicitante.putExtra("zipCode", zipCode);
                registraSolicitante.putExtra("nroCelular", nroCelular);
                registraSolicitante.putExtra("nroVerificacion", nroVerificacion);
                registraSolicitante.putExtra("tipoUsuario", tipoUsuario); // manda el tipo de usuario
                startActivityForResult(registraSolicitante, 0);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            finish();
        }
    }

    /* procedimiento para obtener el numero randomico y verificar el envio de mensajes*/
    public void generaNumero() {
        double nroRandom;
        nroRandom = Math.random() * (9000) + 1000; //siempre 4 digitos
        BigDecimal bd = new BigDecimal(String.valueOf(nroRandom));
        BigDecimal iPart = new BigDecimal(bd.toBigInteger());
        nroVerificacion = iPart.toString();
        ///nroVerificacion = "1111";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(nroCelular, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS enviado.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS fallo, trate nuevamente.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


    /* registra un usuario de tipo ofertante*/
    public void registraUsuario(String zip, String fono, String correo, String nombre) {

        ContentValues values = new ContentValues();
        values = new ContentValues();
        values.put("pais", zip);
        values.put("instancia", fono);
        values.put("correo", correo);
        values.put("tipoUsuario", "2");
        values.put("estado", "AC");
        values.put("nombre",nombre);
        values.put("contrasenia", clave);
        db.insert("usuario", null, values);
        // new GrabarDatos().execute(Base_datos.direccion + "inserta_usuario.php?pais=" + zip + "&ins=" + fono + "&cor=" + correo + "&tip=2");
        //    INSERTA USUARIO
        //   http://190.129.95.187/aineed/inserta_usuario.php?pais=591&ins=70676031&cor=gonzalo.murillo@gmail.com&tip=1

    }





}
