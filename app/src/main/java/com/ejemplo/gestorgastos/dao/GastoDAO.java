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
        values.put("fecha", gasto.getFecha() != null ? gasto.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", gasto.getCantidad());
        values.put("precio", gasto.getPrecio());
        values.put("detalles", gasto.getDetalles());
        values.put("esProducto", gasto.isEsProducto() ? 1 : 0);
        return db.insert("gastos", null, values);
    }

    public List<Gasto> getAllGastos() {
        List<Gasto> gastos = new ArrayList<>();
        Cursor cursor = db.query("gastos", null, null, null, null, null, "fecha DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                gasto.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                gasto.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")));
                gasto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                gasto.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                gasto.setEsProducto(cursor.getInt(cursor.getColumnIndexOrThrow("esProducto")) == 1);
                gastos.add(gasto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return gastos;
    }

    public int deleteGasto(int id) {
        return db.delete("gastos", "id = ?", new String[]{String.valueOf(id)});
    }
}
