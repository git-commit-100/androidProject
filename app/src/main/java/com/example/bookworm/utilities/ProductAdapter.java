package com.example.bookworm.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookworm.R;
import com.example.bookworm.pages.ProductDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewPrice.setText(String.format("$ %.02f", product.getPrice()));

        // Load image using Glide library
        Glide.with(context)
                .load(product.getImgUrl())
                .into(holder.imageViewProduct);

        // Set OnClickListener to handle product item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to start ProductDetailsActivity
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                // Pass product details as extras
                intent.putExtra("title", product.getTitle());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("imgUrl", product.getImgUrl());
                // Start activity
                context.startActivity(intent);
            }
        });

        // Add OnClickListener to the "Add to Cart" button
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store the clicked product in Firebase Realtime Database under the "cart" node
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
                addProductToCart(cartRef, product);
            }
        });
    }

    public void addProductToCart(DatabaseReference cartRef, Product product) {
        Query query = cartRef.orderByChild("title").equalTo(product.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Product already exists in the cart -> update its quantity
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product existingProduct = snapshot.getValue(Product.class);
                        int currentQuantity = existingProduct.getQuantity();
                        existingProduct.setQuantity(currentQuantity + 1); // Increment quantity
                        snapshot.getRef().setValue(existingProduct);
                    }
                    Toast.makeText(context, "Product quantity updated", Toast.LENGTH_SHORT).show();
                } else {
                    // Product doesn't exist in the cart -> add it with quantity 1
                    product.setQuantity(1);
                    String cartItemId = cartRef.push().getKey();
                    cartRef.child(cartItemId).setValue(product)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("ProductAdapter", "Failed to add product to cart: " + e.getMessage());
                                    Toast.makeText(context, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProductAdapter", "Database error: " + databaseError.getMessage());
                Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewTitle, textViewDescription, textViewPrice;
        Button addToCartBtn;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            addToCartBtn = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}


