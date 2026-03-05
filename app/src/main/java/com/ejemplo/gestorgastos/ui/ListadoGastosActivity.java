package com.ejemplo.gestorgastos.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.ejemplo.gestorgastos.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ListadoGastosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_gastos);

        TabLayout tabLayout = findViewById(R.id.tabLayoutGastos);
        ViewPager2 viewPager = findViewById(R.id.viewPagerGastos);

        viewPager.setAdapter(new GastosPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("MENSUAL");
            else tab.setText("CUOTAS PENDIENTES");
        }).attach();
    }

    private static class GastosPagerAdapter extends FragmentStateAdapter {
        public GastosPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new GastosMensualFragment() : new GastosCuotasFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
