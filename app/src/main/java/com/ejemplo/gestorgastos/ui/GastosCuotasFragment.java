package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import java.util.List;

public class GastosCuotasFragment extends Fragment {

    private RecyclerView rvCuotas;
    private CuotasAdapter adapter;
    private GastoDAO gastoDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Usaremos un layout simple con solo un RecyclerView
        View view = inflater.inflate(R.layout.activity_listado_productos, container, false);
        rvCuotas = view.findViewById(R.id.rvProductos); // Reusamos el ID del recycler
        rvCuotas.setLayoutManager(new LinearLayoutManager(getContext()));

        gastoDAO = new GastoDAO(getContext());
        gastoDAO.open();

        actualizarLista();

        return view;
    }

    private void actualizarLista() {
        List<Gasto> listaCuotas = gastoDAO.getGastosConCuotas();
        adapter = new CuotasAdapter(listaCuotas);
        rvCuotas.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gastoDAO != null) {
            actualizarLista();
        }
    }
}
