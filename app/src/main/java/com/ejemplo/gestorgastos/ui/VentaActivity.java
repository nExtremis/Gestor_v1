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
    private int ventaId = -1;

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

        cargarProductos();
        cargarContactos();

        String[] tiposPago = {"Efectivo", "Tarjeta", "Transferencia"};
        ArrayAdapter<String> adapterPago = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposPago);
        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoPago.setAdapter(adapterPago);

        // Lógica de Edición
        if (getIntent().hasExtra("ID")) {
            ventaId = getIntent().getIntExtra("ID", -1);
            etFecha.setText(getIntent().getStringExtra("FECHA"));
            etCantidad.setText(String.valueOf(getIntent().getDoubleExtra("CANTIDAD", 1.0)));
            etPrecio.setText(String.valueOf(getIntent().getDoubleExtra("PRECIO", 0.0)));
            cbF.setChecked(getIntent().getBooleanExtra("F_OPTION", false));
            etDetalles.setText(getIntent().getStringExtra("DETALLES"));
            
            // Seleccionar Producto en Spinner
            int pId = getIntent().getIntExtra("PRODUCTO_ID", -1);
            for(int i=0; i<listaProductos.size(); i++) {
                if(listaProductos.get(i).getId() == pId) {
                    spProductos.setSelection(i);
                    break;
                }
            }

            // Seleccionar Contacto
            int cId = getIntent().getIntExtra("CONTACTO_ID", -1);
            for(Contacto c : listaContactos) {
                if(c.getId() == cId) {
                    contactoSeleccionado = c;
                    actvContacto.setText(c.getNombre(), false);
                    break;
                }
            }
            btnGuardar.setText("ACTUALIZAR VENTA");
        } else {
            etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        }

        btnAddContactoQuick.setOnClickListener(v -> startActivity(new Intent(this, ContactoActivity.class)));
        btnGuardar.setOnClickListener(v -> guardarVenta());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarContactos();
    }

    private void cargarContactos() {
        listaContactos = contactoDAO.getAllContactos();
        ArrayAdapter<Contacto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaContactos);
        actvContacto.setAdapter(adapter);
        actvContacto.setOnItemClickListener((parent, view, position, id) -> contactoSeleccionado = (Contacto) parent.getItemAtPosition(position));
    }

    private void cargarProductos() {
        listaProductos = productoDAO.getAllProductos();
        if (listaProductos.isEmpty()) { finish(); return; }
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
            venta.setContactoId(contactoSeleccionado != null ? contactoSeleccionado.getId() : 0);
            venta.setFecha(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(etFecha.getText().toString()));
            venta.setCantidad(Double.parseDouble(etCantidad.getText().toString()));
            venta.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            venta.setF(cbF.isChecked());
            venta.setTipoPago(spTipoPago.getSelectedItem().toString());
            venta.setDetalles(etDetalles.getText().toString());

            if (ventaId == -1) {
                ventaDAO.insertVenta(venta);
                Toast.makeText(this, "Venta guardada", Toast.LENGTH_SHORT).show();
            } else {
                venta.setId(ventaId);
                // Aquí deberías tener un método updateVenta en tu DAO
                // ventaDAO.updateVenta(venta); 
                Toast.makeText(this, "Venta actualizada", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
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
