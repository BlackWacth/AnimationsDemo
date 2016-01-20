package com.hua.animationsdemo;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ZoomActivity extends AppCompatActivity {

    private GridView mGridView;
    private ImageView mImageView;

    private List<Integer> mList;
    private ZoomAdapter mAdapter;

    private Animator mCurrentAnim;
    private int animTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mGridView = (GridView) findViewById(R.id.zoom_gridview);
        mImageView = (ImageView) findViewById(R.id.zoom_imageview);
        animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        initGridView();
    }

    private void initGridView() {
        if(mList == null) {
            mList = new ArrayList<>();
            mList.add(R.drawable.img01);
            mList.add(R.drawable.img02);
            mList.add(R.drawable.img03);
            mList.add(R.drawable.img04);
            mList.add(R.drawable.img05);
            mList.add(R.drawable.img06);
            mList.add(R.drawable.img07);
            mList.add(R.drawable.img08);
            mList.add(R.drawable.img09);
            mList.add(R.drawable.img10);
            mList.add(R.drawable.img11);
            mList.add(R.drawable.img12);
            mList.add(R.drawable.img13);
            mList.add(R.drawable.img14);
            mList.add(R.drawable.img15);
            mList.add(R.drawable.image1);
            mList.add(R.drawable.image2);
            mList.add(R.drawable.img01);
            mList.add(R.drawable.img02);
            mList.add(R.drawable.img03);
            mList.add(R.drawable.img04);
            mList.add(R.drawable.img05);
            mList.add(R.drawable.img06);
            mList.add(R.drawable.img07);
            mList.add(R.drawable.img08);
            mList.add(R.drawable.img09);
            mList.add(R.drawable.img10);
            mList.add(R.drawable.img11);
            mList.add(R.drawable.img12);
            mList.add(R.drawable.img13);
            mList.add(R.drawable.img14);
            mList.add(R.drawable.img15);
            mList.add(R.drawable.image1);
            mList.add(R.drawable.image2);
        }

        mAdapter = new ZoomAdapter(this, mList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ZoomActivity.this, position + "", Toast.LENGTH_SHORT).show();
                zoomViewAnim(view, mList.get(position));
            }
        });


    }

    private void zoomViewAnim(final View thumbView, final int imageResId) {
        if(mCurrentAnim != null) {
            mCurrentAnim.cancel();
        }

        mImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.zoom_container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        final float startScale;
        if((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        mImageView.setVisibility(View.VISIBLE);

        mImageView.setPivotX(0f);
        mImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mImageView, View.X, startBounds.left, finalBounds.left))
           .with(ObjectAnimator.ofFloat(mImageView, View.Y, startBounds.top, finalBounds.top))
           .with(ObjectAnimator.ofFloat(mImageView, View.SCALE_X, startScale, 1f))
           .with(ObjectAnimator.ofFloat(mImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(animTime);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnim = null;
            }
        });
        set.start();
        mCurrentAnim = set;

        final float startScaleFinal = startScale;
        mImageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mCurrentAnim != null) {
                    mCurrentAnim.cancel();
                }
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(mImageView, View.X, startBounds.left))
                   .with(ObjectAnimator.ofFloat(mImageView, View.Y, startBounds.top))
                   .with(ObjectAnimator.ofFloat(mImageView, View.SCALE_X, startScaleFinal))
                   .with(ObjectAnimator.ofFloat(mImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(animTime);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        mImageView.setVisibility(View.GONE);
                        mCurrentAnim = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        mImageView.setVisibility(View.GONE);
                        mCurrentAnim = null;
                    }
                });
                set.start();
                mCurrentAnim = set;
            }
        });
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        final int width = options.outWidth;
        final int height = options.outHeight;
        if(height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    class ZoomAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> mList;

        public ZoomAdapter(Context mContext, List<Integer> mList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ZoomHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.zoom_item, parent, false);
                holder = new ZoomHolder();
                holder.img = (ImageView) convertView.findViewById(R.id.zoom_item_img);
                convertView.setTag(holder);
            }else {
                holder = (ZoomHolder) convertView.getTag();
            }

//            holder.img.setImageResource(mList.get(position));
//            holder.img.setImageBitmap(decodeBitmapFromResource(getResources(), mList.get(position), 125, 125));
            loadBitmap(mList.get(position), holder.img);

            return convertView;
        }
    }

    public void loadBitmap(int resId, ImageView imageView) {
        if(cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(getResources(), null,task);
            imageView.setImageDrawable(asyncDrawable);
//            BitmapWorkerTask loadTask = new BitmapWorkerTask(imageView);
            task.execute(resId);
        }

    }

    class ZoomHolder {
        ImageView img;
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap>{

        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            return decodeBitmapFromResource(getResources(), params[0], 125, 125);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null && bitmap != null) {

                final ImageView img = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(img);

                if(img != null && this == bitmapWorkerTask) {
                    img.setImageBitmap(bitmap);
                }
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if(imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if(drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if(bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if(bitmapData == 0 || bitmapData != data) {
                bitmapWorkerTask.cancel(true);
            }else {
                return false;
            }
        }
        return true;
    }

}
