package com.grebtsew.app.ultimategamecounter.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.grebtsew.app.ultimategamecounter.Library.SharePreferences;
import com.grebtsew.app.ultimategamecounter.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private NumberPicker np;
    private SwitchCompat shake;
    private SwitchCompat UPDOWN;
    private SwitchCompat turnbased;
    private SwitchCompat wanted_score;

    private int old_np;
    private boolean old_wanted_score;
    private boolean old_shake;
    private boolean old_UPDOWN;
    private boolean old_turnbased;

    private Animation moveback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpSettings();
        setUpLayout();
        setUpAnimation();
    }

    /**
     * Set up animations used several times
     */
    private void setUpAnimation() {
        moveback = AnimationUtils.loadAnimation(this,
                R.anim.moveback);
    }

    @Override
    public void onResume() {
        super.onResume();

        //First time start
        if (SharePreferences.GetSetting("MULTIPLY", MainActivity.this) != null) {

            TextView tv = (TextView) findViewById(R.id.textView3);
            tv.startAnimation(moveback);
        }
    }

    /**
     * Set up Settings in drawer used several times
     */
    private void updateSettingsView() {

        np = (NumberPicker) findViewById(R.id.numberPicker);
        shake = (SwitchCompat) findViewById(R.id.shaker);
        UPDOWN = (SwitchCompat) findViewById(R.id.updown);
        turnbased = (SwitchCompat) findViewById(R.id.switcher);
        wanted_score = (SwitchCompat) findViewById(R.id.wanted_score);

        // if values are unset in drawer
        if (np != null && shake != null && UPDOWN != null && turnbased != null && wanted_score != null) {


            // first time start app
            if (SharePreferences.GetSetting("MULTIPLY", MainActivity.this) != "null") {


                np.setValue(Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", MainActivity.this)));
                shake.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE", MainActivity.this)));
                UPDOWN.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE_UP_DOWN", MainActivity.this)));
                turnbased.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("TURN", MainActivity.this)));
                wanted_score.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("WANTED_SCORE", MainActivity.this)));
            }
        }
    }

    /**
     * Initiate settings
     */
    private void setUpSettings() {
        // Get All Settings

        try {
            old_np = Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", this));
            old_shake = Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE", this));
            old_UPDOWN = Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE_UP_DOWN", this));
            old_turnbased = Boolean.parseBoolean(SharePreferences.GetSetting("TURN", this));
            old_wanted_score = Boolean.parseBoolean(SharePreferences.GetSetting("WANTED_SCORE", this));

        } catch (Throwable e) {

        }
    }

    /**
     * Set up layout
     */
    private void setUpLayout() {

        // Add Button Listener
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);

        // Initiate Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initiate drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Drawer Listener
        View drawerView = mDrawer;
        if (drawerView != null && drawerView instanceof DrawerLayout) {
            mDrawer = (DrawerLayout) drawerView;
            mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View view, float v) {
                }

                @Override
                public void onDrawerOpened(View view) {
                    // update Settings
                    updateSettingsView();
                }

                @Override
                public void onDrawerClosed(View view) {

                    np = (NumberPicker) findViewById(R.id.numberPicker);
                    // System.out.println("Multiplyer "+ np.getValue());

                    shake = (SwitchCompat) findViewById(R.id.shaker);
                    // System.out.println("Shake Is  "+ shake.isChecked());

                    UPDOWN = (SwitchCompat) findViewById(R.id.updown);
                    //  System.out.println("UP/DOWN Is  "+ UPDOWN.isChecked());

                    turnbased = (SwitchCompat) findViewById(R.id.switcher);
                    //  System.out.println("TURN Is  "+ turnbased.isChecked());

                    wanted_score = (SwitchCompat) findViewById(R.id.wanted_score);
                    //  System.out.println("Wanted score Is  "+ wanted_score.isChecked());

                    if (SettingsChanged()) {

                        // Sett and save
                        SharePreferences.SaveAllSettings(Integer.toString(np.getValue()), Boolean.toString(shake.isChecked()), Boolean.toString(UPDOWN.isChecked()), Boolean.toString(turnbased.isChecked()), Boolean.toString(wanted_score.isChecked()), MainActivity.this);

                        old_np = np.getValue();
                        old_shake = shake.isChecked();
                        old_turnbased = turnbased.isChecked();
                        old_UPDOWN = UPDOWN.isChecked();
                        old_wanted_score = wanted_score.isChecked();

                        String value = "Settings Saved!";
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
        }
    }

    /**
     * Settings changed when drawer was opened
     *
     * @return boolean if Settings has changed
     */
    private boolean SettingsChanged() {

        try {
            // System.out.println(old_np + " " + np.getValue());
            // System.out.println(old_shake + " " + shake.isChecked());
            // System.out.println(old_UPDOWN + " " + UPDOWN.isChecked());
            // System.out.println(old_turnbased + " " + turnbased.isChecked())
            // System.out.println(old_wanted_score + " " + wanted_score.isChecked());

            if (old_np != np.getValue()) {
                return true;
            }
            if (old_shake != shake.isChecked()) {
                return true;
            }
            if (old_UPDOWN != UPDOWN.isChecked()) {
                return true;
            }
            if (old_turnbased != turnbased.isChecked()) {
                return true;
            }
            if (old_wanted_score != wanted_score.isChecked()) {
                return true;
            }

        } catch (Throwable e) {
            return true;
        }

        return false;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        mDrawer.openDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            /**
             * New Game button pressed
             */
            case R.id.button2:
                final Animation shake_anim = AnimationUtils.loadAnimation(this,
                        R.anim.shake);
                final Animation move_anim = AnimationUtils.loadAnimation(this,
                        R.anim.move);

                move_anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent myIntent = new Intent(MainActivity.this, StartGameActivity.class);
                            myIntent.putExtra("key", 1); //Optional parameters
                            MainActivity.this.startActivity(myIntent);
                        }
                    }
                });

                view.startAnimation(shake_anim);
                TextView tv = (TextView) findViewById(R.id.textView3);
                tv.startAnimation(move_anim);
                break;
        }
    }

}
