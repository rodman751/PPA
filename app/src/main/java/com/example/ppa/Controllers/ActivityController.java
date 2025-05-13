package com.example.ppa.Controllers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ppa.SqlAdmin;
import com.example.ppa.Entidades.Actividad;

import java.util.ArrayList;
import java.util.List;
public class ActivityController {
    private SqlAdmin sqlAdmin;
    private SQLiteDatabase db;

    public ActivityController(Context context) {
        sqlAdmin = new SqlAdmin(context);
        db = sqlAdmin.getWritableDatabase();
    }

    public long crearActividad(String nombre, String descripcion, String fechaInicio, String fechaFin, String estado, int proyectoId) {
        ContentValues values = new ContentValues();
        values.put(SqlAdmin.COL_ACTIVIDAD_NOMBRE, nombre);
        values.put(SqlAdmin.COL_ACTIVIDAD_DESCRIPCION, descripcion);
        values.put(SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO, fechaInicio);
        values.put(SqlAdmin.COL_ACTIVIDAD_FECHA_FIN, fechaFin);
        values.put(SqlAdmin.COL_ACTIVIDAD_ESTADO, estado);
        values.put(SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK, proyectoId);
        long newRowId = db.insert(SqlAdmin.TABLE_ACTIVIDADES, null, values);
        return newRowId;
    }

    public Actividad obtenerActividad(int actividadId) {
        String[] projection = {
                SqlAdmin.COL_ACTIVIDAD_ID,
                SqlAdmin.COL_ACTIVIDAD_NOMBRE,
                SqlAdmin.COL_ACTIVIDAD_DESCRIPCION,
                SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO,
                SqlAdmin.COL_ACTIVIDAD_FECHA_FIN,
                SqlAdmin.COL_ACTIVIDAD_ESTADO,
                SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK
        };

        String selection = SqlAdmin.COL_ACTIVIDAD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(actividadId)};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_ACTIVIDADES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Actividad actividad = null;
        if (cursor.moveToFirst()) {
            actividad = new Actividad(
                    cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_DESCRIPCION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_FIN)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ESTADO)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK))
            );
        }
        cursor.close();
        return actividad;
    }

    public List<Actividad> listarActividadesPorProyecto(int proyectoId) {
        List<Actividad> actividades = new ArrayList<>();
        String[] projection = {
                SqlAdmin.COL_ACTIVIDAD_ID,
                SqlAdmin.COL_ACTIVIDAD_NOMBRE,
                SqlAdmin.COL_ACTIVIDAD_DESCRIPCION,
                SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO,
                SqlAdmin.COL_ACTIVIDAD_FECHA_FIN,
                SqlAdmin.COL_ACTIVIDAD_ESTADO
        };

        String selection = SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(proyectoId)};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_ACTIVIDADES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                SqlAdmin.COL_ACTIVIDAD_NOMBRE + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Actividad actividad = new Actividad();
                actividad.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ID)));
                actividad.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_NOMBRE)));
                actividad.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_DESCRIPCION)));
                actividad.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO)));
                actividad.setFechaFin(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_FIN)));
                actividad.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ESTADO)));
                actividades.add(actividad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return actividades;
    }

    public int actualizarActividad(Actividad actividad) {
        ContentValues values = new ContentValues();
        values.put(SqlAdmin.COL_ACTIVIDAD_NOMBRE, actividad.getNombre());
        values.put(SqlAdmin.COL_ACTIVIDAD_DESCRIPCION, actividad.getDescripcion());
        values.put(SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO, actividad.getFechaInicio());
        values.put(SqlAdmin.COL_ACTIVIDAD_FECHA_FIN, actividad.getFechaFin());
        values.put(SqlAdmin.COL_ACTIVIDAD_ESTADO, actividad.getEstado());

        String selection = SqlAdmin.COL_ACTIVIDAD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(actividad.getId())};

        int count = db.update(
                SqlAdmin.TABLE_ACTIVIDADES,
                values,
                selection,
                selectionArgs
        );
        return count;
    }

    public int eliminarActividad(int actividadId) {
        String selection = SqlAdmin.COL_ACTIVIDAD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(actividadId)};
        int deletedRows = db.delete(SqlAdmin.TABLE_ACTIVIDADES, selection, selectionArgs);
        return deletedRows;
    }

    public void close() {
        db.close();
        sqlAdmin.close();
    }
    public List<Actividad> listarTodasLasActividades() {
        List<Actividad> actividades = new ArrayList<>();
        String[] projection = {
                SqlAdmin.COL_ACTIVIDAD_ID,
                SqlAdmin.COL_ACTIVIDAD_NOMBRE,
                SqlAdmin.COL_ACTIVIDAD_DESCRIPCION,
                SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO,
                SqlAdmin.COL_ACTIVIDAD_FECHA_FIN,
                SqlAdmin.COL_ACTIVIDAD_ESTADO,
                SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK
        };

        Cursor cursor = db.query(
                SqlAdmin.TABLE_ACTIVIDADES,
                projection,
                null,
                null,
                null,
                null,
                SqlAdmin.COL_ACTIVIDAD_NOMBRE + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Actividad actividad = new Actividad(
                        cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_DESCRIPCION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_INICIO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_FECHA_FIN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_ESTADO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_ACTIVIDAD_PROYECTO_ID_FK))
                );
                actividades.add(actividad);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return actividades;
    }
}
