package com.example.picasodemo.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    public final static String TAG = SimpleItemTouchHelper.class.getSimpleName();
    private ItemTouchAdapter adapter;

    public SimpleItemTouchHelper(ItemTouchAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT );
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemDragged(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped : " + direction);
        adapter.onItemSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        NormalRecycleAdapter.NormalViewHolder normalViewHolder = (NormalRecycleAdapter.NormalViewHolder) viewHolder;
        normalViewHolder.mCatLayout.setTranslationX(0);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        NormalRecycleAdapter.NormalViewHolder normalViewHolder = (NormalRecycleAdapter.NormalViewHolder) viewHolder;
        int deleteWidth = normalViewHolder.mDeleteLayout.getWidth();
        float fraction = deleteWidth / (float)normalViewHolder.itemView.getWidth();
        normalViewHolder.mCatLayout.setTranslationX(fraction * dX);
    }
}
