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
 * A fragment for our current home screen that allows the user to start and stop the alarm,
 * as well as enter the map.
 */
public class EnterFragment extends Fragment {

    private Button btnStart;
    private Button btnStop;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public static EnterFragment newInstance() {
        return new EnterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_enter, container, false);

        // Layout elements.
        btnStart = v.findViewById(R.id.enter_btn_start_alarm);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarm();
            }
        });

        btnStop = v.findViewById(R.id.enter_btn_stop_alarm);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
            }
        });

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        pendingIntent = PendingIntent.getBroadcast(
                getActivity(),
                0,
                new Intent(getActivity(), AlarmCalledReceiver.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        // This check is used to see if our alarm is already running.
        // It could be running from our receiver that called it once the phone booted up.
        setAlarmButtons(!isAlarmOn());

        return v;
    }

    /**
     * Starts an instance of the alarmManager for getting the user's location.
     */
    public void startAlarm() {
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 5000,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent);
        setAlarmButtons(false);
    }

    /**
     * Stops an instance of the alarmManager for getting the user's location.
     */
    public void stopAlarm() {
        alarmManager.cancel(pendingIntent);
        setAlarmButtons(true);
    }

    /**
     * Checks if there is a current instance of an alarmManager.
     *
     * @return true if there is a current instance of the alarmManager, false otherwise.
     */
    public boolean isAlarmOn() {
        return PendingIntent.getBroadcast(
                getActivity(), 0,
                new Intent(getActivity(), AlarmCalledReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null;
    }

    /**
     * Enables and disables our start and stop button depending on if the alarm is currently on.
     *
     * @param isAlarmOn If true, set the start alarm button to enable and the stop alarm button to false.
     */
    private void setAlarmButtons(boolean isAlarmOn) {
        btnStart.setEnabled(isAlarmOn);
        btnStop.setEnabled(!isAlarmOn);
    }

}
