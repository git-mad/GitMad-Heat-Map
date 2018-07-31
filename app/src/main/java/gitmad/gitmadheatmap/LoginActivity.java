package gitmad.gitmadheatmap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private TextView registerHere;
    private EditText passwordEntry;
    private EditText emailEntry;
    private Button signInButton;
    private String username;
    private String password;

    // Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.login_btn_signIn);
        passwordEntry = findViewById(R.id.login_editText_password);
        emailEntry = findViewById(R.id.login_editText_email);
        registerHere = findViewById(R.id.login_txt_register_here);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = emailEntry.getText().toString();
                password = passwordEntry.getText().toString();
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                intent.putExtra("enteredUsername", username);
                intent.putExtra("enteredPassword", password);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( final View view) {
                String email = emailEntry.getText().toString();
                String password = passwordEntry.getText().toString();



                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent( view.getContext(), EnterActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                switch( ((FirebaseAuthException) e).getErrorCode()) {
                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText( LoginActivity.this, R.string.auth_user_not_found, Toast.LENGTH_LONG ).show();
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText( LoginActivity.this, R.string.auth_password_error, Toast.LENGTH_LONG ).show();
                                        break;
                                }
                            }
                        });
            }
        });
    }

    public boolean signInUser(String username, String password) {
        //Sign in user with Firebase, and return if sign in was successful.
        return true;
    }
}
