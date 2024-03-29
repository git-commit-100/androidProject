package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productTitle, productDescription, productPrice;
    ImageView productImg, backImg;

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

            // back button
            backImg = findViewById(R.id.backImg);
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
