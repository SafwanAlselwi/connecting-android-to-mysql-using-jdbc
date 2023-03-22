package com.androidmysql;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.androidmysql.databinding.ActivitySignInBinding;

import java.sql.*;

import static com.androidmysql.MainActivity.*;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the input values from the EditText
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if (isValidEmail(email) && password.length() > 0) {
                    // Show the progress bar
                    binding.progressBar.setVisibility(View.VISIBLE);

                    new Thread(() -> {
                        try {
                            //load the MySQL JDBC driver class dynamically at runtime
                            Class.forName("com.mysql.jdbc.Driver");
                            //establish a connection to a MySQL database using the JDBC driver
                            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                            String query = "SELECT * FROM student WHERE email = ? AND password = ?";
                            PreparedStatement stmt = con.prepareStatement(query);
                            stmt.setString(1, email);
                            stmt.setString(2, password);

                            ResultSet rs = stmt.executeQuery();
                            boolean exists = rs.next();
                            runOnUiThread(() -> {
                                //Hide the progress bar
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                if (exists) {
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                                }
                            });
                            con.close();
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Add your own email validation logic here
        return email.contains("@");
    }
}