package com.example.tidtanima.Worker;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tidtanima.Data.kullanici;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LigUpdateWorler extends Worker {
    private static final String TAG = "LigUpdateWorler";

    public LigUpdateWorler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.d(TAG, "LigUpdateWorker initialized");
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork started - checking if it's the last day of the month");
        Calendar calendar = Calendar.getInstance();
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDay == lastDay) {
            Log.d(TAG, "It's the last day of the month - updating leagues");
            updateLeagues();
            return Result.success();
        } else {
            Log.d(TAG, "Not the last day of the month - no league update needed");
            return Result.failure();
        }
    }

    public void updateLeagues() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("kullanici");
        Log.d(TAG, "Fetching users for league update");

        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<kullanici> users = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    kullanici user = document.toObject(kullanici.class);
                    users.add(user);
                }

                Log.d(TAG, "Users fetched successfully - sorting users by points");
                Collections.sort(users, new Comparator<kullanici>() {
                    @Override
                    public int compare(kullanici u1, kullanici u2) {
                        return Integer.compare(u2.getL_puan(), u1.getL_puan());
                    }
                });

                // Update league rankings and reset points
                int topCount = Math.min(3, users.size());
                for (int i = 0; i < topCount; i++) {
                    kullanici user = users.get(i);
                    user.setL_ID(YuksekLig(user.getL_ID()));
                    user.setL_puan((int) (user.getK_puan()*0.25));
                    usersRef.document(user.getK_ID()).set(user);
                    if (user.getM_ID() == null) {
                        user.setM_ID(new ArrayList<>());
                    }
                    user.getM_ID().add(getMedalForPosition(i + 1));
                }

                int bottomCount = Math.min(3, users.size());
                for (int i = users.size() - 1; i >= users.size() - bottomCount; i--) {
                    kullanici user = users.get(i);
                    user.setL_ID(DusukLig(user.getL_ID()));
                    user.setL_puan((int) (user.getK_puan()*0.25));
                    usersRef.document(user.getK_ID()).set(user);
                }

                for (int i = topCount; i < users.size() - bottomCount; i++) {
                    kullanici user = users.get(i);
                    user.setL_puan((int) (user.getK_puan()*0.25));
                    usersRef.document(user.getK_ID()).set(user);
                }

                Log.d(TAG, "Leagues updated successfully");
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
        });
    }
    private String YuksekLig(String currentLeague) {
        switch (currentLeague) {
            case "8EfsaneLigi":
                return "8EfsaneLigi";
            case "7SampiyonLigi":
                return "8EfsaneLigi";
            case "6MasterLigi":
                return "7SampiyonLigi";
            case "5ElmasLigi" :
                return "6MasterLigi";
            case "4AltinLigi" :
                return "5ElmasLigi";
            case "3GumusLigi" :
                return "4AltinLigi";
            case "2bronzLigi" :
                return "3GumusLigi";
            case "1baslangicSeviyesi" :
                return "2bronzLigi";
            default:
                return currentLeague;
        }
    }

    private String DusukLig(String currentLeague) {
        switch (currentLeague) {
            case "1baslangicSeviyesi":
                return "1baslangicSeviyesi";
            case "2bronzLigi":
                return "1baslangicSeviyesi";
            case "3GumusLigi":
                return "2bronzLigi";
            case "4AltinLigi":
                return "3GumusLigi";
            case "5ElmasLigi":
                return "4AltinLigi";
            case "6MasterLigi":
                return "5ElmasLigi";
            case "7SampiyonLigi":
                return "6MasterLigi";
            case "8EfsaneLigi":
                return "7SampiyonLigi";
            default:
                return currentLeague;
        }
    }
    private String getMedalForPosition(int position) {
        switch (position) {
            case 1:
                return "1_altin";
            case 2:
                return "2Gumus";
            case 3:
                return "3Bronz";
            default:
                return "";
        }
    }
}
