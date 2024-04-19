package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;
import com.example.bookworm.utilities.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckoutActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Double priceToPay;
    TextInputEditText name, email, address, city, postal, cardHolderName, cardNumber, expiry, security;
    Button btnSubmitCheckout;

    TextView formError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        name = findViewById(R.id.checkoutName);
        email = findViewById(R.id.checkoutEmail);
        address = findViewById(R.id.checkoutAddress);
        city = findViewById(R.id.checkoutCity);
        postal = findViewById(R.id.checkoutPostalCode);
        cardHolderName = findViewById(R.id.checkoutCardholderName);
        cardNumber = findViewById(R.id.checkoutCardNumber);
        expiry = findViewById(R.id.checkoutExpiryDate);
        security = findViewById(R.id.checkoutSecurityCode);
        btnSubmitCheckout = findViewById(R.id.btnSubmitCheckout);
        formError = findViewById(R.id.checkoutFormError);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // set email to user email
        email.setText(user.getEmail());

        Intent i = getIntent();
        double priceToPay = i.getDoubleExtra("priceToPay", 0.0);

        btnSubmitCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset error message
                formError.setVisibility(View.GONE);
                formError.setText("");

                // Retrieve input values
                String nameValue = name.getText().toString().trim();
                String emailValue = email.getText().toString().trim();
                String addressValue = address.getText().toString().trim();
                String cityValue = city.getText().toString().trim();
                String postalValue = postal.getText().toString().trim();
                String cardHolderNameValue = cardHolderName.getText().toString().trim();
                String cardNumberValue = cardNumber.getText().toString().trim();
                String expiryValue = expiry.getText().toString().trim();
                String securityValue = security.getText().toString().trim();

                // Perform input validation
                boolean isValid = true;

                // Name validation
                if (nameValue.isEmpty()) {
                    name.setError("Please enter your name");
                    isValid = false;
                }

                // Email validation
                if (emailValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
                    email.setError("Please enter a valid email address");
                    isValid = false;
                }

                // Address validation
                if (addressValue.isEmpty()) {
                    address.setError("Please enter your address");
                    isValid = false;
                }

                // City validation
                if (cityValue.isEmpty()) {
                    city.setError("Please enter your city");
                    isValid = false;
                }

                // Postal code validation
                if (postalValue.isEmpty() || !Pattern.matches("^[A-Za-z0-9]{3}[\\s]{0,1}[a-zA-Z0-9]{3}$", postalValue)) {
                    postal.setError("Please enter a valid postal code, eg:X1Y2Z3");
                    isValid = false;
                }

                // Cardholder's name validation
                if (cardHolderNameValue.isEmpty()) {
                    cardHolderName.setError("Please enter the cardholder's name");
                    isValid = false;
                }

                // Card number validation
                if (cardNumberValue.isEmpty() || !Pattern.matches("^\\d{16}$", cardNumberValue)) {
                    cardNumber.setError("Please enter a valid 16-digit card number");
                    isValid = false;
                }

                // Expiry date validation
                if (expiryValue.isEmpty() || !Pattern.matches("^(0[1-9]|1[0-2])\\/([0-9]{2})$", expiryValue)) {
                    expiry.setError("Please enter a valid expiry date (MM/YY)");
                    isValid = false;
                }

                // Security code validation
                if (securityValue.isEmpty() || !Pattern.matches("^\\d{3}$", securityValue)) {
                    security.setError("Please enter a valid 3-digit security code");
                    isValid = false;
                }

                // If any field is invalid, display error message
                if (!isValid) {
                    formError.setVisibility(View.VISIBLE);
                    formError.setText("Please correct the errors in the form.");
                } else {
                    // All fields are valid, proceed with submission
                    // Make an order node
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(userId).push();
                    // Get the cart items as a list
                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<CartItem> itemList = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                CartItem cartItem = snapshot.getValue(CartItem.class);
                                cartItem.setId(snapshot.getKey());
                                itemList.add(cartItem);
                            }

                            // Set the value of the "items" node to the list of cart items
                            orderRef.child("items").setValue(itemList);

                            // Set the value of the "priceToPay" field to the calculated price
                            orderRef.child("amount").setValue(priceToPay);

                            // set user info
                            orderRef.child("userInfo").child("name").setValue(nameValue);
                            orderRef.child("userInfo").child("email").setValue(emailValue);
                            orderRef.child("userInfo").child("address").setValue(addressValue);
                            orderRef.child("userInfo").child("city").setValue(cityValue);
                            orderRef.child("userInfo").child("postalCode").setValue(postalValue);
                            orderRef.child("userInfo").child("cardHolderName").setValue(cardHolderNameValue);
                            orderRef.child("userInfo").child("cardNumber").setValue(cardNumberValue);

                            // clear user cart
                            cartRef.removeValue();

                            // go to thank you page
                            startActivity(new Intent(CheckoutActivity.this, ThankYouActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("CheckoutActivity", "Failed to retrieve cart items: " + databaseError.getMessage());
                        }
                    });
                }
            }
        });


        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.cartItem).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ID = item.getItemId();
                bottomNavigationView.getMenu().findItem(ID).setChecked(true);
                if (ID == R.id.productsItem) {
                    startActivity(new Intent(CheckoutActivity.this, ProductsActivity.class));
                    return true;
                } else if (ID == R.id.accountItem) {
                    startActivity(new Intent(CheckoutActivity.this, AccountActivity.class));
                    return true;
                } else {
                    startActivity(new Intent(CheckoutActivity.this, CartActivity.class));
                }
                return false;
            }
        });
    }
}