package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Venta;
import java.util.List;

public class ListadoVentasActivity extends AppCompatActivity {

    private RecyclerView rvVentas;
    private VentaAdapter adapter;
    private VentaDAO ventaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_ventas);

        rvVentas = findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new LinearLayoutManager(this));

        ventaDAO = new VentaDAO(this);
        ventaDAO.open();

        List<Venta> listaVentas = ventaDAO.getAllVentas();
        adapter = new VentaAdapter(listaVentas);
        rvVentas.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ventaDAO != null) {
            List<Venta> listaVentas = ventaDAO.getAllVentas();
            adapter = new VentaAdapter(listaVentas);
            rvVentas.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ventaDAO.close();
    }
}
