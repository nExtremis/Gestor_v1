package com.ejemplo.gestorgastos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.model.Venta;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> {

    private List<Venta> ventaList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public VentaAdapter(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new VentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaViewHolder holder, int position) {
        Venta venta = ventaList.get(position);
        
        holder.tvNombreProducto.setText(venta.getNombreProducto());
        holder.tvNombreContacto.setText(venta.getNombreContacto() != null ? "Cliente: " + venta.getNombreContacto() : "Cliente: No asignado");
        holder.tvFecha.setText(dateFormat.format(venta.getFecha()));
        holder.tvCantidad.setText("Cant: " + venta.getCantidad());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "$%.2f (%s)", 
                (venta.getCantidad() * venta.getPrecio()), venta.getTipoPago()));
        holder.tvF.setText(venta.isF() ? "F: SI" : "F: NO");
    }

    @Override
    public int getItemCount() {
        return ventaList.size();
    }

    public static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvNombreContacto, tvFecha, tvCantidad, tvPrecio, tvF;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProductoVenta);
            tvNombreContacto = itemView.findViewById(R.id.tvNombreContactoVenta);
            tvFecha = itemView.findViewById(R.id.tvFechaVenta);
            tvCantidad = itemView.findViewById(R.id.tvCantidadVenta);
            tvPrecio = itemView.findViewById(R.id.tvPrecioVenta);
            tvF = itemView.findViewById(R.id.tvFVenta);
        }
    }
}
