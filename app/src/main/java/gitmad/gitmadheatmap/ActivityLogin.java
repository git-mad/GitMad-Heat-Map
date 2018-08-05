package gitmad.gitmadheatmap;

import android.content.Context;
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
    private String username;
    private String password;

    private Context mContext;

    // Firebase
    FbAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase
        mAuth = new FbAuth();

        // Activity context
        mContext = this;

        if( mAuth.isUserLoggedIn() ) {
            transitionToEnterActivity();
        }

        signInButton = findViewById(R.id.login_btn_signIn);
        passwordEntry = findViewById(R.id.login_editText_password);
        emailEntry = findViewById(R.id.login_editText_email);
        registerHere = findViewById(R.id.login_txt_register_here);


        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = emailEntry.getText().toString();
                password = passwordEntry.getText().toString();
                Intent intent = new Intent(view.getContext(), ActivityRegistration.class);
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

                mAuth.signUserIn( email, password );
            }
        });
    }

    private void transitionToEnterActivity() {
        Intent intent = new Intent( this, ActivityUserLoggedIn.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intent );
    }
}
