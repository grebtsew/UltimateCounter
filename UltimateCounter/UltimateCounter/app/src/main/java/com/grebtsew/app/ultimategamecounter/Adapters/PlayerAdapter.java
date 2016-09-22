package com.grebtsew.app.ultimategamecounter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grebtsew.app.ultimategamecounter.Library.SharePreferences;
import com.grebtsew.app.ultimategamecounter.R;
import com.grebtsew.app.ultimategamecounter.Structures.Player;

import java.util.ArrayList;

/**
 * Created by Daniel Westberg on 2016-08-10.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {

    public ArrayList<Player> players;
    Context context;


    public PlayerAdapter(Context _context, ArrayList<Player> players) {
        super(_context, 0, players);
        this.players = players;
        context = _context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_player, parent, false);
        }
        // Get the data item for this position
        final Player player = getItem(position);
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final ImageButton tvBack = (ImageButton) convertView.findViewById(R.id.tvBack);
        final TextView tvScore = (TextView) convertView.findViewById(R.id.tvScore);
        final TextView tvTotal = (TextView) convertView.findViewById(R.id.tvTotal);
        final ImageButton tvRemove = (ImageButton) convertView.findViewById(R.id.tvRemove);
        final RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.layout);


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation bounce = AnimationUtils.loadAnimation(context,
                        R.anim.bounce);

                bounce.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        if (!player.scores.isEmpty()) {
                            player.scores.remove(player.scores.size() - 1);
                            player.score = player.getTotScore();
                            tvScore.setText(player.scores.toString());
                            tvTotal.setText(Integer.toString(player.score));
                            SharePreferences.SavePlayerList(players, context);
                        }
                    }
                });

                tvBack.startAnimation(bounce);

            }
        });

        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation slide_up = AnimationUtils.loadAnimation(context,
                        R.anim.slide_up);

                Animation rotateright = AnimationUtils.loadAnimation(context,
                        R.anim.rotate360right);

                slide_up.setFillAfter(true);
                slide_up.setFillEnabled(true);

                slide_up.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        players.remove(player);
                        notifyDataSetChanged();
                        rl.clearAnimation();
                        tvRemove.clearAnimation();
                        SharePreferences.SavePlayerList(players, context);
                    }
                });


                tvRemove.startAnimation(rotateright);
                rl.startAnimation(slide_up);


            }
        });

        // Populate the data into the template view using the data object
        tvName.setText(player.name);
        tvTotal.setText(Integer.toString(player.score));
        tvScore.setText(player.scores.toString());
        // Return the completed view to render on screen
        return convertView;
    }


    public void clearScores() {
        for (Player player : players) {
            player.score = 0;
            player.scores.clear();
            notifyDataSetChanged();
        }
    }

}
