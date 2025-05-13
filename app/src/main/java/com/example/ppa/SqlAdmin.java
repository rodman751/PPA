package com.example.ppa;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
public class SqlAdmin extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "proyectos_personales.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de Usuarios
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COL_USUARIO_ID = "id";
    public static final String COL_USUARIO_USERNAME = "username";
    public static final String COL_USUARIO_PASSWORD = "password";

    // Tabla de Proyectos
    public static final String TABLE_PROYECTOS = "proyectos";
    public static final String COL_PROYECTO_ID = "id";
    public static final String COL_PROYECTO_NOMBRE = "nombre";
    public static final String COL_PROYECTO_DESCRIPCION = "descripcion";
    public static final String COL_PROYECTO_FECHA_INICIO = "fecha_inicio";
    public static final String COL_PROYECTO_FECHA_FIN = "fecha_fin";
    public static final String COL_PROYECTO_USUARIO_ID_FK = "usuario_id_fk";

    // Tabla de Actividades
    public static final String TABLE_ACTIVIDADES = "actividades";
    public static final String COL_ACTIVIDAD_ID = "id";
    public static final String COL_ACTIVIDAD_NOMBRE = "nombre";
    public static final String COL_ACTIVIDAD_DESCRIPCION = "descripcion";
    public static final String COL_ACTIVIDAD_FECHA_INICIO = "fecha_inicio";
    public static final String COL_ACTIVIDAD_FECHA_FIN = "fecha_fin";
    public static final String COL_ACTIVIDAD_ESTADO = "estado"; // Planificado, En ejecución, Realizado
    public static final String COL_ACTIVIDAD_PROYECTO_ID_FK = "proyecto_id_fk";

    public SqlAdmin(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de usuarios
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USUARIOS + " (" +
                COL_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USUARIO_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_USUARIO_PASSWORD + " TEXT NOT NULL)");

        // Crear tabla de proyectos
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PROYECTOS + " (" +
                COL_PROYECTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROYECTO_NOMBRE + " TEXT NOT NULL, " +
                COL_PROYECTO_DESCRIPCION + " TEXT, " +
                COL_PROYECTO_FECHA_INICIO + " TEXT, " +
                COL_PROYECTO_FECHA_FIN + " TEXT, " +
                COL_PROYECTO_USUARIO_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COL_PROYECTO_USUARIO_ID_FK + ") REFERENCES " + TABLE_USUARIOS + "(" + COL_USUARIO_ID + "))");

        // Crear tabla de actividades
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVIDADES + " (" +
                COL_ACTIVIDAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ACTIVIDAD_NOMBRE + " TEXT NOT NULL, " +
                COL_ACTIVIDAD_DESCRIPCION + " TEXT, " +
                COL_ACTIVIDAD_FECHA_INICIO + " TEXT, " +
                COL_ACTIVIDAD_FECHA_FIN + " TEXT, " +
                COL_ACTIVIDAD_ESTADO + " TEXT NOT NULL, " +
                COL_ACTIVIDAD_PROYECTO_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COL_ACTIVIDAD_PROYECTO_ID_FK + ") REFERENCES " + TABLE_PROYECTOS + "(" + COL_PROYECTO_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes implementar la lógica para actualizar la base de datos si cambia la versión
        // Por ahora, simplemente eliminaremos las tablas antiguas y crearemos las nuevas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROYECTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);
        onCreate(db);
    }
}
