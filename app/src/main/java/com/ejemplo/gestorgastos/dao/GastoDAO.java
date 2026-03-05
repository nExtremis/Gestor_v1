package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
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
        values.put("metodoPago", gasto.getMetodoPago());
        values.put("cuotas", gasto.getCuotas());
        values.put("precio", gasto.getPrecio());
        values.put("detalles", gasto.getDetalles());
        values.put("esProducto", gasto.isEsProducto() ? 1 : 0);
        return db.insert("gastos", null, values);
    }

    public int updateGasto(Gasto gasto) {
        ContentValues values = new ContentValues();
        values.put("fecha", gasto.getFecha() != null ? gasto.getFecha().getTime() : new Date().getTime());
        values.put("metodoPago", gasto.getMetodoPago());
        values.put("cuotas", gasto.getCuotas());
        values.put("precio", gasto.getPrecio());
        values.put("detalles", gasto.getDetalles());
        values.put("esProducto", gasto.isEsProducto() ? 1 : 0);
        return db.update("gastos", values, "id = ?", new String[]{String.valueOf(gasto.getId())});
    }

    public List<Gasto> getAllGastos() {
        List<Gasto> gastos = new ArrayList<>();
        Cursor cursor = db.query("gastos", null, null, null, null, null, "fecha DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                gasto.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                gasto.setMetodoPago(cursor.getString(cursor.getColumnIndexOrThrow("metodoPago")));
                gasto.setCuotas(cursor.getInt(cursor.getColumnIndexOrThrow("cuotas")));
                gasto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                gasto.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                gasto.setEsProducto(cursor.getInt(cursor.getColumnIndexOrThrow("esProducto")) == 1);
                gastos.add(gasto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return gastos;
    }

    public List<Gasto> getGastosByMonth(int month, int year) {
        List<Gasto> gastos = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1, 0, 0, 0);
        long start = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        long end = cal.getTimeInMillis();

        String selection = "fecha >= ? AND fecha <= ?";
        String[] selectionArgs = {String.valueOf(start), String.valueOf(end)};

        Cursor cursor = db.query("gastos", null, selection, selectionArgs, null, null, "fecha DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                gasto.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                gasto.setMetodoPago(cursor.getString(cursor.getColumnIndexOrThrow("metodoPago")));
                gasto.setCuotas(cursor.getInt(cursor.getColumnIndexOrThrow("cuotas")));
                gasto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                gasto.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                gasto.setEsProducto(cursor.getInt(cursor.getColumnIndexOrThrow("esProducto")) == 1);
                gastos.add(gasto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return gastos;
    }

    public List<Gasto> getGastosConCuotas() {
        List<Gasto> gastos = new ArrayList<>();
        String selection = "cuotas > 1";
        Cursor cursor = db.query("gastos", null, selection, null, null, null, "fecha DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                gasto.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                gasto.setMetodoPago(cursor.getString(cursor.getColumnIndexOrThrow("metodoPago")));
                gasto.setCuotas(cursor.getInt(cursor.getColumnIndexOrThrow("cuotas")));
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
