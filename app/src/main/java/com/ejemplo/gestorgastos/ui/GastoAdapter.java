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
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<Gasto> gastoList;
    private Context context;
    private GastoDAO gastoDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public GastoAdapter(List<Gasto> gastoList, Context context) {
        this.gastoList = gastoList;
        this.context = context;
        this.gastoDAO = new GastoDAO(context);
        this.gastoDAO.open();
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastoList.get(position);
        holder.tvFecha.setText(dateFormat.format(gasto.getFecha()));
        
        // Mostrar metodo de pago y cuotas si corresponde
        String infoPago = "Pago: " + gasto.getMetodoPago();
        if ("Tarjeta".equals(gasto.getMetodoPago()) && gasto.getCuotas() > 1) {
            infoPago += " (" + gasto.getCuotas() + " cuotas)";
        }
        holder.tvMetodoPago.setText(infoPago);
        
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "$%.2f", gasto.getPrecio()));
        holder.tvDetalles.setText(gasto.getDetalles());
        
        if (gasto.isEsProducto()) {
            holder.tvTipoGasto.setText("TIPO: PRODUCTO/COSTO");
            holder.tvTipoGasto.setTextColor(0xFF1976D2);
        } else {
            holder.tvTipoGasto.setText("TIPO: PERSONAL");
            holder.tvTipoGasto.setTextColor(0xFF757575);
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, GastoActivity.class);
            intent.putExtra("ID", gasto.getId());
            intent.putExtra("FECHA", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(gasto.getFecha()));
            intent.putExtra("METODO_PAGO", gasto.getMetodoPago());
            intent.putExtra("CUOTAS", gasto.getCuotas());
            intent.putExtra("PRECIO", gasto.getPrecio());
            intent.putExtra("DETALLES", gasto.getDetalles());
            intent.putExtra("ES_PRODUCTO", gasto.isEsProducto());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Eliminar Gasto")
                .setMessage("¿Estás seguro de eliminar este gasto?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    gastoDAO.deleteGasto(gasto.getId());
                    gastoList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, gastoList.size());
                    Toast.makeText(context, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return gastoList.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvMetodoPago, tvPrecio, tvDetalles, tvTipoGasto;
        ImageButton btnEdit, btnDelete;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvMetodoPago = itemView.findViewById(R.id.tvCantidad); // Reusamos el ID tvCantidad para no romper el XML
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            tvTipoGasto = itemView.findViewById(R.id.tvTipoGasto);
            btnEdit = itemView.findViewById(R.id.btnEditGasto);
            btnDelete = itemView.findViewById(R.id.btnDeleteGasto);
        }
    }
}
