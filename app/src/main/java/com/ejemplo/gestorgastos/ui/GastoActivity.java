package com.ejemplo.gestorgastos.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GastoActivity extends AppCompatActivity {

    private EditText etFecha;
    private EditText etCantidad;
    private EditText etPrecio;
    private EditText etDetalles;
    private GastoDAO gastoDAO;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        etFecha = findViewById(R.id.etFecha);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etDetalles = findViewById(R.id.etDetalles);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        // Hacer que el EditText de fecha abra el DatePicker y no el teclado
        etFecha.setFocusable(false);
        etFecha.setClickable(true);
        etFecha.setOnClickListener(v -> showDatePickerDialog());

        gastoDAO = new GastoDAO(this);
        gastoDAO.open();

        btnGuardar.setOnClickListener(v -> {
            Gasto gasto = new Gasto();
            try {
                gasto.setFecha(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(etFecha.getText().toString()));
            } catch (ParseException e) {
                gasto.setFecha(new Date());
            }
            gasto.setCantidad(Double.parseDouble(etCantidad.getText().toString()));
            gasto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            gasto.setDetalles(etDetalles.getText().toString());
            gastoDAO.insertGasto(gasto);
            finish(); // Cerrar actividad después de guardar
        });
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            etFecha.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gastoDAO.close();
    }
}
