package com.ejemplo.gestorgastos.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.model.Venta;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AjustesFragment extends Fragment {

    private GastoDAO gastoDAO;
    private VentaDAO ventaDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        gastoDAO = new GastoDAO(getContext());
        ventaDAO = new VentaDAO(getContext());

        Button btnExportar = view.findViewById(R.id.btnExportarCSV);
        btnExportar.setOnClickListener(v -> exportarDatos());

        return view;
    }

    private void exportarDatos() {
        gastoDAO.open();
        ventaDAO.open();

        List<Gasto> gastos = gastoDAO.getAllGastos();
        List<Venta> ventas = ventaDAO.getAllVentas();

        StringBuilder csvData = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Cabecera Gastos
        csvData.append("--- GASTOS ---\n");
        csvData.append("Fecha,MetodoPago,Cuotas,Precio,Detalles,EsProducto\n");
        for (Gasto g : gastos) {
            csvData.append(sdf.format(g.getFecha())).append(",")
                   .append(g.getMetodoPago()).append(",")
                   .append(g.getCuotas()).append(",")
                   .append(g.getPrecio()).append(",")
                   .append(g.getDetalles()).append(",")
                   .append(g.isEsProducto() ? "SI" : "NO").append("\n");
        }

        // Cabecera Ventas
        csvData.append("\n--- VENTAS ---\n");
        csvData.append("Fecha,Producto,Cliente,Cantidad,PrecioUnit,Total,F,MetodoPago\n");
        for (Venta v : ventas) {
            csvData.append(sdf.format(v.getFecha())).append(",")
                   .append(v.getNombreProducto()).append(",")
                   .append(v.getNombreContacto() != null ? v.getNombreContacto() : "N/A").append(",")
                   .append(v.getCantidad()).append(",")
                   .append(v.getPrecio()).append(",")
                   .append(v.getCantidad() * v.getPrecio()).append(",")
                   .append(v.isF() ? "SI" : "NO").append(",")
                   .append(v.getTipoPago()).append("\n");
        }

        try {
            // Crear archivo temporal
            File file = new File(getContext().getCacheDir(), "reporte_negocio.csv");
            FileOutputStream out = new FileOutputStream(file);
            out.write(csvData.toString().getBytes());
            out.close();

            // Compartir archivo
            Context context = getContext();
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Gestor de Negocio");
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Enviar reporte..."));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al exportar", Toast.LENGTH_SHORT).show();
        }

        gastoDAO.close();
        ventaDAO.close();
    }
}
