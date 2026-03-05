package com.ejemplo.gestorgastos.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.dao.ContactoDAO;
import com.ejemplo.gestorgastos.dao.ProductoDAO;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Contacto;
import com.ejemplo.gestorgastos.model.Producto;
import com.ejemplo.gestorgastos.model.Venta;
import com.ejemplo.gestorgastos.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentaActivity extends AppCompatActivity {

    private Spinner spProductos, spTipoPago;
    private AutoCompleteTextView actvContacto;
    private EditText etFecha, etCantidad, etPrecio, etDetalles;
    private CheckBox cbF;
    private VentaDAO ventaDAO;
    private ProductoDAO productoDAO;
    private ContactoDAO contactoDAO;
    private List<Producto> listaProductos;
    private List<Contacto> listaContactos;
    private Contacto contactoSeleccionado;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        spProductos = findViewById(R.id.spProductos);
        spTipoPago = findViewById(R.id.spTipoPago);
        actvContacto = findViewById(R.id.actvContacto);
        etFecha = findViewById(R.id.etFecha);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etDetalles = findViewById(R.id.etDetalles);
        cbF = findViewById(R.id.cbF);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        ImageButton btnAddContactoQuick = findViewById(R.id.btnAddContactoQuick);

        ventaDAO = new VentaDAO(this);
        ventaDAO.open();
        productoDAO = new ProductoDAO(this);
        productoDAO.open();
        contactoDAO = new ContactoDAO(this);
        contactoDAO.open();

        etFecha.setFocusable(false);
        etFecha.setOnClickListener(v -> showDatePickerDialog());
        etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        cargarProductos();
        cargarContactos();

        // Botón rápido para agregar contacto
        btnAddContactoQuick.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactoActivity.class);
            startActivity(intent);
        });

        String[] tiposPago = {"Efectivo", "Tarjeta", "Transferencia"};
        ArrayAdapter<String> adapterPago = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposPago);
        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoPago.setAdapter(adapterPago);

        spProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etPrecio.setText(String.valueOf(listaProductos.get(position).getPrecioVentaMayor()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnGuardar.setOnClickListener(v -> guardarVenta());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar contactos al volver de ContactoActivity
        cargarContactos();
    }

    private void cargarContactos() {
        if (contactoDAO != null) {
            listaContactos = contactoDAO.getAllContactos();
            ArrayAdapter<Contacto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaContactos);
            actvContacto.setAdapter(adapter);
            
            actvContacto.setOnItemClickListener((parent, view, position, id) -> {
                contactoSeleccionado = (Contacto) parent.getItemAtPosition(position);
            });
        }
    }

    private void cargarProductos() {
        listaProductos = productoDAO.getAllProductos();
        if (listaProductos.isEmpty()) {
            Toast.makeText(this, "Registra un producto primero", Toast.LENGTH_LONG).show();
            finish(); return;
        }
        List<String> nombres = new ArrayList<>();
        for (Producto p : listaProductos) nombres.add(p.getNombre());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductos.setAdapter(adapter);
    }

    private void guardarVenta() {
        try {
            Venta venta = new Venta();
            venta.setProductoId(listaProductos.get(spProductos.getSelectedItemPosition()).getId());
            
            // Validar si el texto en el autocompletado coincide con un contacto de la lista
            String nombreIngresado = actvContacto.getText().toString();
            if (contactoSeleccionado == null || !contactoSeleccionado.getNombre().equals(nombreIngresado)) {
                // Si el usuario escribió un nombre pero no lo seleccionó de la lista
                contactoSeleccionado = buscarContactoPorNombre(nombreIngresado);
            }
            
            venta.setContactoId(contactoSeleccionado != null ? contactoSeleccionado.getId() : 0);
            venta.setFecha(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(etFecha.getText().toString()));
            venta.setCantidad(Double.parseDouble(etCantidad.getText().toString()));
            venta.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            venta.setF(cbF.isChecked());
            venta.setTipoPago(spTipoPago.getSelectedItem().toString());
            venta.setDetalles(etDetalles.getText().toString());

            ventaDAO.insertVenta(venta);
            Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Contacto buscarContactoPorNombre(String nombre) {
        for (Contacto c : listaContactos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        }
        return null;
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ventaDAO.close(); productoDAO.close(); contactoDAO.close();
    }
}
