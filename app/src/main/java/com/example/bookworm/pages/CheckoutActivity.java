package com.example.bookworm.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookworm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class CheckoutActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    TextInputEditText name, email, address, city, postal, cardHolderName, cardNumber, expiry, security;
    Button btnSubmitCheckout;

    TextView formError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        // set email to user email
        email.setText(user.getEmail());

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
                if (postalValue.isEmpty() || !Pattern.matches("^\\d{5}(?:[-\\s]\\d{4})?$", postalValue)) {
                    postal.setError("Please enter a valid postal code");
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
                if (expiryValue.isEmpty() || !Pattern.matches("^\\d{2}/\\d{2}$", expiryValue)) {
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
                    // Your submission logic here
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
                }
                return false;
            }
        });
    }
}