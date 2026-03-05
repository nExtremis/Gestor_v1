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

        btnGuardar.setOnClickListener(v -> {
            if (etNombre.getText().toString().isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Producto producto = new Producto();
                producto.setNombre(etNombre.getText().toString());
                producto.setPrecioCosto(Double.parseDouble(etPrecioCosto.getText().toString()));
                producto.setPrecioVentaMenor(Double.parseDouble(etPrecioVentaMenor.getText().toString()));
                producto.setPrecioVentaMayor(Double.parseDouble(etPrecioVentaMayor.getText().toString()));

                productoDAO.insertProducto(producto);
                Toast.makeText(this, "Producto guardado con éxito", Toast.LENGTH_SHORT).show();
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
