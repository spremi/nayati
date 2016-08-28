/*
 * [Nayati] ItemDetailRvAdapter.java
 *
 * Implements RecyclerViewer adapter for showing details of an item being tracked.
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
import android.widget.TextView;

import java.util.List;

import self.premi.sanjeev.nayati.model.TrackInfo;


/**
 * RecyclerViewer adapter for showing details of item being tracked.
 */
public class ItemDetailRvAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Tracking information associated with the item
     */
    private List<TrackInfo> items;


    /**
     * Constructor
     */
    public ItemDetailRvAdapter(List<TrackInfo> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_info_item, parent, false);

        return new InfoListHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InfoListHolder h = (InfoListHolder) holder;

        h.bindTrackInfo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Update info
     */
    public void refresh(List<TrackInfo> info) {
        this.items.clear();

        this.items = info;

        notifyDataSetChanged();
    }


    /**
     * Defines view holder
     */
    public static class InfoListHolder
            extends RecyclerView.ViewHolder {

        private TrackInfo info;

        public TextView infDate;
        public TextView infCountry;
        public TextView infCurLoc;
        public TextView infEvent;
        public TextView infNexLoc;
        public TextView infMailCat;

        /**
         * Constructor
         */
        public InfoListHolder(View iv) {
            super(iv);

            infDate     = (TextView) iv.findViewById(R.id.item_detail_text_date);
            infCountry  = (TextView) iv.findViewById(R.id.item_detail_text_country);
            infCurLoc   = (TextView) iv.findViewById(R.id.item_detail_text_cloc);
            infEvent    = (TextView) iv.findViewById(R.id.item_detail_text_event);
            infNexLoc   = (TextView) iv.findViewById(R.id.item_detail_text_nloc);
            infMailCat  = (TextView) iv.findViewById(R.id.item_detail_text_mcat);
        }


        /**
         * Bind an item
         */
        public void bindTrackInfo(TrackInfo inf) {
            info = inf;

            infDate.setText(info.getDate());
            infCountry.setText(info.getCountry());
            infCurLoc.setText(info.getCurrLoc());
            infEvent.setText(info.getEvent());
            infNexLoc.setText(info.getNextLoc());
            infMailCat.setText(info.getMailCat());
        }
    }
}
