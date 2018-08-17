package gitmad.gitmadheatmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class FbAuth {

    private FirebaseAuth mAuth;
    private FbDatabase mDatabase;

    public FbAuth() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = new FbDatabase();
    }

    /**
     * Create a new user within both our auth and database on Firebase.
     * Auth is specifically for authentication purposes, while the database entry allows us to store
     * more information about the user like their name.
     * @param user The associated user.
     * @param password The user's password.
     */
    public void createNewUser(final User user, String password) {
        final String email = user.getEmail();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();

        // Create new task promise for creating an auth entry.
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword( email, password );

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            /**
             * Create a new database entry if the auth entry succeeds.
             * NOTE: Do not ever store someone's password. That is something not even our eyes deserve to see.
             */
            public void onSuccess(AuthResult authResult) {
                String username = emailToUsername( email );
                User user = new User( firstName, lastName, username );
                mDatabase.setReferenceValue( "users/" + username, new User( firstName, lastName, username ) );

                // Set local shared preferences for user when they create a new account and enter the app.
                setUserPreferences( user );
                // Get our user information and then create local shared preference for user username.

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            /**
             * Notify the user if there was an error when creating a new auth entry.
             */
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( MyApp.getContext(), ( (FirebaseAuthException) e).getErrorCode() , Toast.LENGTH_LONG ).show();
                switch( ((FirebaseAuthException) e).getErrorCode()) {
                    case "ERROR_EMAIL_ALREADY_IN_USE":
                        Toast.makeText( MyApp.getContext(), R.string.auth_email_in_use, Toast.LENGTH_LONG ).show();
                        break;
                    case "ERROR_INVALID_EMAIL":
                        Toast.makeText( MyApp.getContext(), R.string.auth_invalid_email, Toast.LENGTH_LONG ).show();
                        break;
                }
            }
        });
    }

    /**
     * Create a local username reference.
     * This will be helpful when our alarm sounds. Instead of creating a new Auth instance, we can just
     * use this reference instead.
     * @param user the user whose credentials we should be saving
     */
    private void setUserPreferences( User user ) {
        SharedPreferences sharedPreferences = MyApp.getContext().getSharedPreferences( MyApp.getContext().getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create preferences
        editor.putString( MyApp.getContext().getString( R.string.pref_user_username ), user.getEmail() );
        editor.putString( MyApp.getContext().getString( R.string.pref_first_name), user.getFirstName() );
        editor.putString( MyApp.getContext().getString( R.string.pref_last_name), user.getLastName() );

        editor.apply();
    }

    /**
     * Remove the username SharedPreference value.
     * Without the username SharedPreference, we cannot associate a location with a specific user.
     */
    private void removeUsernamePreference() {
        SharedPreferences sharedPreferences = MyApp.getContext().getSharedPreferences( MyApp.getContext().getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove preferences
        editor.remove( MyApp.getContext().getString( R.string.pref_user_username ) );
        editor.remove( MyApp.getContext().getString( R.string.pref_first_name ) );
        editor.remove( MyApp.getContext().getString( R.string.pref_last_name ) );

        editor.apply();
    }

    /**
     * Retrieve the currently logged in user's username.
     * @return Current user username.
     */
    public String getUserUsername() {
        if( isUserLoggedIn() ) {
            String userEmail = mAuth.getCurrentUser().getEmail();
            return emailToUsername( userEmail );
        }
        return "";
    }

    /**
     * Perform an auth login request to log a user into our app.
     * @param email The user's email.
     * @param password The user's password.
     */
    public void signUserIn(String email, String password) {

        // Set our preference.
        mDatabase.getUser( emailToUsername(email), new RetrieveUserCallback() {
            @Override
            public void onFinish(User user) {
                setUserPreferences( user );
            }
        });

        // Create new task promise for signing in a user.
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword( email, password );

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            /**
             * Start the UserLoggedIn activity.
             */
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent( MyApp.getContext(), ActivityUserLoggedIn.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                intent.putExtra( Integer.toString( R.string.intent_menu_item ), "nav_home_option" );
                MyApp.getContext().startActivity( intent );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            /**
             * Notify the user as to why they were unable to login to the app.
             */
            public void onFailure(@NonNull Exception e) {
                switch( ((FirebaseAuthException) e).getErrorCode()) {
                    case "ERROR_USER_NOT_FOUND":
                        Toast.makeText( MyApp.getContext(), R.string.auth_user_not_found, Toast.LENGTH_LONG ).show();
                        break;
                    case "ERROR_WRONG_PASSWORD":
                        Toast.makeText( MyApp.getContext(), R.string.auth_password_error, Toast.LENGTH_LONG ).show();
                        break;
                }
            }
        });
    }

    /**
     * Signs the user out of our auth and returns them to the login screen.
     */
    public void signUserOutAndReturnToLogin() {
        // Prevent from calling when user is not already logged in.
        if( isUserLoggedIn() ) {
            // Sign out from auth instance.
            mAuth.signOut();

            // Remove username preference.
            removeUsernamePreference();
        }

        // Return user to login screen.
        Intent intent = new Intent( MyApp.getContext(), ActivityLogin.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        MyApp.getContext().startActivity( intent );
    }

    /**
     * Converts a user's email into an username.
     * @param email A email address.
     * @return The username that would be associated with the email.
     */
    private String emailToUsername( String email ) {
        int at_location = email.indexOf( '@' );
        return email.substring( 0, at_location );
    }

    /**
     * Indicates if a user is logged in.
     * @return true if the user is currently logged into the auth, false otherwise.
     */
    public boolean isUserLoggedIn() {
        if( mAuth.getCurrentUser() != null ) {
            return true;
        }
        return false;
    }
}
