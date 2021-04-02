package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    public static final int REQUEST_CODE_AND_NOTE = 1;
    TextView register;
    private EditText eEmail, ePassword;
    private Button signIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        register = findViewById(R.id.textRegister);
        register.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), Register.class),
                REQUEST_CODE_AND_NOTE
        ));

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, Trending.class));
            this.finish();
        }

        signIn = (Button) findViewById(R.id.buttonSignIn);
        signIn.setOnClickListener(v -> userLogin());

        eEmail = (EditText) findViewById(R.id.inputEmail);
        ePassword = (EditText) findViewById(R.id.inputPassword);

        mAuth = FirebaseAuth.getInstance();
    }

    private void userLogin() {

        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (email.isEmpty()) {
            eEmail.setError("Email is required!");
            eEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eEmail.setError("Please provide a valid email!");
            eEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ePassword.setError("Password is required!");
            ePassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            ePassword.setError("Password must be at least 6 characters long!");
            ePassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(Login.this, Trending.class);
                startActivity(intent);
                Login.this.finish();

            } else {
                Toast.makeText(Login.this, "Incorrect Email or Password!", Toast.LENGTH_LONG).show();
            }
        });
    }
}