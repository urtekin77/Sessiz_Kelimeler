package com.example.tidtanima.MenuFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.tidtanima.SozcukFragment.AlfabetikFragment;
import com.example.tidtanima.SozcukFragment.IsaretFragment;
import com.example.tidtanima.SozcukFragment.AramaFragment;

import com.example.tidtanima.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class  SozlukFragment extends Fragment {

    BottomNavigationView sozlukMenu;

    public SozlukFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sozluk,container,false);

        sozlukMenu = view.findViewById(R.id.sozlukMenu);

        sozlukMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.sozcuk) {
                    Log.d("SozlukFragment", "AramaFragment seçildi");
                    selectedFragment = new AramaFragment();
                } else if (item.getItemId() == R.id.isaret) {
                    Log.d("SozlukFragment", "IsaretFragment seçildi");
                    selectedFragment = new IsaretFragment();
                } else if (item.getItemId() == R.id.alfabetik) {
                    Log.d("SozlukFragment", "AlfabetikFragment seçildi");
                    selectedFragment = new AlfabetikFragment();
                }

                if (selectedFragment != null) {
                    getChildFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.fragmentSozluk, selectedFragment)
                            .commit();
                }
                return true;
            }
        });


        // İlk açılışta varsayılan olarak SozcukFragment'ı yükle
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentSozluk, new AramaFragment()).commit();
        return view;
    }
}