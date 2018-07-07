package com.doubtnut.doubtnuttest.util

import android.content.Context
import android.os.Build
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView


class Utilities {


    companion object {
        fun getLinearRecycler(mRecyclerView: RecyclerView, context: Context) {
            try {
                val layout = LinearLayoutManager(context)
                mRecyclerView.layoutManager = layout
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun getRecyclerGallery(mRecyclerView: RecyclerView, context: Context) {

            try {
                val layout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                mRecyclerView.layoutManager = layout
                layout.isSmoothScrollbarEnabled = true
                val recyclerViewMargin = RecyclerViewMargin(dpToPx(context, 4), 1)
                mRecyclerView.addItemDecoration(recyclerViewMargin)
                mRecyclerView.isNestedScrollingEnabled = false
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    val snap = LinearSnapHelper()
                    snap.attachToRecyclerView(mRecyclerView)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun getRecyclerGrid(mRecyclerView: RecyclerView, context: Context, item: Int) {
            try {
                val snap = LinearSnapHelper()
                val layout = GridLayoutManager(context, item)
                mRecyclerView.layoutManager = layout
                layout.isSmoothScrollbarEnabled = false
                val recyclerViewMargin = RecyclerViewMargin(dpToPx(context, 4), item)
                mRecyclerView.addItemDecoration(recyclerViewMargin)
                mRecyclerView.isNestedScrollingEnabled = false
                snap.attachToRecyclerView(mRecyclerView)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun dpToPx(context: Context, dp: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()

        }

    }


}
