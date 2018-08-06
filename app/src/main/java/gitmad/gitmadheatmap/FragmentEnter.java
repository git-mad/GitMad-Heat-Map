package gitmad.gitmadheatmap;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEnter extends Fragment implements View.OnClickListener {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Button startButton;
    private Button stopButton;
    private Button mapButton;
    private Button logoutButton;

    public FragmentEnter() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        startButton = getView().findViewById( R.id.enter_btn_start_alarm);
        stopButton = getView().findViewById( R.id.enter_btn_stop_alarm);
        mapButton = getView().findViewById( R.id.enter_btn_enter_map );
        logoutButton = getView().findViewById( R.id.enter_btn_logout);

        startButton.setOnClickListener( this );
        stopButton.setOnClickListener( this );
        mapButton.setOnClickListener( this );
        logoutButton.setOnClickListener( this );


        // This check is used to see if our alarm is already running.
        // It could be running from our receiver that called it once the phone booted up.
        if( isAlarmOn() ) {
            setAlarmButtons( false );
        } else {
            setAlarmButtons( true );
        }
    }

    @Override
    public void onClick( View view ) {
        switch( view.getId() )  {
            case R.id.enter_btn_enter_map:
                enter_map_activity( view );
                break;
            case R.id.enter_btn_start_alarm:
                start_alarm( view );
                break;
            case R.id.enter_btn_stop_alarm:
                stop_alarm( view );
                break;
            case R.id.enter_btn_logout:
                logoutUser( view );
                break;
        }
    }

    public void enter_map_activity( View view )
    {
        Intent intent = new Intent( getActivity(), ActivityHeatMap.class );
        startActivity( intent );
    }

    public void start_alarm( View view )
    {
        alarmManager = (AlarmManager) getActivity().getSystemService( Context.ALARM_SERVICE );
        Intent alarmIntent = new Intent( getActivity(), AlarmCalledReceiver.class);

        pendingIntent = PendingIntent.getBroadcast( getActivity().getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        setAlarmButtons( false );

    }

    public void stop_alarm( View view ) {
        alarmManager = (AlarmManager) getActivity().getSystemService( Context.ALARM_SERVICE );
        Intent alarmIntent = new Intent( getActivity(), AlarmCalledReceiver.class);

        pendingIntent = PendingIntent.getBroadcast( getActivity(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmManager.cancel( pendingIntent );
        startButton.setEnabled( true );
        stopButton.setEnabled( false );
        setAlarmButtons( true );
    }

    public boolean isAlarmOn() {
        return PendingIntent.getBroadcast( getActivity(), 0, new Intent( getActivity(), AlarmCalledReceiver.class ), PendingIntent.FLAG_NO_CREATE ) != null;
    }

    // If true, set the start alarm button to enable and the stop alarm button to false.
    private void setAlarmButtons( boolean alarm_on ) {
        startButton.setEnabled( alarm_on );
        stopButton.setEnabled( !alarm_on );
    }

    public void logoutUser( View view ) {
        FbAuth mAuth = new FbAuth();
        mAuth.signUserOutAndReturnToLogin();
    }

}
