package com.ejemplo.gestorgastos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.model.Gasto;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CuotasAdapter extends RecyclerView.Adapter<CuotasAdapter.CuotaViewHolder> {

    private List<Gasto> cuotaList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));

    public CuotasAdapter(List<Gasto> cuotaList) {
        this.cuotaList = cuotaList;
    }

    @NonNull
    @Override
    public CuotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuota, parent, false);
        return new CuotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuotaViewHolder holder, int position) {
        Gasto gasto = cuotaList.get(position);
        
        holder.tvDetalle.setText(gasto.getDetalles());
        holder.tvMontoTotal.setText("Total de la compra: $" + String.format(Locale.getDefault(), "%.2f", gasto.getPrecio()));

        // Etiqueta Préstamo
        if ("Préstamo".equals(gasto.getMetodoPago())) {
            holder.tvTagPrestamo.setVisibility(View.VISIBLE);
        } else {
            holder.tvTagPrestamo.setVisibility(View.GONE);
        }

        Calendar fechaCompra = Calendar.getInstance();
        fechaCompra.setTime(gasto.getFecha());
        Calendar fechaHoy = Calendar.getInstance();
        
        int diffMeses = (fechaHoy.get(Calendar.YEAR) - fechaHoy.get(Calendar.YEAR)) * 12 + 
                        (fechaHoy.get(Calendar.MONTH) - fechaCompra.get(Calendar.MONTH));
        
        // Corrección de lógica para años diferentes
        diffMeses = (fechaHoy.get(Calendar.YEAR) - fechaCompra.get(Calendar.YEAR)) * 12 + 
                    (fechaHoy.get(Calendar.MONTH) - fechaCompra.get(Calendar.MONTH));

        int cuotaActual = diffMeses + 1;
        
        holder.pbCuotas.setMax(gasto.getCuotas());

        if (cuotaActual > gasto.getCuotas()) {
            holder.tvProgreso.setText("ESTADO: PAGADO TOTALMENTE");
            holder.tvProgreso.setTextColor(0xFF388E3C);
            holder.pbCuotas.setProgress(gasto.getCuotas());
            holder.tvInfoAdicional.setText("Deuda saldada.");
        } else if (cuotaActual <= 0) {
            holder.tvProgreso.setText("ESTADO: POR COMENZAR");
            holder.tvProgreso.setTextColor(0xFF757575);
            holder.pbCuotas.setProgress(0);
            holder.tvInfoAdicional.setText("El pago inicia el próximo mes.");
        } else {
            holder.tvProgreso.setText("CUOTA ACTUAL: " + cuotaActual + " de " + gasto.getCuotas());
            holder.tvProgreso.setTextColor(0xFF1976D2);
            holder.pbCuotas.setProgress(cuotaActual);
            int restantes = gasto.getCuotas() - cuotaActual;
            holder.tvInfoAdicional.setText("Quedan " + restantes + " cuotas pendientes.");
        }

        Calendar fechaFin = (Calendar) fechaCompra.clone();
        fechaFin.add(Calendar.MONTH, gasto.getCuotas() - 1);
        
        String fechaFormateada = dateFormat.format(fechaFin.getTime());
        if (fechaFormateada.length() > 0) {
            fechaFormateada = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);
        }
        holder.tvFechaFin.setText(fechaFormateada);
    }

    @Override
    public int getItemCount() {
        return cuotaList.size();
    }

    public static class CuotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDetalle, tvMontoTotal, tvProgreso, tvFechaFin, tvInfoAdicional, tvTagPrestamo;
        ProgressBar pbCuotas;

        public CuotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetalle = itemView.findViewById(R.id.tvDetalleCuota);
            tvMontoTotal = itemView.findViewById(R.id.tvMontoTotalCuota);
            tvProgreso = itemView.findViewById(R.id.tvProgresoCuotas);
            tvFechaFin = itemView.findViewById(R.id.tvFechaFin);
            tvInfoAdicional = itemView.findViewById(R.id.tvInfoAdicional);
            tvTagPrestamo = itemView.findViewById(R.id.tvTagPrestamo);
            pbCuotas = itemView.findViewById(R.id.pbCuotas);
        }
    }
}
