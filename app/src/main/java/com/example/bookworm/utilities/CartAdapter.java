package com.example.bookworm.utilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookworm.R;
import com.example.bookworm.pages.CartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {
    private List<CartItem> cartItemList;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    public CartAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CartItem cartItem = cartItemList.get(position);

        // Display the cart item
        holder.productTitle.setText(cartItem.getTitle());
        holder.productPrice.setText("$ " + cartItem.getPrice());
        holder.productQuantity.setText("X " + cartItem.getQuantity());

        // Load image using Glide library
        Glide.with(context)
                .load(cartItem.getImgUrl())
                .into(holder.productImg);

        holder.btnIncrementBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int updatedQuantity = cartItem.getQuantity() + 1;
                cartItem.setQuantity(updatedQuantity);
                notifyItemChanged(position);
                updateCartItem(cartItem);
                // updating the prices as treating adapter as instance of activity class
                ((CartActivity) context).calculateTotalPriceAndTax();
            }
        });

        holder.btnDecrementBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int updatedQuantity = cartItem.getQuantity() - 1;
                if (updatedQuantity == 0) {
                    // Remove the item from the cart if the quantity becomes 1
                    cartItemList.remove(position);
                    notifyItemRemoved(position);
                    deleteCartItem(cartItem);
                } else {
                    cartItem.setQuantity(updatedQuantity);
                    notifyItemChanged(position);
                    updateCartItem(cartItem);
                }
                ((CartActivity) context).calculateTotalPriceAndTax();
            }
        });
    }

    // Method to update the cart item in Firebase Realtime Database
    private void updateCartItem(CartItem cartItem) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
        userCartRef.child(cartItem.getId()).setValue(cartItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {

                        // Notify the adapter that the data has changed
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CartAdapter", "Failed to update cart item: " + e.getMessage());
                    }
                });
    }

    // Method to delete the cart item from Firebase Realtime Database
    private void deleteCartItem(CartItem cartItem) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
        userCartRef.child(cartItem.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Notify the adapter that the data has changed
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CartAdapter", "Failed to delete cart item: " + e.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, productPrice, productQuantity;
        ImageView productImg;
        Button btnIncrementBook, btnDecrementBook;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.cartItemTitle);
            productPrice = itemView.findViewById(R.id.cartItemPrice);
            productQuantity = itemView.findViewById(R.id.cartItemQuantity);
            productImg = itemView.findViewById(R.id.cartItemImg);
            btnIncrementBook = itemView.findViewById(R.id.cartAdd1);
            btnDecrementBook = itemView.findViewById(R.id.cartRemove1);
        }
    }
}
