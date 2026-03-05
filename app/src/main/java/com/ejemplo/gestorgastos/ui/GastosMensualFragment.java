package com.ejemplo.gestorgastos.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GastosMensualFragment extends Fragment {

    private RecyclerView rvGastos;
    private GastoAdapter adapter;
    private GastoDAO gastoDAO;
    private TextView tvMesSeleccionado;
    private int mesActual, anioActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // CORRECCIÓN: Inflar el layout específico del fragmento
        View view = inflater.inflate(R.layout.fragment_gastos_mensual, container, false);

        rvGastos = view.findViewById(R.id.rvGastos);
        rvGastos.setLayoutManager(new LinearLayoutManager(getContext()));
        tvMesSeleccionado = view.findViewById(R.id.tvMesSeleccionado);
        Button btnFiltrarMes = view.findViewById(R.id.btnFiltrarMes);

        gastoDAO = new GastoDAO(getContext());
        gastoDAO.open();

        Calendar cal = Calendar.getInstance();
        mesActual = cal.get(Calendar.MONTH);
        anioActual = cal.get(Calendar.YEAR);

        btnFiltrarMes.setOnClickListener(v -> mostrarSelectorMes());
        actualizarLista();

        return view;
    }

    private void mostrarSelectorMes() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            mesActual = month;
            anioActual = year;
            actualizarLista();
        }, anioActual, mesActual, 1);

        datePickerDialog.setTitle("Seleccionar Mes y Año");
        datePickerDialog.show();
    }

    private void actualizarLista() {
        List<Gasto> listaGastos = gastoDAO.getGastosByMonth(mesActual, anioActual);
        adapter = new GastoAdapter(listaGastos, getContext());
        rvGastos.setAdapter(adapter);
        tvMesSeleccionado.setText(String.format(Locale.getDefault(), "%s %d", obtenerNombreMes(mesActual), anioActual));
    }

    private String obtenerNombreMes(int month) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[month];
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gastoDAO != null) {
            actualizarLista();
        }
    }
}
