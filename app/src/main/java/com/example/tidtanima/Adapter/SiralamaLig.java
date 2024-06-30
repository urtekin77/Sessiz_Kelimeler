package com.example.tidtanima.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.R;

import java.util.List;

public class SiralamaLig extends RecyclerView.Adapter<SiralamaLig.UserViewHolder> {
    List<kullanici> userList;

    public SiralamaLig(List<kullanici> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public SiralamaLig.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kullanici_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiralamaLig.UserViewHolder holder, int position) {
        kullanici user = userList.get(position);
        holder.sira.setText(String.valueOf(position + 1));
        holder.userName.setText(user.getK_ad());
        holder.userPuan.setText(String.valueOf(user.getL_puan()));

        // Add trophy icons for the first three positions
        if (position == 0) {
            holder.trophyImage.setImageResource(R.drawable.birincilik);
        } else if (position == 1) {
            holder.trophyImage.setImageResource(R.drawable.ikincilik);
        } else if (position == 2) {
            holder.trophyImage.setImageResource(R.drawable.ucunculuk);
        } else {
            holder.trophyImage.setImageDrawable(null);
        }

        // Add upper league icon for the first three positions
        if (position < 3) {
            holder.leagueImage.setImageResource(R.drawable.ust_lig);
        } else {
            holder.leagueImage.setImageDrawable(null);
        }

        // Add lower league icon for the last three positions
        if (position >= userList.size() - 3) {
            holder.leagueImage.setImageResource(R.drawable.alt_lig);
        } else if (position >= 3) {
            holder.leagueImage.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView sira;
        TextView userName;
        TextView userPuan;
        ImageView trophyImage;
        ImageView leagueImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            sira = itemView.findViewById(R.id.sira);
            userName = itemView.findViewById(R.id.userName);
            userPuan = itemView.findViewById(R.id.userPuan);
            trophyImage = itemView.findViewById(R.id.trophyImage);
            leagueImage = itemView.findViewById(R.id.leagueImage);
        }
    }
}
