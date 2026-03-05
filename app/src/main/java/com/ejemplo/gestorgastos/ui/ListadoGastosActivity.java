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

        actualizarLista();
    }

    private void actualizarLista() {
        List<Gasto> listaGastos = gastoDAO.getAllGastos();
        adapter = new GastoAdapter(listaGastos, this); // Pasamos 'this' como context
        rvGastos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gastoDAO != null) {
            actualizarLista();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gastoDAO.close();
    }
}
