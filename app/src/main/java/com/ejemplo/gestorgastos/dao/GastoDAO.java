package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GastoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public GastoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertGasto(Gasto gasto) {
        ContentValues values = new ContentValues();
        // Guardamos los milisegundos de la fecha
        values.put("fecha", gasto.getFecha() != null ? gasto.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", gasto.getCantidad());
        values.put("precio", gasto.getPrecio());
        values.put("detalles", gasto.getDetalles());
        return db.insert("gastos", null, values);
    }

    public int updateGasto(Gasto gasto) {
        ContentValues values = new ContentValues();
        values.put("fecha", gasto.getFecha() != null ? gasto.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", gasto.getCantidad());
        values.put("precio", gasto.getPrecio());
        values.put("detalles", gasto.getDetalles());
        return db.update("gastos", values, "id = ?", new String[]{String.valueOf(gasto.getId())});
    }

    public int deleteGasto(int id) {
        return db.delete("gastos", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Gasto> getAllGastos() {
        List<Gasto> gastos = new ArrayList<>();
        Cursor cursor = db.query("gastos", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                // Leemos los milisegundos y los convertimos a un objeto Date
                long fechaMilis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha"));
                gasto.setFecha(new Date(fechaMilis));
                gasto.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")));
                gasto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                gasto.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                gastos.add(gasto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return gastos;
    }
}
