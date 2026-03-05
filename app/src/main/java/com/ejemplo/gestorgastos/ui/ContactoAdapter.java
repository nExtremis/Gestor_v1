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

        // Acción de WhatsApp
        holder.btnWhatsapp.setOnClickListener(v -> {
            String telefono = contacto.getTelefono();
            if (telefono == null || telefono.isEmpty()) {
                Toast.makeText(context, "El contacto no tiene teléfono", Toast.LENGTH_SHORT).show();
                return;
            }

            // Limpiar el número: solo dejar dígitos
            String soloNumeros = telefono.replaceAll("[^0-9]", "");
            
            // Si el número no tiene código de país, podrías añadir el de tu país por defecto.
            // Ejemplo para Argentina (+54): if (!soloNumeros.startsWith("54")) soloNumeros = "54" + soloNumeros;

            try {
                String url = "https://wa.me/" + soloNumeros;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            } catch (Exception e) {
                Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show();
            }
        });

        // Acción de Editar
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactoActivity.class);
            intent.putExtra("ID", contacto.getId());
            intent.putExtra("NOMBRE", contacto.getNombre());
            intent.putExtra("TELEFONO", contacto.getTelefono());
            intent.putExtra("DIRECCION", contacto.getDireccion());
            context.startActivity(intent);
        });

        // Acción de Eliminar con Confirmación
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("Eliminar Contacto")
                .setMessage("¿Estás seguro de que quieres eliminar a " + contacto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    contactoDAO.deleteContacto(contacto.getId());
                    contactoList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, contactoList.size());
                    Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show();
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
