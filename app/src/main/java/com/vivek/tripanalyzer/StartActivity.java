package com.vivek.tripanalyzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    Button Login;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        getSupportActionBar().setTitle("Welcome");

        Login = findViewById(R.id.butLogin);
        Register = findViewById(R.id.butRegister);

        final Intent intent1 = new Intent(this,LoginActivity.class);
        final  Intent intent2 = new Intent(this,RegisterActivity.class);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
