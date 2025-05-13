package com.example.ppa.Controllers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ppa.SqlAdmin;
import com.example.ppa.Entidades.Proyecto;

import java.util.ArrayList;
import java.util.List;
public class ProjectController {
    private SqlAdmin sqlAdmin;
    private SQLiteDatabase db;

    public ProjectController(Context context) {
        sqlAdmin = new SqlAdmin(context);
        db = sqlAdmin.getWritableDatabase();
    }

    public long crearProyecto(String nombre, String descripcion, String fechaInicio, String fechaFin, int usuarioId) {
        ContentValues values = new ContentValues();
        values.put(SqlAdmin.COL_PROYECTO_NOMBRE, nombre);
        values.put(SqlAdmin.COL_PROYECTO_DESCRIPCION, descripcion);
        values.put(SqlAdmin.COL_PROYECTO_FECHA_INICIO, fechaInicio);
        values.put(SqlAdmin.COL_PROYECTO_FECHA_FIN, fechaFin);
        values.put(SqlAdmin.COL_PROYECTO_USUARIO_ID_FK, usuarioId);
        long newRowId = db.insert(SqlAdmin.TABLE_PROYECTOS, null, values);
        return newRowId;
    }

    public Proyecto obtenerProyecto(int proyectoId) {
        String[] projection = {
                SqlAdmin.COL_PROYECTO_ID,
                SqlAdmin.COL_PROYECTO_NOMBRE,
                SqlAdmin.COL_PROYECTO_DESCRIPCION,
                SqlAdmin.COL_PROYECTO_FECHA_INICIO,
                SqlAdmin.COL_PROYECTO_FECHA_FIN,
                SqlAdmin.COL_PROYECTO_USUARIO_ID_FK
        };

        String selection = SqlAdmin.COL_PROYECTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(proyectoId)};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_PROYECTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Proyecto proyecto = null;
        if (cursor.moveToFirst()) {
            proyecto = new Proyecto(
                    cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_DESCRIPCION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_FECHA_INICIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_FECHA_FIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_USUARIO_ID_FK))
            );
        }
        cursor.close();
        return proyecto;
    }

    public List<Proyecto> listarProyectos(int usuarioId) {
        List<Proyecto> proyectos = new ArrayList<>();
        String[] projection = {
                SqlAdmin.COL_PROYECTO_ID,
                SqlAdmin.COL_PROYECTO_NOMBRE,
                SqlAdmin.COL_PROYECTO_DESCRIPCION,
                SqlAdmin.COL_PROYECTO_FECHA_INICIO,
                SqlAdmin.COL_PROYECTO_FECHA_FIN
        };

        String selection = SqlAdmin.COL_PROYECTO_USUARIO_ID_FK + " = ?";
        String[] selectionArgs = {String.valueOf(usuarioId)};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_PROYECTOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                SqlAdmin.COL_PROYECTO_NOMBRE + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                Proyecto proyecto = new Proyecto();
                proyecto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_ID)));
                proyecto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_NOMBRE)));
                proyecto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_DESCRIPCION)));
                proyecto.setFechaInicio(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_FECHA_INICIO)));
                proyecto.setFechaFin(cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_PROYECTO_FECHA_FIN)));
                proyectos.add(proyecto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return proyectos;
    }

    public int actualizarProyecto(Proyecto proyecto) {
        ContentValues values = new ContentValues();
        values.put(SqlAdmin.COL_PROYECTO_NOMBRE, proyecto.getNombre());
        values.put(SqlAdmin.COL_PROYECTO_DESCRIPCION, proyecto.getDescripcion());
        values.put(SqlAdmin.COL_PROYECTO_FECHA_INICIO, proyecto.getFechaInicio());
        values.put(SqlAdmin.COL_PROYECTO_FECHA_FIN, proyecto.getFechaFin());

        String selection = SqlAdmin.COL_PROYECTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(proyecto.getId())};

        int count = db.update(
                SqlAdmin.TABLE_PROYECTOS,
                values,
                selection,
                selectionArgs
        );
        return count;
    }

    public int eliminarProyecto(int proyectoId) {
        String selection = SqlAdmin.COL_PROYECTO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(proyectoId)};
        int deletedRows = db.delete(SqlAdmin.TABLE_PROYECTOS, selection, selectionArgs);
        return deletedRows;
    }

    public void close() {
        db.close();
        sqlAdmin.close();
    }
}
