package com.example.noteapp.listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This TouchListener is Using for very First time ThemeSelection Screen with SwipeLeft Finger
 */
public abstract class OnSwipeTouchListener implements OnTouchListener {
    private static int collapse = 0;
    private final GestureDetector gestureDetector;
    private final GestureListener gestureListener;
    private MyCallBack callBack;
    private int position;
    private boolean isMoving = false;

    public OnSwipeTouchListener(Context ctx, int position) {
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetector(ctx, gestureListener);

        this.position = position;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public boolean onDoubleTouch() {
        if (collapse == 2) {
            collapse = 0;
            return true;
        }
        return false;
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 1;
        private static final int SWIPE_VELOCITY_THRESHOLD = 1;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            collapse++;

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}