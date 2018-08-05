package gitmad.gitmadheatmap;

//import android.app.FragmentManager;
import android.content.Intent;
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
import android.view.View;
import android.widget.SlidingDrawer;

public class ActivityUserLoggedIn extends AppCompatActivity {

    private FragmentManager mFragmentManager;

    private DrawerLayout mDrawerLayout;
    private Intent drawerIntent;

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

    /**
     * Method to set up the navigation drawer for this activity.
     */
    private void setupDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // We initialize this listener mainly for its ability to remove lag when changing activities.
        // By waiting for the drawer to fully close, the animation looks smooth and cleanly executed.
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if( drawerIntent != null) {
                    startActivity( drawerIntent );
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //
            }
        });

        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setCheckedItem( getMenuItemSelected() );

        // A listener that handles onClicks for menu items in the navigation drawer.
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
                                break;
                            case R.id.nav_map_option:
                                Intent intent = new Intent( MyApp.getContext(), ActivityHeatMap.class );
                                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                drawerIntent = intent;
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

    /**
     * Obtains the drawer menu item selected (pasesed from previous activity) and returns that value.
     * @return Passes the int value fo the id for the menu item we want to be preselected
     */
    private int getMenuItemSelected() {
        String selectedMenuItem = getIntent().getStringExtra( Integer.toString( R.string.intent_menu_item ) );
        switch( selectedMenuItem ) {
            case "nav_home_option":
                return R.id.nav_home_option;
        }

        return R.id.nav_home_option;
    }
}
