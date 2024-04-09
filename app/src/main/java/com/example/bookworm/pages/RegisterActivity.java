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

                // Reset error message
                registerError.setVisibility(View.GONE);
                registerError.setText("");

                boolean isValid = true;

                String valueName = name.getText().toString().trim();
                String valueEmail = email.getText().toString().trim();
                String valuePassword1 = password1.getText().toString().trim();
                String valuePassword2 = password2.getText().toString().trim();

                if (TextUtils.isEmpty(valueName)) {
                    name.setError("Name cannot be empty");
                    isValid = false;
                }

                if (TextUtils.isEmpty(valueEmail) || !isValidEmail(valueEmail)) {
                    email.setError("Please enter a valid email");
                    isValid = false;
                }

                if (TextUtils.isEmpty(valuePassword1) || !isValidPassword(valuePassword1)) {
                    password1.setError("Password should have a UPPERCASE, a LOWERCASE, a NUMBER and a SPECIAL SYMBOL and minimum 8 digits");
                    isValid = false;
                }

                if (TextUtils.isEmpty(valuePassword2) || !doesPasswordMatch(valuePassword1, valuePassword2)) {
                    password2.setError("Password does not match");
                    isValid = false;
                }

                // If any field is invalid, display error message
                if (!isValid) {
                    registerError.setVisibility(View.VISIBLE);
                    registerError.setText("Please correct the errors in the form.");
                } else {
                    // All fields are valid, proceed with submission
                    registerError.setText("Looks good !");
                    registerError.setTextColor(Color.parseColor("#006400"));
                    registerError.setVisibility(View.VISIBLE);

                    // valid form -> register user
                    mAuth.createUserWithEmailAndPassword(valueEmail, valuePassword2)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

}