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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
        //
        // Show alert for deletion
        //
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setCancelable(false)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        long itemId = item.getId();

                        //
                        // Delete tracking information associated with item
                        //
                        daoTrackInfo.open(DbConst.RW_MODE);
                        daoTrackInfo.delete(itemId);
                        daoTrackInfo.close();

                        //
                        // Delete item itself
                        //
                        daoTrackItem.open(DbConst.RW_MODE);
                        daoTrackItem.delete(itemId);
                        daoTrackItem.close();

                        //
                        // Update change flag in the activity
                        //
                        ((ItemDetailActivity)getActivity()).setDbChanged();

                        //
                        // Return to previous activity
                        //
                        getActivity().onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog ad = builder.create();

        ad.show();
    }


    /**
     * Refresh details of current item
     */
    private void actionItemRefresh() {
        new GetDetails().execute(trackNum);
    }


    /**
     * Asynchronous task to get tracking information from IPS Web
     */
    private class GetDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            IpsWeb web = new IpsWeb();

            if (web.request(params[0])) {
                return web.read();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //
            // No information?
            //
            if (s.contains("No information")) {
                // TODO
                return;
            }

            List<TrackInfo> fresh = parseIpsResponse(s);


            daoTrackInfo.open(DbConst.RW_MODE);

            //
            // The lists have been reversed.
            // Compare contents backwards.
            //

            int i = info.size()  - 1;
            int j = fresh.size() - 1;

            //
            // Update existing information
            //
            for (; (i >= 0) && (j >= 0); i--, j--) {
                if (!info.get(i).matches(fresh.get(j))) {
                    fresh.get(j).setId(info.get(i).getId());

                    daoTrackInfo.update(fresh.get(j));
                }
            }

            //
            // Add 'new' data to database.
            //
            for (; j >=0 ; j--) {
                daoTrackInfo.add(fresh.get(j));
            }

            daoTrackInfo.close();
        }


        /**
         * Parse response from IPS Web Tracking into
         */
        private List<TrackInfo> parseIpsResponse(String s) {
            List<TrackInfo> parseItems = new ArrayList<>();

            StringBuilder sb = new StringBuilder(s);

            //
            // Tracking information is contained in the 'deepest' table
            // Remove extra content
            //
            String tableStart = "<table";
            String tableEnd   = "</table>";

            sb.delete(0, sb.lastIndexOf(tableStart));
            sb.delete(sb.indexOf(tableEnd) + tableEnd.length(), sb.length());

            String rowStart = "<tr";
            String rowEnd   = "</tr>";

            //
            // Date formats for conversion.
            //
            SimpleDateFormat fmtInp = new SimpleDateFormat("M/d/yyyy h:m:s aa");
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String row;

            int pos = 0;
            int len = sb.lastIndexOf(rowEnd);

            while (pos < len) {
                //
                // Get a row from table
                //
                int start = sb.indexOf(rowStart, pos);
                int end   = sb.indexOf(rowEnd, pos);

                row = sb.substring(start, end);

                //
                // Remove all comments in the row
                //
                row = row.replaceAll("<!--.*?-->", "");

                //
                // Remove opening and closing tags - tr, th, td
                //
                row = row.replaceAll("<t[rdh]\\s.*?>", "|");
                row = row.replaceAll("</t[rdh]>", "|");

                //
                // Trim multiple occurrences of delimiter
                //
                row = row.replaceAll("\\|+(\\s*)?\\|", "|");

                //
                // Skip the 'header' rows
                //
                if ((row.indexOf("new search") == -1) &&
                    (row.indexOf("Local Date and Time") == -1)) {
                    String[] parts = row.split("\\|");

                    //
                    // Convert date format
                    //
                    try {
                        parts[1] = fmtOut.format(fmtInp.parse(parts[1]));
                    }
                    catch (ParseException e) {
                    }

                    parseItems.add(new TrackInfo(parts[1], parts[2], parts[3], parts[4],
                                                    parts[6], parts[5], item.getId()));
                }

                pos = end + rowEnd.length();
            }

            if (parseItems.size() > 1) {
                Collections.reverse(parseItems);
            }

            return parseItems;
        }
    }
}
