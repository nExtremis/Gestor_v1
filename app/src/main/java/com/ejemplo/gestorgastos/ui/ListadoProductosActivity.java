package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.ProductoDAO;
import com.ejemplo.gestorgastos.model.Producto;
import java.util.List;

public class ListadoProductosActivity extends AppCompatActivity {

    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private ProductoDAO productoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_productos);

        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        productoDAO = new ProductoDAO(this);
        productoDAO.open();

        actualizarLista();
    }

    private void actualizarLista() {
        List<Producto> listaProductos = productoDAO.getAllProductos();
        adapter = new ProductoAdapter(listaProductos, this); // Pasamos 'this' como context
        rvProductos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (productoDAO != null) {
            actualizarLista();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productoDAO.close();
    }
}
