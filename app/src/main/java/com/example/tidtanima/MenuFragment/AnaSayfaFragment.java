package com.example.tidtanima.MenuFragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tidtanima.Activity.QuizTekrarActivity;
import com.example.tidtanima.Adapter.UniteAdapter;
import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.Data.unite;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnaSayfaFragment extends Fragment {
    UniteAdapter uniteAdapter;
    List<unite> unites;
    List<isaret> isaretList;
    RecyclerView uniteSiralama;
    String userId;
    TextView canDurumu, puan;
    AppCompatImageButton tekrarlama;

    public AnaSayfaFragment() {
        // Gerekli boş kurucu
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unites = new ArrayList<>();
        isaretList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();

        puan = view.findViewById(R.id.puan);
        canDurumu = view.findViewById(R.id.canDurumu);

        tekrarlama = view.findViewById(R.id.tekrarlama);

        uniteSiralama = view.findViewById(R.id.uniteSiralama);
        if (uniteSiralama != null) {
            uniteSiralama.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            Log.e("kaemm", "uniteSiralama is null");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference kullaniciRef = db.collection("kullanici");
        kullaniciRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    kullanici kullaniciBilgisi = documentSnapshot.toObject(kullanici.class);
                    Log.d(TAG, "Kullanıcı verileri alındı: " + kullaniciBilgisi.getK_ad());
                    canDurumu.setText(String.valueOf(kullaniciBilgisi.getCan()));
                    puan.setText(String.valueOf(kullaniciBilgisi.getK_puan()));

                    boolean isUserAlive = kullaniciBilgisi.getCan() > 0;

                    // Kullanıcının mevcut adım ve birimini kontrol edin
                    String currentUnit = kullaniciBilgisi.getU_ID();
                    String currentStep = kullaniciBilgisi.getU_adim();

                    if (currentUnit != null && currentStep != null) {
                        loadUniteData(db, kullaniciBilgisi, isUserAlive, currentUnit, currentStep);
                    } else {
                        loadUniteData(db, kullaniciBilgisi, isUserAlive, null, null);
                    }
                } else {
                    Log.d(TAG, "Kullanıcı verisi bulunamadı.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Kullanıcı verileri alınamadı.", e);
            }
        });
        tekrarlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuizTekrarActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadUniteData(FirebaseFirestore db, kullanici currentUser, boolean isUserAlive, String currentUnit, String currentStep) {
        CollectionReference unitesRef = db.collection("unite");

        unitesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        unite unite = documentSnapshot.toObject(unite.class);
                        unites.add(unite);
                    }
                    loadIsaretData(db, currentUser, isUserAlive, currentUnit, currentStep);
                } else {
                    Log.d(TAG, "Veri bulunamadı.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Veri alınırken hata oluştu.", e);
            }
        });
    }

    private void loadIsaretData(FirebaseFirestore db, kullanici currentUser, boolean isUserAlive, String currentUnit, String currentStep) {
        CollectionReference isaretRef = db.collection("isaret");

        isaretRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        isaret isaret = documentSnapshot.toObject(isaret.class);
                        isaretList.add(isaret);
                    }

                    uniteAdapter = new UniteAdapter(unites, currentUser, isaretList);
                    uniteSiralama.setAdapter(uniteAdapter);

                    // Mevcut birim ve adıma kaydır
                    if (currentUnit != null && currentStep != null) {
                        scrollToCurrentStep(currentUnit, Integer.parseInt(currentStep));
                    }
                } else {
                    Log.d(TAG, "İşaret verisi bulunamadı.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "İşaret verisi alınırken hata oluştu.", e);
            }
        });
    }

    private void scrollToCurrentStep(String currentUnit, int currentStep) {
        for (int i = 0; i < unites.size(); i++) {
            if (unites.get(i).getU_ID().equals(currentUnit)) {
                // Scroll the RecyclerView to the current unit
                uniteSiralama.scrollToPosition(i);
                // Optionally, highlight the item (you'll need to implement this in the adapter)
                uniteAdapter.highlightCurrentStep(i, currentStep);
                break;
            }
        }
    }
}
