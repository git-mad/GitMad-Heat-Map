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


public class ActivityRegistration extends AppCompatActivity {

    private Button registerButton;
    private EditText passwordEntry;
    private EditText emailEntry;
    private EditText fNameEntry;
    private EditText lNameEntry;
    private String email;
    private String password;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Layout elements.
        registerButton = findViewById(R.id.register_btn_create_account);
        passwordEntry = findViewById(R.id.register_editText_password);
        emailEntry = findViewById(R.id.register_editText_email);
        fNameEntry = findViewById(R.id.register_editText_first_name);
        lNameEntry = findViewById(R.id.register_editText_last_name);

        // Set information passed in from previous activity if it exists.
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        emailEntry.setText(email);
        passwordEntry.setText(password);

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    /**
     * Register a new user within our firebase auth instance.
     */
    public void registerUser() {
        // Layout elements.
        final String email = emailEntry.getText().toString().toLowerCase();
        final String password = passwordEntry.getText().toString();
        final String firstName = fNameEntry.getText().toString();
        final String lastName = lNameEntry.getText().toString();

        // If the entered credentials by the user are not valid, then prevent them from registering.
        if (!areCredentialsValid()) {
            return;
        }

        // Create new user in firebase.
        FbAuth mAuth = new FbAuth();
        mAuth.createNewUser(new User(firstName, lastName, email), password);

        // Start the UserLoggedIn activity.
        Intent intent = new Intent(this, ActivityUserLoggedIn.class);
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
        String email = emailEntry.getText().toString();
        String password = passwordEntry.getText().toString();

        // Check if the user entered a password and if it is considered valid.
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
                Toast.makeText(ActivityRegistration.this, R.string.reg_invalid_password, Toast.LENGTH_LONG).show();
                break;
            case "EMPTY_EMAIL":
                Toast.makeText(ActivityRegistration.this, R.string.reg_invalid_email, Toast.LENGTH_LONG).show();
                break;
            case "INVALID_EMAIL":
                Toast.makeText(ActivityRegistration.this, R.string.reg_empty_email, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * Our check for password validation.
     *
     * @param password The user's password
     * @return password's validity.
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
