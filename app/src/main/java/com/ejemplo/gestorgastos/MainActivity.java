package com.ejemplo.gestorgastos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.ui.GastoActivity;
import com.ejemplo.gestorgastos.ui.ListadoGastosActivity;
import com.ejemplo.gestorgastos.ui.ListadoProductosActivity;
import com.ejemplo.gestorgastos.ui.ListadoVentasActivity;
import com.ejemplo.gestorgastos.ui.ProductoActivity;
import com.ejemplo.gestorgastos.ui.VentaActivity;
import com.ejemplo.gestorgastos.ui.ContactoActivity;
import com.ejemplo.gestorgastos.ui.ListadoContactosActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botones de Registro
        findViewById(R.id.btnGasto).setOnClickListener(v -> startActivity(new Intent(this, GastoActivity.class)));
        findViewById(R.id.btnVenta).setOnClickListener(v -> startActivity(new Intent(this, VentaActivity.class)));
        findViewById(R.id.btnProducto).setOnClickListener(v -> startActivity(new Intent(this, ProductoActivity.class)));
        findViewById(R.id.btnContacto).setOnClickListener(v -> startActivity(new Intent(this, ContactoActivity.class)));

        // Botones de Historial
        findViewById(R.id.btnVerGastos).setOnClickListener(v -> startActivity(new Intent(this, ListadoGastosActivity.class)));
        findViewById(R.id.btnVerVentas).setOnClickListener(v -> startActivity(new Intent(this, ListadoVentasActivity.class)));
        findViewById(R.id.btnVerProductos).setOnClickListener(v -> startActivity(new Intent(this, ListadoProductosActivity.class)));
        findViewById(R.id.btnVerContactos).setOnClickListener(v -> startActivity(new Intent(this, ListadoContactosActivity.class)));
    }
}
