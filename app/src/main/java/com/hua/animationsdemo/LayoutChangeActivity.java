package com.hua.animationsdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class LayoutChangeActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView mTextView;
    private FloatingActionButton mAction;

    private List<String> mList;
    private int animTime;
    private ChangeAdapter mAdapter;
    int count = 0;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain",
            "Austria", "Russia", "Poland", "Croatia", "Greece",
            "Ukraine",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_change);
        mListView = (ListView) findViewById(R.id.change_list_view);
        mTextView = (TextView) findViewById(R.id.change_text);
        mAction = (FloatingActionButton) findViewById(R.id.change_action);
        animTime = getResources().getInteger(android.R.integer.config_longAnimTime);
        checkListView();
        initListView();
        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void checkListView(){
        if(mList == null || mList.size() <= 0) {
            mTextView.setText("Nothing!!!!");
            showAnim(mTextView, true);
        }else {
            showAnim(mTextView, false);
        }
    }

    private void addItem() {
        mList.add(COUNTRIES[count]);
        checkListView();
        mAdapter.notifyDataSetChanged();
        count = count + 1 >= COUNTRIES.length ? 0 : count + 1;
    }

    private void initListView() {
        if(mList == null) {
            mList = new LinkedList<>();
        }
        mAdapter = new ChangeAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    public void showAnim(final View view, final boolean isShow) {
        float alpha = isShow ? 1f : 0f;
        if(isShow) {
            view.setVisibility(View.VISIBLE);
        }
        view.animate()
            .alpha(alpha)
            .setDuration(animTime)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isShow) {
                        view.setVisibility(View.GONE);
                    }
                }
            })
            .start();
    }

    class ChangeAdapter extends BaseAdapter{

        private Context mContext;
        private List<String> mList;

        public ChangeAdapter(Context mContext, List<String> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChangeHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.change_item, parent, false);
                holder = new ChangeHolder();
                holder.title = (TextView) convertView.findViewById(R.id.change_title);
                holder.delete = (ImageView) convertView.findViewById(R.id.change_delete);
                convertView.setTag(holder);
            }else {
                holder = (ChangeHolder) convertView.getTag();
            }

            holder.title.setText(mList.get(position));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                    checkListView();
                }
            });

            return convertView;
        }
    }

    class ChangeHolder {
        TextView title;
        ImageView delete;
    }
}
