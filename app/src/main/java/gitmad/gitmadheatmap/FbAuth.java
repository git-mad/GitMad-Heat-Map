package gitmad.gitmadheatmap;

import android.content.Context;
import android.content.Intent;
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

    public void createNewUser(final User user, String password) {
        final String email = user.getEmail();
        Toast.makeText( MyApp.getContext(), email, Toast.LENGTH_LONG ).show();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword( email, password );

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String username = emailToUsername( email );
                mDatabase.setReferenceValue( "users/" + username, new User( firstName, lastName, username ) );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
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

    public String getUserUsername() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        return emailToUsername( userEmail );
    }

    public void signUserIn( String email, String password) {

        Task<AuthResult> task = mAuth.signInWithEmailAndPassword( email, password );

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent( MyApp.getContext(), EnterActivity.class );
                MyApp.getContext().startActivity( intent );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
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

    private String emailToUsername( String email ) {
        int at_location = email.indexOf( '@' );
        return email.substring( 0, at_location );
    }
}
