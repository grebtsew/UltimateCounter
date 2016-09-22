package com.grebtsew.app.ultimategamecounter.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.grebtsew.app.ultimategamecounter.Activities.GameActivity;
import com.grebtsew.app.ultimategamecounter.Fragments.CountFragment;
import com.grebtsew.app.ultimategamecounter.Library.SharePreferences;
import com.grebtsew.app.ultimategamecounter.R;

/**
 * Created by Daniel Westberg on 2016-08-08.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    static int START_POSITION = 501;

    private GameActivity t;
    Animation pulse;
    private Button b;
    private int currentscore = 0;
    private int oldcurrentscore = 0;
    int multiplyer = 1;

    public SwipeAdapter(FragmentManager fm, GameActivity _t) {
        super(fm);
        t = _t;
        pulse = AnimationUtils.loadAnimation(t,
                R.anim.pulse);
    }

    // on Swipe!
    @Override
    public Fragment getItem(int i) {

        Fragment fragment = new CountFragment();
        Bundle args = new Bundle();

        t.scoreChanged();

        // if first time
        if(SharePreferences.GetSetting("MULTIPLY", t) != "null"){
             multiplyer = Integer.parseInt(SharePreferences.GetSetting("MULTIPLY", t));
        } else {
             multiplyer = 1;
        }

        oldcurrentscore = currentscore;
        currentscore = multiplyer * (i - START_POSITION);

        // positiv or negative
        if (currentscore > oldcurrentscore) {
            b = (Button) t.findViewById(R.id.button9);
        } else {
            b = (Button) t.findViewById(R.id.button8);
        }
        b.startAnimation(pulse);

        args.putInt(CountFragment.ARG_OBJECT, multiplyer * (i - START_POSITION + 1));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

}
