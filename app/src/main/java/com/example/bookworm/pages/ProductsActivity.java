package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookworm.R;
import com.example.bookworm.utilities.Product;
import com.example.bookworm.utilities.ProductAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView userEmail;
    ImageView logout;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.productsItem).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ID = item.getItemId();
                bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                if (ID == R.id.productsItem) {
                    return true;
                } else if (ID == R.id.cartItem) {
                    startActivity(new Intent(ProductsActivity.this, CartActivity.class));
                    return true;
                } else {
                    startActivity(new Intent(ProductsActivity.this, AccountActivity.class));
                }
                return false;
            }
        });

        // Initialize productList
        productList = new ArrayList<>();

        // Retrieve data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }
                // Notify adapter of data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "Failed to read value.", databaseError.toException());
            }
        });

        // Initialize and set adapter for RecyclerView
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }
}
