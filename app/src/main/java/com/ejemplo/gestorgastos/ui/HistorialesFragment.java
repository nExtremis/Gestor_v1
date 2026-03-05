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

public class HistorialesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historiales, container, false);

        view.findViewById(R.id.btnVerGastos).setOnClickListener(v -> startActivity(new Intent(getContext(), ListadoGastosActivity.class)));
        view.findViewById(R.id.btnVerVentas).setOnClickListener(v -> startActivity(new Intent(getContext(), ListadoVentasActivity.class)));
        view.findViewById(R.id.btnVerProductos).setOnClickListener(v -> startActivity(new Intent(getContext(), ListadoProductosActivity.class)));
        view.findViewById(R.id.btnVerContactos).setOnClickListener(v -> startActivity(new Intent(getContext(), ListadoContactosActivity.class)));

        return view;
    }
}
