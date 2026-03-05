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
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.model.Venta;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FinanzasFragment extends Fragment {

    private TextView tvTotalVentas, tvVentasConF, tvVentasSinF, tvGastoInsumos, tvGastoPersonal, tvTotalGastos, tvBalanceFinal;
    private TextView tvGastoEfectivo, tvGastoTarjeta, tvGastoCuotas, tvDeudaTotal, tvMesFinanzas;
    private VentaDAO ventaDAO;
    private GastoDAO gastoDAO;
    private int mesSeleccionado, anioSeleccionado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finanzas, container, false);

        tvTotalVentas = view.findViewById(R.id.tvTotalVentas);
        tvVentasConF = view.findViewById(R.id.tvVentasConF);
        tvVentasSinF = view.findViewById(R.id.tvVentasSinF);
        tvGastoInsumos = view.findViewById(R.id.tvGastoInsumos);
        tvGastoPersonal = view.findViewById(R.id.tvGastoPersonal);
        tvGastoEfectivo = view.findViewById(R.id.tvGastoEfectivo);
        tvGastoTarjeta = view.findViewById(R.id.tvGastoTarjeta);
        tvGastoCuotas = view.findViewById(R.id.tvGastoCuotas);
        tvDeudaTotal = view.findViewById(R.id.tvDeudaTotal);
        tvTotalGastos = view.findViewById(R.id.tvTotalGastos);
        tvBalanceFinal = view.findViewById(R.id.tvBalanceFinal);
        tvMesFinanzas = view.findViewById(R.id.tvMesFinanzas);
        Button btnFiltrar = view.findViewById(R.id.btnFiltrarMesFinanzas);

        ventaDAO = new VentaDAO(getContext());
        gastoDAO = new GastoDAO(getContext());

        Calendar cal = Calendar.getInstance();
        mesSeleccionado = cal.get(Calendar.MONTH);
        anioSeleccionado = cal.get(Calendar.YEAR);

        btnFiltrar.setOnClickListener(v -> mostrarSelectorMes());

        actualizarCalculos();
        return view;
    }

    private void mostrarSelectorMes() {
        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            mesSeleccionado = month;
            anioSeleccionado = year;
            actualizarCalculos();
        }, anioSeleccionado, mesSeleccionado, 1).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarCalculos();
    }

    private void actualizarCalculos() {
        ventaDAO.open();
        gastoDAO.open();

        List<Venta> ventas = ventaDAO.getAllVentas();
        List<Gasto> gastos = gastoDAO.getAllGastos();

        double totalConF = 0, totalSinF = 0;
        for (Venta v : ventas) {
            Calendar calV = Calendar.getInstance(); calV.setTime(v.getFecha());
            if (calV.get(Calendar.MONTH) == mesSeleccionado && calV.get(Calendar.YEAR) == anioSeleccionado) {
                double sub = v.getCantidad() * v.getPrecio();
                if (v.isF()) totalConF += sub; else totalSinF += sub;
            }
        }

        double totalInsumos = 0, totalPersonal = 0, totalEfectivo = 0, totalTarjetaUnPago = 0, totalCuotasMes = 0, deudaGlobal = 0;
        Calendar hoy = Calendar.getInstance();

        for (Gasto g : gastos) {
            Calendar calG = Calendar.getInstance(); calG.setTime(g.getFecha());
            double valorCuota = g.getPrecio() / g.getCuotas();

            // 1. Calculo para el mes seleccionado (Proyección o Historial)
            if (g.getCuotas() > 1) {
                int diffMeses = (anioSeleccionado - calG.get(Calendar.YEAR)) * 12 + (mesSeleccionado - calG.get(Calendar.MONTH));
                if (diffMeses >= 0 && diffMeses < g.getCuotas()) {
                    totalCuotasMes += valorCuota;
                    if (g.isEsProducto()) totalInsumos += valorCuota; else totalPersonal += valorCuota;
                }
            } else if (calG.get(Calendar.MONTH) == mesSeleccionado && calG.get(Calendar.YEAR) == anioSeleccionado) {
                if ("Tarjeta".equals(g.getMetodoPago())) totalTarjetaUnPago += g.getPrecio();
                else totalEfectivo += g.getPrecio();
                if (g.isEsProducto()) totalInsumos += g.getPrecio(); else totalPersonal += g.getPrecio();
            }

            // 2. Calculo de Deuda Total Pendiente (de HOY en adelante)
            if (g.getCuotas() > 1) {
                int cuotasPagadasHastaHoy = (hoy.get(Calendar.YEAR) - calG.get(Calendar.YEAR)) * 12 + (hoy.get(Calendar.MONTH) - calG.get(Calendar.MONTH));
                if (cuotasPagadasHastaHoy < 0) cuotasPagadasHastaHoy = -1; // Aun no empezo a pagar
                
                int cuotasRestantes = g.getCuotas() - (cuotasPagadasHastaHoy + 1);
                if (cuotasRestantes > 0) {
                    deudaGlobal += (cuotasRestantes * valorCuota);
                }
            }
        }

        // Actualizar UI
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        tvMesFinanzas.setText(meses[mesSeleccionado] + " " + anioSeleccionado);
        
        tvVentasConF.setText(String.format(Locale.getDefault(), "$%.2f", totalConF));
        tvVentasSinF.setText(String.format(Locale.getDefault(), "$%.2f", totalSinF));
        tvTotalVentas.setText(String.format(Locale.getDefault(), "$%.2f", (totalConF + totalSinF)));
        tvGastoInsumos.setText(String.format(Locale.getDefault(), "$%.2f", totalInsumos));
        tvGastoPersonal.setText(String.format(Locale.getDefault(), "$%.2f", totalPersonal));
        tvGastoEfectivo.setText(String.format(Locale.getDefault(), "$%.2f", totalEfectivo));
        tvGastoTarjeta.setText(String.format(Locale.getDefault(), "$%.2f", totalTarjetaUnPago));
        tvGastoCuotas.setText(String.format(Locale.getDefault(), "$%.2f", totalCuotasMes));
        tvTotalGastos.setText(String.format(Locale.getDefault(), "$%.2f", (totalInsumos + totalPersonal)));
        tvDeudaTotal.setText(String.format(Locale.getDefault(), "$%.2f", deudaGlobal));
        
        double balance = (totalConF + totalSinF) - (totalInsumos + totalPersonal);
        tvBalanceFinal.setText(String.format(Locale.getDefault(), "$%.2f", balance));
        tvBalanceFinal.setTextColor(balance >= 0 ? 0xFF1B5E20 : 0xFFC62828);

        ventaDAO.close(); gastoDAO.close();
    }
}
