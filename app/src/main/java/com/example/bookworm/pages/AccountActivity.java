package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.MainActivity;
import com.example.bookworm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView accountEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountEmail = findViewById(R.id.accountEmail);
        btnLogout = findViewById(R.id.btnLogout);

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.accountItem).setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ID = item.getItemId();
                bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                if (ID == R.id.productsItem) {
                    startActivity(new Intent(AccountActivity.this, ProductsActivity.class));
                    return true;
                } else if (ID == R.id.cartItem) {
                    startActivity(new Intent(AccountActivity.this, CartActivity.class));
                    return true;
                }
                return false;
            }
        });

        // get Emal from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        accountEmail.setText("Hello, " + email);

        // logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
                Toast.makeText(AccountActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

}