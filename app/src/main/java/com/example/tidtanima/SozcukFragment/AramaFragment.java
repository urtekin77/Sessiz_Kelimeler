package com.example.tidtanima.SozcukFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tidtanima.Adapter.KelimeViewAdapter;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AramaFragment extends Fragment {

    private RecyclerView aramaRecyclerView;
    private KelimeViewAdapter kelimeViewAdapter;
    private List<isaret> isaretList = new ArrayList<>();

    EditText aramaText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_arama, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference isaretRef = db.collection("isaret");

        aramaRecyclerView = view.findViewById(R.id.kelimeListesiArama);
        aramaRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false));

        kelimeViewAdapter = new KelimeViewAdapter(isaretList);
        aramaRecyclerView.setAdapter(kelimeViewAdapter);
        aramaText = view.findViewById(R.id.editSearch);

        aramaText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sorgu = s.toString().toLowerCase();
                if (!sorgu.isEmpty()) {
                    sorgu = Character.toUpperCase(sorgu.charAt(0)) + sorgu.substring(1); // İlk harfi büyük harfe dönüştür
                }
                if (sorgu.isEmpty()) {
                    isaretList.clear();
                    kelimeViewAdapter.notifyDataSetChanged();
                } else {
                    isaretRef.orderBy("I_ad")
                            .startAt(sorgu)
                            .endAt(sorgu + "\uf8ff")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    isaretList.clear();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        isaret isaret = documentSnapshot.toObject(isaret.class);
                                        isaretList.add(isaret);
                                    }
                                    kelimeViewAdapter.notifyDataSetChanged();
                                }
                            });
                }






            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}