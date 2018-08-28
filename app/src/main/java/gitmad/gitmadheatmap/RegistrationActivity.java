package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import gitmad.gitmadheatmap.firebase.FbAuth;
import gitmad.gitmadheatmap.model.User;


public class RegistrationActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etFirstName;
    private EditText etLastName;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Layout elements.
        btnRegister = findViewById(R.id.register_btn_create_account);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        etPassword = findViewById(R.id.register_editText_password);

        etEmail = findViewById(R.id.register_editText_email);
        // Set information passed in from previous activity if it exists.
        etEmail.setText(getIntent().getStringExtra("email"));

        etFirstName = findViewById(R.id.register_editText_first_name);
        etLastName = findViewById(R.id.register_editText_last_name);
    }

    /**
     * Register a new user within our firebase auth instance.
     */
    public void registerUser() {
        // Layout elements.
        final String email = etEmail.getText().toString().toLowerCase();
        final String password = this.etPassword.getText().toString();
        final String firstName = etFirstName.getText().toString();
        final String lastName = etLastName.getText().toString();

        // If the entered credentials by the user are not valid, then prevent them from registering.
        if (!areCredentialsValid()) {
            return;
        }

        // Create new user in firebase.
        FbAuth auth = new FbAuth();
        auth.createNewUser(new User(firstName, lastName, email), password);

        // Start the UserLoggedIn activity.
        Intent intent = new Intent(this, UserLoggedInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Integer.toString(R.string.intent_menu_item), "nav_home_option");
        startActivity(intent);
    }

    /**
     * Method for checking if the user credentials entered are valid to create a new account.
     *
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean areCredentialsValid() {
        // Store values at the time of the login attempt.
        String email = etEmail.getText().toString();
        String password = this.etPassword.getText().toString();

        // Check if the user entered a etPassword and if it is considered valid.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            notifyUserError("INVALID_PASSWORD");

            return false;
        }

        // Check if there is an entered email, and if so, if it is considered valid.
        if (TextUtils.isEmpty(email)) {
            notifyUserError("EMPTY_EMAIL");

            return false;
        } else if (!isEmailValid(email)) {
            notifyUserError("INVALID_EMAIL");

            return false;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.

        return true;
    }

    /**
     * On a failed attempt to create a new account, notify the user why the account creation failed.
     *
     * @param error Error reason for failed account creation.
     */
    private void notifyUserError(String error) {
        switch (error) {
            case "INVALID_PASSWORD":
                Toast.makeText(RegistrationActivity.this, R.string.reg_invalid_password, Toast.LENGTH_LONG).show();
                break;
            case "EMPTY_EMAIL":
                Toast.makeText(RegistrationActivity.this, R.string.reg_invalid_email, Toast.LENGTH_LONG).show();
                break;
            case "INVALID_EMAIL":
                Toast.makeText(RegistrationActivity.this, R.string.reg_empty_email, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Our check for etPassword validation.
     *
     * @param password The user's etPassword
     * @return etPassword's validity.
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    /**
     * Our check for email validation.
     *
     * @param email The user's email.
     * @return email's validity.
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
