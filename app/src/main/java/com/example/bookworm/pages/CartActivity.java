package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworm.R;
import com.example.bookworm.utilities.CartAdapter;
import com.example.bookworm.utilities.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CartActivity extends AppCompatActivity {

    List<CartItem> cartItemList;
    CartAdapter cartAdapter;
    TextView taxTextView, totalPriceTextView, priceToPayTextView;
    Button btnGoToBooks, btnPlaceOrder;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    double totalPrice, taxPrice, priceToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView emptyCart = findViewById(R.id.emptyCart);
        ScrollView cartView = findViewById(R.id.scrollView4);

        // cart has items
        btnGoToBooks = findViewById(R.id.btnGoToProducts);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        taxTextView = findViewById(R.id.taxTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        priceToPayTextView = findViewById(R.id.priceToPay);

        btnGoToBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ProductsActivity.class));
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to checkout page
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.cartItem).setChecked(true);

        cartItemList = new ArrayList<>();
        // Retrieve cart items from Firebase Realtime Database
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItemList.clear();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    cartItem.setId(snapshot.getKey());
                    cartItemList.add(cartItem);
                }
                // calculate price and tax
                for (CartItem cartItem : cartItemList) {
                    double itemPrice = cartItem.getPrice() * cartItem.getQuantity();
                    double tax = cartItem.getPrice() * cartItem.getQuantity() * 0.13;
                    totalPrice += itemPrice;
                    taxPrice += tax;
                }

                priceToPay = totalPrice + taxPrice;

                taxTextView.setText(String.format("$ %.2f", taxPrice));
                totalPriceTextView.setText(String.format("$ %.2f", totalPrice));
                priceToPayTextView.setText(String.format("$ %.2f", priceToPay));

                // Pass cartItemList to the adapter and update RecyclerView
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CartActivity", "Failed to read cart items: " + databaseError.getMessage());
            }
        });


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

        // Initialize and set adapter for RecyclerView
        cartAdapter = new CartAdapter(this, cartItemList);
        recyclerView.setAdapter(cartAdapter);
    }
}