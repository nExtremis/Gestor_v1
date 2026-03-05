package com.ejemplo.gestorgastos.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.ContactoDAO;
import com.ejemplo.gestorgastos.model.Contacto;
import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private List<Contacto> contactoList;
    private Context context;
    private ContactoDAO contactoDAO;

    public ContactoAdapter(List<Contacto> contactoList, Context context) {
        this.contactoList = contactoList;
        this.context = context;
        this.contactoDAO = new ContactoDAO(context);
        this.contactoDAO.open();
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

        holder.btnWhatsapp.setOnClickListener(v -> {
            String telefono = contacto.getTelefono();
            if (telefono == null || telefono.isEmpty()) {
                Toast.makeText(context, "El contacto no tiene teléfono", Toast.LENGTH_SHORT).show();
                return;
            }

            // Limpiar: dejar solo dígitos
            String soloNumeros = telefono.replaceAll("[^0-9]", "");
            
            // Lógica automática para Argentina (54 + 9 + prefijo + numero)
            // Si tiene 10 dígitos (ej 11 2233 4455), le agregamos 549
            if (soloNumeros.length() == 10) {
                soloNumeros = "549" + soloNumeros;
            } 
            // Si tiene 11 dígitos y empieza con 15 (ej 11 15 2233 4455), corregimos a formato internacional
            else if (soloNumeros.length() == 11 && soloNumeros.startsWith("15")) {
                 // Esto es más complejo, lo ideal es agendar sin el 15. 
                 // Por ahora, priorizamos el formato de 10 dígitos + prefijo.
            }

            try {
                String url = "https://wa.me/" + soloNumeros;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            } catch (Exception e) {
                Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactoActivity.class);
            intent.putExtra("ID", contacto.getId());
            intent.putExtra("NOMBRE", contacto.getNombre());
            intent.putExtra("TELEFONO", contacto.getTelefono());
            intent.putExtra("DIRECCION", contacto.getDireccion());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Eliminar Contacto")
                .setMessage("¿Estás seguro de que quieres eliminar a " + contacto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    contactoDAO.deleteContacto(contacto.getId());
                    contactoList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, contactoList.size());
                })
                .setNegativeButton("No", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return contactoList.size();
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvDireccion;
        ImageButton btnEdit, btnDelete, btnWhatsapp;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreContactoItem);
            tvTelefono = itemView.findViewById(R.id.tvTelefonoContactoItem);
            tvDireccion = itemView.findViewById(R.id.tvDireccionContactoItem);
            btnEdit = itemView.findViewById(R.id.btnEditContacto);
            btnDelete = itemView.findViewById(R.id.btnDeleteContacto);
            btnWhatsapp = itemView.findViewById(R.id.btnWhatsappContacto);
        }
    }
}
