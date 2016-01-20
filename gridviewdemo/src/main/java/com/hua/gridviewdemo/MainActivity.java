package com.hua.gridviewdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String tag = "hzw";
    private int mImageThumbSize;
    private int mImageThumbSpacing;

    private GridView mGridView;
    private List<Integer> mList;

    private ImageAdapter mAdapter;

    private Animator mAnimator;
    private int animTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelOffset(R.dimen.image_thumbnail_spacing);

        mGridView = (GridView) findViewById(R.id.gridview);
        animTime = getResources().getInteger(android.R.integer.config_longAnimTime);

        initGridView();
    }

    private void initGridView() {
        if (mList == null) {
            mList = new ArrayList<>();
            mList.add(R.mipmap.img01);
            mList.add(R.mipmap.img02);
            mList.add(R.mipmap.img03);
            mList.add(R.mipmap.img04);
            mList.add(R.mipmap.img05);
            mList.add(R.mipmap.img06);
            mList.add(R.mipmap.img07);
            mList.add(R.mipmap.img08);
            mList.add(R.mipmap.img09);
            mList.add(R.mipmap.img10);
            mList.add(R.mipmap.img11);
            mList.add(R.mipmap.img12);
            mList.add(R.mipmap.img13);
            mList.add(R.mipmap.img14);
            mList.add(R.mipmap.img15);
            mList.add(R.mipmap.image1);
            mList.add(R.mipmap.image2);
            mList.add(R.mipmap.img01);
            mList.add(R.mipmap.img02);
            mList.add(R.mipmap.img03);
            mList.add(R.mipmap.img04);
            mList.add(R.mipmap.img05);
            mList.add(R.mipmap.img06);
            mList.add(R.mipmap.img07);
            mList.add(R.mipmap.img08);
            mList.add(R.mipmap.img09);
            mList.add(R.mipmap.img10);
            mList.add(R.mipmap.img11);
            mList.add(R.mipmap.img12);
            mList.add(R.mipmap.img13);
            mList.add(R.mipmap.img14);
            mList.add(R.mipmap.img15);
            mList.add(R.mipmap.image1);
            mList.add(R.mipmap.image2);

        }
        mAdapter = new ImageAdapter(this, mList);
        //根据GridView的大小来计算Item的大小，并且把值传给Adapter。
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int numColumns = (int) Math.floor(mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                if (numColumns > 0) {
                    int columnWidth = (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                    mAdapter.setItemHeight(columnWidth);
                }
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zoomImageView((ImageView) view, mList.get(position));
            }
        });
    }

    public Bitmap decodeImageById(Resources res, int imgId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, imgId, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, imgId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int inSample = 1;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;

        if (outWidth > width || outHeight > height) {
            int halfOutWidth = outWidth / 2;
            int halfOutHeight = outHeight / 2;
            while (halfOutWidth / inSample > width && halfOutHeight / inSample > height) {
                inSample *= 2;
            }
        }
        return inSample;
    }

    public void loadImage(ImageView imageView, int imgId) {
        LoadImageTask loadImageTask = new LoadImageTask(imageView);
        loadImageTask.execute(imgId);
    }

    /**
     * 放大缩小动画
     *
     * @param thumbImage 缩略图
     * @param imgId      原图ID
     */
    public void zoomImageView(final ImageView thumbImage, int imgId) {

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        final ImageView expandImage = (ImageView) findViewById(R.id.expandImage);
        expandImage.setImageResource(imgId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        //确定ImageView开始位置和结束位置
        thumbImage.getGlobalVisibleRect(startBounds);
        findViewById(R.id.contain).getGlobalVisibleRect(finalBounds, globalOffset);

        //减去Actionbar+Statusbar的高度
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        final float scale;
        //计算缩放率和调整初始坐标
        if (((float) finalBounds.width() / finalBounds.height()) > ((float) startBounds.width() / startBounds.height())) {
            scale = (float) startBounds.height() / finalBounds.height();
            float startWidth = finalBounds.width() * scale;
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            scale = (float) startBounds.width() / finalBounds.width();
            float startHeight = finalBounds.height() * scale;
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        //移动中心到左上角
        expandImage.setPivotX(0);
        expandImage.setPivotY(0);

        //隐藏缩略图，显示大图。
        thumbImage.setAlpha(0f);
        expandImage.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandImage, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandImage, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandImage, View.SCALE_X, scale, 1f))
                .with(ObjectAnimator.ofFloat(expandImage, View.SCALE_Y, scale, 1f));
        set.setDuration(animTime);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimator = null;
            }
        });
        set.start();
        mAnimator = set;

        final float finalScale = scale;
        expandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandImage, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandImage, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandImage, View.SCALE_X, finalScale))
                        .with(ObjectAnimator.ofFloat(expandImage, View.SCALE_Y, finalScale));
                set.setDuration(animTime);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimator = null;
                        thumbImage.setAlpha(1f);
                        expandImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mAnimator = null;
                        thumbImage.setAlpha(1f);
                        expandImage.setVisibility(View.GONE);
                    }
                });
                set.start();
                mAnimator = set;
            }
        });
    }

    class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {
        private ImageView mImageView;

        public LoadImageTask(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            return decodeImageById(getResources(), params[0], mImageThumbSize, mImageThumbSize);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> mList;
        private int mItemHeight = 0;

        private GridView.LayoutParams mImageViewLayoutParams;

        public ImageAdapter(Context mContext, List<Integer> mList) {
            this.mContext = mContext;
            this.mList = mList;
            mImageViewLayoutParams = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                Log.i(tag, "mItemHeight = " + mItemHeight);
                imageView.setLayoutParams(mImageViewLayoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            loadImage(imageView, mList.get(position));
            return imageView;
        }

        public void setItemHeight(int height) {
            this.mItemHeight = height;
            mImageViewLayoutParams = new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
            notifyDataSetChanged();
        }
    }
}
