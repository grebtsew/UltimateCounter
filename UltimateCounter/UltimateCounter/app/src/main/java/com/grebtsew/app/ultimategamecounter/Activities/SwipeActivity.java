package com.grebtsew.app.ultimategamecounter.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.grebtsew.app.ultimategamecounter.R;
import com.grebtsew.app.ultimategamecounter.Adapters.SwipeAdapter;

/**
 * Created by Daniel Westberg on 2016-08-08.
 */

public class SwipeActivity extends FragmentActivity {

    ViewPager viewPager;
    SwipeAdapter swipeAdapter;
    GameActivity ga;

    public void onCreate(Bundle savedInstanceState, GameActivity _ga) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_counter);
        ga = _ga;

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        swipeAdapter =
                new SwipeAdapter(
                        getSupportFragmentManager(), ga);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(swipeAdapter);
    }
}

