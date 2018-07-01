package gitmad.gitmadheatmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface LocationCallback {
    void onFinish( List<LatLng> locations);
}
