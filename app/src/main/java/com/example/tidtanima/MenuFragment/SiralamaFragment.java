package com.example.tidtanima.MenuFragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tidtanima.Adapter.LigAdapter;
import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.Data.lig;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SiralamaFragment extends Fragment {

    LigAdapter ligAdapter;
    List<lig> ligs;
    ViewPager2 ligSiralama;
    String userId, ligID;

    String kullaniciL_ID; // Kullanıcının l_ID değerini tutacak değişken

    public SiralamaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ligs = new ArrayList<>();

        // Bundle'dan kullanıcının l_ID değerini alın
        if (getArguments() != null) {
            kullaniciL_ID = getArguments().getString("kullaniciL_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_siralama, container, false);

        ligSiralama = view.findViewById(R.id.view_pager);
        ligAdapter = new LigAdapter(ligs);

        ligSiralama.setAdapter(ligAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference kullaniciRef = db.collection("kullanici");
        kullaniciRef.document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    kullanici kullaniciBilgisi = documentSnapshot.toObject(kullanici.class);
                    Log.d(TAG, "Kullanıcı verileri alındı: " + kullaniciBilgisi.getK_ad());
                    ligID = kullaniciBilgisi.getL_ID();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Kullanıcı verileri alınamadı.", e);
            }
        });



        db.collection("lig").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        lig lig = documentSnapshot.toObject(com.example.tidtanima.Data.lig.class);
                        ligs.add(lig);
                    }
                    ligAdapter.notifyDataSetChanged();
                    scrollToCurrentLig(ligID);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting ligs", e);
            }
        });

        return view;
    }

    private void scrollToCurrentLig(String currentLig) {
        for (int i = 0; i < ligs.size(); i++) {
            if (ligs.get(i).getL_ID().equals(currentLig)) {
                // ViewPager2'de ilgili konuma geçiş yap
                ligSiralama.setCurrentItem(i, true);
                // Optionally, highlight the item (you'll need to implement this in the adapter)
                ligAdapter.highlightCurrentLig(i);
                break;
            }
        }
    }



}
