package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookworm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProductDetailsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView productTitle, productDescription, productPrice;
    ImageView productImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Retrieve product details from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            double price = intent.getDoubleExtra("price", 0.0);
            String imgUrl = intent.getStringExtra("imgUrl");

            // Update views with product details
            productTitle = findViewById(R.id.productDetailsTitle);
            productTitle.setText(title);

            productDescription = findViewById(R.id.productDetailsDescription);
            productDescription.setText(description);

            productPrice = findViewById(R.id.productDetailsPrice);
            productPrice.setText(String.format("$ %.02f", price));

            // Load image using Glide library
            productImg = findViewById(R.id.productDetailsImg);
            Glide.with(this)
                    .load(imgUrl)
                    .into(productImg);

            // bottom nav
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int ID = item.getItemId();
                    bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                    if (ID == R.id.productsItem) {
                        startActivity(new Intent(ProductDetailsActivity.this, ProductsActivity.class));
                        return true;
                    } else if (ID == R.id.cartItem) {
                        startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                        return true;
                    } else {
                        startActivity(new Intent(ProductDetailsActivity.this, AccountActivity.class));
                    }
                    return false;
                }
            });


        }
    }
}
