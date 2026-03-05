package com.ejemplo.gestorgastos.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ejemplo.gestorgastos.dao.GastoDAO;
import com.ejemplo.gestorgastos.model.Gasto;
import com.ejemplo.gestorgastos.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GastoActivity extends AppCompatActivity {

    private EditText etFecha, etPrecio, etDetalles, etCuotas;
    private Spinner spMetodoPago;
    private CheckBox cbEsProducto;
    private GastoDAO gastoDAO;
    private final Calendar calendar = Calendar.getInstance();
    private int gastoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        etFecha = findViewById(R.id.etFecha);
        etPrecio = findViewById(R.id.etPrecio);
        etDetalles = findViewById(R.id.etDetalles);
        etCuotas = findViewById(R.id.etCuotas);
        spMetodoPago = findViewById(R.id.spMetodoPago);
        cbEsProducto = findViewById(R.id.cbEsProducto);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        // Añadimos "Préstamo" a las opciones
        String[] metodos = {"Efectivo", "Tarjeta", "Préstamo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMetodoPago.setAdapter(adapter);

        // Mostrar campo cuotas si es Tarjeta O Préstamo
        spMetodoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1 || position == 2) { // Tarjeta o Préstamo
                    etCuotas.setVisibility(View.VISIBLE);
                } else {
                    etCuotas.setVisibility(View.GONE);
                    etCuotas.setText("1");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etFecha.setFocusable(false);
        etFecha.setOnClickListener(v -> showDatePickerDialog());

        gastoDAO = new GastoDAO(this);
        gastoDAO.open();

        if (getIntent().hasExtra("ID")) {
            gastoId = getIntent().getIntExtra("ID", -1);
            etFecha.setText(getIntent().getStringExtra("FECHA"));
            etPrecio.setText(String.valueOf(getIntent().getDoubleExtra("PRECIO", 0.0)));
            etDetalles.setText(getIntent().getStringExtra("DETALLES"));
            cbEsProducto.setChecked(getIntent().getBooleanExtra("ES_PRODUCTO", false));
            etCuotas.setText(String.valueOf(getIntent().getIntExtra("CUOTAS", 1)));
            
            String mPago = getIntent().getStringExtra("METODO_PAGO");
            if ("Tarjeta".equals(mPago)) spMetodoPago.setSelection(1);
            else if ("Préstamo".equals(mPago)) spMetodoPago.setSelection(2);
            else spMetodoPago.setSelection(0);

            btnGuardar.setText("ACTUALIZAR GASTO");
        } else {
            etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        }

        btnGuardar.setOnClickListener(v -> guardarGasto());
    }

    private void guardarGasto() {
        try {
            Gasto gasto = new Gasto();
            gasto.setFecha(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(etFecha.getText().toString()));
            gasto.setMetodoPago(spMetodoPago.getSelectedItem().toString());
            
            int cuotas = 1;
            if (spMetodoPago.getSelectedItemPosition() > 0) { // Tarjeta o Préstamo
                String cStr = etCuotas.getText().toString();
                cuotas = cStr.isEmpty() ? 1 : Integer.parseInt(cStr);
            }
            gasto.setCuotas(cuotas);
            gasto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            gasto.setDetalles(etDetalles.getText().toString());
            gasto.setEsProducto(cbEsProducto.isChecked());

            if (gastoId == -1) {
                gastoDAO.insertGasto(gasto);
                Toast.makeText(this, "Gasto guardado", Toast.LENGTH_SHORT).show();
            } else {
                gasto.setId(gastoId);
                gastoDAO.updateGasto(gasto);
                Toast.makeText(this, "Gasto actualizado", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gastoDAO.close();
    }
}
