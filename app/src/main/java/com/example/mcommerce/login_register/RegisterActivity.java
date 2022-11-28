package com.example.mcommerce.login_register;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcommerce.MainActivity;
import com.example.mcommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends LoginActivity
{
    private String email;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        checkIfUserLoggedOut();

        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText registerPassword = findViewById(R.id.registerPassword);
        EditText registerUsername = findViewById(R.id.usernameEditText);
        Button registerBtn = findViewById(R.id.registerBtn);
        TextView cancelBtn = findViewById(R.id.registerCancelBtn);

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = registerEmail.getText().toString().trim();
                password = registerPassword.getText().toString().trim();
                username = registerUsername.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    registerEmail.setError("Email is required!");
                    return;
                }
                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher mat = pattern.matcher(email);
                if (!mat.matches())
                {
                    registerEmail.setError("This is not an email!");
                }

                if (TextUtils.isEmpty(password))
                {
                    registerPassword.setError("Password is required!");
                    return;
                }

                if (password.length() < 6)
                {
                    registerPassword.setError("Password requires at least 6 characters!");
                    return;
                }

                if (TextUtils.isEmpty(username))
                {
                    registerUsername.setError("Username is required!");
                    return;
                }

                pattern = Pattern.compile("^[a-zA-Z0-9 ]+$");
                mat = pattern.matcher(username);
                if (!mat.matches())
                {
                    registerUsername.setError("Username must contain letters and digits only!");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            createUserInDatabase(task.getResult(), username);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Register failure!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    public void createUserInDatabase(AuthResult authResult, String username)
    {
        FirebaseUser rUser = authResult.getUser();
        assert rUser != null;
        rUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void unused)
            {
                navigateToMainActivity();
            }
        });
        DocumentReference documentReference = firebaseFirestore.collection("users").document(rUser.getUid());
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userId", rUser.getUid());
        userMap.put("username", username);
        userMap.put("email", rUser.getEmail());
        documentReference.set(userMap);
    }

    private void checkIfUserLoggedOut()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            firebaseAuth.signOut();
        }
    }

    public void navigateToMainActivity()
    {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}