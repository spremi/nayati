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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import self.premi.sanjeev.nayati.db.DaoItemCategory;
import self.premi.sanjeev.nayati.db.DaoTrackInfo;
import self.premi.sanjeev.nayati.db.DaoTrackItem;
import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.ItemCategory;
import self.premi.sanjeev.nayati.model.TrackInfo;
import self.premi.sanjeev.nayati.model.TrackItem;
import self.premi.sanjeev.nayati.model.TrackNum;


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
     * DAO for item categories
     */
    private DaoItemCategory daoItemCategory= null;

    /**
     * Item being tracked
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

    /**
     * Text view - Item Category
     */
    private TextView textItemCat = null;

    /**
     * Image view - Mail service logo
     */
    private ImageView logoSvc = null;

    /**
     * Image view - Item state
     */
    private ImageView itemState = null;

    /**
     * Flag indicating availability of information
     */
    private boolean gotInfo = false;


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

        //
        // Get information on the item category
        //
        if (daoItemCategory == null) {
            daoItemCategory = new DaoItemCategory(getContext());
        }

        daoItemCategory.open(DbConst.RO_MODE);
        ItemCategory icat = daoItemCategory.get(item.getCategory());
        daoItemCategory.close();

        rva = new ItemDetailRvAdapter(info);

        rv.setAdapter(rva);
        rv.setLayoutManager(llm);

        textTrackNum = (TextView) v.findViewById(R.id.item_detail_text_track_num);
        textItemName = (TextView) v.findViewById(R.id.item_detail_text_track_name);
        textItemCat  = (TextView) v.findViewById(R.id.item_detail_text_category);

        textTrackNum.setText(trackNum);
        textItemName.setText(item.getName());
        textItemCat.setText(icat.getSymbol());

        logoSvc = (ImageView) v.findViewById(R.id.item_detail_image_post);

        TrackNum t = new TrackNum(trackNum);

        switch (t.identify()) {
            case TrackNum.F_EMS:
                logoSvc.setImageResource(R.drawable.logo_ems);
                break;

            case TrackNum.F_REG:
                logoSvc.setImageResource(R.drawable.logo_reg);
                break;

            case TrackNum.F_EPP:
                logoSvc.setImageResource(R.drawable.logo_epp);
                break;

            case TrackNum.F_EMO:
                logoSvc.setImageResource(R.drawable.logo_emo);
                break;

            default:
        }

        itemState = (ImageView) v.findViewById(R.id.item_detail_status);

        showItemState();

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
        boolean allow = false;

        View v = getActivity().findViewById(R.id.layout_act_item_detail);

        //
        // No need to refresh, if item has reached 'final' state
        //
        if (item.getState() == DbConst.ITEM_STATE_FINAL) {
            Snackbar.make(v, R.string.msg_already_delivered, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();

            return;
        }

        //
        // Check internet connectivity before moving ahead.
        //
        if (!isInternetAvailable()) {
            Snackbar.make(v, R.string.msg_no_internet, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();

            return;
        }

        String prevSync = item.getSync();

        if (prevSync.equals("")) {
            //
            // Never sync'd
            //
            allow = true;
        } else {
            try {
                Date curr = new Date();
                Date last = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(prevSync);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(curr.getTime() - last.getTime());

                //
                // Don't refresh for few hours...
                //
                if (minutes > 240) allow = true;
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (allow) {
            new GetDetails().execute(trackNum);
        } else {
            Snackbar.make(v, R.string.msg_refresh_wait, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
        }
    }


    /**
     * Read tracking information from database & notify adapter.
     */
    public void refresh() {
        View v = getActivity().findViewById(R.id.layout_act_item_detail);

        info.clear();

        if (gotInfo) {
            Snackbar.make(v, R.string.msg_refresh_info, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();

            //
            // Refresh item state to be in sync with any change of state
            //
            showItemState();

            daoTrackInfo.open(DbConst.RO_MODE);
            info = daoTrackInfo.list(item.getId());
            daoTrackInfo.close();

            rva.refresh(info);
        } else {
            Snackbar.make(v, R.string.msg_refresh_no_info, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
        }
    }


    /**
     * Is internet connection available?
     */
    private boolean isInternetAvailable() {
        boolean ret = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();

        if ((info != null) && info.isConnected()) ret = true;

        return ret;
    }


    /**
     * Save sync time for the item in database
     */
    private void saveSyncTime() {
        item.setSync(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));

        daoTrackItem.open(DbConst.RW_MODE);
        daoTrackItem.update(item);
        daoTrackItem.close();
    }

    /**
     * Set tracking state for the item in database
     */
    private void saveState(int state) {
        item.setState(state);

        daoTrackItem.open(DbConst.RW_MODE);
        daoTrackItem.update(item);
        daoTrackItem.close();

        //
        // Update change flag in the activity to refresh the state icon.
        //
        ((ItemDetailActivity)getActivity()).setDbChanged();
    }


    /**
     * Show image associated with item state
     */
    private void showItemState() {
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
                gotInfo = false;

                saveSyncTime();
                refresh();

                return;
            }

            int state = DbConst.ITEM_STATE_NONE;

            gotInfo = true;

            List<TrackInfo> fresh = parseIpsResponse(s);

            if (fresh.size() > 0) state = DbConst.ITEM_STATE_TRANSIT;

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
                TrackInfo ti = fresh.get(j);
                daoTrackInfo.add(ti);

                if (ti.getEvent().contains("Deliver item")) {
                    state = DbConst.ITEM_STATE_FINAL;
                }
            }

            daoTrackInfo.close();

            saveSyncTime();
            saveState(state);

            //
            // Refresh the view
            //
            refresh();
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
            SimpleDateFormat fmtInp = new SimpleDateFormat("M/d/yyyy h:m:s aa", Locale.US);
            SimpleDateFormat fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

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
