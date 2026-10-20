package li.com.bobsonclinic.adapter;
/**
 * Agricola
 *
 * Created by Milla.Wang on 2016/1/5.
 * Copyright © 2016 bobsonclinic. All rights reserved.
 */

import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
		extends RecyclerView.Adapter<VH>
{
	private Cursor cursor;
	private final String TAG = RecyclerViewCursorAdapter.class.getSimpleName();
	private boolean isHeaderView = false;
	private boolean isFooterView = false;
	public static final int IS_HEADER = 2;
	public static final int IS_FOOTER = 3;
	public static final int IS_NORMAL = 1;
	public int showCount = 0;

	public boolean isHeaderView() {
		return isHeaderView;
	}

	public void setIsHeaderView(boolean isHeaderView) {
		this.isHeaderView = isHeaderView;
	}

	public boolean isFooterView() {
		return isFooterView;
	}

	public void setIsFooterView(boolean isFooterView) {
		this.isFooterView = isFooterView;
	}

	public void swapCursor(final Cursor cursor)
	{
		this.cursor = cursor;
		this.notifyDataSetChanged();
	}

	public void setShowCount( int showcount){
		this.showCount = showcount;
		this.notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		if(isHeaderView && isFooterView) {

			if(showCount > 0) {
				if(this.cursor != null && this.cursor.getCount() > showCount)
					return showCount+ 2;
			}

			return this.cursor != null ? this.cursor.getCount() + 2 : 2;
		} else if(isHeaderView || isFooterView) {

			if(showCount > 0) {
				if(this.cursor != null && this.cursor.getCount() > showCount)
					return showCount + 1;
			}

			return this.cursor != null ? this.cursor.getCount() + 1 : 1;
		}

		if(showCount > 0) {
			if(this.cursor != null && this.cursor.getCount() > showCount)
				return showCount;
		}

		return this.cursor != null? this.cursor.getCount(): 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (isHeaderView() && position == 0) {
			return IS_HEADER;
		} else if(showCount > 0 && this.cursor.getCount() > showCount && isFooterView() && ((isHeaderView() && position==(this.cursor != null? showCount+1: 1)) || (!isHeaderView() && position==(this.cursor != null? showCount: 0)))){
			return IS_FOOTER;
		} else if(isFooterView() && ((isHeaderView() && position==(this.cursor != null? this.cursor.getCount()+1: 1)) || (!isHeaderView() && position==(this.cursor != null? this.cursor.getCount(): 0)))){
			return IS_FOOTER;
		} else {
			return IS_NORMAL;
		}
	}

	public Cursor getItem(final int position)
	{
		if (this.cursor != null && !this.cursor.isClosed())
		{
			this.cursor.moveToPosition(position);
		}

		return this.cursor;
	}

	public Cursor getCursor()
	{
		return this.cursor;
	}

	@Override
	public final void onBindViewHolder(final VH holder, final int position)
	{
		if(isHeaderView() && position != 0) {
			final Cursor cursor = this.getItem(position -1);
			this.onBindViewHolder(holder, position, cursor);
		} else {
			final Cursor cursor = this.getItem(position);
			this.onBindViewHolder(holder, position, cursor);
		}
	}

	public abstract void onBindViewHolder(final VH holder, final int position, final Cursor cursor);
}