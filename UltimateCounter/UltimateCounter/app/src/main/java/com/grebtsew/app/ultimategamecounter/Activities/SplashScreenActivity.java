package com.grebtsew.app.ultimategamecounter.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grebtsew.app.ultimategamecounter.R;

/**
 * Created by Daniel Westberg on 2016-08-09.
 */
public class SplashScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.grebtsewlogosplash);

        final TextView tv1 = (TextView) findViewById(R.id.U);
        final TextView tv2 = (TextView) findViewById(R.id.G);
        final TextView tv3 = (TextView) findViewById(R.id.C);
        final TextView tv4 = (TextView) findViewById(R.id.ultimate);
        final TextView tv5 = (TextView) findViewById(R.id.game);
        final TextView tv6 = (TextView) findViewById(R.id.counter);

        tv4.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);

        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv2.setRotation(90);
        tv3.setVisibility(View.INVISIBLE);
        tv3.setRotation(90);
        fixGrebtsewLogo();
    }

    private void fixGrebtsewLogo() {
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);

        final Animation animation1 = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);

        final ImageView iv = (ImageView) findViewById(R.id.image);


        animation.setAnimationListener(new Animation.AnimationListener() {
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
                    iv.startAnimation(animation1);
                }
            }
        });

        animation1.setAnimationListener(new Animation.AnimationListener() {
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
                    fixGameLogo();

                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            iv.startAnimation(animation);
        }

    }

    // fade in, rotate
    private void fixGameLogo() {


        Animation animation2 = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);

        final TextView tv1 = (TextView) findViewById(R.id.U);
        final TextView tv2 = (TextView) findViewById(R.id.G);
        final TextView tv3 = (TextView) findViewById(R.id.C);


        animation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
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

                    // Rotate back
                    tv3.animate().rotation(0).setDuration(600);
                    tv2.animate().rotation(0).setDuration(600).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            nextAnimation();
                        }
                    });
                }
            }
        });


        tv1.startAnimation(animation2);
        tv2.startAnimation(animation2);
        tv3.startAnimation(animation2);

        //  startApp();
    }

    // fade in, fade out
    private void nextAnimation() {


        final Animation animation5 = AnimationUtils.loadAnimation(this,
                R.anim.fade_in);

        final Animation animation6 = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);
        final Animation animation7 = AnimationUtils.loadAnimation(this,
                R.anim.zoom_out);

        // animation3.setFillEnabled(true);
        //animation3.setFillAfter(true);


        final TextView tv1 = (TextView) findViewById(R.id.U);
        final TextView tv2 = (TextView) findViewById(R.id.G);
        final TextView tv3 = (TextView) findViewById(R.id.C);
        final TextView tv4 = (TextView) findViewById(R.id.ultimate);
        final TextView tv5 = (TextView) findViewById(R.id.game);
        final TextView tv6 = (TextView) findViewById(R.id.counter);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.background);

        animation5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

                tv4.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
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
                    //  tv4.startAnimation(animation6);
                    //  tv5.startAnimation(animation6);
                    //  tv6.startAnimation(animation6);

                    rl.startAnimation(animation6);


                }
            }
        });


        animation6.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {


                startApp();


            }
        });

        tv4.startAnimation(animation5);
        tv5.startAnimation(animation5);
        tv6.startAnimation(animation5);


    }

    private void startApp() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}





