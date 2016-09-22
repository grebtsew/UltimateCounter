package com.grebtsew.app.ultimategamecounter.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.grebtsew.app.ultimategamecounter.Adapters.PlayerAdapter;
import com.grebtsew.app.ultimategamecounter.Library.SharePreferences;
import com.grebtsew.app.ultimategamecounter.R;
import com.grebtsew.app.ultimategamecounter.Structures.Player;
import com.grebtsew.app.ultimategamecounter.Structures.PlayerNegComparator;
import com.grebtsew.app.ultimategamecounter.Structures.PlayerPosComparator;

import java.util.ArrayList;
import java.util.Collections;

public class StartGameActivity extends AppCompatActivity implements View.OnClickListener {

    int SelectedItemPosition = 0;
    PlayerAdapter adapter;
    ListView listView;
    ArrayList<Player> arrayOfUsers;

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NumberPicker np;
    private SwitchCompat shake;
    private SwitchCompat UPDOWN;
    private SwitchCompat turnbased;
    private SwitchCompat wanted_score;
    private boolean old_wanted_score;
    private int old_np;
    private boolean old_shake;
    private boolean old_UPDOWN;
    private boolean old_turnbased;
    Animation moveback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start_game);

        setUpAnimations();
        setUpMemory();
        setUpSettings();
        setUpLayout();
        setUpData();
        addButtonListeners();
    }

    private void setUpAnimations() {
        moveback = AnimationUtils.loadAnimation(this,
                R.anim.moveback);

    }

    private void addButtonListeners() {
        // ActionListeners for buttons
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(this);
        ImageButton button1 = (ImageButton) findViewById(R.id.button5);
        button1.setOnClickListener(this);
        ImageButton button2 = (ImageButton) findViewById(R.id.clearbutton);
        button2.setOnClickListener(this);
    }

    private void setUpData() {
        // Construct the data source
        arrayOfUsers = new ArrayList<Player>();
// Create the adapter to convert the array to views
        adapter = new PlayerAdapter(this, arrayOfUsers);
// Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


// Add item to adapter
        Player newPlayer = new Player("Player 1");
        adapter.add(newPlayer);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedItemPosition = i;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        ListView l = (ListView) findViewById(R.id.listView);
        l.startAnimation(moveback);

        arrayOfUsers = SharePreferences.GetPlayerList(this);
        SortList();

        // Update viewlist
        updateViewList();
    }

    private void updateSettingsView() {
        np = (NumberPicker) findViewById(R.id.numberPicker);
        shake = (SwitchCompat) findViewById(R.id.shaker);
        UPDOWN = (SwitchCompat) findViewById(R.id.updown);
        turnbased = (SwitchCompat) findViewById(R.id.switcher);
        wanted_score = (SwitchCompat) findViewById(R.id.wanted_score);

        // if null
        if (np != null && shake != null && UPDOWN != null && turnbased != null && wanted_score != null) {

            // first time start app
            if (SharePreferences.GetSetting("MULTIPLY", this) != "null") {

                np.setValue(Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", this)));
                shake.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE", this)));
                UPDOWN.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE_UP_DOWN", this)));
                turnbased.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("TURN", this)));
                wanted_score.setChecked(Boolean.parseBoolean(SharePreferences.GetSetting("WANTED_SCORE", this)));
            }
        }

    }

    private void updateViewList() {


        adapter.clear();

        for (Player p : arrayOfUsers) {
            adapter.add(p);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // clearbutton
            case R.id.clearbutton:

                final Animation animation4 = AnimationUtils.loadAnimation(this,
                        R.anim.rotate360left);

                animation4.setAnimationListener(new Animation.AnimationListener() {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(StartGameActivity.this);

                            builder.setTitle("Clear All player scores");
                            builder.setMessage("Are you sure?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing but close the dialog
                                    adapter.clearScores();
                                    saveData();
                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog a = builder.create();
                            a.show();


                        }
                    }
                });

                view.startAnimation(animation4);


                break;

            // Start Game
            case R.id.button4:

                final Animation animation = AnimationUtils.loadAnimation(this,
                        R.anim.shake);

                final Animation animation2 = AnimationUtils.loadAnimation(this,
                        R.anim.zoom_in);


                animation2.setAnimationListener(new Animation.AnimationListener() {
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

                            arrayOfUsers = adapter.players;

                            if (adapter.players.isEmpty()) {
                                arrayOfUsers = new ArrayList<Player>();
                                Player p = new Player("Player1");
                                arrayOfUsers.add(p);

                                SharePreferences.SavePlayerList(arrayOfUsers, StartGameActivity.this);
                                SharePreferences.SaveFocusedPlayer(Integer.toString(0), StartGameActivity.this);
                            } else {
                                SharePreferences.SavePlayerList(arrayOfUsers, StartGameActivity.this);

                                if (SelectedItemPosition >= adapter.players.size()) {
                                    SelectedItemPosition = 0;
                                }
                                SharePreferences.SaveFocusedPlayer(Integer.toString(SelectedItemPosition), StartGameActivity.this);

                            }

                            Intent myIntent = new Intent(StartGameActivity.this, GameActivity.class);
                            myIntent.putExtra("key", 2); //Optional parameters
                            StartGameActivity.this.startActivity(myIntent);


                        }
                    }
                });

                ListView lv = (ListView) findViewById(R.id.listView);

                view.startAnimation(animation);
                lv.startAnimation(animation2);

                break;


            case R.id.button5:

                // Add Player to List

                final Animation animation5 = AnimationUtils.loadAnimation(this,
                        R.anim.rotate360right);


                animation5.setAnimationListener(new Animation.AnimationListener() {
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

                            final AlertDialog.Builder alert = new AlertDialog.Builder(StartGameActivity.this);
                            final EditText input = new EditText(StartGameActivity.this);
                            input.setText("Player" + getOpenPlayerNumber());

                            alert.setTitle("Add Player");
                            alert.setMessage("To Add a new player write a name bellow and press OK!");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
                                    Toast.makeText(getApplicationContext(), "Added Player < " + value + " >",
                                            Toast.LENGTH_SHORT).show();

                                    Player newPlayer = new Player(value);
                                    adapter.add(newPlayer);
                                    saveData();
                                    adapter.notifyDataSetChanged();


                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    }
                });

                view.startAnimation(animation5);


                break;

        }

    }

    private int getOpenPlayerNumber() {
        int max = 100;
        int min = 0;
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

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

    private void setUpLayout() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitle("UGC Overview");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();


        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);


        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);


        View drawerView = mDrawer;
        if (drawerView != null && drawerView instanceof DrawerLayout) {
            mDrawer = (DrawerLayout) drawerView;
            mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View view, float v) {

                }

                @Override
                public void onDrawerOpened(View view) {
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
                    //  System.out.println("TURN Is  "+ turnbased.isChecked());


                    //check settings with the saved
                    if (SettingsChanged()) {

                        // Sett and save
                        SharePreferences.SaveAllSettings(Integer.toString(np.getValue()), Boolean.toString(shake.isChecked()), Boolean.toString(UPDOWN.isChecked()), Boolean.toString(turnbased.isChecked()), Boolean.toString(wanted_score.isChecked()), StartGameActivity.this);

                        old_np = np.getValue();
                        old_shake = shake.isChecked();
                        old_turnbased = turnbased.isChecked();
                        old_UPDOWN = UPDOWN.isChecked();
                        old_wanted_score = wanted_score.isChecked();

                        String value = "Settings Saved!";
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                        arrayOfUsers = SharePreferences.GetPlayerList(StartGameActivity.this);
                        SortList();
                        updateViewList();
                    }


                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
        }
    }

    private boolean SettingsChanged() {

        try {


            // System.out.println(old_np + " " + np.getValue());
            // System.out.println(old_shake + " " + shake.isChecked());
            // System.out.println(old_UPDOWN + " " + UPDOWN.isChecked());
            // System.out.println(old_turnbased + " " + turnbased.isChecked());
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

    private void setUpMemory() {
        arrayOfUsers = new ArrayList<Player>();

        // Get Player information
        arrayOfUsers = SharePreferences.GetPlayerList(this);

        if (arrayOfUsers == null) {
            arrayOfUsers = new ArrayList<Player>();
            arrayOfUsers.add(new Player("Player 1"));
            SharePreferences.SavePlayerList(arrayOfUsers, this);
        }


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

    private void SortList() {
        boolean wanted_score = Boolean.parseBoolean(SharePreferences.GetSetting("WANTED_SCORE", this));
        PlayerNegComparator comparator1 = new PlayerNegComparator();
        PlayerPosComparator comparator2 = new PlayerPosComparator();

        if (!wanted_score) {

            Collections.sort(arrayOfUsers, comparator1);
        } else {
            Collections.sort(arrayOfUsers, comparator2);
            // ShowPlayers();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            saveData();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveData() {
        // save this players score to the list
        SharePreferences.SavePlayerList(adapter.players, this);
    }
}
