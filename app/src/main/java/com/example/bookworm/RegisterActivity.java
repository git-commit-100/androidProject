package com.example.bookworm;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText name, email, password1, password2;
    Button btnSubmitRegister;
    TextView registerError, loginHref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        Intent goToLogin = new Intent(RegisterActivity.this, LoginActivity.class);

        name = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password1 = findViewById(R.id.registerPassword1);
        password2 = findViewById(R.id.registerPassword2);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);
        registerError = findViewById(R.id.registerError);
        loginHref = findViewById(R.id.loginHref);

        loginHref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToLogin);
            }
        });

        btnSubmitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

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

    private boolean doesPasswordMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

    public void validateForm() {
        String valueName, valueEmail, valuePassword1, valuePassword2;
        valueName = name.getText().toString().trim();
        valueEmail = email.getText().toString().trim();
        valuePassword1 = password1.getText().toString().trim();
        valuePassword2 = password2.getText().toString().trim();

        if (TextUtils.isEmpty(valueName)) {
            registerError.setText("Name cannot be empty");
            registerError.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(valueEmail) || !isValidEmail(valueEmail)) {
            registerError.setText("Please enter a valid email");
            registerError.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(valuePassword1) || !isValidPassword(valuePassword1)) {
            registerError.setText("Please enter a valid password");
            registerError.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(valuePassword2) && doesPasswordMatch(valuePassword1, valuePassword2)) {
            registerError.setText("Password does not match");
            registerError.setVisibility(View.VISIBLE);
            return;
        }

        registerError.setText("Looks good !");
        registerError.setTextColor(Color.parseColor("#006400"));
        registerError.setVisibility(View.VISIBLE);

// valid form -> register user
        mAuth.createUserWithEmailAndPassword(valueEmail, valuePassword2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                             FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this,
                                    "Account created ! Please login to continue",
                                    Toast.LENGTH_LONG).show();
                            // switch to login page
                            Intent goToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                            goToLogin.putExtra("email", valueEmail);
                            startActivity(goToLogin);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this,
                                    "Authentication failed ! Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}