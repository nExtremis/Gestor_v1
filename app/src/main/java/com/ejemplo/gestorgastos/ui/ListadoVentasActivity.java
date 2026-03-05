package com.ejemplo.gestorgastos.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Venta;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListadoVentasActivity extends AppCompatActivity {

    private RecyclerView rvVentas;
    private VentaAdapter adapter;
    private VentaDAO ventaDAO;
    private TextView tvMesSeleccionado;
    private int mesActual, anioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_ventas);

        rvVentas = findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new LinearLayoutManager(this));
        tvMesSeleccionado = findViewById(R.id.tvMesSeleccionado);
        Button btnFiltrarMes = findViewById(R.id.btnFiltrarMes);

        ventaDAO = new VentaDAO(this);
        ventaDAO.open();

        // Por defecto, mostrar el mes actual
        Calendar cal = Calendar.getInstance();
        mesActual = cal.get(Calendar.MONTH);
        anioActual = cal.get(Calendar.YEAR);

        btnFiltrarMes.setOnClickListener(v -> mostrarSelectorMes());
        actualizarLista();
    }

    private void mostrarSelectorMes() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            mesActual = month;
            anioActual = year;
            actualizarLista();
        }, anioActual, mesActual, 1);

        // Intento de ocultar el selector de día para que sea solo mes/año
        try {
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        datePickerDialog.show();
    }

    private void actualizarLista() {
        List<Venta> listaVentas = ventaDAO.getVentasByMonth(mesActual, anioActual);
        adapter = new VentaAdapter(listaVentas, this);
        rvVentas.setAdapter(adapter);
        tvMesSeleccionado.setText(String.format(Locale.getDefault(), "%s %d", obtenerNombreMes(mesActual), anioActual));
    }

    private String obtenerNombreMes(int month) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[month];
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ventaDAO != null) {
            actualizarLista();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ventaDAO.close();
    }
}
