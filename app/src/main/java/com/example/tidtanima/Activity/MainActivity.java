package com.example.tidtanima.Activity;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;
import com.example.tidtanima.Worker.CanYenilemeWorker;
import com.example.tidtanima.MenuFragment.AnaSayfaFragment;
import com.example.tidtanima.MenuFragment.ProfilFragment;
import com.example.tidtanima.MenuFragment.SiralamaFragment;
import com.example.tidtanima.MenuFragment.SozlukFragment;
import com.example.tidtanima.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private String kullaniciID; // Kullanıcının ID'si
    private FirebaseFirestore db;
    private static final String LAST_LOGIN_DATE_KEY = "lastLoginDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth instance ve kullanıcı ID'si al
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            kullaniciID = user.getUid();
            db = FirebaseFirestore.getInstance();
        }


        // Sistem pencereleri için kenar boşluklarını ayarla
        View mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // İlk fragmentı yükle
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new AnaSayfaFragment())
                    .commit();
        }

        // BottomNavigationView'daki öğeler için listener ekle
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.anaSayfa) {
                selectedFragment = new AnaSayfaFragment();
            } else if (itemId == R.id.sozluk) {
                selectedFragment = new SozlukFragment();
            } else if (itemId == R.id.siralama) {
                selectedFragment = new SiralamaFragment();
            } else if (itemId == R.id.profil) {
                selectedFragment = new ProfilFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, selectedFragment)
                        .commit();
            }

            return true;
        });
    }


   
}
