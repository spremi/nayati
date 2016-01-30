/*
 * [Nayati] ItemListActivityFragment.java
 *
 * Implements fragment to list items being tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import self.premi.sanjeev.nayati.db.DaoTrackItem;
import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.TrackItem;


/**
 * Fragment to view list of items being tracked.
 */
public class ItemListActivityFragment
        extends Fragment
        implements ItemClickListener {

    /**
     * Recycler view for the activity.
     */
    private RecyclerView rv = null;

    /**
     * Linear layout manager for the recycler view
     */
    LinearLayoutManager llm = null;

    /**
     * Adapter for recycler view
     */
    private ItemListRvAdapter rva = null;

    /**
     * DAO for items being tracked
     */
    private DaoTrackItem daoTrackItem = null;

    /**
     * List of items being tracked
     */
    List<TrackItem> items = null;


    public ItemListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (llm == null) {
            llm = new LinearLayoutManager(getActivity());

            llm.setOrientation(LinearLayoutManager.VERTICAL);
        }

        if (rv == null) {
            rv = (RecyclerView) v.findViewById(R.id.rv_item_list);

            rv.setHasFixedSize(true);
        }

        //
        // Read list of items from database
        //
        if (daoTrackItem == null) {
            daoTrackItem = new DaoTrackItem(getContext());
        }

        if (items == null) {
            daoTrackItem.open(DbConst.RO_MODE);
            items = daoTrackItem.list();
            daoTrackItem.close();
        }

        rva = new ItemListRvAdapter(items);
        rva.setItemClickListener(this);

        rv.setAdapter(rva);
        rv.setLayoutManager(llm);

        return v;
    }


    /**
     * Read list of items from database & notify adapter.
     */
    public void refresh() {
        items.clear();

        daoTrackItem.open(DbConst.RO_MODE);
        items = daoTrackItem.list();
        daoTrackItem.close();

        rva.refresh(items);
    }


    /**
     * Handle item click event
     */
    public void onItemClick(int pos) {
        /*
         * Create intent to start 'detail' activity
         */
        Intent i = new Intent(getActivity(), ItemDetailActivity.class);

        i.putExtra("trackNum", items.get(pos).getTrackNum());

        getActivity().startActivityForResult(i, ItemListActivity.REQUEST_ITEM_DETAIL);
    }


    /**
     * Handle long item click event
     */
    public void onItemLongClick(int pos) {
        /*
         * Create intent to start 'add' activity - for editing
         */
        Intent i = new Intent(getActivity(), ItemAddActivity.class);

        i.putExtra("trackNum", items.get(pos).getTrackNum());

        getActivity().startActivityForResult(i, ItemListActivity.REQUEST_ITEM_EDIT);
    }
}
