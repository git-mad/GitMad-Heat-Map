package gitmad.gitmadheatmap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import gitmad.gitmadheatmap.firebase.FbAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView tvRegisterHere;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignIn;

    // Firebase.
    FbAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase.
        auth = new FbAuth();

        // Create a userId local to the user's app.
        if (getUserPrefId() == null) {
            createUserPrefId();
        }

        // We should test to see if our alarm is on as soon as someone enters the app,
        // and turn on the alarm if it is off.
        if (!isAlarmOn()) {
            startAlarm();
        }

        // If user is already logged in, transition them to the UserLoggedIn activity
        if (auth.isUserLoggedIn()) {
            transitionToEnterActivity();
        }

        // Layout elements.
        btnSignIn = findViewById(R.id.login_btn_signIn);
        etPassword = findViewById(R.id.login_editText_password);
        etEmail = findViewById(R.id.login_editText_email);
        tvRegisterHere = findViewById(R.id.login_btn_register_here);

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String email = etEmail.getText().toString().toLowerCase();
                String password = etPassword.getText().toString();

                auth.signUserIn(email, password);
            }
        });
    }

    /**
     * Method for transitioning from the current activity to the UserLoggedIn activity when the user
     * is already signed in and opening the app.
     */
    private void transitionToEnterActivity() {
        Intent intent = new Intent(this, UserLoggedInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Integer.toString(R.string.intent_menu_item), "nav_home_option");
        startActivity(intent);
    }

    /**
     * Method used by a layout button to enter into the app without creating an account.
     *
     * @param view
     */
    public void enterAppWithoutSigningIn(View view) {
        transitionToEnterActivity();
    }

    /**
     * Grab the user id shared preference if one exists.
     *
     * @return
     */
    private String getUserPrefId() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.pref_user_id), null);
    }

    /**
     * Create a new userId SharedPreference value.
     * This value is used so that we can still upload user's locations anonymously.
     */
    private void createUserPrefId() {
        String userId = UUID.randomUUID().toString().substring(0, 10);
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_user_id), userId);
        editor.apply();
    }

    /**
     * Starts an instance of the alarmManager for getting the user's location.
     */
    public void startAlarm() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmCalledReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    /**
     * Checks if there is a current instance of an alarmManager.
     *
     * @return true if there is a current instance of the alarmManager, false otherwise.
     */
    public boolean isAlarmOn() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmCalledReceiver.class), PendingIntent.FLAG_NO_CREATE) != null;
    }
}
