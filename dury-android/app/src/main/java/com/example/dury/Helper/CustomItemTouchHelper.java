package com.example.dury.Helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Adaper.CardAdapter;
import com.example.dury.Model.CardItem;

public class CustomItemTouchHelper extends ItemTouchHelper.Callback {
    private final CardAdapter todoAdapter;
    private final CardAdapter doingAdapter;
    private final CardAdapter doneAdapter;

    public CustomItemTouchHelper(CardAdapter todoAdapter, CardAdapter doingAdapter, CardAdapter doneAdapter) {
        this.todoAdapter = todoAdapter;
        this.doingAdapter = doingAdapter;
        this.doneAdapter = doneAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // Enable drag and drop in all directions
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);  // No swipe gestures
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // Handle movement of items between adapters and RecyclerViews
        int fromPosition = viewHolder.getBindingAdapterPosition();
        int toPosition = target.getBindingAdapterPosition();

        if (fromPosition == RecyclerView.NO_POSITION || toPosition == RecyclerView.NO_POSITION) {
            return false;
        }

        if (recyclerView.getAdapter() == todoAdapter) {
            if (toPosition < doingAdapter.getItemCount()) {
                CardItem item = todoAdapter.removeItem(fromPosition);
                doingAdapter.addItem(item, toPosition);
            } else if (toPosition >= doingAdapter.getItemCount() && toPosition < doneAdapter.getItemCount()) {
                CardItem item = todoAdapter.removeItem(fromPosition);
                doneAdapter.addItem(item, toPosition - doingAdapter.getItemCount());
            }
        }

        else if (recyclerView.getAdapter() == doingAdapter) {
            if (toPosition > todoAdapter.getItemCount()) {

                CardItem item = doingAdapter.removeItem(fromPosition);
                todoAdapter.addItem(item, toPosition);
            } else if (toPosition >= (todoAdapter.getItemCount() + doneAdapter.getItemCount())) {

                CardItem item = doingAdapter.removeItem(fromPosition);
                doneAdapter.addItem(item, toPosition - (todoAdapter.getItemCount() + doingAdapter.getItemCount()));
            }
        }

        else if (recyclerView.getAdapter() == doneAdapter) {
            if (toPosition < doneAdapter.getItemCount()) {

                CardItem item = doneAdapter.removeItem(fromPosition);
                todoAdapter.addItem(item, toPosition);
            } else if (toPosition >= (todoAdapter.getItemCount() + doingAdapter.getItemCount()) && toPosition < (todoAdapter.getItemCount() + doingAdapter.getItemCount() + doneAdapter.getItemCount())) {
                // Move item from DONE list to DOING list
                CardItem item = doneAdapter.removeItem(fromPosition);
                doingAdapter.addItem(item, toPosition - (todoAdapter.getItemCount() + doingAdapter.getItemCount()));
            }
        }

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Not used in this implementation
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;  // Enable long press to drag items
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;  // Disable swiping items
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setAlpha(0.5f);  // Dim the view
            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setAlpha(1.0f);  // Restore full opacity
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder != null) {
            viewHolder.itemView.setAlpha(1.0f);  // Ensure full opacity when item is released
        }
    }
}
