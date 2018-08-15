package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class ActivityRegistration extends AppCompatActivity {

    private EditText emailEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Layout elements.
        emailEntry = findViewById(R.id.register_editText_email);

        // Set information passed in from previous activity if it exists.
        Intent intent = getIntent();
        String email = intent.getStringExtra("username");
        emailEntry.setText(email);
    }
}
