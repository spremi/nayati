/*
 * [Nayati] ItemDetailActivityFragment.java
 *
 * Implements fragment to view detailed information for an item being tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import self.premi.sanjeev.nayati.db.DaoTrackInfo;
import self.premi.sanjeev.nayati.db.DaoTrackItem;
import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.TrackInfo;
import self.premi.sanjeev.nayati.model.TrackItem;


/**
 * Fragment to view detailed information for an item being tracked.
 */
public class ItemDetailActivityFragment extends Fragment {

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
    private ItemDetailRvAdapter rva = null;

    /**
     * Tracking number of the item
     */
    private String trackNum;

    /**
     * DAO for items being tracked
     */
    private DaoTrackItem daoTrackItem = null;

    /**
     * DAO for tracking information for an item
     */
    private DaoTrackInfo daoTrackInfo = null;

    /**
     * Items being tracked
     */
    private TrackItem item = null;

    /**
     * Tracking information related to the item
     */
    private List<TrackInfo> info = null;

    /**
     * Text view - Tracking number
     */
    private TextView textTrackNum = null;

    /**
     * Text view - Item Name
     */
    private TextView textItemName = null;


    public ItemDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (llm == null) {
            llm = new LinearLayoutManager(getActivity());

            llm.setOrientation(LinearLayoutManager.VERTICAL);
        }

        if (rv == null) {
            rv = (RecyclerView) v.findViewById(R.id.rv_item_detail);

            rv.setHasFixedSize(true);
        }

        //
        // Fetch item from database
        //
        if (daoTrackItem == null) {
            daoTrackItem = new DaoTrackItem(getContext());
        }

        daoTrackItem.open(DbConst.RO_MODE);
        item = daoTrackItem.get(trackNum);
        daoTrackItem.close();

        //
        // Read tracking information related to 'item' from database
        //
        if (daoTrackInfo == null) {
            daoTrackInfo = new DaoTrackInfo(getContext());
        }

        daoTrackInfo.open(DbConst.RO_MODE);

        info = daoTrackInfo.list(item.getId());

        daoTrackInfo.close();

        rva = new ItemDetailRvAdapter(info);

        rv.setAdapter(rva);
        rv.setLayoutManager(llm);

        textTrackNum = (TextView) v.findViewById(R.id.item_detail_text_track_num);
        textItemName = (TextView) v.findViewById(R.id.item_detail_text_track_name);

        textTrackNum.setText(trackNum);
        textItemName.setText(item.getName());

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        trackNum = ((ItemDetailActivity) getActivity()).getTrackNum();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //
        // Inflate the menu and add to the action bar
        //
        inflater.inflate(R.menu.menu_item_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_detail_action_delete) {
            actionItemDelete();

            return true;
        }
        else if (id == R.id.item_detail_action_refresh) {
            actionItemRefresh();

            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Delete the current item and its history
     */
    private void actionItemDelete() {
    }


    /**
     * Refresh details of current item
     */
    private void actionItemRefresh() {
    }
}
