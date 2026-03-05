package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.model.Venta;
import java.util.List;
import java.util.Locale;

public class FinanzasFragment extends Fragment {

    private TextView tvTotalVentas, tvVentasConF, tvVentasSinF, tvGastoInsumos, tvGastoPersonal, tvTotalGastos, tvBalanceFinal;
    private VentaDAO ventaDAO;
    private GastoDAO gastoDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finanzas, container, false);

        tvTotalVentas = view.findViewById(R.id.tvTotalVentas);
        tvVentasConF = view.findViewById(R.id.tvVentasConF);
        tvVentasSinF = view.findViewById(R.id.tvVentasSinF);
        tvGastoInsumos = view.findViewById(R.id.tvGastoInsumos);
        tvGastoPersonal = view.findViewById(R.id.tvGastoPersonal);
        tvTotalGastos = view.findViewById(R.id.tvTotalGastos);
        tvBalanceFinal = view.findViewById(R.id.tvBalanceFinal);

        ventaDAO = new VentaDAO(getContext());
        gastoDAO = new GastoDAO(getContext());

        actualizarCalculos();

        return view;
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

        double totalConF = 0;
        double totalSinF = 0;
        for (Venta v : ventas) {
            double subtotal = v.getCantidad() * v.getPrecio();
            if (v.isF()) {
                totalConF += subtotal;
            } else {
                totalSinF += subtotal;
            }
        }
        double totalFacturado = totalConF + totalSinF;

        double totalInsumos = 0;
        double totalPersonal = 0;
        for (Gasto g : gastos) {
            double subtotalGasto = g.getCantidad() * g.getPrecio();
            if (g.isEsProducto()) {
                totalInsumos += subtotalGasto;
            } else {
                totalPersonal += subtotalGasto;
            }
        }

        double totalGastadoGlobal = totalInsumos + totalPersonal;
        double balance = totalFacturado - totalGastadoGlobal;

        // Mostrar en UI
        tvVentasConF.setText(String.format(Locale.getDefault(), "$%.2f", totalConF));
        tvVentasSinF.setText(String.format(Locale.getDefault(), "$%.2f", totalSinF));
        tvTotalVentas.setText(String.format(Locale.getDefault(), "$%.2f", totalFacturado));
        
        tvGastoInsumos.setText(String.format(Locale.getDefault(), "$%.2f", totalInsumos));
        tvGastoPersonal.setText(String.format(Locale.getDefault(), "$%.2f", totalPersonal));
        tvTotalGastos.setText(String.format(Locale.getDefault(), "$%.2f", totalGastadoGlobal));
        tvBalanceFinal.setText(String.format(Locale.getDefault(), "$%.2f", balance));

        if (balance >= 0) {
            tvBalanceFinal.setTextColor(0xFF1B5E20);
        } else {
            tvBalanceFinal.setTextColor(0xFFC62828);
        }

        ventaDAO.close();
        gastoDAO.close();
    }
}
