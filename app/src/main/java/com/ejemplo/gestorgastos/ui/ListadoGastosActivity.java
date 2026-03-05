package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import java.util.List;

public class ListadoGastosActivity extends AppCompatActivity {

    private RecyclerView rvGastos;
    private GastoAdapter adapter;
    private GastoDAO gastoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_gastos);

        rvGastos = findViewById(R.id.rvGastos);
        rvGastos.setLayoutManager(new LinearLayoutManager(this));

        gastoDAO = new GastoDAO(this);
        gastoDAO.open();

        List<Gasto> listaGastos = gastoDAO.getAllGastos();
        adapter = new GastoAdapter(listaGastos);
        rvGastos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la lista si volvemos de agregar un gasto
        if (gastoDAO != null) {
            List<Gasto> listaGastos = gastoDAO.getAllGastos();
            adapter = new GastoAdapter(listaGastos);
            rvGastos.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gastoDAO.close();
    }
}
