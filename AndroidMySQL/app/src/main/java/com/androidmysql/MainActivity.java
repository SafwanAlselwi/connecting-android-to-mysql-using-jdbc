package com.androidmysql;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.androidmysql.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    static final String DB_URL = "jdbc:mysql://localhost.net:3306/DB_NAME";
    static final String DB_USER = "DB_USER";
    static final String DB_PASSWORD = "DB_PASSWORD";

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignIn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class))
        );

        binding.btnSignUp.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class))
        );
    }
}