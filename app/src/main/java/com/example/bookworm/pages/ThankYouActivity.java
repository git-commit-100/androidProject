package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ThankYouActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThankYouActivity.this, ProductsActivity.class));
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ID = item.getItemId();
                bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                if (ID == R.id.productsItem) {
                    startActivity(new Intent(ThankYouActivity.this, ProductsActivity.class));
                    return true;
                } else if (ID == R.id.cartItem) {
                    startActivity(new Intent(ThankYouActivity.this, CartActivity.class));
                    return true;
                } else {
                    startActivity(new Intent(ThankYouActivity.this, AccountActivity.class));
                }
                return false;
            }
        });
    }
}