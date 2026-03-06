package com.ejemplo.gestorgastos.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gestorgastos.db";
    private static final int DATABASE_VERSION = 8; // Mantenemos la 8 si ya la tienes

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha INTEGER, " +
                "metodoPago TEXT, " +
                "cuotas INTEGER DEFAULT 1, " +
                "precio REAL, " +
                "detalles TEXT, " +
                "esProducto INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS productos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, precioCosto REAL, precioVentaMenor REAL, precioVentaMayor REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono TEXT, direccion TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ventas (id INTEGER PRIMARY KEY AUTOINCREMENT, productoId INTEGER, contactoId INTEGER, fecha INTEGER, cantidad REAL, precio REAL, f_option INTEGER, tipo_pago TEXT, detalles TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // COMENTADO PARA NO PERDER DATOS
        // db.execSQL("DROP TABLE IF EXISTS gastos");
        // db.execSQL("DROP TABLE IF EXISTS ventas");
        // db.execSQL("DROP TABLE IF EXISTS productos");
        // db.execSQL("DROP TABLE IF EXISTS contactos");
        // onCreate(db);
        
        // Aquí se deberían poner comandos ALTER TABLE si añades columnas en el futuro
    }
}
