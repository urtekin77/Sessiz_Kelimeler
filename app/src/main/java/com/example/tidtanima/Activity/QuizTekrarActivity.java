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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.R;
import com.example.tidtanima.Data.isaret;
import com.example.tidtanima.Data.unite;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizTekrarActivity extends AppCompatActivity {
    ImageView optionA, optionB, optionC, optionD;
    VideoView optionVideoA, optionVideoB, optionVideoC, optionVideoD;
    LinearLayout aButon, bButon, cButon, dButon;
    TextView questionText;
    AppCompatButton kontrolEt;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userId;
    Vibrator vibrator;
    TextView sinavPuani, can, progressText, timerText;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    MediaPlayer correctSound, wrongSound;
    Handler handler = new Handler();
    CollectionReference uniteRef, kullaniciRef;
    ArrayList<isaret> signsList = new ArrayList<>();
    ArrayList<isaret> selectedSigns = new ArrayList<>();
    List<isaret> incorrectQuestions = new ArrayList<>();
    int currentQuestionIndex = 0;
    int puan = 0;
    int ligPuan = 0;
    int kalanCan = 3;
    long remainingTime = 50000; // Başlangıçta 50 saniye
    LinearLayout selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_tekrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        questionText = findViewById(R.id.questionText);

        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        optionVideoA = findViewById(R.id.optionVideoA);
        optionVideoB = findViewById(R.id.optionVideoB);
        optionVideoC = findViewById(R.id.optionVideoC);
        optionVideoD = findViewById(R.id.optionVideoD);

        aButon = findViewById(R.id.aButon);
        bButon = findViewById(R.id.bButon);
        cButon = findViewById(R.id.cButon);
        dButon = findViewById(R.id.dButon);

        kontrolEt = findViewById(R.id.kontrolButon);
        sinavPuani = findViewById(R.id.sinavPuaniTekrar);
        can = findViewById(R.id.canTekrar);
        progressBar = findViewById(R.id.progressBarTekrar);
        progressText = findViewById(R.id.progressTextTekrar);
        timerText = findViewById(R.id.timerTextTekrar);

        firestore = FirebaseFirestore.getInstance();
        uniteRef = firestore.collection("unite");
        kullaniciRef = firestore.collection("kullanici");

        getCurrentUnitAndFetchSigns();

        kontrolEt.setOnClickListener(v -> checkSelectedAnswer());
    }

    private void getCurrentUnitAndFetchSigns() {
        kullaniciRef.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                kullanici kullanici = task.getResult().toObject(kullanici.class);
                String currentUnitId = kullanici.getU_ID();
                uniteRef.get().addOnCompleteListener(taska -> {
                    if (taska.isSuccessful()) {
                        List<String> unitIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : taska.getResult()) {
                            unite unit = document.toObject(unite.class);
                            unitIds.add(unit.getU_ID());
                            if (unit.getU_ID().equals(currentUnitId)) {
                                break;
                            }
                        }
                        CollectionReference signsRef = firestore.collection("isaret");
                        for (String unitId : unitIds) {
                            signsRef.whereEqualTo("U_ID", unitId).get().addOnCompleteListener(taskb -> {
                                if (taskb.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : taskb.getResult()) {
                                        isaret isaret = document.toObject(isaret.class);
                                        signsList.add(isaret);
                                        Log.d("QuizTekrarActivity", "Sign ID: " + isaret.getI_ID());
                                    }
                                    createQuizQuestions();
                                } else {
                                    Log.e("QuizTekrarActivity", "İşaretleri alırken hata oluştu", taskb.getException());
                                }
                            });
                        }
                    } else {
                        Log.e("QuizTekrarActivity", "Üniteleri alırken hata oluştu", taska.getException());
                    }
                });
            } else {
                Log.e("QuizTekrarActivity", "Kullanıcı verilerini alırken hata oluştu", task.getException());
            }
        });
    }

    private void createQuizQuestions() {
        Collections.shuffle(signsList);
        selectedSigns = new ArrayList<>(signsList.subList(0, 15));
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < selectedSigns.size()) {
            isaret currentSign = selectedSigns.get(currentQuestionIndex);
            questionText.setText(currentSign.getI_anlam());

            List<isaret> options = new ArrayList<>(signsList);
            options.remove(currentSign);
            Collections.shuffle(options);
            resetButtonColors();
            if (options.size() >= 3) {
                options = options.subList(0, 3);
            }
            options.add(currentSign);
            Collections.shuffle(options);

            setOptions(options, currentSign);

            currentQuestionIndex++;
            updateProgress();
            startTimer();
        } else {
            showSummary();
        }
    }

    private void setOptions(List<isaret> choices, isaret correctSign) {
        setOptionView(choices.get(0), optionA, optionVideoA, aButon);
        setOptionView(choices.get(1), optionB, optionVideoB, bButon);
        setOptionView(choices.get(2), optionC, optionVideoC, cButon);
        setOptionView(choices.get(3), optionD, optionVideoD, dButon);

        aButon.setOnClickListener(view -> {
            selectOption(choices.get(0), aButon);
            playVideo(optionVideoA);
        });
        bButon.setOnClickListener(view -> {
            selectOption(choices.get(1), bButon);
            playVideo(optionVideoB);
        });
        cButon.setOnClickListener(view -> {
            selectOption(choices.get(2), cButon);
            playVideo(optionVideoC);
        });
        dButon.setOnClickListener(view -> {
            selectOption(choices.get(3), dButon);
            playVideo(optionVideoD);
        });
    }

    private void setOptionView(isaret sign, ImageView imageView, VideoView videoView, LinearLayout button) {
        String resimUrl = sign.getI_resim();
        String videoUrl = sign.getI_video();
        if (resimUrl != null && !resimUrl.isEmpty()) {
            videoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(resimUrl).into(imageView);
            button.setTag(sign);
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            loadVideo(videoUrl, videoView);
            button.setTag(sign);
        } else {
            Log.e("Media", "Her iki medya URL'si de boş veya null.");
        }
    }

    private void loadVideo(String videoUrl, VideoView videoView) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl);
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(mp -> videoView.start());
            videoView.setOnErrorListener((mp, what, extra) -> {
                Log.e("QuizActivity", "Video oynatımı sırasında hata oluştu. what: " + what + " extra: " + extra);
                return true;
            });
        }).addOnFailureListener(e -> Log.e("FirebaseStorage", "Video indirme başarısız: " + e.getMessage()));
    }

    private void playVideo(VideoView videoView) {
        if (videoView.getVisibility() == View.VISIBLE) {
            videoView.start();
        }
    }

    private void selectOption(isaret selectedSign, LinearLayout selectedView) {
        resetButtonColors();
        selectedOption = selectedView;
        selectedView.setBackground(getResources().getDrawable(R.drawable.button_gray));
        selectedView.setTag(selectedSign);
    }

    private void checkSelectedAnswer() {
        if (selectedOption != null && selectedOption.getTag() != null) {
            isaret selectedSign = (isaret) selectedOption.getTag();
            isaret correctSign = selectedSigns.get(currentQuestionIndex - 1);
            checkAnswer(selectedSign, correctSign, selectedOption);
        }
    }

    private void checkAnswer(isaret selectedAnswer, isaret correctAnswer, LinearLayout selectedView) {
        if (selectedAnswer.equals(correctAnswer)) {
            int baseScore = 10;
            int timeBonus = (int) ((remainingTime / 1000) * 2); // Kalan süre bonusu
            puan += baseScore + timeBonus;
            ligPuan += timeBonus * 2;
            vibrator.vibrate(100);
            highlightCorrectAnswer(correctAnswer);
        } else {
            puan -= 5;
            kalanCan -= 1;
            ligPuan -= 10;
            incorrectQuestions.add(correctAnswer);
            if (wrongSound != null) {
                wrongSound.start();
            }
            vibrator.vibrate(new long[]{0, 100, 100, 100}, -1);
            highlightIncorrectAnswer(selectedView, correctAnswer);
        }
        sinavPuani.setText(String.valueOf(puan));
        can.setText(String.valueOf(kalanCan));
        if (kalanCan == 0) {
            showFailureDialog();
        } else {
            handler.postDelayed(this::showNextQuestion, 2000);
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(50000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                timerText.setText("Süre: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                puan -= 5;
                kalanCan -= 1;
                ligPuan -= 5;
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

    private void updateProgress() {
        progressBar.setProgress((currentQuestionIndex) * 100 / 15);
        progressText.setText((currentQuestionIndex) + "/15");
    }

    private void highlightCorrectAnswer(isaret correctAnswer) {
        if (aButon.getTag().equals(correctAnswer)) {
            aButon.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (bButon.getTag().equals(correctAnswer)) {
            bButon.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (cButon.getTag().equals(correctAnswer)) {
            cButon.setBackground(getResources().getDrawable(R.drawable.correct_box));
        } else if (dButon.getTag().equals(correctAnswer)) {
            dButon.setBackground(getResources().getDrawable(R.drawable.correct_box));
        }
    }

    private void highlightIncorrectAnswer(LinearLayout selectedView, isaret correctAnswer) {
        selectedView.setBackground(getResources().getDrawable(R.drawable.wrong_box));
        highlightCorrectAnswer(correctAnswer);
    }

    private void resetButtonColors() {
        aButon.setBackground(getResources().getDrawable(R.drawable.red_box));
        bButon.setBackground(getResources().getDrawable(R.drawable.red_box));
        cButon.setBackground(getResources().getDrawable(R.drawable.red_box));
        dButon.setBackground(getResources().getDrawable(R.drawable.red_box));
    }

    private void showSummary() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View dialogView = getLayoutInflater().inflate(R.layout.ozet_dialog, null);
            builder.setView(dialogView);

            TextView correctAnswersText = dialogView.findViewById(R.id.correctAnswers);
            TextView incorrectAnswersText = dialogView.findViewById(R.id.incorrectAnswers);
            TextView scoreText = dialogView.findViewById(R.id.score);
            TextView lig_puanText = dialogView.findViewById(R.id.lig_puan);
            Button gec = dialogView.findViewById(R.id.gec);

            correctAnswersText.setText(String.valueOf(currentQuestionIndex - incorrectQuestions.size()));
            incorrectAnswersText.setText(String.valueOf(incorrectQuestions.size()));
            scoreText.setText(String.valueOf(puan));
            lig_puanText.setText(String.valueOf(ligPuan));

            gec.setOnClickListener(v -> {
                if (user != null) {
                    DocumentReference userRef = firestore.collection("kullanici").document(userId);
                    userRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            kullanici user = documentSnapshot.toObject(kullanici.class);
                            if (user != null) {
                                user.setK_puan(user.getK_puan() + puan);
                                user.setL_puan(user.getL_puan() + ligPuan);
                                userRef.set(user).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(QuizTekrarActivity.this, "Veri başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(QuizTekrarActivity.this, MainActivity.class));
                                    finish();
                                }).addOnFailureListener(e -> Toast.makeText(QuizTekrarActivity.this, "Veri güncellenemedi", Toast.LENGTH_SHORT).show());
                            }
                        }
                    }).addOnFailureListener(e -> Toast.makeText(QuizTekrarActivity.this, "Kullanıcı verisi alınamadı", Toast.LENGTH_SHORT).show());
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void showFailureDialog() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View dialogView = getLayoutInflater().inflate(R.layout.failure_dialog, null);
            builder.setView(dialogView);

            final AlertDialog dialog = builder.create();

            Button anaSayfaGit = dialogView.findViewById(R.id.anaSayfaGit);
            anaSayfaGit.setOnClickListener(v -> {
                dialog.dismiss();
                updateFirestoreUserLives();
            });

            dialog.show();
        }
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
                                user.setCan(can);
                                userRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QuizTekrarActivity.this, "1000 puan silindi", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();  // Dismiss the dialog
                                        QuizTekrarActivity.super.onBackPressed();  // Call super.onBackPressed to handle the back press action
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuizTekrarActivity.this, "Puan silme başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizTekrarActivity.this, "Kullanıcı verisi alınamadı", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();
    }

    private void updateFirestoreUserLives() {
        DocumentReference userRef = firestore.collection("kullanici").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                kullanici user = documentSnapshot.toObject(kullanici.class);
                if (user != null) {
                    int currentLives = user.getCan();
                    if (currentLives > 0) {
                        user.setCan(currentLives - 1);
                        userRef.set(user).addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(QuizTekrarActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(QuizTekrarActivity.this, "Kullanıcı verisi güncellenemedi", Toast.LENGTH_SHORT).show());
                    } else {
                        showFailureDialog();
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(QuizTekrarActivity.this, "Kullanıcı verisi alınamadı", Toast.LENGTH_SHORT).show());
    }
}
