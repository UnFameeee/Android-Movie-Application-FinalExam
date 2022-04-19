package com.android.movie_application.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.movie_application.R;
import com.android.movie_application.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private EditText editTextFname,editTextLname,editTextEmail,editTextPass,editTextRepass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        editTextFname = (EditText) findViewById(R.id.editTextFname);
        editTextLname = (EditText) findViewById(R.id.editTextLname);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextRepass = (EditText) findViewById(R.id.editTextRepass);
    }

    public void register(View view)
    {
        validate();
    }

    private void validate()
    {
        String fname,lname,email,pass,repass;
        fname = editTextFname.getText().toString().trim();
        lname = editTextLname.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        pass = editTextPass.getText().toString().trim();
        repass = editTextRepass.getText().toString().trim();
        if (fname.isEmpty())
        {
            editTextFname.setError("First name is required!");
            editTextFname.requestFocus();
            return;
        }
        if (lname.isEmpty())
        {
            editTextLname.setError("Last name is required!");
            editTextLname.requestFocus();
            return;
        }
        if (email.isEmpty())
        {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if (pass.isEmpty())
        {
            editTextPass.setError("Password is required!");
            editTextPass.requestFocus();
            return;
        }
        if (repass.isEmpty())
        {
            editTextRepass.setError("Re-password is required!");
            editTextRepass.requestFocus();
            return;
        }
        if (!pass.equals(repass))
        {
            editTextRepass.setError("Password is not correct!");
            editTextRepass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Email type is invalid!");
            editTextEmail.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    User user = new User(fname,lname,email,pass);
                    FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this,"Register successfully",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Failed to register",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}