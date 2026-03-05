package com.ejemplo.gestorgastos.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gestorgastos.db";
    private static final int DATABASE_VERSION = 5; // Versión 5: Añade contactos y relación con ventas

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gastos (id INTEGER PRIMARY KEY AUTOINCREMENT, fecha INTEGER, cantidad REAL, precio REAL, detalles TEXT)");

        db.execSQL("CREATE TABLE productos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, precioCosto REAL, precioVentaMenor REAL, precioVentaMayor REAL)");

        db.execSQL("CREATE TABLE contactos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "telefono TEXT, " +
                "direccion TEXT)");

        db.execSQL("CREATE TABLE ventas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productoId INTEGER, " +
                "contactoId INTEGER, " + // Nueva relación con contacto
                "fecha INTEGER, " +
                "cantidad REAL, " +
                "precio REAL, " +
                "f_option INTEGER, " +
                "tipo_pago TEXT, " +
                "detalles TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS gastos");
        db.execSQL("DROP TABLE IF EXISTS ventas");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS contactos");
        onCreate(db);
    }
}
