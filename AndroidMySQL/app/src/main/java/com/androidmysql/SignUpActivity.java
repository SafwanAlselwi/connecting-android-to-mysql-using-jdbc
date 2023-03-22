package com.androidmysql;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.androidmysql.databinding.ActivitySignUpBinding;
import java.sql.*;
import static com.androidmysql.MainActivity.*;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    private String[] groups = {"G1", "G2", "G3", "G4", "G5", "G6", "G7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGroups.setAdapter(adapter);

        binding.buttonSignUp.setOnClickListener(view -> {
            // Get the input values from the EditText and Spinner views
            String name = binding.editTextName.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String matricID = binding.editTextMatricID.getText().toString();
            String group = binding.spinnerGroups.getSelectedItem().toString();

            // Get the selected gender from the RadioGroup
            int genderId = binding.radioGroupGender.getCheckedRadioButtonId();
            String gender;
            if (genderId == R.id.radioButtonMale) {
                gender = "Male";
            } else if (genderId == R.id.radioButtonFemale) {
                gender = "Female";
            } else {
                gender = "";
            }

            // Perform validation checks on the input values
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || matricID.isEmpty() || gender.isEmpty() || group.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Show the progress bar
                binding.progressBar.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    try {
                        //load the MySQL JDBC driver class dynamically at runtime
                        Class.forName("com.mysql.jdbc.Driver");
                        //establish a connection to a MySQL database using the JDBC driver.
                        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        // Create a prepared statement to insert the user data
                        String query = "INSERT INTO student (name, email, password, matric_id, group_id, gender) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement stmt = con.prepareStatement(query);
                        stmt.setString(1, name);
                        stmt.setString(2, email);
                        stmt.setString(3, password);
                        stmt.setString(4, matricID);
                        stmt.setString(5, group);
                        stmt.setString(6, gender);

                        // Execute the prepared statement to insert the data
                        int rows = stmt.executeUpdate();
                        runOnUiThread(() -> {
                            //Hide the progress bar
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            if (rows > 0) {
                                Toast.makeText(getApplicationContext(), "Student Signed up successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                        con.close();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        binding.goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}