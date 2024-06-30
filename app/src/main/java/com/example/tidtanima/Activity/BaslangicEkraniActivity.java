package com.example.tidtanima.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tidtanima.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaslangicEkraniActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_baslangic_ekrani);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    startActivity(new Intent(BaslangicEkraniActivity.this, MainActivity.class));

                }else {
                    startActivity(new Intent(BaslangicEkraniActivity.this, UyeGirisiActivity.class));
                }
                finish();

            }
        }, 1000);
    }
}