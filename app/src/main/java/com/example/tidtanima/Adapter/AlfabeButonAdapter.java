package com.example.tidtanima.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tidtanima.Data.alfabe;
import com.example.tidtanima.R;

import java.util.List;

public class AlfabeButonAdapter extends RecyclerView.Adapter<AlfabeButonAdapter.AlfabeViewHolder> {

    private List<alfabe> alfabeList;
    private OnAlfabeClickListener listener;

    public AlfabeButonAdapter(List<alfabe> alfabeList, OnAlfabeClickListener listener){
        this.alfabeList = alfabeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlfabeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alfabe_buton_view, parent, false);
        return new AlfabeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlfabeViewHolder holder, int position) {
        alfabe alfabe = alfabeList.get(position);
        holder.ad.setText(alfabe.getA_ad());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAlfabeClick(alfabe.getA_ID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alfabeList.size();
    }

    public interface OnAlfabeClickListener {
        void onAlfabeClick(String A_ID);
    }

    public class AlfabeViewHolder extends RecyclerView.ViewHolder {
        TextView ad;

        public AlfabeViewHolder(@NonNull View itemView) {
            super(itemView);
            ad = itemView.findViewById(R.id.Name);
        }
    }
}
