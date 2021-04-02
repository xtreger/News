package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {


    private Button registerButton;
    private EditText email, name, phone, password, repeatPassword;
    private String nameString, emailString, phoneString;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        registerButton = (Button) findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        email = (EditText) findViewById(R.id.inputEmail);
        name = (EditText) findViewById(R.id.inputName);
        phone = (EditText) findViewById(R.id.inputPhone);
        password = (EditText) findViewById(R.id.inputPassword);
        repeatPassword = (EditText) findViewById(R.id.inputRepeatPassword);
    }

    private void registerUser() {
        emailString = email.getText().toString().trim();
        String password2 = password.getText().toString().trim();
        String repeatPassword2 = repeatPassword.getText().toString().trim();
        nameString = name.getText().toString().trim();
        phoneString = phone.getText().toString().trim();

        if (nameString.isEmpty()) {
            name.setError("Full name is required!");
            name.requestFocus();
            return;
        }

        if (phoneString.isEmpty()) {
            phone.setError("Phone is required!");
            phone.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(phoneString).matches()) {
            phone.setError("Not a valid phone number!");
            phone.requestFocus();
            return;
        }

        if (emailString.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please provide a valid email!");
            email.requestFocus();
            return;
        }

        if (password2.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if (password2.length() < 6) {
            password.setError("Password must be at least 6 characters long!");
            password.requestFocus();
            return;
        }

        if (repeatPassword2.isEmpty()) {
            repeatPassword.setError("You must enter the password again!");
            repeatPassword.requestFocus();
            return;
        }

        if (!repeatPassword2.equals(password2)) {
            repeatPassword.setError("Passwords do not match!");
            repeatPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailString, password2)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Register.this, Trending.class));
                        Toast.makeText(Register.this, "You have been registered.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Register.this, "This email address already exists!", Toast.LENGTH_LONG).show();
                    }
                });

    }
}