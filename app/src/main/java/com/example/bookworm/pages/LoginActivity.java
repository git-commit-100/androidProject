package com.example.bookworm.pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText loginEmail, loginPassword;
    TextView loginError, registerHref;
    Button btnSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Intent goToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        Intent getFromRegister = getIntent();
        String emailFromRegister = getFromRegister.getStringExtra("email");

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginError = findViewById(R.id.loginError);
        btnSubmitLogin = findViewById(R.id.btnSubmitLogin);
        registerHref = findViewById(R.id.registerHref);

        registerHref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToRegister);
            }
        });

        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // hide error
                loginError.setVisibility(View.GONE);
                loginError.setText("");

                // check for empty field
                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                    loginEmail.setError("Please enter a valid email");
                    isValid = false;
                }

                if (TextUtils.isEmpty(password) || !isValidPassword(password)) {
                    loginPassword.setError("Please enter a valid password");
                    isValid = false;
                }


                if (!isValid) {
                    loginError.setVisibility(View.VISIBLE);
                    loginError.setText("Please correct the errors in the form.");
                } else {
                    loginError.setText("Looks good !");
                    loginError.setTextColor(Color.parseColor("#006400"));
                    loginError.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        Intent goToProducts = new Intent(LoginActivity.this, ProductsActivity.class);
                                        startActivity(goToProducts);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // set email from register page
        if (!TextUtils.isEmpty(emailFromRegister)) {
            loginEmail.setText(emailFromRegister);
        }
        // setting inputs to automate login
        loginEmail.setText("bhargav@gmail.com");
        loginPassword.setText("Bhargav@123");
    }

    // validate email using regex
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // validate password using regex
    private boolean isValidPassword(String password) {
        Pattern pattern;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

}