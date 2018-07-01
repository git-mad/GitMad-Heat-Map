package gitmad.gitmadheatmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
    }

    public void enter_map_activity( View view )
    {
        Intent intent = new Intent( this, MapsActivity.class );
        startActivity( intent );
    }
}
