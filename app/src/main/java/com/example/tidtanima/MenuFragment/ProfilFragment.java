package com.example.tidtanima.MenuFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.Data.lig;
import com.example.tidtanima.Data.mesajlar;
import com.example.tidtanima.R;
import com.example.tidtanima.Activity.UyeGirisiActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;


public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }
    LinearLayout editLayout, detayEdit, massageLayout, massageCard,
            deleteCard, deleteLayout, detayHakkinda, hakkinda;
    TextView logout, kullaniciAdi, kullaniciMail, puan, madalya1, madalya2, madalya3, ligAdi, ligPuan;
    FirebaseFirestore firestore;
    EditText edit_username, edit_password, sendMessage;
    Button btnUpdateUsername, btnUpdatePassword, btnSendMessage;

    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        ArrayList<kullanici> kullanicilar = new ArrayList<>();
    }

    // profil bilgisi güncelleme
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        kullaniciAdi = view.findViewById(R.id.kullaniciAdi);
        kullaniciMail = view.findViewById(R.id.kullaniciMail);
        ligAdi = view.findViewById(R.id.ligADi);
        ligPuan = view.findViewById(R.id.ligPuan);

        puan = view.findViewById(R.id.puanDurumu);

        madalya1 = view.findViewById(R.id.madalya1);
        madalya2 = view.findViewById(R.id.madalya2);
        madalya3 = view.findViewById(R.id.madalya3);

        detayEdit = view.findViewById(R.id.detayEdit);
        editLayout = view.findViewById(R.id.editLayout);
        edit_username = view.findViewById(R.id.edit_username);
        btnUpdateUsername = view.findViewById(R.id.btnUpdateUN);
        edit_password = view.findViewById(R.id.edit_password);
        btnUpdatePassword = view.findViewById(R.id.btnUpdate2);



        massageLayout = view.findViewById(R.id.MessageLayout);
        massageCard = view.findViewById(R.id.massageCard);
        sendMessage = view.findViewById(R.id.editSendMessage);
        btnSendMessage = view.findViewById(R.id.sendMessageBtn);

        deleteCard = view.findViewById(R.id.deleteCard);
        deleteLayout = view.findViewById(R.id.deleteLayout);
        detayHakkinda = view.findViewById(R.id.HakkindaLayout);
        hakkinda = view.findViewById(R.id.uygulama);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();


        // kullanıcı bilgileri
        if(user != null){

            firestore.collection("kullanici").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        showToast("Failed to fetch user data: " + error.getMessage());
                        return;
                    }
                    if (value != null && value.exists()) {
                        kullanici user = value.toObject(kullanici.class);
                        if (user != null) {
                            kullaniciAdi.setText(user.getK_ad());
                            kullaniciMail.setText(user.getK_mail());
                            puan.setText(String.valueOf(user.getK_puan()));
                            ligPuan.setText(String.valueOf(user.getL_puan()));
                            firestore.collection("lig").document(user.getL_ID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    lig lig = value.toObject(com.example.tidtanima.Data.lig.class);
                                    ligAdi.setText(lig.getL_Ad());
                                }
                            });
                            // Fetch and set medal counts
                            setMedalCounts(user.getM_ID());
                        } else {
                            showToast("User data not found");
                        }
                    } else {
                        showToast("User data not found");
                    }
                }
            });





        }


        // profil düzenleme alanı açılıp kapanma
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand(v);
            }
        });

        btnUpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edit_username.getText().toString().trim();
                if (user != null){
                    btnUpdateUsername.setEnabled(true);
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build();
                    user.updateProfile(profileChangeRequest);

                    firestore.collection("kullanici").document(userId)
                            .update("k_ad", userName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Hataaa", "DocumentSnapshot successfully updated!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Ataaaa", "Error updating document", e);
                                }
                            });
                }
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edit_password.getText().toString();
                user.updatePassword(newPassword);
                showToast("Password update successfully");
            }
        });





        // mesaj gönderme alanının açılıp kapanması
        massageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand2(v);
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMesaj = sendMessage.getText().toString().trim();
                if (!newMesaj.isEmpty()) {
                    DocumentReference docRef = firestore.collection("mesajlar").document(userId);

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Doküman mevcut, mesajları güncelle
                                    ArrayList<String> mesajList = (ArrayList<String>) document.get("k_mesaj");
                                    if (mesajList == null) {
                                        mesajList = new ArrayList<>();
                                    }
                                    mesajList.add(newMesaj);
                                    docRef.update("k_mesaj", mesajList)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    showToast("Mesaj gönderildi");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    showToast("Mesaj gönderilemedi.");
                                                }
                                            });
                                } else {
                                    // Doküman mevcut değil, yeni doküman oluştur
                                    ArrayList<String> mesajList = new ArrayList<>();
                                    mesajList.add(newMesaj);
                                    mesajlar mesaj = new mesajlar(userId, mesajList);
                                    docRef.set(mesaj)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    showToast("Mesaj gönderildi");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    showToast("Mesaj gönderilemedi.");
                                                }
                                            });
                                }
                            } else {
                                showToast("Mesaj gönderilemedi.");
                            }
                        }
                    });
                } else {
                    showToast("Mesaj boş olamaz.");
                }
            }
        });





        // uygulama hakkında
        hakkinda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand1(v);
            }
        });
        // hesap silme işlemi alanı açılıp kapanma
        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand4(v);
            }
        });
        //çıkış işlemi
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), UyeGirisiActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void setMedalCounts(ArrayList<String> m_ids) {
        int altinCount = 0;
        int bronzCount = 0;
        int gumusCount = 0;

        if (m_ids != null) {
            for (String id : m_ids) {
                switch (id) {
                    case "1_altin":
                        altinCount++;
                        break;
                    case "3Bronz":
                        bronzCount++;
                        break;
                    case "2Gumus":
                        gumusCount++;
                        break;
                }
            }
        }

        madalya1.setText(String.valueOf(altinCount));
        madalya2.setText(String.valueOf(bronzCount));
        madalya3.setText(String.valueOf(gumusCount));
    }

    public void expand(View view) {
        int v = (detayEdit.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(editLayout, new AutoTransition());
        detayEdit.setVisibility(v);
    }
    public void expand1(View view){
        int v = (detayHakkinda.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(hakkinda, new AutoTransition());
        detayHakkinda.setVisibility(v);
    }

    public void expand2(View view) {
        int v = (massageLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(massageCard, new AutoTransition());
        massageLayout.setVisibility(v);
    }

    public void expand4(View view) {
        int v = (deleteLayout.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
        TransitionManager.beginDelayedTransition(deleteLayout, new AutoTransition());
        deleteLayout.setVisibility(v);
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
