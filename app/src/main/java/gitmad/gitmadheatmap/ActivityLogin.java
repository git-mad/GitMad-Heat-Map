package gitmad.gitmadheatmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLogin extends AppCompatActivity {


    private TextView registerHere;
    private EditText passwordEntry;
    private EditText emailEntry;
    private Button signInButton;

    // Firebase.
    FbAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase.
        mAuth = new FbAuth();

         // If user is already logged in, transition them to the UserLoggedIn activity
        if( mAuth.isUserLoggedIn() ) {
            transitionToEnterActivity();
        }

        // Layout elements.
        signInButton = findViewById(R.id.login_btn_signIn);
        passwordEntry = findViewById(R.id.login_editText_password);
        emailEntry = findViewById(R.id.login_editText_email);
        registerHere = findViewById(R.id.login_btn_register_here);

        // TODO 1: Create a new OnClickListener for our register button. This button should start the ActivityRegistration Activity.
        // TODO 1.1 (optional): Pass along the user's email in this intent.

        // TODO 5: Create a OnClickListener for our login button.
        // We will not handle the intent logic here. It is handled in the mAuth class. Look in that class and see what method you should call to transition to the ActivityUserLoggedIn activity.

    }

    /**
     * Method for transitioning from the current activity to the UserLoggedIn activity when the user
     * is already signed in and opening the app.
     */
    private void transitionToEnterActivity() {
        Intent intent = new Intent( this, ActivityUserLoggedIn.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intent );
    }

    /**
     * Method used by a layout button to enter into the app without creating an account.
     * @param view
     */
    public void enterAppWithoutSigningIn( View view ) {
        transitionToEnterActivity();
    }

}
