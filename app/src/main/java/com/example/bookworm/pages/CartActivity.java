package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class CartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.cartItem).setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ID = item.getItemId();
                bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                if (ID == R.id.productsItem) {
                    startActivity(new Intent(CartActivity.this, ProductsActivity.class));
                    return true;
                } else if (ID == R.id.accountItem) {
                    startActivity(new Intent(CartActivity.this, AccountActivity.class));
                    return true;
                }
                return false;
            }
        });

    }
}