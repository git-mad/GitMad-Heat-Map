package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText usernameEntry;
    private EditText passwordEntry;
    private EditText emailEntry;
    private EditText fNameEntry;
    private EditText mNameEntry;
    private EditText lNameEntry;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerButton = findViewById(R.id.register_btn_create_account);
        passwordEntry = findViewById(R.id.register_editText_password);
        emailEntry = findViewById(R.id.register_editText_email);
        fNameEntry = findViewById(R.id.register_editText_first_name);
        lNameEntry = findViewById(R.id.register_editText_last_name);

        Intent intent = getIntent();
        email = intent.getStringExtra("enteredUsername");
        password = intent.getStringExtra("enteredPassword");
        emailEntry.setText(email);
        passwordEntry.setText(password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser( emailEntry.getText().toString(), passwordEntry.getText().toString() );
            }
        });
    }

    public boolean registerUser( String email, String password) {
        //Register the username, password, and stuff with the Firebase.
        return true;
    }

}
