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
import com.ejemplo.gestorgastos.dao.ProductoDAO;
import com.ejemplo.gestorgastos.model.Producto;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;
    private Context context;
    private ProductoDAO productoDAO;

    public ProductoAdapter(List<Producto> productoList, Context context) {
        this.productoList = productoList;
        this.context = context;
        this.productoDAO = new ProductoDAO(context);
        this.productoDAO.open();
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

        // CORRECCIÓN: Pasar todos los datos al editar
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductoActivity.class);
            intent.putExtra("ID", producto.getId());
            intent.putExtra("NOMBRE", producto.getNombre());
            intent.putExtra("PRECIO_COSTO", producto.getPrecioCosto());
            intent.putExtra("PRECIO_VENTA_MENOR", producto.getPrecioVentaMenor());
            intent.putExtra("PRECIO_VENTA_MAYOR", producto.getPrecioVentaMayor());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que quieres eliminar " + producto.getNombre() + "? Esta acción podría afectar los registros de ventas existentes.")
                .setPositiveButton("Sí, ELIMINAR", (dialog, which) -> {
                    productoDAO.deleteProducto(producto.getId());
                    productoList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, productoList.size());
                    Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("NO", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecioCosto, tvPrecioVentaMenor, tvPrecioVentaMayor;
        ImageButton btnEdit, btnDelete;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecioCosto = itemView.findViewById(R.id.tvPrecioCosto);
            tvPrecioVentaMenor = itemView.findViewById(R.id.tvPrecioVentaMenor);
            tvPrecioVentaMayor = itemView.findViewById(R.id.tvPrecioVentaMayor);
            btnEdit = itemView.findViewById(R.id.btnEditProducto);
            btnDelete = itemView.findViewById(R.id.btnDeleteProducto);
        }
    }
}
