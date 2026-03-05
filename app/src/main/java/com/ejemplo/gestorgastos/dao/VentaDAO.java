package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Venta;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
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
        values.put("contactoId", venta.getContactoId()); // Nuevo: Guardar el contacto
        values.put("fecha", venta.getFecha() != null ? venta.getFecha().getTime() : new Date().getTime());
        values.put("cantidad", venta.getCantidad());
        values.put("precio", venta.getPrecio());
        values.put("f_option", venta.isF() ? 1 : 0);
        values.put("tipo_pago", venta.getTipoPago());
        values.put("detalles", venta.getDetalles());
        return db.insert("ventas", null, values);
    }

    public List<Venta> getAllVentas() {
        List<Venta> ventas = new ArrayList<>();
        // JOIN triple para obtener nombres de producto y contacto
        String query = "SELECT v.*, p.nombre as nombre_producto, c.nombre as nombre_contacto " +
                       "FROM ventas v " +
                       "INNER JOIN productos p ON v.productoId = p.id " +
                       "LEFT JOIN contactos c ON v.contactoId = c.id"; // LEFT JOIN por si no se eligió contacto
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Venta venta = new Venta();
                venta.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                venta.setProductoId(cursor.getInt(cursor.getColumnIndexOrThrow("productoId")));
                venta.setNombreProducto(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")));
                
                // Nuevo: Nombre del contacto
                int contactoIdx = cursor.getColumnIndex("nombre_contacto");
                if (contactoIdx != -1) {
                    venta.setDetalles(cursor.getString(contactoIdx)); // Reusamos detalles o añadimos un campo en el modelo
                }

                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("fecha"))));
                venta.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")));
                venta.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")));
                venta.setF(cursor.getInt(cursor.getColumnIndexOrThrow("f_option")) == 1);
                venta.setTipoPago(cursor.getString(cursor.getColumnIndexOrThrow("tipo_pago")));
                ventas.add(venta);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ventas;
    }
}
