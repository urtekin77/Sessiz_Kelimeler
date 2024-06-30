package com.example.tidtanima.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tidtanima.Adapter.KelimeViewAdapter;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    ImageView resimView;
    VideoView videoView;
    Button a, b, c, d;
    String uId, userId;
    List<isaret> unite1Docs = new ArrayList<>();
    int currentQuestionIndex = 0;
    TextView sinavPuani, can, progressText, timerText;
    ProgressBar progressBar;
    int puan = 0;
    int ligPuan = 0;
    int kalanCan = 3;  // Quiz sırasında kullanılacak can sayısı
    CountDownTimer countDownTimer;
    MediaPlayer correctSound, wrongSound;
    long remainingTime = 20000;  // Başlangıçta 20 saniye
    List<isaret> incorrectQuestions = new ArrayList<>();
    Vibrator vibrator;
    Handler handler = new Handler();
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    int currentStep;
    int currentUnite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        firestore = FirebaseFirestore.getInstance();
        ArrayList<kullanici> kullanicilar = new ArrayList<>();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // titreşim
        resimView = findViewById(R.id.soru);
        videoView = findViewById(R.id.videoView);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);

        sinavPuani = findViewById(R.id.sinavPuani);
        can = findViewById(R.id.can);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        timerText = findViewById(R.id.timerText);

        uId = getIntent().getStringExtra("U_ID");
        currentStep = getIntent().getIntExtra("STEP", 1);
        currentUnite = Integer.parseInt(uId.replace("unite", ""));

        Log.d("U_ID", uId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference unite1Ref = db.collection("isaret");

        Query query = unite1Ref.whereEqualTo("U_ID", uId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    isaret isaretObject = document.toObject(isaret.class);
                    unite1Docs.add(isaretObject);
                }
                Collections.shuffle(unite1Docs);
                showNextQuestion();
            } else {
                Log.d("Firestore", "Belgeler alınamadı.", task.getException());
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < 15 && currentQuestionIndex < unite1Docs.size()) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            resetButtonColors();

            final isaret currentQuestion = unite1Docs.get(currentQuestionIndex);
            String resimUrl = currentQuestion.getI_resim();
            String videoUrl = currentQuestion.getI_video();

            if (resimUrl != null && !resimUrl.isEmpty()) {
                Log.d("QuizActivity", "Resim URL'sinden görüntüleniyor: " + resimUrl);
                videoView.setVisibility(View.GONE);
                resimView.setVisibility(View.VISIBLE);
                Picasso.get().load(resimUrl).into(resimView);
            } else if (videoUrl != null && !videoUrl.isEmpty()) {
                Log.d("QuizActivity", "Video URL'sinden yüklenmeye çalışılıyor: " + videoUrl);
                resimView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl);
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("QuizActivity", "Video URI başarıyla alındı: " + uri.toString());
                        videoView.setVideoURI(uri);
                        videoView.setOnPreparedListener(mp -> {
                            Log.d("QuizActivity", "Video hazır, oynatılmaya başlanıyor.");
                            videoView.start();
                        });
                        videoView.setOnErrorListener((mp, what, extra) -> {
                            Log.e("QuizActivity", "Video oynatımı sırasında hata oluştu. what: " + what + " extra: " + extra);
                            return true;
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseStorage", "Video indirme başarısız: " + e.getMessage());
                    }
                });
            } else {
                Log.e("Media", "Her iki medya URL'si de boş veya null.");
            }
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                    } else {
                        videoView.start();
                    }
                }
            });

            List<isaret> choices = new ArrayList<>(unite1Docs);
            choices.remove(currentQuestion);
            Collections.shuffle(choices);
            if (choices.size() >= 3) {
                choices = choices.subList(0, 3);
            }
            choices.add(currentQuestion);
            Collections.shuffle(choices);

            List<isaret> finalChoices = choices;

            a.setText(finalChoices.get(0).getI_ad());
            b.setText(finalChoices.get(1).getI_ad());
            c.setText(finalChoices.get(2).getI_ad());
            d.setText(finalChoices.get(3).getI_ad());

            a.setOnClickListener(view -> checkAnswer(finalChoices.get(0), currentQuestion, a, a, b, c, d));
            b.setOnClickListener(view -> checkAnswer(finalChoices.get(1), currentQuestion, b, a, b, c, d));
            c.setOnClickListener(view -> checkAnswer(finalChoices.get(2), currentQuestion, c, a, b, c, d));
            d.setOnClickListener(view -> checkAnswer(finalChoices.get(3), currentQuestion, d, a, b, c, d));

            currentQuestionIndex++;
            updateProgress();
            startTimer();
        } else {
            updateUserProgress();
            showSummary();
        }
    }

    private void checkAnswer(isaret selectedAnswer, isaret correctAnswer, Button selectedButton, Button a, Button b, Button c, Button d) {
        if (selectedAnswer.equals(correctAnswer)) {
            int baseScore = 10;
            int timeBonus = (int) ((remainingTime / 1000) * 2); // Kalan saniye başına ek 2 puan
            puan += baseScore + timeBonus;
            ligPuan += timeBonus * 2;
            if (correctSound != null) {
                correctSound.start();
            }
            vibrator.vibrate(100); // 100 ms boyunca titreşim
            highlightCorrectAnswer(correctAnswer, a, b, c, d);
        } else {
            puan -= 5;
            kalanCan -= 1;
            ligPuan -= 10;
            incorrectQuestions.add(correctAnswer);
            if (wrongSound != null) {
                wrongSound.start();
            }
            vibrator.vibrate(new long[]{0, 100, 100, 100}, -1); // İki kez titreşim
            highlightIncorrectAnswer(selectedButton, correctAnswer, a, b, c, d);
        }
        sinavPuani.setText(String.valueOf(puan));
        can.setText(String.valueOf(kalanCan));
        if (kalanCan == 0) {
            showFailureDialog(); // Show failure dialog when the user fails
        } else {
            handler.postDelayed(this::showNextQuestion, 2000); // 2 saniye bekle ve sonra bir sonraki soruyu göster
        }
    }

    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.failure_dialog, null);
        builder.setView(dialogView);

        // Create the dialog
        final AlertDialog dialog = builder.create();

        // Find the button in the custom layout and set the click listener
        Button anaSayfaGit = dialogView.findViewById(R.id.anaSayfaGit);
        anaSayfaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateFirestoreUserLives();
            }
        });

        // Show the dialog
        dialog.show();
    }



    private void updateFirestoreUserLives() {
        DocumentReference userRef = firestore.collection("kullanici").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    kullanici user = documentSnapshot.toObject(kullanici.class);
                    if (user != null) {
                        int currentLives = user.getCan();
                        if (currentLives > 0) {
                            user.setCan(currentLives - 1);
                            userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(QuizActivity.this, "Kullanıcı verisi güncellenemedi", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            showFailureDialog();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizActivity.this, "Kullanıcı verisi alınamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgress() {
        progressBar.setProgress((currentQuestionIndex) * 100 / 15); // Doğru soru sayısını yansıtacak şekilde güncelle
        progressText.setText((currentQuestionIndex) + "/15");
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(20000, 1000) {  // Her soru için 20 saniye
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                timerText.setText("Süre: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                puan -= 5;
                ligPuan -= 10;
                kalanCan -= 1;
                updateUIAfterWrongAnswer();
                if (kalanCan == 0) {
                    showFailureDialog();
                } else {
                    showNextQuestion();
                }
            }
        }.start();
    }

    private void updateUIAfterWrongAnswer() {
        sinavPuani.setText(String.valueOf(puan));
        can.setText(String.valueOf(kalanCan));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // AlertDialog için özel layout oluşturun
        View dialogView = getLayoutInflater().inflate(R.layout.warning_alert, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button vazgec = dialogView.findViewById(R.id.btnVazgec);
        Button cik = dialogView.findViewById(R.id.btnCik);

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog and continue with the quiz
                dialog.dismiss();
            }
        });

        cik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference userRef = firestore.collection("kullanici").document(userId);
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            kullanici user = documentSnapshot.toObject(kullanici.class);
                            if (user != null) {
                                int currentPoints = user.getK_puan();
                                int can = user.getCan()-1;
                                int newPoints = currentPoints - 1000;
                                user.setK_puan(newPoints);
                                user.setL_puan(user.getL_puan() - ligPuan);
                                user.setCan(can);
                                userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QuizActivity.this, "1000 puan silindi", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();  // Dismiss the dialog
                                        QuizActivity.super.onBackPressed();  // Call super.onBackPressed to handle the back press action
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuizActivity.this, "Puan silme başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizActivity.this, "Kullanıcı verisi alınamadı", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }

    private void highlightCorrectAnswer(isaret correctAnswer, Button a, Button b, Button c, Button d) {
        if (a.getText().toString().equals(correctAnswer.getI_ad())) {
            a.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (b.getText().toString().equals(correctAnswer.getI_ad())) {
            b.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (c.getText().toString().equals(correctAnswer.getI_ad())) {
            c.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (d.getText().toString().equals(correctAnswer.getI_ad())) {
            d.setBackground(getResources().getDrawable(R.drawable.correct_box));
        }
    }

    private void highlightIncorrectAnswer(Button selectedButton, isaret correctAnswer, Button a, Button b, Button c, Button d) {
        selectedButton.setBackground(getResources().getDrawable(R.drawable.wrong_box));
        highlightCorrectAnswer(correctAnswer, a, b, c, d);
    }

    private void resetButtonColors() {
        a.setBackground(getResources().getDrawable(R.drawable.red_box));
        b.setBackground(getResources().getDrawable(R.drawable.red_box));
        c.setBackground(getResources().getDrawable(R.drawable.red_box));
        d.setBackground(getResources().getDrawable(R.drawable.red_box));
    }

    private void updateUserProgress() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("kullanici").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    kullanici user = documentSnapshot.toObject(kullanici.class);
                    if (user != null) {
                        if (currentUnite == Integer.parseInt(user.getU_ID().replace("unite", ""))) {
                            if (currentStep == 9) {
                                // If the user completed the last step, update to the next unite
                                user.setU_ID("unite" + (Integer.parseInt(user.getU_ID().replace("unite", "")) + 1));
                                user.setU_adim("1");
                            } else {
                                // Otherwise, just update to the next step
                                user.setU_adim(String.valueOf(currentStep));
                            }
                        }
                        userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(QuizActivity.this, "User progress updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(QuizActivity.this, "Failed to update user progress", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSummary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // AlertDialog için özel layout oluşturun
        View dialogView = getLayoutInflater().inflate(R.layout.ozet_dialog, null);
        builder.setView(dialogView);

        // Doğru ve yanlış cevapların sayısını gösteren TextView'leri ayarlayın
        TextView correctAnswersText = dialogView.findViewById(R.id.correctAnswers);
        TextView incorrectAnswersText = dialogView.findViewById(R.id.incorrectAnswers);
        TextView scoreText = dialogView.findViewById(R.id.score);
        TextView ligPuanText = dialogView.findViewById(R.id.lig_puan);
        Button gec = dialogView.findViewById(R.id.gec);

        // Correctly set text with string values
        correctAnswersText.setText(String.valueOf(currentQuestionIndex - incorrectQuestions.size()));
        incorrectAnswersText.setText(String.valueOf(incorrectQuestions.size()));
        scoreText.setText(String.valueOf(puan));
        ligPuanText.setText(String.valueOf(ligPuan));

        // Set the click listener for the "geç" button
        gec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    DocumentReference userRef = firestore.collection("kullanici").document(userId);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                kullanici user = documentSnapshot.toObject(kullanici.class);
                                if (user != null) {
                                    // Update the user's score and lives
                                    user.setK_puan(user.getK_puan() + puan);
                                    user.setL_puan(user.getL_puan() + ligPuan);
                                    user.setCan(user.getCan());  // Quiz'deki can sayısını değil, veritabanındaki can sayısını koru
                                    // Save the updated user back to Firestore
                                    userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(QuizActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(QuizActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(QuizActivity.this, "Failed to update data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QuizActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // RecyclerView ve adaptörünü ayarlayın
        RecyclerView recyclerView = dialogView.findViewById(R.id.tekarlar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        KelimeViewAdapter kelimeViewAdapter = new KelimeViewAdapter(incorrectQuestions);
        recyclerView.setAdapter(kelimeViewAdapter);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (correctSound != null) {
            correctSound.release();
        }
        if (wrongSound != null) {
            wrongSound.release();
        }
        super.onDestroy();
    }
}
