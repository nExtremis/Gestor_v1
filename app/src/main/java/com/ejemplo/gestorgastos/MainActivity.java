package com.ejemplo.gestorgastos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.ejemplo.gestorgastos.ui.HistorialesFragment;
import com.ejemplo.gestorgastos.ui.RegistrosFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new MainViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("REGISTROS");
            } else {
                tab.setText("CONSULTAS"); // Cambiado de HISTORIALES a CONSULTAS
            }
        }).attach();
    }

    private static class MainViewPagerAdapter extends FragmentStateAdapter {
        public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new RegistrosFragment() : new HistorialesFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
