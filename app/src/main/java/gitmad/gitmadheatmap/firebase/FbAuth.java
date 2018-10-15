package gitmad.gitmadheatmap.firebase;

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

import gitmad.gitmadheatmap.AppContext;
import gitmad.gitmadheatmap.IRetrieveUserCallback;
import gitmad.gitmadheatmap.LoginActivity;
import gitmad.gitmadheatmap.MainActivity;
import gitmad.gitmadheatmap.R;
import gitmad.gitmadheatmap.model.Friend;
import gitmad.gitmadheatmap.model.User;

public class FbAuth {

    private FirebaseAuth auth;
    private FbDatabase database;

    public FbAuth() {
        auth = FirebaseAuth.getInstance();
        database = new FbDatabase();
    }

    /**
     * Create a new user within both our auth and database on Firebase.
     * Auth is specifically for authentication purposes, while the database entry allows us to store
     * more information about the user like their name.
     *
     * @param user     The associated user.
     * @param password The user's password.
     */
    public void createNewUser(final User user, String password) {
        final String email = user.getEmail();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();

        // Create new task promise for creating an auth entry.
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            /**
             * Create a new database entry if the auth entry succeeds.
             * NOTE: Do not ever store someone's password. That is something not even our eyes deserve to see.
             */
            public void onSuccess(AuthResult authResult) {
                User user = new User(firstName, lastName, email);
                Friend friend = new Friend( firstName, lastName, email, user.getHash() );

                database.setReferenceValue("users/" + user.getUsername(), user);
                database.setReferenceValue( "friends/" + friend.getHash(), friend );

                // Set local shared preferences for user when they create a new account and enter the app.
                setUserPreferences(user);
                // Get our user information and then create local shared preference for user username.

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            /**
             * Notify the user if there was an error when creating a new auth entry.
             */
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppContext.getContext(), ((FirebaseAuthException) e).getErrorCode(), Toast.LENGTH_LONG).show();
                switch (((FirebaseAuthException) e).getErrorCode()) {
                    case "ERROR_EMAIL_ALREADY_IN_USE":
                        Toast.makeText(AppContext.getContext(), R.string.auth_email_in_use, Toast.LENGTH_LONG).show();
                        break;
                    case "ERROR_INVALID_EMAIL":
                        Toast.makeText(AppContext.getContext(), R.string.auth_invalid_email, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    /**
     * Create a local username reference.
     * This will be helpful when our alarm sounds. Instead of creating a new Auth instance, we can just
     * use this reference instead.
     *
     * @param user the user whose credentials we should be saving
     */
    private void setUserPreferences(User user) {
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences(AppContext.getContext().getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create preferences
        editor.putString(AppContext.getContext().getString(R.string.pref_user_username), user.getUsername());
        editor.putString(AppContext.getContext().getString(R.string.pref_user_email), user.getEmail());
        editor.putString(AppContext.getContext().getString(R.string.pref_first_name), user.getFirstName());
        editor.putString(AppContext.getContext().getString(R.string.pref_last_name), user.getLastName());
        editor.putString(AppContext.getContext().getString(R.string.pref_user_friends), user.getFriends());
        editor.apply();
    }

    /**
     * Remove the username SharedPreference value.
     * Without the username SharedPreference, we cannot associate a location with a specific user.
     */
    private void removeUsernamePreference() {
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences(AppContext.getContext().getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove preferences
        editor.remove(AppContext.getContext().getString(R.string.pref_user_username));
        editor.remove(AppContext.getContext().getString(R.string.pref_first_name));
        editor.remove(AppContext.getContext().getString(R.string.pref_last_name));
        editor.remove(AppContext.getContext().getString(R.string.pref_user_email));
        editor.remove(AppContext.getContext().getString(R.string.pref_user_friends));

        editor.apply();
    }

    /**
     * Retrieve the currently logged in user's username.
     *
     * @return Current user username.
     */
    public String getUserUsername() {
        if (isUserLoggedIn()) {
            String userEmail = auth.getCurrentUser().getEmail();
            return emailToUsername(userEmail);
        }
        return "";
    }

    /**
     * Perform an auth login request to log a user into our app.
     *
     * @param email    The user's email.
     * @param password The user's password.
     */
    public void signUserIn(final String email, String password) {
        // Create new task promise for signing in a user.
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    /**
                     * Start the UserLoggedIn activity.
                     */
                    public void onSuccess(AuthResult authResult) {
                        // Set our preference.
                        database.getUser(emailToUsername(email), new IRetrieveUserCallback() {
                            @Override
                            public void onFinish(User user) {
                                setUserPreferences(user);
                            }
                        });

                        Intent intent = new Intent(AppContext.getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(Integer.toString(R.string.intent_menu_item), "nav_home_option");
                        AppContext.getContext().startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            /**
             * Notify the user as to why they were unable to login to the app.
             */
            public void onFailure(@NonNull Exception e) {
                switch (((FirebaseAuthException) e).getErrorCode()) {
                    case "ERROR_USER_NOT_FOUND":
                        Toast.makeText(AppContext.getContext(), R.string.auth_user_not_found, Toast.LENGTH_LONG).show();
                        break;
                    case "ERROR_WRONG_PASSWORD":
                        Toast.makeText(AppContext.getContext(), R.string.auth_password_error, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(AppContext.getContext(), ((FirebaseAuthException) e).getErrorCode(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Signs the user out of our auth and returns them to the login screen.
     */
    public void signUserOutAndReturnToLogin() {
        // Prevent from calling when user is not already logged in.
        if (isUserLoggedIn()) {
            // Sign out from auth instance.
            auth.signOut();

            // Remove username preference.
            removeUsernamePreference();
        }

        // Return user to login screen.
        Intent intent = new Intent(AppContext.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        AppContext.getContext().startActivity(intent);
    }

    /**
     * Converts a user's email into an username.
     *
     * @param email A email address.
     * @return The username that would be associated with the email.
     */
    private String emailToUsername(String email) {
        return email.substring(0, email.indexOf('@'));
    }

//    /**
//     * Converts a user's email into an username.
//     * @param email A email address.
//     * @return The username that would be associated with the email.
//     */
//    private String makeEmailSafeForFB( String email ) {
//        int at_location = email.indexOf( '@' );
//        return email.substring( 0, at_location ) + "::" + email.substring( at_location + 1, email.length() );
//    }

    /**
     * Indicates if a user is logged in.
     *
     * @return true if the user is currently logged into the auth, false otherwise.
     */
    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }
}
