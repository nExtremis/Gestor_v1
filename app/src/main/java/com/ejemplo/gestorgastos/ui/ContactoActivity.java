package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.dao.ContactoDAO;
import com.ejemplo.gestorgastos.model.Contacto;
import com.ejemplo.gestorgastos.R;

public class ContactoActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etDireccion;
    private ContactoDAO contactoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        etNombre = findViewById(R.id.etNombreContacto);
        etTelefono = findViewById(R.id.etTelefonoContacto);
        etDireccion = findViewById(R.id.etDireccionContacto);
        Button btnGuardar = findViewById(R.id.btnGuardarContacto);

        contactoDAO = new ContactoDAO(this);
        contactoDAO.open();

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            Contacto contacto = new Contacto();
            contacto.setNombre(nombre);
            contacto.setTelefono(etTelefono.getText().toString());
            contacto.setDireccion(etDireccion.getText().toString());

            long id = contactoDAO.insertContacto(contacto);
            if (id != -1) {
                Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactoDAO.close();
    }
}
