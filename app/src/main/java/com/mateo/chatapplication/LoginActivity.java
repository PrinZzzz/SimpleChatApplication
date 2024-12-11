package com.mateo.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText loginEmail, loginPassword;
    private Button buttonSignin, buttonSignup;
    private TextView textViewForgot;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null ){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.editTextForgotEmailReset);
        loginPassword = findViewById(R.id.editTextLoginPassword);
        buttonSignin = findViewById(R.id.buttonSignin);
        buttonSignup = findViewById(R.id.buttonForgotPasswordReset);
        textViewForgot = findViewById(R.id.textViewForgot);

        auth = FirebaseAuth.getInstance();

        buttonSignin.setOnClickListener(view -> {

            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();

            if (!email.equals("") && !password.equals("")) {
                signIn(email, password);
            }else {
                Toast.makeText(this, "Please enter invalid email and password"
                        , Toast.LENGTH_SHORT).show();
            }

        });

        buttonSignup.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        textViewForgot.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(i);
        });


    }

    public void signIn(String email, String password){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(LoginActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Sign in is not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}