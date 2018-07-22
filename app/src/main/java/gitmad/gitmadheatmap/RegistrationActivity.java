package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private EditText emailEntry;
    private EditText fNameEntry;
    private EditText mNameEntry;
    private EditText lNameEntry;
    private String username;
    private String password;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerButton = findViewById(R.id.registerButton);
        usernameEntry = findViewById(R.id.username_entry);
        passwordEntry = findViewById(R.id.password_entry);
        emailEntry = findViewById(R.id.email_entry);
        fNameEntry = findViewById(R.id.first_name_entry);
        mNameEntry = findViewById(R.id.middle_name_entry);
        lNameEntry = findViewById(R.id.last_name_entry);

        Intent intent = getIntent();
        username = intent.getStringExtra("enteredUsername");
        password = intent.getStringExtra("enteredPassword");
        usernameEntry.setText(username);
        passwordEntry.setText(password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEntry.getText().toString();
                password = passwordEntry.getText().toString();
                String firstName = fNameEntry.getText().toString();
                String lastName = lNameEntry.getText().toString();
                String middleName = mNameEntry.getText().toString();
                String email = emailEntry.getText().toString();

                User newUser = new User(username, password, email, firstName,
                                        middleName, lastName);
                if (registerUser(newUser)) {
                    Intent intent = new Intent(view.getContext(), EnterActivity.class);
                    startActivity(intent);
                } else {
                    //There was an issue with registration. Display a toast.
                }
            }
        });
    }

    public boolean registerUser(User newUser) {
        //Register the username, password, and stuff with the Firebase.
        return true;
    }

}
