package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.btnRegister);
        Intent intentGoToLogin = new Intent(MainActivity.this, LoginActivity.class);
        Intent intentGoToRegister = new Intent(MainActivity.this, RegisterActivity.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentGoToLogin);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentGoToRegister);
            }
        });

    }
}