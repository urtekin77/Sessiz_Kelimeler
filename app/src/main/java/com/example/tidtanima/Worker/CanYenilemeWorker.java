package com.example.tidtanima.Worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tidtanima.Data.kullanici;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CanYenilemeWorker extends Worker {
    private static final String TAG = "CanYenilemeWorker";
    private static final String USER_ID_KEY = "userId";

    public CanYenilemeWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        Log.d(TAG, "CanYenilemeWorker created");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork started");
        String userId = getInputData().getString(USER_ID_KEY);
        if (userId == null) {
            Log.e(TAG, "User ID is null");
            return Result.failure();
        }

        CountDownLatch latch = new CountDownLatch(1);
        final Result[] result = new Result[1];

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore instance obtained");

        firestore.collection("kullanici").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(TAG, "Document fetch successful");
                    if (documentSnapshot.exists()) {
                        kullanici kullanici = documentSnapshot.toObject(kullanici.class);
                        if (kullanici != null) {
                            Log.d(TAG, "User fetched: Current can: " + kullanici.getCan());
                            if (kullanici.getCan() < 5) {
                                kullanici.setCan(kullanici.getCan() + 1);
                                firestore.collection("kullanici").document(userId).set(kullanici)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Can updated successfully");
                                            if (kullanici.getCan() < 5) {
                                                scheduleNextWork(getApplicationContext(), userId);
                                            }
                                            result[0] = Result.success();
                                            latch.countDown();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to update can", e);
                                            result[0] = Result.failure();
                                            latch.countDown();
                                        });
                            } else {
                                Log.d(TAG, "No need to update can");
                                result[0] = Result.success();
                                latch.countDown();
                            }
                        } else {
                            Log.e(TAG, "User object is null");
                            result[0] = Result.failure();
                            latch.countDown();
                        }
                    } else {
                        Log.e(TAG, "Document does not exist");
                        result[0] = Result.failure();
                        latch.countDown();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch document", e);
                    result[0] = Result.failure();
                    latch.countDown();
                });

        try {
            latch.await();
            Log.d(TAG, "Firestore operation completed");
        } catch (InterruptedException e) {
            Log.e(TAG, "Latch await interrupted", e);
            return Result.failure();
        }

        return result[0];
    }

    public static void scheduleNextWork(Context context, String userId) {
        Log.d(TAG, "Scheduling next work for user: " + userId);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CanYenilemeWorker.class)
                .setInitialDelay(30, TimeUnit.MINUTES)
                .setInputData(new androidx.work.Data.Builder().putString(USER_ID_KEY, userId).build())
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                "CanYenilemeWork",
                ExistingWorkPolicy.REPLACE,
                workRequest
        );
        Log.d(TAG, "Next work scheduled");
    }
}
