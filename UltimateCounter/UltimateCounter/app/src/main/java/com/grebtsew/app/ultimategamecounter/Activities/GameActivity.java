package com.grebtsew.app.ultimategamecounter.Activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.grebtsew.app.ultimategamecounter.Adapters.SwipeAdapter;
import com.grebtsew.app.ultimategamecounter.Library.ShakeDetector;
import com.grebtsew.app.ultimategamecounter.Library.SharePreferences;
import com.grebtsew.app.ultimategamecounter.R;
import com.grebtsew.app.ultimategamecounter.Structures.Player;
import com.grebtsew.app.ultimategamecounter.Structures.PlayerNegComparator;
import com.grebtsew.app.ultimategamecounter.Structures.PlayerPosComparator;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    static int START_POSITION = 500;
    Player currentPlayer;
    ArrayList<Player> playerList;
    SwipeAdapter swipeAdapter;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    boolean shakeEnabled = false;
    boolean shakecount = false;
    Animation pulse;
    Animation shake;
    int score;
    int nextPlayerPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);

        setUpAnimations();
        setUpMemory();
        setUpLayout();
        setUpSettings();
        addButtonListeners();
    }

    private void setUpAnimations() {
        pulse = AnimationUtils.loadAnimation(this,
                R.anim.pulse);
        shake = AnimationUtils.loadAnimation(this,
                R.anim.shake);

    }

    private void addButtonListeners() {

        Button button3 = (Button) findViewById(R.id.button3);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);

        button3.setOnClickListener(this);
        button6.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);

        button3.setEnabled(true);
    }

    private void setUpSettings() {
        // if first time start
        if (SharePreferences.GetSetting("MULTIPLY", this) != "null") {
            scoreChanged();
            int mutliplyer = Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", this));

            // If turnbased, restart count each round
            boolean turnbased = Boolean.parseBoolean(SharePreferences.GetSetting("TURN", this));

            if (turnbased || currentPlayer.scores.isEmpty()) {
                viewPager.setCurrentItem(viewPager.getCurrentItem());
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + (currentPlayer.scores.get(currentPlayer.scores.size() - 1)) / mutliplyer);
            }

            shakeEnabled = Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE", this));
            shakecount = Boolean.parseBoolean(SharePreferences.GetSetting("SHAKE_UP_DOWN", this));

            if (shakeEnabled) {

                // ShakeDetector initialization
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                mAccelerometer = mSensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mShakeDetector = new ShakeDetector();
                mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

                    @Override
                    public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */

                        if (shakecount) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                            scoreChanged();
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            scoreChanged();
                        }
                    }

                });
            }

        }
    }

    private void setUpMemory() {
        // get player information and who to start with
        playerList = SharePreferences.GetPlayerList(this);

        // if list is empty
        if (playerList.isEmpty() || playerList == null) {
            playerList = new ArrayList<Player>();
            currentPlayer = new Player("Player1");
            playerList.add(currentPlayer);
        } else {
            currentPlayer = playerList.get(Integer.parseInt(SharePreferences.GetFocusedPlayer(this)));

        }

    }

    private void setUpLayout() {
        // initiate Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("UGC");

        // initiate viewpager and swipefunctions
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(START_POSITION);

        // Add player name
        TextView name = (TextView) findViewById(R.id.textView9);
        name.setText(currentPlayer.name);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (shakeEnabled) {
            // Add the following line to register the Session Manager Listener onResume
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {

        if (shakeEnabled) {
            // Add the following line to unregister the Sensor Manager onPause
            mSensorManager.unregisterListener(mShakeDetector);
        }
        super.onPause();
    }

    private void ShowPlayers() {
        int i = 1;
        for (Player p : playerList) {
            System.out.println(i + " " + p.score + " " + p.name);
            i++;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /**
             *   swipe left
             */
            case R.id.button8:
                view.startAnimation(pulse);
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                scoreChanged();
                break;

            /**
             * swipe right
             */
            case R.id.button9:
                view.startAnimation(pulse);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                scoreChanged();
                break;

            /**
             * next player
             */
            case R.id.button3:

                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Button b3 = (Button) findViewById(R.id.button3);
                        b3.setEnabled(false);

                        SortList();
                        saveDataForNewFragment();

                        // get which player is next
                        nextPlayerPos = getNextPlayer();

                        // switch player
                        SharePreferences.SaveFocusedPlayer(Integer.toString(nextPlayerPos), GameActivity.this);

                        // reset swipe
                        Intent myIntent = new Intent(GameActivity.this, GameActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                    }
                });
                view.startAnimation(shake);
                break;

            /**
             * return to overview
             */
            case R.id.button6:
                SortList();
                saveDataForNewFragment();
                finish();
                break;
        }
    }

    public void scoreChanged() {
        if(SharePreferences.GetSetting("MULTIPLY", this) == "null" ){
            score = (viewPager.getCurrentItem() - START_POSITION);
        } else {
            score = (viewPager.getCurrentItem() - START_POSITION) * Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", this));
        }

        currentPlayer.score = currentPlayer.getTotScore() + score;
        SortList();
        TextView pos = (TextView) findViewById(R.id.textView2);
        pos.setText("Position : " + (getPlayerPosition(currentPlayer) + 1) + "/" + playerList.size());
        TextView s = (TextView) findViewById(R.id.textView7);
        s.setText("Score : " + (currentPlayer.getTotScore() + score));
    }

    private void saveDataForNewFragment() {
        // save this players score to the list
        if(SharePreferences.GetSetting("MULTIPLY", this) == "null"){
            score = (viewPager.getCurrentItem() - START_POSITION);
        } else {
            score = (viewPager.getCurrentItem() - START_POSITION) * Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", this));
        }

        currentPlayer.scores.add(score);
        currentPlayer.score = currentPlayer.getTotScore();
        SharePreferences.SavePlayerList(playerList, this);
    }

    public int getNextPlayer() {
        int i = 0;
        for (Player p : playerList) {
            if (p == currentPlayer) {
                if (playerList.size() - 1 < i + 1) {
                    return 0;
                } else {
                    return i + 1;
                }
            }
            i++;
        }
        return 0;
    }

    public int getPlayerPosition(Player pla) {
        int res = playerList.size() - 1;
        for (Player p : playerList) {
            if (pla == p) {
                return res;
            }
            res--;
        }
        return res;
    }

    private void SortList() {
        boolean wanted_score = Boolean.parseBoolean(SharePreferences.GetSetting("WANTED_SCORE", this));
        PlayerNegComparator comparator1 = new PlayerNegComparator();
        PlayerPosComparator comparator2 = new PlayerPosComparator();

        if (wanted_score) {
            Collections.sort(playerList, comparator1);
        } else {
            Collections.sort(playerList, comparator2);
        }
    }

}
