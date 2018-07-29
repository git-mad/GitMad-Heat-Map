package gitmad.gitmadheatmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private TextView registerHere;
    private EditText passwordEntry;
    private EditText usernameEntry;
    private Button signInButton;
    private String username;
    private String password;
    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.signIn);
        passwordEntry = findViewById(R.id.passwordEntry);
        usernameEntry = findViewById(R.id.usernameEntry);
        registerHere = findViewById(R.id.registerText);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEntry.getText().toString();
                password = passwordEntry.getText().toString();
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                intent.putExtra("enteredUsername", username);
                intent.putExtra("enteredPassword", password);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEntry.getText().toString();
                password = passwordEntry.getText().toString();
                if (signInUser(username, password)) {
                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    startActivity(intent);
                } else {
                    //Light the text boxes up and display a toast
                }
            }
        });
    }

    public boolean signInUser(String username, String password) {
        //Sign in user with Firebase, and return if sign in was successful.
        return true;
    }
}
