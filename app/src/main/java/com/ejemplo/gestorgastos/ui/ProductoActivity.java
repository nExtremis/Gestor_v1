package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.dao.ProductoDAO;
import com.ejemplo.gestorgastos.model.Producto;
import com.ejemplo.gestorgastos.R;

public class ProductoActivity extends AppCompatActivity {

    private EditText etNombre, etPrecioCosto, etPrecioVentaMenor, etPrecioVentaMayor;
    private ProductoDAO productoDAO;
    private int productoId = -1; // -1 indica nuevo producto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        etNombre = findViewById(R.id.etNombre);
        etPrecioCosto = findViewById(R.id.etPrecioCosto);
        etPrecioVentaMenor = findViewById(R.id.etPrecioVentaMenor);
        etPrecioVentaMayor = findViewById(R.id.etPrecioVentaMayor);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        productoDAO = new ProductoDAO(this);
        productoDAO.open();

        // Lógica de Edición: Cargar datos si vienen del adaptador
        if (getIntent().hasExtra("ID")) {
            productoId = getIntent().getIntExtra("ID", -1);
            etNombre.setText(getIntent().getStringExtra("NOMBRE"));
            etPrecioCosto.setText(String.valueOf(getIntent().getDoubleExtra("PRECIO_COSTO", 0.0)));
            etPrecioVentaMenor.setText(String.valueOf(getIntent().getDoubleExtra("PRECIO_VENTA_MENOR", 0.0)));
            etPrecioVentaMayor.setText(String.valueOf(getIntent().getDoubleExtra("PRECIO_VENTA_MAYOR", 0.0)));
            
            btnGuardar.setText("ACTUALIZAR PRODUCTO");
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Producto producto = new Producto();
                producto.setNombre(nombre);
                producto.setPrecioCosto(Double.parseDouble(etPrecioCosto.getText().toString()));
                producto.setPrecioVentaMenor(Double.parseDouble(etPrecioVentaMenor.getText().toString()));
                producto.setPrecioVentaMayor(Double.parseDouble(etPrecioVentaMayor.getText().toString()));

                if (productoId == -1) {
                    // Crear nuevo
                    productoDAO.insertProducto(producto);
                    Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                } else {
                    // Actualizar existente
                    producto.setId(productoId);
                    productoDAO.updateProducto(producto);
                    Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                }
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, ingresa precios válidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productoDAO.close();
    }
}
