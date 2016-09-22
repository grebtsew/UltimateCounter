package com.grebtsew.app.ultimategamecounter.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grebtsew.app.ultimategamecounter.R;


public class CountFragment extends android.support.v4.app.Fragment {

    TextView points;

    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        View view = inflater.inflate(R.layout.fragment_counter, container, false);


        Bundle args = getArguments();

        points = (TextView) view.findViewById(R.id.number);
        String message = Integer.toString(args.getInt(ARG_OBJECT));

        points.setText(message);

        return view;
    }
}
