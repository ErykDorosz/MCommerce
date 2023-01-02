package com.example.mcommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcommerce.login_register.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        TextView email = findViewById(R.id.emailTextView);
        Button logoutBtn = findViewById(R.id.logoutButton);

        email.setText(firebaseAuth.getCurrentUser().getEmail());

        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }


}