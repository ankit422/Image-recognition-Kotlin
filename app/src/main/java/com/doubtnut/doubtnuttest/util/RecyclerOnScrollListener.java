package com.doubtnut.doubtnuttest.util;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ankit on 18/12/15.
 */
public abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = RecyclerOnScrollListener.class.getSimpleName();

    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private int current_page = 0;
    private Handler handler=new Handler();

    private LinearLayoutManager mLinearLayoutManager;

    public RecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        try {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && totalItemCount - visibleItemCount
                    <= firstVisibleItem + visibleThreshold) {
                // End has been reached

                // Do something
                current_page++;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onLoadMore(current_page);
                    }
                });

                loading = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        onScroll(newState);
    }

    public abstract void onLoadMore(int current_page);
    public abstract void onScroll(int newState);

    public void reSetscroll(){
         previousTotal = 0; // The total number of items in the dataset after the last load
        current_page = 1;
    }

    public void reSetScrollToPage2(){
        previousTotal = 0; // The total number of items in the dataset after the last load
        current_page = 2;
    }
}