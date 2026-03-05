package com.ejemplo.gestorgastos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ejemplo.gestorgastos.model.Contacto;
import com.ejemplo.gestorgastos.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ContactoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContacto(Contacto contacto) {
        ContentValues values = new ContentValues();
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("direccion", contacto.getDireccion());
        return db.insert("contactos", null, values);
    }

    public int updateContacto(Contacto contacto) {
        ContentValues values = new ContentValues();
        values.put("nombre", contacto.getNombre());
        values.put("telefono", contacto.getTelefono());
        values.put("direccion", contacto.getDireccion());
        return db.update("contactos", values, "id = ?", new String[]{String.valueOf(contacto.getId())});
    }

    public int deleteContacto(int id) {
        return db.delete("contactos", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Contacto> getAllContactos() {
        List<Contacto> contactos = new ArrayList<>();
        Cursor cursor = db.query("contactos", null, null, null, null, null, "nombre ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto();
                contacto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                contacto.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                contacto.setTelefono(cursor.getString(cursor.getColumnIndexOrThrow("telefono")));
                contacto.setDireccion(cursor.getString(cursor.getColumnIndexOrThrow("direccion")));
                contactos.add(contacto);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contactos;
    }
}
