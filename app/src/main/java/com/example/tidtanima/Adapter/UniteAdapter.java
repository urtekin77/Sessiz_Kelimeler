package com.example.tidtanima.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tidtanima.Activity.QuizActivity;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.Data.unite;
import com.example.tidtanima.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UniteAdapter extends RecyclerView.Adapter<UniteAdapter.UniteViewHolder> {

    List<unite> uniteListe;
    private kullanici currentUser;
    private List<isaret> isaretList;
    private int highlightedPosition = -1;
    private int highlightedStep = -1;

    public UniteAdapter(List<unite> uniteListe, kullanici currentUser, List<isaret> isaretList) {
        this.uniteListe = uniteListe;
        this.currentUser = currentUser;
        this.isaretList = isaretList;
    }

    @NonNull
    @Override
    public UniteAdapter.UniteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unite_rv, parent, false);
        return new UniteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniteAdapter.UniteViewHolder holder, int position) {
        unite unite = uniteListe.get(position);
        holder.U_ID = unite.getU_ID();
        holder.uniteAd.setText(unite.getU_Ad());
        holder.uniteIcerik.setText(unite.getU_Icerik());

        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.note_dialog, null);
                RecyclerView recyclerView = dialogView.findViewById(R.id.isaretler);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                Button noteKapat = dialogView.findViewById(R.id.noteKapat);
                List<isaret> filteredIsarets = new ArrayList<>();
                for (isaret isaret : isaretList) {
                    if (isaret.getU_ID().equals(holder.U_ID)) {
                        filteredIsarets.add(isaret);
                    }
                }

                KelimeViewAdapter adapter = new KelimeViewAdapter(filteredIsarets);
                recyclerView.setAdapter(adapter);

                AlertDialog alertDialog = builder.setView(dialogView)
                        .create();

                noteKapat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        Picasso.get()
                .load(unite.getU_background())
                .placeholder(R.drawable.sign_language)
                .error(R.drawable.error)
                .into(holder.uniteBackground);

        String currentUserUnitID = currentUser.getU_ID();
        String currentUserStep = currentUser.getU_adim();

        if (currentUserUnitID != null && currentUserStep != null) {
            int userCurrentUnite = Integer.parseInt(currentUserUnitID.replace("unite", ""));
            int userStep = Integer.parseInt(currentUserStep);

            int currentUnite = Integer.parseInt(unite.getU_ID().replace("unite", ""));

            for (int i = 1; i <= 9; i++) {
                Button button = holder.getButtonByNumber(i);
                if (currentUnite < userCurrentUnite || (currentUnite == userCurrentUnite && i <= userStep)) {
                    button.setEnabled(false);
                    button.setBackgroundResource(R.drawable.button_green);
                } else if (currentUnite == userCurrentUnite && i == userStep + 1) {
                    button.setEnabled(true);
                    button.setBackgroundResource(R.drawable.button_red);
                    int finalI = i;
                    button.setOnClickListener(v -> {
                        if (currentUser.getCan() == 0) {
                            Toast.makeText(v.getContext(), "Canınız bitti!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(v.getContext(), QuizActivity.class);
                            intent.putExtra("U_ID", holder.U_ID);
                            intent.putExtra("STEP", finalI);
                            intent.putExtra("USER_ID", currentUser.getK_ID());
                            v.getContext().startActivity(intent);
                        }
                    });
                } else {
                    button.setEnabled(false);
                    button.setBackgroundResource(R.drawable.button_gray);
                }
            }

            // Genel tekrar butonu sadece 9. adımı bitirilen ünitelerde aktif
            if (currentUnite < userCurrentUnite || (currentUnite == userCurrentUnite && userStep >= 9)) {
                holder.genelTekrar.setEnabled(true);
                holder.genelTekrar.setBackgroundResource(R.drawable.buton);
                holder.genelTekrar.setOnClickListener(v -> {
                    if (currentUser.getCan() == 0) {
                        Toast.makeText(v.getContext(), "Canınız bitti!", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(v.getContext(), QuizActivity.class);
                        intent.putExtra("U_ID", holder.U_ID);
                        intent.putExtra("STEP", 10);
                        intent.putExtra("USER_ID", currentUser.getK_ID());
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                holder.genelTekrar.setEnabled(false);
                holder.genelTekrar.setBackgroundResource(R.drawable.button_gray);
            }
        }


    }

    @Override
    public int getItemCount() {
        return uniteListe.size();
    }

    public void highlightCurrentStep(int position, int step) {
        highlightedPosition = position;
        highlightedStep = step;
        notifyDataSetChanged();
    }

    public static class UniteViewHolder extends RecyclerView.ViewHolder {
        TextView uniteAd, uniteIcerik;
        String U_ID;
        ImageView uniteBackground, note;
        Button button1, button2, button3, button4, button5, button6, button7, button8, button9, genelTekrar;

        public UniteViewHolder(@NonNull View itemView) {
            super(itemView);
            uniteAd = itemView.findViewById(R.id.uniteAD);
            uniteIcerik = itemView.findViewById(R.id.uniteIcerik);
            note = itemView.findViewById(R.id.note);
            uniteBackground = itemView.findViewById(R.id.background);
            button1 = itemView.findViewById(R.id.button1);
            button2 = itemView.findViewById(R.id.button2);
            button3 = itemView.findViewById(R.id.button3);
            button4 = itemView.findViewById(R.id.button4);
            button5 = itemView.findViewById(R.id.button5);
            button6 = itemView.findViewById(R.id.button6);
            button7 = itemView.findViewById(R.id.button7);
            button8 = itemView.findViewById(R.id.button8);
            button9 = itemView.findViewById(R.id.button9);
            genelTekrar = itemView.findViewById(R.id.buttonTekrar);
        }

        public Button getButtonByNumber(int number) {
            switch (number) {
                case 1:
                    return button1;
                case 2:
                    return button2;
                case 3:
                    return button3;
                case 4:
                    return button4;
                case 5:
                    return button5;
                case 6:
                    return button6;
                case 7:
                    return button7;
                case 8:
                    return button8;
                case 9:
                    return button9;
                case 10:
                    return genelTekrar;
                default:
                    return null;
            }
        }
    }
}
