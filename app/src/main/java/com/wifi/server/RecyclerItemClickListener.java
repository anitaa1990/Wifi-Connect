package com.wifi.server;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    private OnRecyclerViewItemClickListener recyclerViewItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View parentView, View childView, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    public RecyclerItemClickListener(Context context, OnRecyclerViewItemClickListener listener) {
        recyclerViewItemClickListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
        } else if (childView != null && recyclerViewItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            recyclerViewItemClickListener.onItemClick(view, childView, view.getChildPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}