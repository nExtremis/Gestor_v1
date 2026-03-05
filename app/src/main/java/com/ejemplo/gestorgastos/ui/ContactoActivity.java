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
    private int contactoId = -1; // -1 significa que es un nuevo contacto

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

        // Verificar si estamos editando (recibiendo datos por Intent)
        if (getIntent().hasExtra("ID")) {
            contactoId = getIntent().getIntExtra("ID", -1);
            etNombre.setText(getIntent().getStringExtra("NOMBRE"));
            etTelefono.setText(getIntent().getStringExtra("TELEFONO"));
            etDireccion.setText(getIntent().getStringExtra("DIRECCION"));
            btnGuardar.setText("ACTUALIZAR CONTACTO");
        }

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

            if (contactoId == -1) {
                // Nuevo contacto
                long id = contactoDAO.insertContacto(contacto);
                if (id != -1) {
                    Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Modificar contacto existente
                contacto.setId(contactoId);
                int filasAfectadas = contactoDAO.updateContacto(contacto);
                if (filasAfectadas > 0) {
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactoDAO.close();
    }
}
