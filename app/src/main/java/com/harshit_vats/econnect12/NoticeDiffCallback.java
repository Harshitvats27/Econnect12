package com.harshit_vats.econnect12;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import android.util.Log;
import java.util.List;

public class NoticeDiffCallback extends DiffUtil.Callback {

    private final List<Notice> oldList;
    private final List<Notice> newList;

    public NoticeDiffCallback(List<Notice> oldList, List<Notice> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        Log.d("NoticeDiffCallback", "Old list size: " + oldList.size());
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        Log.d("NoticeDiffCallback", "New list size: " + newList.size());
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Notice oldNotice = oldList.get(oldItemPosition);
        Notice newNotice = newList.get(newItemPosition);

        // Log comparison of items
        Log.d("NoticeDiffCallback", "Comparing items at positions " + oldItemPosition + " and " + newItemPosition);
        Log.d("NoticeDiffCallback", "Old title: " + oldNotice.getNotice_title() + ", New title: " + newNotice.getNotice_title());

        boolean result = oldNotice.getNotice_title().equals(newNotice.getNotice_title());
        Log.d("NoticeDiffCallback", "Items are the same: " + result);
        return result;
    }
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Notice oldNotice = oldList.get(oldItemPosition);
        Notice newNotice = newList.get(newItemPosition);

        // Log comparison of content
        Log.d("NoticeDiffCallback", "Comparing content at positions " + oldItemPosition + " and " + newItemPosition);
        Log.d("NoticeDiffCallback", "Old description: " + oldNotice.getNotice_description() + ", New description: " + newNotice.getNotice_description());
        Log.d("NoticeDiffCallback", "Old date: " + oldNotice.getNotice_date() + ", New date: " + newNotice.getNotice_date());
        Log.d("NoticeDiffCallback", "Old posted_by: " + oldNotice.getPosted_by() + ", New posted_by: " + newNotice.getPosted_by());

        boolean result = (oldNotice.getNotice_description() != null && oldNotice.getNotice_description().equals(newNotice.getNotice_description())) &&
                (oldNotice.getNotice_date() != null && oldNotice.getNotice_date().equals(newNotice.getNotice_date())) &&
                (oldNotice.getPosted_by() != null && oldNotice.getPosted_by().equals(newNotice.getPosted_by()));

        Log.d("NoticeDiffCallback", "Contents are the same: " + result);
        return result;
    }

}
