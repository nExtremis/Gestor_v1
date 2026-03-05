package com.ejemplo.gestorgastos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.model.Gasto;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private List<Gasto> gastoList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public GastoAdapter(List<Gasto> gastoList) {
        this.gastoList = gastoList;
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
        holder.tvCantidad.setText("Cant: " + gasto.getCantidad());
        holder.tvPrecio.setText("$" + gasto.getPrecio());
        holder.tvDetalles.setText(gasto.getDetalles());
    }

    @Override
    public int getItemCount() {
        return gastoList.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvCantidad, tvPrecio, tvDetalles;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
        }
    }
}
