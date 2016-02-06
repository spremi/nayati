/*
 * [Nayati] ItemListRvAdapter.java
 *
 * Implements RecyclerViewer adapter for showing list of items being tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.TrackItemView;


/**
 * RecyclerViewer adapter for showing list of items being tracked.
 */
public class ItemListRvAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * List of items being tracked.
     */
    private List<TrackItemView> items;

    /**
     * Listener for the click event on items in the list.
     * Must be static to as it would be called from ItemListHolder.onClick()
     */
    private static ItemClickListener itemClickListener = null;


    /**
     * Constructor
     */
    public ItemListRvAdapter(List<TrackItemView> items) {
        this.items = items;
    }

    public void setItemClickListener(ItemClickListener listener) {
        itemClickListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_track_item, parent, false);

        return new ItemListHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemListHolder h = (ItemListHolder) holder;

        h.bindTrackItem(items.get(position));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Update list of items
     */
    public void refresh(List<TrackItemView> items) {
        this.items.clear();

        this.items = items;

        notifyDataSetChanged();
    }


    /**
     * Defines view holder
     */
    public static class ItemListHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private TrackItemView item;

        public TextView trackNum;
        public TextView itemName;
        public TextView itemCat;
        public ImageView itemState;


        /**
         * Constructor
         */
        public ItemListHolder(View iv) {
            super(iv);

            trackNum = (TextView) iv.findViewById(R.id.item_list_text_track_num);
            itemName = (TextView) iv.findViewById(R.id.item_list_text_track_name);
            itemCat  = (TextView) iv.findViewById(R.id.item_list_text_category);
            itemState = (ImageView) iv.findViewById(R.id.item_list_text_track_state);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        /**
         * Bind an item
         */
        public void bindTrackItem(TrackItemView it) {
            item = it;

            trackNum.setText(item.getTrackNum());
            itemName.setText(item.getName());
            itemCat.setText(item.getCategory());

            switch (item.getState()) {
                case DbConst.ITEM_STATE_FINAL:
                    itemState.setImageResource(R.drawable.icon_arrived);
                    break;

                case DbConst.ITEM_STATE_TRANSIT:
                    itemState.setImageResource(R.drawable.icon_transit);
                    break;

                default:
                    itemState.setImageResource(R.drawable.icon_unknown);
            }
        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemLongClick(getLayoutPosition());

                return true;
            }

            return false;
        }
    }
}
