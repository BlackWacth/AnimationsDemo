package com.hua.zoomdemo;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int lastX = 0, lastY = 0;
    private ImageView thumb;
    private RelativeLayout layout;
    private TextView local, global, offset, globalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thumb = (ImageView) findViewById(R.id.thumb);
        local = (TextView) findViewById(R.id.local);
        global = (TextView) findViewById(R.id.global);
        offset = (TextView) findViewById(R.id.offset);
        layout = (RelativeLayout) findViewById(R.id.containal);
        globalLayout = (TextView) findViewById(R.id.layout_global);

        thumb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - lastX);
                        int dy = (int) (event.getRawY() - lastY);

                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        Rect localRect = new Rect();
                        v.getLocalVisibleRect(localRect);
                        local.setText("Local : " + localRect.toString());
                        local.append("\nWidth : " + localRect.width());
                        local.append("\nHeight : " + localRect.height());

                        Rect globalRect = new Rect();
                        Point globalPoint = new Point();
                        v.getGlobalVisibleRect(globalRect, globalPoint);
                        global.setText("Global : " + globalRect.toString());
                        offset.setText("Offset : " + globalPoint.toString());
                        global.append("\nWidth : " + globalRect.width());
                        global.append("\nHeight : " + globalRect.height());

                        Rect globalLayoutRect = new Rect();
                        Point globalLayoutPoint = new Point();
                        layout.getGlobalVisibleRect(globalLayoutRect, globalLayoutPoint);
                        globalLayout.setText("LayoutGlobal : " + globalLayoutRect.toString());
                        globalLayout.append("\nLayoutPoint : " + globalLayoutPoint.toString());
                        globalLayout.append("\nWidth : " + globalLayoutRect.width());
                        globalLayout.append("\nHeight : " + globalLayoutRect.height());
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });
    }
}
