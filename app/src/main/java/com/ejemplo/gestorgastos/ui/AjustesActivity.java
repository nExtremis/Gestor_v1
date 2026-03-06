package com.ejemplo.gestorgastos.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.ejemplo.gestorgastos.R;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.dao.VentaDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.model.Venta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AjustesActivity extends AppCompatActivity {

    private GastoDAO gastoDAO;
    private VentaDAO ventaDAO;
    private SharedPreferences prefs;
    private EditText etPrefijo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ajustes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Configuración");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prefs = getSharedPreferences("ConfigNegocio", MODE_PRIVATE);
        gastoDAO = new GastoDAO(this);
        ventaDAO = new VentaDAO(this);

        etPrefijo = findViewById(R.id.etPrefijoWsp);
        Button btnGuardarPrefijo = findViewById(R.id.btnGuardarPrefijo);
        Button btnExportarCSV = findViewById(R.id.btnExportarCSV);
        Button btnBackup = findViewById(R.id.btnBackupDB);
        Button btnBorrar = findViewById(R.id.btnBorrarTodo);

        // Cargar prefijo guardado
        etPrefijo.setText(prefs.getString("prefijo_wsp", "549"));

        btnGuardarPrefijo.setOnClickListener(v -> {
            prefs.edit().putString("prefijo_wsp", etPrefijo.getText().toString()).apply();
            Toast.makeText(this, "Prefijo guardado", Toast.LENGTH_SHORT).show();
        });

        btnExportarCSV.setOnClickListener(v -> exportarDatos());
        
        btnBackup.setOnClickListener(v -> realizarBackup());

        btnBorrar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("¡ATENCIÓN!")
                .setMessage("¿Estás seguro de borrar TODA la base de datos? Esta acción no se puede deshacer.")
                .setPositiveButton("SÍ, BORRAR", (dialog, which) -> {
                    deleteDatabase("gestorgastos.db");
                    Toast.makeText(this, "Base de datos borrada. Reinicia la app.", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("CANCELAR", null)
                .show();
        });
    }

    private void realizarBackup() {
        try {
            File dbFile = getDatabasePath("gestorgastos.db");
            if (dbFile.exists()) {
                File backupFile = new File(getCacheDir(), "respaldo_gestor.db");
                
                // Copiar archivo DB a cache
                FileInputStream fis = new FileInputStream(dbFile);
                FileOutputStream fos = new FileOutputStream(backupFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) fos.write(buffer, 0, length);
                fos.flush(); fos.close(); fis.close();

                Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", backupFile);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/octet-stream");
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Guardar copia de seguridad..."));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al crear backup", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportarDatos() {
        gastoDAO.open();
        ventaDAO.open();

        List<Gasto> gastos = gastoDAO.getAllGastos();
        List<Venta> ventas = ventaDAO.getAllVentas();

        StringBuilder csvData = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        csvData.append("--- GASTOS ---\n");
        csvData.append("Fecha,MetodoPago,Cuotas,Precio,Detalles,EsProducto\n");
        for (Gasto g : gastos) {
            csvData.append(sdf.format(g.getFecha())).append(",")
                   .append(g.getMetodoPago()).append(",")
                   .append(g.getCuotas()).append(",")
                   .append(g.getPrecio()).append(",")
                   .append(g.getDetalles()).append(",")
                   .append(g.isEsProducto() ? "SI" : "NO").append("\n");
        }

        csvData.append("\n--- VENTAS ---\n");
        csvData.append("Fecha,Producto,Cliente,Cantidad,PrecioUnit,Total,F,MetodoPago\n");
        for (Venta v : ventas) {
            csvData.append(sdf.format(v.getFecha())).append(",")
                   .append(v.getNombreProducto()).append(",")
                   .append(v.getNombreContacto() != null ? v.getNombreContacto() : "N/A").append(",")
                   .append(v.getCantidad()).append(",")
                   .append(v.getPrecio()).append(",")
                   .append(v.getCantidad() * v.getPrecio()).append(",")
                   .append(v.isF() ? "SI" : "NO").append(",")
                   .append(v.getTipoPago()).append("\n");
        }

        try {
            File file = new File(getCacheDir(), "reporte_negocio.csv");
            FileOutputStream out = new FileOutputStream(file);
            out.write(csvData.toString().getBytes());
            out.close();

            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Enviar reporte CSV..."));
        } catch (Exception e) {
            Toast.makeText(this, "Error al exportar CSV", Toast.LENGTH_SHORT).show();
        }

        gastoDAO.close();
        ventaDAO.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
