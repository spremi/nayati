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

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import self.premi.sanjeev.nayati.model.TrackItem;


/**
 * RecyclerViewer adapter for showing list of items being tracked.
 */
public class ItemListRvAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * List of items being tracked.
     */
    private List<TrackItem> items;


    /**
     * Constructor
     */
    public ItemListRvAdapter(List<TrackItem> items) {
        this.items = items;
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
     * Defines view holder
     */
    public static class ItemListHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TrackItem item;

        public TextView trackNum;
        public TextView itemName;

        /**
         * Constructor
         */
        public ItemListHolder(View iv) {
            super(iv);

            trackNum = (TextView) iv.findViewById(R.id.item_list_text_track_num);
            itemName = (TextView) iv.findViewById(R.id.item_list_text_track_name);

            itemView.setOnClickListener(this);
        }


        /**
         * Bind an item
         */
        public void bindTrackItem(TrackItem it) {
            item = it;

            trackNum.setText(item.getTrackNum());
            itemName.setText(item.getName());
        }


        @Override
        public void onClick(View v) {
            if (item != null) {
                Intent i = new Intent(v.getContext(), ItemDetailActivity.class);

                i.putExtra("trackNum", item.getTrackNum());

                v.getContext().startActivity(i);
            }
        }
    }
}
