package gitmad.gitmadheatmap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Button startButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        startButton = findViewById( R.id.enter_btn_start_alarm);
        stopButton = findViewById( R.id.enter_btn_stop_alarm);

        // This check is used to see if our alarm is already running.
        // It could be running from our receiver that called it once the phone booted up.
        if( isAlarmOn() ) {
            setAlarmButtons( false );
        } else {
            setAlarmButtons( true );
        }
    }

    public void enter_map_activity( View view )
    {
        Intent intent = new Intent( this, MapsActivity.class );
        startActivity( intent );
    }

    public void start_alarm( View view )
    {
        alarmManager = (AlarmManager) this.getSystemService( Context.ALARM_SERVICE );
        Intent alarmIntent = new Intent( this, AlarmCalledReceiver.class);

        pendingIntent = PendingIntent.getBroadcast( this.getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        setAlarmButtons( false );

    }

    public void stop_alarm( View view ) {
        alarmManager = (AlarmManager) this.getSystemService( Context.ALARM_SERVICE );
        Intent alarmIntent = new Intent( this, AlarmCalledReceiver.class);

        pendingIntent = PendingIntent.getBroadcast( this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.cancel( pendingIntent );
        startButton.setEnabled( true );
        stopButton.setEnabled( false );
        setAlarmButtons( true );
    }

    public boolean isAlarmOn() {
        return PendingIntent.getBroadcast( this, 0, new Intent( this, AlarmCalledReceiver.class ), PendingIntent.FLAG_NO_CREATE ) != null;
    }

    // If true, set the start alarm button to enable and the stop alarm button to false.
    private void setAlarmButtons( boolean alarm_on ) {
        startButton.setEnabled( alarm_on );
        stopButton.setEnabled( !alarm_on );
    }
}
