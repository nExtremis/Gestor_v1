package com.ejemplo.gestorgastos.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.model.Contacto;
import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private List<Contacto> contactoList;

    public ContactoAdapter(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = contactoList.get(position);
        holder.tvNombre.setText(contacto.getNombre());
        holder.tvTelefono.setText("Tel: " + contacto.getTelefono());
        holder.tvDireccion.setText("Dir: " + contacto.getDireccion());
    }

    @Override
    public int getItemCount() {
        return contactoList.size();
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvDireccion;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreContactoItem);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoContactoItem);
            tvDireccion = itemView.findViewById(R.id.tvDireccionContactoItem);
        }
    }
}
