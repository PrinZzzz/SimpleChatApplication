package com.mateo.chatapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private TextInputEditText editTextForgot;
    private Button buttonForgot;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        editTextForgot = findViewById(R.id.editTextForgotEmailReset);
        buttonForgot = findViewById(R.id.buttonForgotPasswordReset);

        auth = FirebaseAuth.getInstance();

        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextForgot.getText().toString();

                if (!email.equals("")) {
                    passwordReset(email);
                }else {
                    Toast.makeText(ForgotActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void passwordReset(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotActivity.this, "There is a problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}