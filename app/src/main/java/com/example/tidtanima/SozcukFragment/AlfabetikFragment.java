package com.example.tidtanima.SozcukFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tidtanima.Adapter.AlfabeButonAdapter;
import com.example.tidtanima.Adapter.KelimeViewAdapter;
import com.example.tidtanima.Data.alfabe;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AlfabetikFragment extends Fragment {

    RecyclerView alfabeButon, kelimeler;
    List<alfabe> alfabes;
    List<isaret> isarets;
    AlfabeButonAdapter alfabeButonAdapter;
    KelimeViewAdapter kelimeViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alfabetik, container, false);

        alfabes = new ArrayList<>();
        isarets = new ArrayList<>();

        alfabeButon = view.findViewById(R.id.alfabe);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3, LinearLayoutManager.HORIZONTAL, false);
        alfabeButon.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference alfabeRef = db.collection("alfabe");
        alfabeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        alfabe alfabe = documentSnapshot.toObject(com.example.tidtanima.Data.alfabe.class);
                        alfabes.add(alfabe);
                    }
                    alfabeButonAdapter = new AlfabeButonAdapter(alfabes, new AlfabeButonAdapter.OnAlfabeClickListener() {
                        @Override
                        public void onAlfabeClick(String A_ID) {
                            isarets.clear();
                            CollectionReference isaretRef = db.collection("isaret");
                            isaretRef.whereEqualTo("A_ID", A_ID)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                isaret isaret = documentSnapshot.toObject(com.example.tidtanima.Data.isaret.class);
                                                isarets.add(isaret);
                                            }
                                            kelimeViewAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firestore", "Error getting documents: ", e);
                                        }
                                    });
                        }
                    });
                    alfabeButon.setAdapter(alfabeButonAdapter);

                } else {
                    Log.d("Firestore", "No data found.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: ", e);
            }
        });

        kelimeler = view.findViewById(R.id.kelimeListesi);
        kelimeler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        kelimeViewAdapter = new KelimeViewAdapter(isarets);
        kelimeler.setAdapter(kelimeViewAdapter);

        return view;
    }
}
