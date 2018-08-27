package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import gitmad.gitmadheatmap.firebase.FbAuth;

public class ActivityUserLoggedIn extends AppCompatActivity {

    // Fragment variables.
    private FragmentManager fragmentManager;

    // DrawerLayout variables.
    private DrawerLayout drawerLayout;
    private Intent drawerIntent;
    private boolean logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in);

        // Manage fragment setup
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Set the fragment that the activity initializes to.
        fragmentTransaction.add(R.id.loggedIn_frame_fragment_container, getFragmentToBeDisplayed());
        fragmentTransaction.commit();

        // Manage DrawerLayout
        setupDrawer();

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.loggedIn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);
    }

    /**
     * Add DrawerLayout listener and add navigation view listener.
     */
    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);

        // Set this to false. We use this boolean later to mark that the user has tried to logout and we should fulfill this request.
        // This is used similarly to the drawerIntent variable. It prevents lag with the drawer screen.
        logout = false;

        // We initialize this listener mainly for its ability to remove lag when changing activities.
        // By waiting for the drawer to fully close, the animation looks smooth and cleanly executed.
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerIntent != null) {
                    startActivity(drawerIntent);
                } else if (logout) {
                    FbAuth auth = new FbAuth();
                    auth.signUserOutAndReturnToLogin();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set the current fragment as the selected item in our nav menu.
        navigationView.setCheckedItem(getMenuItemSelected());

        // A listener that handles onClicks for menu items in the navigation drawer.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    FragmentTransaction fragmentTransaction;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_home_option:
                                fragmentTransaction = fragmentManager.beginTransaction();

                                FragmentEnter fragmentEnter = FragmentEnter.newInstance();
                                fragmentTransaction.replace(R.id.loggedIn_frame_fragment_container, fragmentEnter);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_map_option:
                                Intent intent = new Intent(AppContext.getContext(), ActivityHeatMap.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                drawerIntent = intent;
                                break;
                            case R.id.nav_settings_option:
                                fragmentTransaction = fragmentManager.beginTransaction();

                                FragmentSettings fragmentSettings = FragmentSettings.newInstance();
                                fragmentTransaction.replace(R.id.loggedIn_frame_fragment_container, fragmentSettings);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_logout_option:
                                logout = true;
                                break;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    /**
     * Handles all toolbar interactions.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Obtains the drawer menu item selected (passed from previous activity) and returns that value.
     *
     * @return The int value for the id for the menu item we want to be selected.
     */
    private int getMenuItemSelected() {
        String selectedMenuItem = getIntent().getStringExtra(Integer.toString(R.string.intent_menu_item));
        switch (selectedMenuItem) {
            case "nav_home_option":
                return R.id.nav_home_option;
            case "nav_settings_option":
                return R.id.nav_settings_option;
        }

        return R.id.nav_home_option;
    }

    /**
     * Obtains the drawer menu item selected (passed from previous activity) and returns the fragment
     * that should be loaded based on that value.
     * This method is used for when the user is coming from the HeatMap activity. Otherwise we will load the enter fragment.
     *
     * @return The fragment that should be loaded within this activity.
     */
    private Fragment getFragmentToBeDisplayed() {
        String selectedMenuItem = getIntent().getStringExtra(Integer.toString(R.string.intent_menu_item));
        switch (selectedMenuItem) {
            case "nav_home_option":
                return new FragmentEnter();
            case "nav_settings_option":
                return FragmentSettings.newInstance();
        }

        return new FragmentEnter();
    }
}
