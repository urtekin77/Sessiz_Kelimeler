package com.example.tidtanima;

import android.app.Application;
import android.util.Log;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.tidtanima.Worker.CanYenilemeWorker;
import com.example.tidtanima.Worker.LigUpdateWorler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class TIDtanima extends Application {
    private static final String TAG = "TIDtanimaApp";
    private String kullaniciID; // Kullanıcının ID'si
    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application started");

        // Firebase authentication and Firestore instance initialization
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            kullaniciID = user.getUid();
            db = FirebaseFirestore.getInstance();
            Log.d(TAG, "Firebase user logged in: " + kullaniciID);
        } else {
            Log.d(TAG, "No Firebase user logged in");
        }

        // Set work constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        // Create and schedule a daily work request for league updates
        PeriodicWorkRequest leagueUpdateRequest = new PeriodicWorkRequest.Builder(LigUpdateWorler.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "LigUpdateWorler",
                ExistingPeriodicWorkPolicy.REPLACE,
                leagueUpdateRequest
        );

        Log.d(TAG, "League update work scheduled");

        // Schedule next work for health recovery
        if (kullaniciID != null) {
            CanYenilemeWorker.scheduleNextWork(this, kullaniciID);
            Log.d(TAG, "Health recovery work scheduled for user: " + kullaniciID);
        } else {
            Log.d(TAG, "Health recovery work not scheduled due to no user ID");
        }
    }
}
