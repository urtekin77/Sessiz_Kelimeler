package com.example.tidtanima.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class UyeGirisiActivity extends AppCompatActivity {

    private static final String TAG = "UyeGirisiActivity";

    private EditText email, password;
    private TextView email_hata, uye_ol, forgetPassword ;
    private Button giris_btn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_uye_girisi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();


        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        email_hata = findViewById(R.id.email_hata);
        giris_btn = findViewById(R.id.button);

        // uygulama giriş işlemi
        giris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = email.getText().toString().trim();
                String login_password = password.getText().toString().trim();

                if (login_email.isEmpty() || login_password.isEmpty()) {
                    showToast("Lütfen e-posta adresinizi ve şifrenizi giriniz.");
                } else {
                    auth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(UyeGirisiActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                showToast("Hoşgeldiniz");
                                startActivity(new Intent(UyeGirisiActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Exception exception = task.getException();
                                if (exception != null) {
                                    Log.e(TAG, "Authentication failed: ", exception);
                                    if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                        showToast("Geçersiz şifre. Lütfen tekrar deneyiniz.");
                                    } else if (exception instanceof FirebaseAuthInvalidUserException) {
                                        showToast("Kullanıcı bulunamadı. Lütfen e-posta adresinizi kontrol ediniz.");
                                    } else {
                                        showToast("Kimlik doğrulama başarısız oldu. Lütfen tekrar deneyiniz.");
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        uye_ol = findViewById(R.id.uye_ol);
        uye_ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UyeGirisiActivity.this, UyeOlmaActivity.class));
            }
        });

        forgetPassword = findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UyeGirisiActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString().trim();
                        if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            showToast("Enter your registered email id");
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showToast("Check your email");
                                    dialog.dismiss();
                                } else {
                                    showToast("Unable to send, please try again later");
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCanel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(UyeGirisiActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
