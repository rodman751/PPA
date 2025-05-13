package com.example.ppa.Controllers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ppa.SqlAdmin;
import com.example.ppa.Entidades.Usuario;
public class UserController {
    private SqlAdmin sqlAdmin;
    private SQLiteDatabase db;

    public UserController(Context context) {
        sqlAdmin = new SqlAdmin(context);
        db = sqlAdmin.getWritableDatabase();
    }

    public long registrarUsuario(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(SqlAdmin.COL_USUARIO_USERNAME, username);
        values.put(SqlAdmin.COL_USUARIO_PASSWORD, password);
        long newRowId = db.insert(SqlAdmin.TABLE_USUARIOS, null, values);
        return newRowId;
    }

    public Usuario autenticarUsuario(String username, String password) {
        String[] projection = {
                SqlAdmin.COL_USUARIO_ID,
                SqlAdmin.COL_USUARIO_USERNAME,
                SqlAdmin.COL_USUARIO_PASSWORD
        };

        String selection = SqlAdmin.COL_USUARIO_USERNAME + " = ? AND " + SqlAdmin.COL_USUARIO_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_USUARIOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqlAdmin.COL_USUARIO_ID));
            String user = cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_USUARIO_USERNAME));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_USUARIO_PASSWORD));
            usuario = new Usuario(id, user, pass);
        }
        cursor.close();
        return usuario;
    }

    // Simulación de recuperación de contraseña (mostrar en pantalla)
    public String recuperarContraseña(String username) {
        String[] projection = {
                SqlAdmin.COL_USUARIO_PASSWORD
        };

        String selection = SqlAdmin.COL_USUARIO_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_USUARIOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_USUARIO_PASSWORD));
        }
        cursor.close();
        return (password != null) ? "Tu contraseña es: " + password : "Usuario no encontrado.";
    }

    public void close() {
        db.close();
        sqlAdmin.close();
    }
    public String recuperarContrasena(String username) {
        String password = null;
        String[] projection = {SqlAdmin.COL_USUARIO_PASSWORD};
        String selection = SqlAdmin.COL_USUARIO_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                SqlAdmin.TABLE_USUARIOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow(SqlAdmin.COL_USUARIO_PASSWORD));
        }
        cursor.close();
        return password;
    }
}
