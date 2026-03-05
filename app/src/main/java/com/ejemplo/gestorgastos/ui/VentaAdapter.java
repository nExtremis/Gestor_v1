package com.ejemplo.gestorgastos.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Venta;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> {

    private List<Venta> ventaList;
    private Context context;
    private VentaDAO ventaDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public VentaAdapter(List<Venta> ventaList, Context context) {
        this.ventaList = ventaList;
        this.context = context;
        this.ventaDAO = new VentaDAO(context);
        this.ventaDAO.open();
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

        // Acción de Editar Venta
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, VentaActivity.class);
            intent.putExtra("ID", venta.getId());
            intent.putExtra("PRODUCTO_ID", venta.getProductoId());
            intent.putExtra("CONTACTO_ID", venta.getContactoId());
            intent.putExtra("FECHA", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(venta.getFecha()));
            intent.putExtra("CANTIDAD", venta.getCantidad());
            intent.putExtra("PRECIO", venta.getPrecio());
            intent.putExtra("F_OPTION", venta.isF());
            intent.putExtra("TIPO_PAGO", venta.getTipoPago());
            intent.putExtra("DETALLES", venta.getDetalles());
            context.startActivity(intent);
        });

        // Acción de Eliminar Venta
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Eliminar Venta")
                .setMessage("¿Estás seguro de eliminar esta venta?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    ventaDAO.deleteVenta(venta.getId());
                    ventaList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ventaList.size());
                    Toast.makeText(context, "Venta eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return ventaList.size();
    }

    public static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvNombreContacto, tvFecha, tvCantidad, tvPrecio, tvF;
        ImageButton btnEdit, btnDelete;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProductoVenta);
            tvNombreContacto = itemView.findViewById(R.id.tvNombreContactoVenta);
            tvFecha = itemView.findViewById(R.id.tvFechaVenta);
            tvCantidad = itemView.findViewById(R.id.tvCantidadVenta);
            tvPrecio = itemView.findViewById(R.id.tvPrecioVenta);
            tvF = itemView.findViewById(R.id.tvFVenta);
            btnEdit = itemView.findViewById(R.id.btnEditVenta);
            btnDelete = itemView.findViewById(R.id.btnDeleteVenta);
        }
    }
}
