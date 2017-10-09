package com.ineedserv.ineed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andresvasquez on 11/4/16.
 */
public class Base_datos extends SQLiteOpenHelper {
    public static final String NOMBREBD = "ineed10.sqlite";
    public static final String direccion ="http://www.ineedserv.com/app/";

    //Versión de la base de datos
    public Base_datos(Context context, int VERSION) {
        super(context, NOMBREBD, null, VERSION);
    }

    //Método utilizado cuando se crea la base de datos.
    public void onCreate(SQLiteDatabase db) {
        /*
        db.execSQL("drop table productos;");
        db.execSQL("drop table ubicacion;");
        db.execSQL("drop table solicitudes;");
        db.execSQL("drop table usuarios:");
        */
        /* crea las tablas */
       //db.execSQL("create table productos (id integer primary key autoincrement not unull, latitud double, longitud double);");

        /* se crea la tabla que tiene registrado al usuario que se instalo la aplicacion*/
        db.execSQL("create table usuario (id integer primary key autoincrement not null," +
                   "pais varchar, " +
                   "instancia varchar, " +
                   "correo varchar, " +
                   "tipoUsuario varchar, " +
                   "estado varchar, "+
                   "nombre varchar," +
                   "contrasenia varchar);");

        /* estas dos bases se deben sincronizar */
        db.execSQL("create table ofertantes (id integer primary key autoincrement not null," +
                   "pais varchar, " +
                   "instancia varchar, " +
                   "correo varchar, " +
                   "nombre varchar, " +
                   "servicio varchar, " +
                   "latitud double, " +
                   "longitud double, " +
                   "direccion varchar," +
                   "datoServicio varchar,"+
                   "estado varchar);");

        db.execSQL("create table solicitudes (id integer primary key autoincrement not null," +
                   "pais varchar, " +
                   "fechaFin text, "+
                   "instanciaSolicitante varchar, " +
                   "instanciaOfertante  varchar, " +
                   "latSolicitante double, " +
                   "lonSolicitante double, " +
                   "distancia double, " +
                   "servicio varchar, " +
                   "estado varchar," +
                   "fechaSolicitud text, " +
                   "fechaAceptacion text, " +
                   "fechaConfirmacion text," +
                   "fechaHoraCita text," +
                   "nombreSolicitante varchar);");

        /*"SELECT a.fechaFin,a.instanciaSolicitante,a.distancia,a.servicio," +
                " a.estado,a.instanciaOfertante, a.nombreSolicitante,b.nombre," +
                " a.fechaSolicitud, a.fechaAceptacion, a.fechaConfirmacion " +
                "FROM solicitudes a, ofertantes b " +
                " where a.instanciaOfertante=b.instancia " +
                "and a.pais=b.pais " +
                "order by strftime('%dd-%mm-%yyyy %HH:%mm:%ss',a.fechaFin)"*/;







        //pais,fechaFin,instanciaSolicitante,instanciaOfertante,latSolicitante,lonSolicitante,distancia,servicio,estado

 /* carga las tablas */
 // CARGA USUARIOS
        /* tipoUsuario 1= usuario solicitante, 2=usuario ofertantes  de tipo doctor*/

      //  String aux = "insert into solicitudes (pais, fechaFin, instanciaSolicitante,instanciaofertante,latSolicitante,lonSolicitante,distancia,servicio,estado) " +
      //                         "values ('591','15/10/2017 15:30','72550087','67151010',-16.047329615141432,-65.258167929409520,51.21,'Cirugía Pediátrica','AC');";
      //  db.execSQL(aux);

        //aux="insert into usuario (pais,instancia,correo,tipoUsuario,estado) values ('591','72550087','gb@gmail.com','1','AC');";
     //   db.execSQL("insert into usuario (pais,instancia,correo,tipoUsuario,estado) values ('591','72550087','gb@gmail.com','1','AC');");


     //   aux="insert into ubicacion (latitud,longitud,estado) values (-16.509770800000000,-68.123696800000000,'AC');";
     //   db.execSQL(aux);

        //(pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values
/*
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151010','correo@gmail.com','2','-16.509770800000000','-68.123696800000000','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151011','correo@gmail.com','2','-16.512939165050810','-68.123585146032720','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151012','correo@gmail.com','2','-16.546111207435093','-68.076764965057350','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151013','correo@gmail.com','2','-16.500316582098600','-68.134018391015610','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151014','correo@gmail.com','2','-16.540166078202585','-68.090737461722940','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151015','correo@gmail.com','2','-16.781749031191710','-68.182272195816040','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151016','correo@gmail.com','2','-16.384657473139410','-68.158449041803010','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151017','correo@gmail.com','2','-16.966123547954690','-68.109135357086190','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151018','correo@gmail.com','2','-11.018116782729361','-68.753970295695520','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151019','correo@gmail.com','2','-16.588009211655220','-68.752493070153030','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151020','correo@gmail.com','2','-16.046534895723720','-68.259383203388210','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151021','correo@gmail.com','2','-16.533408088715273','-68.734601572591370','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151022','correo@gmail.com','2','-14.834580112439324','-68.902249389024350','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151023','correo@gmail.com','2','-16.505993112989483','-68.162463090480060','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151024','correo@gmail.com','2','-16.393571283676365','-68.277510589965800','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151025','correo@gmail.com','2','-16.501990528078305','-68.130942904949160','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151026','correo@gmail.com','2','-16.499074165693240','-68.134086993008510','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151027','correo@gmail.com','2','-16.538845793818410','-68.077244822692870','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151028','correo@gmail.com','2','-16.538819313666938','-68.077188754081700','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151029','correo@gmail.com','2','-16.511943877909210','-68.164533755839550','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151030','correo@gmail.com','2','-16.511969491081107','-68.164572117999280','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151031','correo@gmail.com','2','-16.375252005800410','-68.159306214572150','nombre','Ortopedia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151032','correo@gmail.com','2','-16.375267364709103','-68.159287439109050','nombre','Otorrinolaringología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151033','correo@gmail.com','2','-16.396060152235932','-68.157157929113730','nombre','Manejo  del Dolor','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151034','correo@gmail.com','2','-16.395987745010288','-68.157185391132370','nombre','Patología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151035','correo@gmail.com','2','-16.760553474514168','-68.200879431807720','nombre','Pediatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151036','correo@gmail.com','2','-16.760624996970197','-68.200908936106884','nombre','Cirugía Pediátrica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151037','correo@gmail.com','2','-16.772156493537917','-68.182920646696460','nombre','Cirugía Plástica y Reconstructiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151038','correo@gmail.com','2','-16.783240127791670','-68.177149055576706','nombre','Podiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151039','correo@gmail.com','2','-16.383548015112720','-68.159403577987690','nombre','Medicina Preventiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151040','correo@gmail.com','2','-16.510718777511314','-68.122190856933570','nombre','Psiquiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151041','correo@gmail.com','2','-16.500000000000000','-68.149999999999980','nombre','Neumología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151042','correo@gmail.com','2','-16.374711709777700','-68.158899322692890','nombre','Radiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151043','correo@gmail.com','2','-16.866667000000000','-68.000000000000000','nombre','Endocrinología Reproductiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151044','correo@gmail.com','2','-16.499675958606590','-68.132962608337380','nombre','Reumatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151045','correo@gmail.com','2','-16.510801069089844','-68.122169399261510','nombre','Cirugía General','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151046','correo@gmail.com','2','-16.494897570915270','-68.137812042236300','nombre','Cirugía Torácica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151047','correo@gmail.com','2','-16.505760645264743','-68.127960288524600','nombre','Urología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151048','correo@gmail.com','2','-16.373667297882797','-68.142806068603470','nombre','Cirugía Vascular','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151049','correo@gmail.com','2','-16.875979568353518','-68.944725036621094','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151050','correo@gmail.com','2','-16.047658981733594','-68.256811658691390','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151051','correo@gmail.com','2','-16.538546762223582','-68.082183027267430','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151052','correo@gmail.com','2','-16.539709142852572','-68.083689373161180','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151053','correo@gmail.com','2','-16.542218970578290','-68.086302358197030','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151054','correo@gmail.com','2','-16.499625860466306','-68.132964002156200','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151055','correo@gmail.com','2','-16.497670887829525','-68.134457014988920','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151056','correo@gmail.com','2','-16.539137251114230','-68.078201947650430','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151057','correo@gmail.com','2','-16.506607846303780','-68.162034796182750','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151058','correo@gmail.com','2','-16.782730094922403','-68.183405764400960','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151059','correo@gmail.com','2','-16.807089557586160','-68.203506704674510','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151060','correo@gmail.com','2','-16.396540869124090','-68.156795420754350','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151061','correo@gmail.com','2','-16.047329615141432','-68.258167929409520','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151062','correo@gmail.com','2','-16.969184002954335','-68.114884337038120','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151063','correo@gmail.com','2','-16.588567824668732','-68.752441680246300','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151064','correo@gmail.com','2','-16.534473336186310','-68.734123768799580','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151065','correo@gmail.com','2','-16.512765770124595','-68.123136335611320','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151066','correo@gmail.com','2','-16.538659700000000','-68.082297499999980','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151067','correo@gmail.com','2','-16.510811355534745','-68.123671436309790','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151068','correo@gmail.com','2','-16.746662367783884','-68.174350802563480','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151069','correo@gmail.com','2','-16.746219978914773','-68.174117353503334','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151070','correo@gmail.com','2','-16.538003919324300','-68.073682142980370','nombre','Ortopedia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151071','correo@gmail.com','2','-16.539764213290490','-68.083777427673340','nombre','Otorrinolaringología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151072','correo@gmail.com','2','-16.539904373142896','-68.080868744850130','nombre','Manejo  del Dolor','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151073','correo@gmail.com','2','-16.501656203088327','-68.132576370239240','nombre','Patología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151074','correo@gmail.com','2','-16.498559811874195','-68.134818696975690','nombre','Pediatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151075','correo@gmail.com','2','-16.383066790462845','-68.152590767089860','nombre','Cirugía Pediátrica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151076','correo@gmail.com','2','-16.393302780247560','-68.157542124931350','nombre','Cirugía Plástica y Reconstructiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151077','correo@gmail.com','2','-16.396328161940495','-68.156981543247240','nombre','Podiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151078','correo@gmail.com','2','-16.509102243354977','-68.163702271044940','nombre','Medicina Preventiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151079','correo@gmail.com','2','-16.785639331920304','-68.181499917860380','nombre','Psiquiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151080','correo@gmail.com','2','-16.783940903557350','-68.183181662912716','nombre','Neumología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151081','correo@gmail.com','2','-16.047129092486400','-68.257908682178480','nombre','Radiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151082','correo@gmail.com','2','-16.533372920027585','-68.734404542501470','nombre','Endocrinología Reproductiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151083','correo@gmail.com','2','-16.501642001252485','-68.132463151555270','nombre','Reumatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151084','correo@gmail.com','2','-16.512889206172016','-68.122577095031710','nombre','Cirugía General','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151085','correo@gmail.com','2','-16.507745970743894','-68.128252649307230','nombre','Cirugía Torácica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151086','correo@gmail.com','2','-16.509628410787908','-68.125532889366130','nombre','Urología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151087','correo@gmail.com','2','-16.526889473129994','-68.107046328173740','nombre','Cirugía Vascular','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151088','correo@gmail.com','2','-16.396455555555600','-68.157105555555600','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151089','correo@gmail.com','2','-16.969725000000000','-68.115577777777800','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151090','correo@gmail.com','2','-16.588044444444400','-68.752325000000000','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151091','correo@gmail.com','2','-16.532227777777800','-68.733977777777800','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151092','correo@gmail.com','2','-16.782336111111100','-68.181600000000000','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151093','correo@gmail.com','2','-14.834280555555600','-68.904238888888900','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151094','correo@gmail.com','2','-16.048294444444400','-68.259152777777800','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151095','correo@gmail.com','2','-16.509222222222200','-68.127350000000000','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151096','correo@gmail.com','2','-16.507397222222200','-68.164100000000000','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151097','correo@gmail.com','2','-16.530591666666700','-68.073222222222200','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151098','correo@gmail.com','2','-16.495341666666700','-68.134936111111100','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151099','correo@gmail.com','2','-16.539952777777800','-68.077986111111100','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151100','correo@gmail.com','2','-16.500955555555600','-68.123080555555500','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151101','correo@gmail.com','2','-16.495391666666700','-68.144602777777800','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151102','correo@gmail.com','2','-16.514005555555600','-68.164491666666700','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151103','correo@gmail.com','2','-16.400638888888900','-68.154050000000000','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151104','correo@gmail.com','2','-16.380427777777800','-68.151266666666700','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151105','correo@gmail.com','2','-16.373022222222200','-68.156172222222200','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151106','correo@gmail.com','2','-16.385611111111100','-68.157755555555600','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151107','correo@gmail.com','2','-16.394247222222200','-68.155825000000000','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151108','correo@gmail.com','2','-16.967938888888900','-68.112950000000000','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151109','correo@gmail.com','2','-16.466408333333300','-68.569661111111100','nombre','Ortopedia','por ahi','AC');");
*/
/*
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676031','correo@gmail.com','Gonzalo Murillo','Alergia/Inmunología','-16.509770800000000','-68.123696800000000','Av. 16 de julio, Ed. 16 de julio Of, 903','Precio de consulta  100 Bs.','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676032','correo@gmail.com','nombre','Anestesiología','-16.512939165050810','-68.123585146032720','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676033','correo@gmail.com','nombre','Cardiología','-16.546111207435093','-68.076764965057350','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676034','correo@gmail.com','nombre','Medicina de Cuidados Intensivos','-16.500316582098600','-68.134018391015610','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676035','correo@gmail.com','nombre','Dermatología','-16.540166078202585','-68.090737461722940','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676036','correo@gmail.com','nombre','Medicina de Emergencia','-17.781749031191710','-63.182272195816040','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676037','correo@gmail.com','nombre','Endocrinología','-17.384657473139410','-66.158449041803010','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676038','correo@gmail.com','nombre','Medicina Familiar','-17.966123547954690','-67.109135357086190','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676039','correo@gmail.com','nombre','Gastroenterología','-11.018116782729361','-68.753970295695520','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676040','correo@gmail.com','nombre','Genética','-19.588009211655220','-65.752493070153030','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676041','correo@gmail.com','nombre','Geriatría','-19.046534895723720','-65.259383203388210','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676042','correo@gmail.com','nombre','Hematología/Oncología','-21.533408088715273','-64.734601572591370','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676043','correo@gmail.com','nombre','Enfermedades Infecciosas','-14.834580112439324','-64.902249389024350','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676044','correo@gmail.com','nombre','Medicina Interna','-16.505993112989483','-68.162463090480060','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676045','correo@gmail.com','nombre','Neonatología','-17.393571283676365','-66.277510589965800','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676046','correo@gmail.com','nombre','Nefrología','-16.501990528078305','-68.130942904949160','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676047','correo@gmail.com','nombre','Neurología','-16.499074165693240','-68.134086993008510','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676048','correo@gmail.com','nombre','Neurocirugía','-16.538845793818410','-68.077244822692870','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676049','correo@gmail.com','nombre','Obstetricia/Ginecología','-16.538819313666938','-68.077188754081700','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676050','correo@gmail.com','nombre','Cirugía Oncológica','-16.511943877909210','-68.164533755839550','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676051','correo@gmail.com','nombre','Oftalmología','-16.511969491081107','-68.164572117999280','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676052','correo@gmail.com','nombre','Ortopedia','-17.375252005800410','-66.159306214572150','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676053','correo@gmail.com','nombre','Otorrinolaringología','-17.375267364709103','-66.159287439109050','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676054','correo@gmail.com','nombre','Manejo  del Dolor','-17.396060152235932','-66.157157929113730','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676055','correo@gmail.com','nombre','Patología','-17.395987745010288','-66.157185391132370','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676056','correo@gmail.com','nombre','Pediatría','-17.760553474514168','-63.200879431807720','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676057','correo@gmail.com','nombre','Cirugía Pediátrica','-17.760624996970197','-63.200908936106884','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676058','correo@gmail.com','nombre','Cirugía Plástica y Reconstructiva','-17.772156493537917','-63.182920646696460','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676059','correo@gmail.com','nombre','Podiatría','-17.783240127791670','-63.177149055576706','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676060','correo@gmail.com','nombre','Medicina Preventiva','-17.383548015112720','-66.159403577987690','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676061','correo@gmail.com','nombre','Psiquiatría','-16.510718777511314','-68.122190856933570','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676062','correo@gmail.com','nombre','Neumología','-16.500000000000000','-68.149999999999980','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676063','correo@gmail.com','nombre','Radiología','-17.374711709777700','-66.158899322692890','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676064','correo@gmail.com','nombre','Endocrinología Reproductiva','-17.866667000000000','-63.000000000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676065','correo@gmail.com','nombre','Reumatología','-16.499675958606590','-68.132962608337380','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676066','correo@gmail.com','nombre','Cirugía General','-16.510801069089844','-68.122169399261510','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676067','correo@gmail.com','nombre','Cirugía Torácica','-16.494897570915270','-68.137812042236300','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676068','correo@gmail.com','nombre','Urología','-16.505760645264743','-68.127960288524600','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676069','correo@gmail.com','nombre','Cirugía Vascular','-17.373667297882797','-66.142806068603470','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676070','correo@gmail.com','nombre','Alergia/Inmunología','-17.875979568353518','-62.944725036621094','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676071','correo@gmail.com','nombre','Anestesiología','-19.047658981733594','-65.256811658691390','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676072','correo@gmail.com','nombre','Cardiología','-16.538546762223582','-68.082183027267430','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676073','correo@gmail.com','nombre','Medicina de Cuidados Intensivos','-16.539709142852572','-68.083689373161180','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676074','correo@gmail.com','nombre','Dermatología','-16.542218970578290','-68.086302358197030','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676075','correo@gmail.com','nombre','Medicina de Emergencia','-16.499625860466306','-68.132964002156200','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676076','correo@gmail.com','nombre','Endocrinología','-16.497670887829525','-68.134457014988920','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676077','correo@gmail.com','nombre','Medicina Familiar','-16.539137251114230','-68.078201947650430','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676078','correo@gmail.com','nombre','Gastroenterología','-16.506607846303780','-68.162034796182750','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676079','correo@gmail.com','nombre','Genética','-17.782730094922403','-63.183405764400960','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676080','correo@gmail.com','nombre','Geriatría','-17.807089557586160','-63.203506704674510','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676081','correo@gmail.com','nombre','Hematología/Oncología','-17.396540869124090','-66.156795420754350','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676082','correo@gmail.com','nombre','Enfermedades Infecciosas','-19.047329615141432','-65.258167929409520','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676083','correo@gmail.com','nombre','Medicina Interna','-17.969184002954335','-67.114884337038120','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676084','correo@gmail.com','nombre','Neonatología','-19.588567824668732','-65.752441680246300','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676085','correo@gmail.com','nombre','Nefrología','-21.534473336186310','-64.734123768799580','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676086','correo@gmail.com','nombre','Neurología','-16.512765770124595','-68.123136335611320','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676087','correo@gmail.com','nombre','Neurocirugía','-16.538659700000000','-68.082297499999980','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676088','correo@gmail.com','nombre','Obstetricia/Ginecología','-16.510811355534745','-68.123671436309790','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676089','correo@gmail.com','nombre','Cirugía Oncológica','-17.746662367783884','-63.174350802563480','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676090','correo@gmail.com','nombre','Oftalmología','-17.746219978914773','-63.174117353503334','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676091','correo@gmail.com','nombre','Ortopedia','-16.538003919324300','-68.073682142980370','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676092','correo@gmail.com','nombre','Otorrinolaringología','-16.539764213290490','-68.083777427673340','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676093','correo@gmail.com','nombre','Manejo  del Dolor','-16.539904373142896','-68.080868744850130','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676094','correo@gmail.com','nombre','Patología','-16.501656203088327','-68.132576370239240','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676095','correo@gmail.com','nombre','Pediatría','-16.498559811874195','-68.134818696975690','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676096','correo@gmail.com','nombre','Cirugía Pediátrica','-17.383066790462845','-66.152590767089860','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676097','correo@gmail.com','nombre','Cirugía Plástica y Reconstructiva','-17.393302780247560','-66.157542124931350','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676098','correo@gmail.com','nombre','Podiatría','-17.396328161940495','-66.156981543247240','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676099','correo@gmail.com','nombre','Medicina Preventiva','-16.509102243354977','-68.163702271044940','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676100','correo@gmail.com','nombre','Psiquiatría','-17.785639331920304','-63.181499917860380','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676101','correo@gmail.com','nombre','Neumología','-17.783940903557350','-63.183181662912716','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676102','correo@gmail.com','nombre','Radiología','-19.047129092486400','-65.257908682178480','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676103','correo@gmail.com','nombre','Endocrinología Reproductiva','-21.533372920027585','-64.734404542501470','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676104','correo@gmail.com','nombre','Reumatología','-16.501642001252485','-68.132463151555270','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676105','correo@gmail.com','nombre','Cirugía General','-16.512889206172016','-68.122577095031710','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676106','correo@gmail.com','nombre','Cirugía Torácica','-16.507745970743894','-68.128252649307230','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676107','correo@gmail.com','nombre','Urología','-16.509628410787908','-68.125532889366130','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676108','correo@gmail.com','nombre','Cirugía Vascular','-16.526889473129994','-68.107046328173740','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676109','correo@gmail.com','nombre','Alergia/Inmunología','-17.396455555555600','-66.157105555555600','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676110','correo@gmail.com','nombre','Anestesiología','-17.969725000000000','-67.115577777777800','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676111','correo@gmail.com','nombre','Cardiología','-19.588044444444400','-65.752325000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676112','correo@gmail.com','nombre','Medicina de Cuidados Intensivos','-21.532227777777800','-64.733977777777800','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676113','correo@gmail.com','nombre','Dermatología','-17.782336111111100','-63.181600000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676114','correo@gmail.com','nombre','Medicina de Emergencia','-14.834280555555600','-64.904238888888900','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676115','correo@gmail.com','nombre','Endocrinología','-19.048294444444400','-65.259152777777800','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676116','correo@gmail.com','nombre','Medicina Familiar','-16.509222222222200','-68.127350000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676117','correo@gmail.com','nombre','Gastroenterología','-16.507397222222200','-68.164100000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676118','correo@gmail.com','nombre','Genética','-16.530591666666700','-68.073222222222200','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676119','correo@gmail.com','nombre','Geriatría','-16.495341666666700','-68.134936111111100','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676120','correo@gmail.com','nombre','Hematología/Oncología','-16.539952777777800','-68.077986111111100','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676121','correo@gmail.com','nombre','Enfermedades Infecciosas','-16.500955555555600','-68.123080555555500','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676122','correo@gmail.com','nombre','Medicina Interna','-16.495391666666700','-68.144602777777800','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676123','correo@gmail.com','nombre','Neonatología','-16.514005555555600','-68.164491666666700','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676124','correo@gmail.com','nombre','Nefrología','-17.400638888888900','-66.154050000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676125','correo@gmail.com','nombre','Neurología','-17.380427777777800','-66.151266666666700','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676126','correo@gmail.com','nombre','Neurocirugía','-17.373022222222200','-66.156172222222200','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676127','correo@gmail.com','nombre','Obstetricia/Ginecología','-17.385611111111100','-66.157755555555600','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676128','correo@gmail.com','nombre','Cirugía Oncológica','-17.394247222222200','-66.155825000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676129','correo@gmail.com','nombre','Oftalmología','-17.967938888888900','-67.112950000000000','por ahi','','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,nombre, servicio,latitud,longitud,direccion,datoServicio,estado) values ('591','70676130','correo@gmail.com','nombre','Ortopedia','-18.466408333333300','-66.569661111111100','por ahi','','AC');");
*/
        Log.d("Todos los tablas: ", "Se crearon las tablas");
    }

    //Método utilizado cuando se actualiza la base de datos
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists ofertantes");
        db.execSQL("drop table if exists solicitudes");
        /* se crea la tabla que tiene registrado al usuario que se instalo la aplicacion*/
        db.execSQL("create table usuario (id integer primary key autoincrement not null," +
                "pais varchar, " +
                "instancia varchar, " +
                "correo varchar, " +
                "tipoUsuario varchar, " +
                "estado varchar);");

        /* estas dos bases se deben sincronizar */
        db.execSQL("create table ofertantes (id integer primary key autoincrement not null," +
                "pais varchar, " +
                "instancia varchar, " +
                "correo varchar, " +
                "tipoUsuario varchar, " +
                "latitud double, " +
                "longitud double, " +
                "nombre varchar, " +
                "servicio varchar, " +
                "direccion varchar" +
                "estado varchar);");

        db.execSQL("create table solicitudes (id integer primary key autoincrement not null," +
                "pais varchar, " +
                "fechaFin text, "+
                "instanciaSolicitante varchar, " +
                "instanciaofertantes  varchar, " +
                "latSolicitante double, " +
                "lonSolicitante double, " +
                "distancia double, " +
                "servicio varchar, " +
                "estado varchar);");
        Log.d("Todos los tablas: ", "Se modificaron las tablas");
    }


    public void cargaOfertantes(SQLiteDatabase db){

        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151010','correo@gmail.com','2','-16.509770800000000','-68.123696800000000','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151011','correo@gmail.com','2','-16.512939165050810','-68.123585146032720','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151012','correo@gmail.com','2','-16.546111207435093','-68.076764965057350','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151013','correo@gmail.com','2','-16.500316582098600','-68.134018391015610','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151014','correo@gmail.com','2','-16.540166078202585','-68.090737461722940','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151015','correo@gmail.com','2','-16.781749031191710','-68.182272195816040','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151016','correo@gmail.com','2','-16.384657473139410','-68.158449041803010','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151017','correo@gmail.com','2','-16.966123547954690','-68.109135357086190','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151018','correo@gmail.com','2','-11.018116782729361','-68.753970295695520','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151019','correo@gmail.com','2','-16.588009211655220','-68.752493070153030','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151020','correo@gmail.com','2','-16.046534895723720','-68.259383203388210','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151021','correo@gmail.com','2','-16.533408088715273','-68.734601572591370','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151022','correo@gmail.com','2','-14.834580112439324','-68.902249389024350','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151023','correo@gmail.com','2','-16.505993112989483','-68.162463090480060','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151024','correo@gmail.com','2','-16.393571283676365','-68.277510589965800','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151025','correo@gmail.com','2','-16.501990528078305','-68.130942904949160','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151026','correo@gmail.com','2','-16.499074165693240','-68.134086993008510','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151027','correo@gmail.com','2','-16.538845793818410','-68.077244822692870','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151028','correo@gmail.com','2','-16.538819313666938','-68.077188754081700','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151029','correo@gmail.com','2','-16.511943877909210','-68.164533755839550','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151030','correo@gmail.com','2','-16.511969491081107','-68.164572117999280','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151031','correo@gmail.com','2','-16.375252005800410','-68.159306214572150','nombre','Ortopedia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151032','correo@gmail.com','2','-16.375267364709103','-68.159287439109050','nombre','Otorrinolaringología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151033','correo@gmail.com','2','-16.396060152235932','-68.157157929113730','nombre','Manejo  del Dolor','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151034','correo@gmail.com','2','-16.395987745010288','-68.157185391132370','nombre','Patología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151035','correo@gmail.com','2','-16.760553474514168','-68.200879431807720','nombre','Pediatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151036','correo@gmail.com','2','-16.760624996970197','-68.200908936106884','nombre','Cirugía Pediátrica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151037','correo@gmail.com','2','-16.772156493537917','-68.182920646696460','nombre','Cirugía Plástica y Reconstructiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151038','correo@gmail.com','2','-16.783240127791670','-68.177149055576706','nombre','Podiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151039','correo@gmail.com','2','-16.383548015112720','-68.159403577987690','nombre','Medicina Preventiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151040','correo@gmail.com','2','-16.510718777511314','-68.122190856933570','nombre','Psiquiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151041','correo@gmail.com','2','-16.500000000000000','-68.149999999999980','nombre','Neumología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151042','correo@gmail.com','2','-16.374711709777700','-68.158899322692890','nombre','Radiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151043','correo@gmail.com','2','-16.866667000000000','-68.000000000000000','nombre','Endocrinología Reproductiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151044','correo@gmail.com','2','-16.499675958606590','-68.132962608337380','nombre','Reumatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151045','correo@gmail.com','2','-16.510801069089844','-68.122169399261510','nombre','Cirugía General','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151046','correo@gmail.com','2','-16.494897570915270','-68.137812042236300','nombre','Cirugía Torácica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151047','correo@gmail.com','2','-16.505760645264743','-68.127960288524600','nombre','Urología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151048','correo@gmail.com','2','-16.373667297882797','-68.142806068603470','nombre','Cirugía Vascular','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151049','correo@gmail.com','2','-16.875979568353518','-68.944725036621094','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151050','correo@gmail.com','2','-16.047658981733594','-68.256811658691390','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151051','correo@gmail.com','2','-16.538546762223582','-68.082183027267430','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151052','correo@gmail.com','2','-16.539709142852572','-68.083689373161180','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151053','correo@gmail.com','2','-16.542218970578290','-68.086302358197030','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151054','correo@gmail.com','2','-16.499625860466306','-68.132964002156200','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151055','correo@gmail.com','2','-16.497670887829525','-68.134457014988920','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151056','correo@gmail.com','2','-16.539137251114230','-68.078201947650430','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151057','correo@gmail.com','2','-16.506607846303780','-68.162034796182750','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151058','correo@gmail.com','2','-16.782730094922403','-68.183405764400960','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151059','correo@gmail.com','2','-16.807089557586160','-68.203506704674510','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151060','correo@gmail.com','2','-16.396540869124090','-68.156795420754350','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151061','correo@gmail.com','2','-16.047329615141432','-68.258167929409520','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151062','correo@gmail.com','2','-16.969184002954335','-68.114884337038120','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151063','correo@gmail.com','2','-16.588567824668732','-68.752441680246300','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151064','correo@gmail.com','2','-16.534473336186310','-68.734123768799580','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151065','correo@gmail.com','2','-16.512765770124595','-68.123136335611320','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151066','correo@gmail.com','2','-16.538659700000000','-68.082297499999980','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151067','correo@gmail.com','2','-16.510811355534745','-68.123671436309790','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151068','correo@gmail.com','2','-16.746662367783884','-68.174350802563480','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151069','correo@gmail.com','2','-16.746219978914773','-68.174117353503334','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151070','correo@gmail.com','2','-16.538003919324300','-68.073682142980370','nombre','Ortopedia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151071','correo@gmail.com','2','-16.539764213290490','-68.083777427673340','nombre','Otorrinolaringología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151072','correo@gmail.com','2','-16.539904373142896','-68.080868744850130','nombre','Manejo  del Dolor','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151073','correo@gmail.com','2','-16.501656203088327','-68.132576370239240','nombre','Patología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151074','correo@gmail.com','2','-16.498559811874195','-68.134818696975690','nombre','Pediatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151075','correo@gmail.com','2','-16.383066790462845','-68.152590767089860','nombre','Cirugía Pediátrica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151076','correo@gmail.com','2','-16.393302780247560','-68.157542124931350','nombre','Cirugía Plástica y Reconstructiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151077','correo@gmail.com','2','-16.396328161940495','-68.156981543247240','nombre','Podiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151078','correo@gmail.com','2','-16.509102243354977','-68.163702271044940','nombre','Medicina Preventiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151079','correo@gmail.com','2','-16.785639331920304','-68.181499917860380','nombre','Psiquiatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151080','correo@gmail.com','2','-16.783940903557350','-68.183181662912716','nombre','Neumología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151081','correo@gmail.com','2','-16.047129092486400','-68.257908682178480','nombre','Radiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151082','correo@gmail.com','2','-16.533372920027585','-68.734404542501470','nombre','Endocrinología Reproductiva','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151083','correo@gmail.com','2','-16.501642001252485','-68.132463151555270','nombre','Reumatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151084','correo@gmail.com','2','-16.512889206172016','-68.122577095031710','nombre','Cirugía General','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151085','correo@gmail.com','2','-16.507745970743894','-68.128252649307230','nombre','Cirugía Torácica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151086','correo@gmail.com','2','-16.509628410787908','-68.125532889366130','nombre','Urología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151087','correo@gmail.com','2','-16.526889473129994','-68.107046328173740','nombre','Cirugía Vascular','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151088','correo@gmail.com','2','-16.396455555555600','-68.157105555555600','nombre','Alergia/Inmunología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151089','correo@gmail.com','2','-16.969725000000000','-68.115577777777800','nombre','Anestesiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151090','correo@gmail.com','2','-16.588044444444400','-68.752325000000000','nombre','Cardiología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151091','correo@gmail.com','2','-16.532227777777800','-68.733977777777800','nombre','Medicina de Cuidados Intensivos','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151092','correo@gmail.com','2','-16.782336111111100','-68.181600000000000','nombre','Dermatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151093','correo@gmail.com','2','-14.834280555555600','-68.904238888888900','nombre','Medicina de Emergencia','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151094','correo@gmail.com','2','-16.048294444444400','-68.259152777777800','nombre','Endocrinología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151095','correo@gmail.com','2','-16.509222222222200','-68.127350000000000','nombre','Medicina Familiar','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151096','correo@gmail.com','2','-16.507397222222200','-68.164100000000000','nombre','Gastroenterología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151097','correo@gmail.com','2','-16.530591666666700','-68.073222222222200','nombre','Genética','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151098','correo@gmail.com','2','-16.495341666666700','-68.134936111111100','nombre','Geriatría','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151099','correo@gmail.com','2','-16.539952777777800','-68.077986111111100','nombre','Hematología/Oncología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151100','correo@gmail.com','2','-16.500955555555600','-68.123080555555500','nombre','Enfermedades Infecciosas','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151101','correo@gmail.com','2','-16.495391666666700','-68.144602777777800','nombre','Medicina Interna','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151102','correo@gmail.com','2','-16.514005555555600','-68.164491666666700','nombre','Neonatología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151103','correo@gmail.com','2','-16.400638888888900','-68.154050000000000','nombre','Nefrología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151104','correo@gmail.com','2','-16.380427777777800','-68.151266666666700','nombre','Neurología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151105','correo@gmail.com','2','-16.373022222222200','-68.156172222222200','nombre','Neurocirugía','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151106','correo@gmail.com','2','-16.385611111111100','-68.157755555555600','nombre','Obstetricia/Ginecología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151107','correo@gmail.com','2','-16.394247222222200','-68.155825000000000','nombre','Cirugía Oncológica','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151108','correo@gmail.com','2','-16.967938888888900','-68.112950000000000','nombre','Oftalmología','por ahi','AC');");
        db.execSQL("insert into ofertantes (pais,instancia,correo,tipoUsuario,latitud,longitud,nombre,servicio,direccion,estado) values ('591','67151109','correo@gmail.com','2','-16.466408333333300','-68.569661111111100','nombre','Ortopedia','por ahi','AC');");

    }
}