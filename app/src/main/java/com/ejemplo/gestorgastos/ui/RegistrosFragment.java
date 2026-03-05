package com.ejemplo.gestorgastos.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ejemplo.gestorgastos.R;

public class RegistrosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registros, container, false);

        view.findViewById(R.id.btnGasto).setOnClickListener(v -> startActivity(new Intent(getContext(), GastoActivity.class)));
        view.findViewById(R.id.btnVenta).setOnClickListener(v -> startActivity(new Intent(getContext(), VentaActivity.class)));
        view.findViewById(R.id.btnProducto).setOnClickListener(v -> startActivity(new Intent(getContext(), ProductoActivity.class)));
        view.findViewById(R.id.btnContacto).setOnClickListener(v -> startActivity(new Intent(getContext(), ContactoActivity.class)));

        return view;
    }
}
