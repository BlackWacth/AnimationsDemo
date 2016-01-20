package com.hua.animationsdemo;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CardFlipActivity extends AppCompatActivity {

    private boolean isFront;
    private FloatingActionButton mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);
        mAction = (FloatingActionButton) findViewById(R.id.card_flip_change);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.containal, new CardFrontFragment())
                    .commit();
        }

        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFront = !isFront;
                cardFlip(isFront);
            }
        });
    }

    private void cardFlip(boolean isFront) {
        if(!isFront) {
            getFragmentManager().popBackStack();
            return;
        }

        getFragmentManager()
            .beginTransaction()
            //动画必须放在replace方法之前，否则没有动画效果
            .setCustomAnimations(R.animator.left_in, R.animator.right_out, R.animator.left_out, R.animator.right_in)
            .replace(R.id.containal, new CardBackFragment())
            .addToBackStack(null)
            .commit();
    }

    class CardFrontFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }
    }

    class CardBackFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }
    }
}
