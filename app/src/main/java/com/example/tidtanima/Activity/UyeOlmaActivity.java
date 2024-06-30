package com.example.tidtanima.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tidtanima.Data.kullanici;
import com.example.tidtanima.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class UyeOlmaActivity extends AppCompatActivity {
    TextView singup_hata;
    TextView password_error;
    boolean isEmailErrorShown = false;
    boolean isPasswordErrorShown = false;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_uye_olma);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseAuth auth = FirebaseAuth.getInstance(); //FirebaseAuth başlatıldı
        firestore = FirebaseFirestore.getInstance();

        EditText kullanici_ad = findViewById(R.id.kullanici_ad);
        EditText singup_email = findViewById(R.id.singup_email);
        EditText singup_password = findViewById(R.id.singup_password);
        singup_hata = findViewById(R.id.singup_hata);
        password_error = findViewById(R.id.password_error);
        Button sing_up = findViewById(R.id.sing_up);

        singup_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    clearEmailError();
                    sing_up.setEnabled(true);
                } else {
                    setEmailError("Lütfen geçerli eposta adresini giriniz!");
                    sing_up.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        singup_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sing_up.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String password = editable.toString();
                if (Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$").matcher(password).matches()) {
                    clearPasswordError();
                    sing_up.setEnabled(true);
                } else {
                    setPasswordError("Güçlü bir şifre girin! Şifreniz en az bir büyük harf, küçük harf, rakam ve özel karakter içermeli ve 8 karakter uzunluğunda olmalıdır.");
                    sing_up.setEnabled(false);
                }
            }
        });

        sing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = kullanici_ad.getText().toString().trim();
                String email = singup_email.getText().toString().trim();
                String password = singup_password.getText().toString().trim();

                if (password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(UyeOlmaActivity.this, "Lütfen bilgilerinizi giriniz!", Toast.LENGTH_LONG).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    String userId = authResult.getUser().getUid();
                                    kullanici user = new kullanici(name, userId, email, 0, null, "1baslangicSeviyesi", 0, 5, "unite1", "0");

                                    firestore.collection("kullanici")
                                            .document(userId)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(UyeOlmaActivity.this, "Başarıyla kaydoldunuz!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(UyeOlmaActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UyeOlmaActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UyeOlmaActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    private void setEmailError(String errorMessage) {
        singup_hata.setText(errorMessage);
        singup_hata.setVisibility(View.VISIBLE);
        isEmailErrorShown = true;
    }

    public void clearEmailError() {
        singup_hata.setText("");
        singup_hata.setVisibility(View.GONE);
        isEmailErrorShown = false;
    }

    private void setPasswordError(String errorMessage) {
        password_error.setText(errorMessage);
        password_error.setVisibility(View.VISIBLE);
        isPasswordErrorShown = true;
    }

    private void clearPasswordError() {
        password_error.setText("");
        password_error.setVisibility(View.GONE);
        isPasswordErrorShown = false;
    }
}
