package gitmad.gitmadheatmap;

//import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class ActivityUserLoggedIn extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in);

        // Manage fragment setup
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        FragmentEnter fragmentEnter = new FragmentEnter();
        fragmentTransaction.add( R.id.loggedIn_frame_fragment_container, fragmentEnter );
        fragmentTransaction.commit();

        // Manage DrawerLayout
        setupDrawer();

        // Setup toolbar
        Toolbar toolbar = findViewById( R.id.loggedIn_toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setHomeAsUpIndicator( R.drawable.ic_menu_icon );
    }

    private void setupDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch( item.getItemId() ) {
                            case R.id.nav_home_option:
                                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                                FragmentEnter fragmentEnter = new FragmentEnter();
                                fragmentTransaction.replace( R.id.loggedIn_frame_fragment_container, fragmentEnter );
                                fragmentTransaction.commit();
                        }
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch( item.getItemId() ) {
            case android.R.id.home:
                mDrawerLayout.openDrawer( GravityCompat.START );
                return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
