/*
 * [Nayati] ItemAddActivityFragment.java
 *
 * Implements fragment to add an item to be tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */


package self.premi.sanjeev.nayati;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import self.premi.sanjeev.nayati.db.DaoItemCategory;
import self.premi.sanjeev.nayati.db.DaoTrackItem;
import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.ItemCategory;
import self.premi.sanjeev.nayati.model.TrackItem;
import self.premi.sanjeev.nayati.model.TrackNum;


/**
 * Fragment to add an item to be tracked.
 */
public class ItemAddActivityFragment
        extends Fragment
        implements AdapterView.OnItemSelectedListener {

    /**
     * DAO for item categories
     */
    private DaoItemCategory daoItemCat;

    /**
     * DAO for items being tracked
     */
    private DaoTrackItem daoTrackItem;

    /**
     * List of item categories
     */
    private List<ItemCategory> itemCats;

    /**
     * Edit control for tracking number
     */
    private EditText editTrackNum;

    /**
     * Edit control for item name
     */
    private EditText editItemName;

    /**
     * Spinner showing item categories
     */
    private Spinner spinItemCat;

    /**
     * Button for adding an item
     */
    private Button btnAddItem;

    /**
     * Item being edited.
     */
    private TrackItem useTrackItem;


    public ItemAddActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoItemCat = new DaoItemCategory(this.getContext());
        daoTrackItem = new DaoTrackItem(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_add, container, false);

        editTrackNum = (EditText) v.findViewById(R.id.item_add_edit_track_num);
        editItemName = (EditText) v.findViewById(R.id.item_add_edit_track_name);

        setupSpinItemCategory(v);
        setupButtonAdd(v);

        String useTrackNum = ((ItemAddActivity) getActivity()).getEditTrackNum();

        if (useTrackNum != "") {
            //
            // Fetch item from database
            //
            daoTrackItem.open(DbConst.RO_MODE);
            useTrackItem = daoTrackItem.get(useTrackNum);
            daoTrackItem.close();

            //
            // Set values in the 'Add' form.
            //
            editTrackNum.setText(useTrackItem.getTrackNum());
            editItemName.setText(useTrackItem.getName());

            long itemCat = useTrackItem.getCategory();

            boolean done = false;
            for (int i = 0; (!done && i < itemCats.size()); i++) {
                if (itemCats.get(i).getId() == itemCat) {
                    spinItemCat.setSelection(i);
                    done = true;
                }
            }

            btnAddItem.setText(R.string.item_add_hint_update);
        } else {
            useTrackItem = null;
        }

        return v;
    }


    /*
     * Setup spinner for item categories
     */
    private void setupSpinItemCategory(View v) {
        spinItemCat = (Spinner) v.findViewById(R.id.item_add_spin_item_cat);

        /*
         * Get list of item categories
         */
        if (itemCats == null) {
            daoItemCat.open(DbConst.RO_MODE);
            itemCats = daoItemCat.list();
            daoItemCat.close();
        }

        /*
         * Set adapter for spinner
         */
        ArrayList<String> spinOpts = new ArrayList<>();

        for (ItemCategory cat : itemCats) {
            spinOpts.add(cat.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                                android.R.layout.simple_spinner_item, spinOpts);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinItemCat.setAdapter(adapter);

        /*
         * Set the selection listener
         */
        spinItemCat.setOnItemSelectedListener(this);
    }


    /*
     * Setup button to add an item
     */
    private void setupButtonAdd(View v) {
        btnAddItem = (Button) v.findViewById(R.id.item_add_btn_add_item);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trackNum = editTrackNum.getText().toString();
                String itemName = editItemName.getText().toString();
                String itemCat  = spinItemCat.getSelectedItem().toString();

                if (trackNum.isEmpty()) {
                    Snackbar.make(v, R.string.msg_empty_track_num, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();

                    return;
                } else {
                    TrackNum tn = new TrackNum(trackNum);

                    if (tn.identify() == TrackNum.F_NONE) {
                        Snackbar.make(v, R.string.msg_track_num_format, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null)
                                .show();

                        return;
                    }
                }

                if (itemName.isEmpty()) {
                    Snackbar.make(v, R.string.msg_empty_item_name, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();

                    return;
                }

                long catId = -1;

                /*
                 * Get id of selected category
                 */
                for (int i = 0; (catId == -1) && (i < itemCats.size()); i++) {
                    if (itemCats.get(i).getName().equals(itemCat)) {
                        catId = itemCats.get(i).getId();
                    }
                }

                /*
                 * Add/ Update item to/in database
                 */
                boolean opStatus = false;
                int opMessage;

                daoTrackItem.open(DbConst.RW_MODE);

                if (useTrackItem == null) {
                    long itemId = daoTrackItem.add(trackNum, itemName, catId);

                    if (itemId == -1) {
                        opMessage = R.string.item_add_failure;
                    } else {
                        opStatus  = true;
                        opMessage = R.string.item_add_success;
                    }
                } else {
                    useTrackItem.setTrackNum(trackNum);
                    useTrackItem.setName(itemName);
                    useTrackItem.setCategory(catId);

                    boolean status = daoTrackItem.update(useTrackItem);

                    if (status) {
                        opStatus  = true;
                        opMessage = R.string.item_update_success;
                    } else {
                        opMessage = R.string.item_update_failure;
                    }
                }

                daoTrackItem.close();

                if (opStatus) {
                    /*
                     * Update change flag in the activity
                     */
                    ((ItemAddActivity) getActivity()).setDbChanged();
                }

                Snackbar.make(v, opMessage, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();

                /*
                 * Clear the form.
                 * Don't change category spinner - assuming it would be immediately
                 * used for adding next item (if any).
                 */
                editTrackNum.setText("");
                editTrackNum.clearFocus();

                editItemName.setText("");
                editItemName.clearFocus();

                spinItemCat.clearFocus();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
