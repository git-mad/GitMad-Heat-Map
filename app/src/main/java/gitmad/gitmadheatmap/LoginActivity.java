package gitmad.gitmadheatmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {


    private TextView registerHere;
    private EditText passwordEntry;
    private EditText emailEntry;
    private Button signInButton;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.login_btn_signIn);
        passwordEntry = findViewById(R.id.login_editText_password);
        emailEntry = findViewById(R.id.login_editText_email);
        registerHere = findViewById(R.id.login_txt_register_here);

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
            public void onClick(View view) {
                username = emailEntry.getText().toString();
                Toast.makeText( LoginActivity.this , username, Toast.LENGTH_LONG ).show();
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
