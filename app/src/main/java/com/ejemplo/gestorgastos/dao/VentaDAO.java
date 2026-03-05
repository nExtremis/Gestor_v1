package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Venta;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VentaDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public VentaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertVenta(Venta venta) {
        ContentValues values = new ContentValues();
        values.put("productoId", venta.getProductoId());
        values.put("contactoId", venta.getContactoId());
        values.put("fecha", venta.getFecha() != null ? venta.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", venta.getCantidad());
        values.put("precio", venta.getPrecio());
        values.put("f_option", venta.isF() ? 1 : 0);
        values.put("tipo_pago", venta.getTipoPago());
        values.put("detalles", venta.getDetalles());
        return db.insert("ventas", null, values);
    }

    public int updateVenta(Venta venta) {
        ContentValues values = new ContentValues();
        values.put("productoId", venta.getProductoId());
        values.put("contactoId", venta.getContactoId());
        values.put("fecha", venta.getFecha() != null ? venta.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", venta.getCantidad());
        values.put("precio", venta.getPrecio());
        values.put("f_option", venta.isF() ? 1 : 0);
        values.put("tipo_pago", venta.getTipoPago());
        values.put("detalles", venta.getDetalles());
        return db.update("ventas", values, "id = ?", new String[]{String.valueOf(venta.getId())});
    }

    public int deleteVenta(int id) {
        return db.delete("ventas", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Venta> getAllVentas() {
        List<Venta> ventas = new ArrayList<>();
        String query = "SELECT v.*, p.nombre as nombre_producto, c.nombre as nombre_contacto " +
                       "FROM ventas v " +
                       "INNER JOIN productos p ON v.productoId = p.id " +
                       "LEFT JOIN contactos c ON v.contactoId = c.id " +
                       "ORDER BY v.fecha DESC";
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                venta.setProductoId(cursor.getInt(cursor.getColumnIndexOrThrow("productoId")));
                venta.setContactoId(cursor.getInt(cursor.getColumnIndexOrThrow("contactoId")));
                venta.setNombreProducto(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                venta.setNombreContacto(cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto")));
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                venta.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")));
                venta.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                venta.setF(cursor.getInt(cursor.getColumnIndexOrThrow("f_option")) == 1);
                venta.setTipoPago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
                venta.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                ventas.add(venta);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ventas;
    }

    public List<Venta> getVentasByMonth(int month, int year) {
        List<Venta> ventas = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1, 0, 0, 0);
        long start = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        long end = cal.getTimeInMillis();

        String query = "SELECT v.*, p.nombre as nombre_producto, c.nombre as nombre_contacto " +
                       "FROM ventas v " +
                       "INNER JOIN productos p ON v.productoId = p.id " +
                       "LEFT JOIN contactos c ON v.contactoId = c.id " +
                       "WHERE v.fecha >= ? AND v.fecha <= ? " +
                       "ORDER BY v.fecha DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(start), String.valueOf(end)});
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                venta.setProductoId(cursor.getInt(cursor.getColumnIndexOrThrow("productoId")));
                venta.setContactoId(cursor.getInt(cursor.getColumnIndexOrThrow("contactoId")));
                venta.setNombreProducto(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                venta.setNombreContacto(cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto")));
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                venta.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")));
                venta.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                venta.setF(cursor.getInt(cursor.getColumnIndexOrThrow("f_option")) == 1);
                venta.setTipoPago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
                venta.setDetalles(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                ventas.add(venta);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ventas;
    }
}
