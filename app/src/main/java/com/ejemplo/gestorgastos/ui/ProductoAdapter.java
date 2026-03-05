package com.ejemplo.gestorgastos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.model.Producto;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;

    public ProductoAdapter(List<Producto> productoList) {
        this.productoList = productoList;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecioCosto.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecioCosto()));
        holder.tvPrecioVentaMenor.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecioVentaMenor()));
        holder.tvPrecioVentaMayor.setText(String.format(Locale.getDefault(), "$%.2f", producto.getPrecioVentaMayor()));
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecioCosto, tvPrecioVentaMenor, tvPrecioVentaMayor;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioCosto = itemView.findViewById(R.id.tvPrecioCosto);
            tvPrecioVentaMenor = itemView.findViewById(R.id.tvPrecioVentaMenor);
            tvPrecioVentaMayor = itemView.findViewById(R.id.tvPrecioVentaMayor);
        }
    }
}
