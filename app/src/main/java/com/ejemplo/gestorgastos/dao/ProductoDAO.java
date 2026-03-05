package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Producto;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ProductoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertProducto(Producto producto) {
        ContentValues values = new ContentValues();
        values.put("nombre", producto.getNombre());
        values.put("precioCosto", producto.getPrecioCosto());
        values.put("precioVentaMenor", producto.getPrecioVentaMenor());
        values.put("precioVentaMayor", producto.getPrecioVentaMayor());
        return db.insert("productos", null, values);
    }

    public int updateProducto(Producto producto) {
        ContentValues values = new ContentValues();
        values.put("nombre", producto.getNombre());
        values.put("precioCosto", producto.getPrecioCosto());
        values.put("precioVentaMenor", producto.getPrecioVentaMenor());
        values.put("precioVentaMayor", producto.getPrecioVentaMayor());
        return db.update("productos", values, "id = ?", new String[]{String.valueOf(producto.getId())});
    }

    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        Cursor cursor = db.query("productos", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Producto producto = new Producto();
                producto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                producto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                producto.setPrecioCosto(cursor.getDouble(cursor.getColumnIndexOrThrow("precioCosto")));
                producto.setPrecioVentaMenor(cursor.getDouble(cursor.getColumnIndexOrThrow("precioVentaMenor")));
                producto.setPrecioVentaMayor(cursor.getDouble(cursor.getColumnIndexOrThrow("precioVentaMayor")));
                productos.add(producto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return productos;
    }

    public int deleteProducto(int id) {
        return db.delete("productos", "id = ?", new String[]{String.valueOf(id)});
    }
}
