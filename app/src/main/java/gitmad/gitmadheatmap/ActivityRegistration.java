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


public class ActivityRegistration extends AppCompatActivity {

    private Button registerButton;
    private EditText passwordEntry;
    private EditText emailEntry;
    private EditText fNameEntry;
    private EditText lNameEntry;
    private String email;

    // Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

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

        // TODO 1.2 (optional): If you passed in the user's email with the intent, grab it here and populate our emailEntry element with the email value.

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    // TODO 2: Before you go on, add a onClick attribute to the button with id register_btn_create_account in activity_registration.xml
    // The onClick attribute should reference the registerUser button so we can call it when the button is pressed.
    /**
     * Register a new user within our firebase auth instance.
     */
    public void registerUser( View view) {
        // Layout elements.
        final String email = emailEntry.getText().toString();
        final String password = passwordEntry.getText().toString();
        final String firstName = fNameEntry.getText().toString();
        final String lastName = lNameEntry.getText().toString();

        // TODO 3: Create a toast here that will display the user's email.

        // TODO 4: Hey, while we are logging information let's actually log it! Log the user's email as well.

        // If the entered credentials by the user are not valid, then prevent them from registering.
        if( !areCredentialsValid() ) {
            return;
        }

        // Create new user in firebase.
        FbAuth mAuth = new FbAuth();
        mAuth.createNewUser( new User( firstName, lastName, email), password);

        // TODO 5: Start the ActivityUserLoggedIn activity here. No need to pass anything extra.
        // TODO 5.1 (optional): Bonus points if you lookup intent flags and make it so that when the user presses the back button, they do not come back to this screen (remove back stack).
    }

    /**
     * Method for checking if the user credentials entered are valid to create a new account.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean areCredentialsValid() {
        // Store values at the time of the login attempt.
        String email = emailEntry.getText().toString();
        String password = passwordEntry.getText().toString();

        // Check if the user entered a password and if it is considered valid.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            notifyUserError( "INVALID_PASSWORD" );

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

        return true;
    }

    /**
     * On a failed attempt to create a new account, notify the user why the account creation failed.
     * @param error Error reason for failed account creation.
     */
    private void notifyUserError( String error ) {
        switch( error) {
            case "INVALID_PASSWORD":
                Toast.makeText( ActivityRegistration.this, R.string.reg_invalid_password, Toast.LENGTH_LONG ).show();
                break;
            case "EMPTY_EMAIL":
                Toast.makeText( ActivityRegistration.this, R.string.reg_invalid_email, Toast.LENGTH_LONG ).show();
                break;
            case "INVALID_EMAIL":
                Toast.makeText( ActivityRegistration.this, R.string.reg_empty_email, Toast.LENGTH_LONG ).show();
                break;
        }
    }

    /**
     * Our check for password validation.
     * @param password The user's password
     * @return password's validity.
     */
    private boolean isPasswordValid( String password ) {
        return password.length() > 5;
    }

    /**
     * Our check for email validation.
     * @param email The user's email.
     * @return email's validity.
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
