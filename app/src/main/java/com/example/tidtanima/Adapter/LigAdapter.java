package com.example.tidtanima.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.Data.lig;
import com.example.tidtanima.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LigAdapter extends RecyclerView.Adapter<LigAdapter.LigViewHolder> {
    private List<lig> ligListe;
    private FirebaseFirestore db;
    private int highlightedPosition = -1;
    private int highlightedStep = -1;
    public LigAdapter(List<lig> ligListe) {
        this.ligListe = ligListe;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public LigAdapter.LigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kupa, parent, false);
        return new LigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LigAdapter.LigViewHolder holder, int position) {
        lig lig = ligListe.get(position);
        holder.adView.setText(lig.getL_Ad());
        String ligID = lig.getL_ID();
        Picasso.get()
                .load(lig.getL_Resim())
                .placeholder(R.drawable.install)
                .error(R.drawable.error)
                .into(holder.ligImage);

        db.collection("kullanici")
                .whereEqualTo("l_ID", ligID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<kullanici> matchingUsers = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            kullanici user = document.toObject(kullanici.class);
                            if (ligID != null && ligID.equals(user.getL_ID())) {
                                matchingUsers.add(user);
                            }
                        }
                        // l_puan değerine göre büyükten küçüğe sıralama yap
                        matchingUsers.sort((u1, u2) -> Integer.compare(u2.getL_puan(), u1.getL_puan()));

                        SiralamaLig siralamaLigAdapter = new SiralamaLig(matchingUsers);
                        holder.siralamaRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                        holder.siralamaRv.setAdapter(siralamaLigAdapter);
                    } else {
                        Log.w("hataaa", "Error getting documents.", task.getException());
                    }
                });

    }
    public void highlightCurrentLig(int position) {
        highlightedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ligListe.size();
    }

    public static class LigViewHolder extends RecyclerView.ViewHolder {
        ImageView ligImage;
        TextView adView;
        RecyclerView siralamaRv;

        public LigViewHolder(@NonNull View itemView) {
            super(itemView);
            ligImage = itemView.findViewById(R.id.imageView);
            adView = itemView.findViewById(R.id.adView);
            siralamaRv = itemView.findViewById(R.id.siralamaLig);
        }
    }
}
