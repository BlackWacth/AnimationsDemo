package com.hua.animationsdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class CrossFadeActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private LinearLayout mLayoutView;
    private int animTime;

    private boolean mContentLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_fade);
        mProgressBar = (ProgressBar) findViewById(R.id.cross_fade_progressbar);
        mLayoutView = (LinearLayout) findViewById(R.id.cross_fade_content);
        animTime = getResources().getInteger(android.R.integer.config_longAnimTime);

        mLayoutView.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentLoaded = !mContentLoaded;
                crossFade(mContentLoaded);
            }
        });
    }

    private void crossFade(boolean contentLoaded){
        final View showView = contentLoaded ? mLayoutView : mProgressBar;
        final View hideView = contentLoaded ? mProgressBar : mLayoutView;

        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        showView.animate()
                .alpha(1f)
                .setDuration(animTime)
                .setListener(null);

        hideView.animate()
                .alpha(0f)
                .setDuration(animTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }

}
