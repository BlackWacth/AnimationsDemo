package com.hua.animationsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<AnimModel> mAnims;
    private AnimsAdapter mAnimsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.animations_listview);
        initListView();
    }

    private void initListView() {
        if(mAnims == null) {
            mAnims = new ArrayList<AnimModel>();
            mAnims.add(new AnimModel(R.string.title_crossfade, CrossFadeActivity.class));
            mAnims.add(new AnimModel(R.string.title_cardflip, CardFlipActivity.class));
            mAnims.add(new AnimModel(R.string.title_screenslide, ScreenSlideActivity.class));
            mAnims.add(new AnimModel(R.string.title_zoom, ZoomActivity.class));
            mAnims.add(new AnimModel(R.string.title_layout_change, LayoutChangeActivity.class));
        }

        mAnimsAdapter = new AnimsAdapter(this, mAnims);
        mListView.setAdapter(mAnimsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnimModel model = (AnimModel) parent.getAdapter().getItem(position);
                startActivity(new Intent(MainActivity.this, model.getCls()));
            }
        });
    }

    class AnimsAdapter extends BaseAdapter {

        private Context mContext;
        private List<AnimModel> mAnims;

        public AnimsAdapter(Context mContext, List<AnimModel> mAnims) {
            this.mContext = mContext;
            this.mAnims = mAnims;
        }

        @Override
        public int getCount() {
            return mAnims == null ? 0 : mAnims.size();
        }

        @Override
        public AnimModel getItem(int position) {
            return mAnims.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AnimHolder holder;
            if(convertView == null) {
                holder = new AnimHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.anim_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.anim_title);
                convertView.setTag(holder);
            }else {
                holder = (AnimHolder) convertView.getTag();
            }

            holder.title.setText(getItem(position).getTitle());

            return convertView;
        }
    }

    class AnimHolder {
        TextView title;
    }


    class AnimModel{
        private String title;
        private Class<? extends Activity> cls;

        public AnimModel(int titleId, Class<? extends Activity> cls) {
            this.title = getResources().getString(titleId);
            this.cls = cls;
        }

        public String getTitle() {
            return title;
        }

        public Class<? extends Activity> getCls() {
            return cls;
        }

        @Override
        public String toString() {
            return "AnimModel{" +
                    "title='" + title + '\'' +
                    ", cls=" + cls +
                    '}';
        }
    }
}
