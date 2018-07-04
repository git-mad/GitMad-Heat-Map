package gitmad.gitmadheatmap;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AlarmCalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent ) {
        // If there is no connection we do not want to send off a request to get user location data
        // Often the task will hang for a while and will stack if they are not stopped. This is one way to stop queuing tasks.
        if( isNetworkAvailable( context ) ) {
            updateUserLocation( context );
        }
    }

    // Checks if we have an internet connection.
    private boolean isNetworkAvailable( Context context ) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Add a users's location to our database.
    private void updateUserLocation(final Context context ) {
        if(ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

            final Task locationResult = mFusedLocationProviderClient.getLastLocation();

            int numCores = Runtime.getRuntime().availableProcessors();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(numCores * 2, numCores * 2, 15L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

            locationResult.addOnCompleteListener(executor, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Send the user's location information to our database.
                        Location mLastKnownLocation;
                        mLastKnownLocation = (Location) task.getResult();
                        LatLng mCoordinates = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        FirebaseInstance mDatabase = new FirebaseInstance();
                        mDatabase.addLocation(mCoordinates);
                    } else {
                        FirebaseInstance mDatabase = new FirebaseInstance();
                        mDatabase.cantAddLocation("Failed for some reason.");
                    }
                }
            });
        }
    }
}
