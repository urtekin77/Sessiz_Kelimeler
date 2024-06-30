package com.example.tidtanima.SozcukFragment;

import static com.example.tidtanima.R.id.elSpinner;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tidtanima.Adapter.ElAdapter;
import com.example.tidtanima.Adapter.KelimeViewAdapter;
import com.example.tidtanima.Adapter.YerAdapter;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.Data.yer;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.tidtanima.Data.el;

import java.util.ArrayList;
import java.util.List;

public class IsaretFragment extends Fragment {
    private Spinner elSpinner, yerSpinner;
    private FirebaseFirestore db;
    private Context context;
    ElAdapter adapter;
    YerAdapter yerAdapter;
    private List<el> elList = new ArrayList<>();
    private List<yer> yerList = new ArrayList<>();

    private el selectedEl;
    private yer selectedYer;
    private  String selectedYerId;
    private String selectedElId;

    private Button sorgulama;
    private RecyclerView sorgulamaRecyclerView;
    private KelimeViewAdapter kelimeViewAdapter;
    private List<isaret> isaretList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_isaret, container, false);

        db = FirebaseFirestore.getInstance();
        elSpinner = view.findViewById(R.id.elSpinner);
        yerSpinner = view.findViewById(R.id.yerSpinner);

        sorgulama = view.findViewById(R.id.sorgula);
        sorgulamaRecyclerView = view.findViewById(R.id.kelimeListesiIsaret);
        sorgulamaRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // kelimeViewAdapter'ı boş liste ile başlat
        kelimeViewAdapter = new KelimeViewAdapter(new ArrayList<>());
        sorgulamaRecyclerView.setAdapter(kelimeViewAdapter);

        // el verileri spinnera ekleniyor
        db.collection("el").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    el el = documentSnapshot.toObject(el.class);
                    elList.add(el);
                }
                // Adapter oluştur ve spinner'a set et
                adapter = new ElAdapter(requireContext(), elList);
                elSpinner.setAdapter(adapter);
            }
        });

        // el Spinner item seçimi dinleyicisi
        elSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEl = (el) parent.getItemAtPosition(position);
                if (selectedEl != null) {
                    selectedElId = selectedEl.getE_ID();
                    Log.d("Selected El ID", selectedElId);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // yer spinner işlemleri
        db.collection("yer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    yer yer = documentSnapshot.toObject(yer.class);
                    yerList.add(yer);
                }
                yerAdapter = new YerAdapter(requireContext(), yerList);
                yerSpinner.setAdapter(yerAdapter);

            }
        });

        // yer Spinner item seçimi dinleyicisi
        yerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYer = yerList.get(position);
                if (selectedYer != null) {
                    selectedYerId = selectedYer.getY_ID();
                    Log.d("Selected Yer ID", selectedYerId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sorgulama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEl != null && selectedYer != null) {
                    db.collection("isaret")
                            .whereEqualTo("E_ID", selectedElId)
                            .whereEqualTo("Y_ID", selectedYerId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    isaretList.clear();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        isaret isaret = documentSnapshot.toObject(isaret.class);
                                        isaretList.add(isaret);
                                    }
                                    // RecyclerView için adaptörü yeniden ayarla
                                    kelimeViewAdapter = new KelimeViewAdapter(isaretList);
                                    sorgulamaRecyclerView.setAdapter(kelimeViewAdapter);
                                    kelimeViewAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });

        return view;
    }
}
