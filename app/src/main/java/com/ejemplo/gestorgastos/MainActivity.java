package com.ejemplo.gestorgastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.ejemplo.gestorgastos.ui.AjustesActivity;
import com.ejemplo.gestorgastos.ui.FinanzasFragment;
import com.ejemplo.gestorgastos.ui.HistorialesFragment;
import com.ejemplo.gestorgastos.ui.RegistrosFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new MainViewPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("REGISTROS"); break;
                case 1: tab.setText("CONSULTAS"); break;
                case 2: tab.setText("FINANZAS"); break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, AjustesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class MainViewPagerAdapter extends FragmentStateAdapter {
        public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new RegistrosFragment();
                case 1: return new HistorialesFragment();
                case 2: return new FinanzasFragment();
                default: return new RegistrosFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Volvemos a 3 pestañas principales
        }
    }
}
