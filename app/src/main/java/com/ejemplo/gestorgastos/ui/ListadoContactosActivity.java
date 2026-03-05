package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.ContactoDAO;
import com.ejemplo.gestorgastos.model.Contacto;
import java.util.List;

public class ListadoContactosActivity extends AppCompatActivity {

    private RecyclerView rvContactos;
    private ContactoAdapter adapter;
    private ContactoDAO contactoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_contactos);

        rvContactos = findViewById(R.id.rvContactos);
        rvContactos.setLayoutManager(new LinearLayoutManager(this));

        contactoDAO = new ContactoDAO(this);
        contactoDAO.open();

        List<Contacto> listaContactos = contactoDAO.getAllContactos();
        adapter = new ContactoAdapter(listaContactos);
        rvContactos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (contactoDAO != null) {
            List<Contacto> listaContactos = contactoDAO.getAllContactos();
            adapter = new ContactoAdapter(listaContactos);
            rvContactos.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactoDAO.close();
    }
}
