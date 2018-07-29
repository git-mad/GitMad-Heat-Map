package gitmad.gitmadheatmap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.widget.Toast;

public class AlarmOnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent ) {

        // If this is broadcast receiver is started from a boot intent.
        if( intent.getAction().equals( "android.intent.action.BOOT_COMPLETED" ) ) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
            Intent alarmIntent = new Intent( context, AlarmCalledReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast( context.getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT );
            alarmManager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, R.integer.repeat_time, pendingIntent);
        }
    }

}
