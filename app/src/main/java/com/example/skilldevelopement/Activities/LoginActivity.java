package com.example.skilldevelopement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skilldevelopement.DialogFragment.ForgotPasswordDialogue;
import com.example.skilldevelopement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialogue.SendResetPassword {

    TextInputEditText emailEditText, passwordEditText;
    String email, password;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    Button loginButton;
    TextView forgotPasswordTextView, signUpTextView;
    FirebaseAuth mAuth;
    String resetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginBt);
        forgotPasswordTextView = findViewById(R.id.forgotPass);
        signUpTextView = findViewById(R.id.notHaveAnAcc);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordDialogue forgotPasswordDialogue=new ForgotPasswordDialogue();
                forgotPasswordDialogue.show(getSupportFragmentManager(),forgotPasswordDialogue.getTag());
            }
        });

    }

    public void SignInBt(View view) {

        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        progressDialog.setTitle("logging in...");
        progressDialog.show();
        progressDialog.setMessage("Please wait...\n Be patient");
        progressDialog.setCancelable(false);
        LoginUser(email, password);

    }

    private void LoginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String uid = firebaseUser.getUid();
                            final String device_token = FirebaseInstanceId.getInstance().getToken();
                         DatabaseReference  userDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                           databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                                   .child("device_token");
                           databaseReference.setValue(device_token);

                           userDatabaseReference.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        if (snapshot.hasChild("type")){
                                            String type=snapshot.child("type").getValue(String.class);
                                            try {
                                                if (type.equals("Admin")){
                                                    // Sign in success, update UI with the signed-in user's information
                                                    startActivity(new Intent(LoginActivity.this,AdminPanel.class));
                                                    finish();
                                                }else {
                                                    startActivity(new Intent(LoginActivity.this,HomePage.class));
                                                    finish();
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {
                                   error.toException().printStackTrace();
                                   Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void ResetEmail(String email) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Sending email");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//       resetEmail=email;
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "We have sent you a password reset link in your email link. Please check your inbox or spam box", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error!!! "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}