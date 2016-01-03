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

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import self.premi.sanjeev.nayati.db.DaoItemCategory;
import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.model.ItemCategory;


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
     * List of item categories
     */
    private List<ItemCategory> itemCats;

    /**
     * Spinner showing item categories
     */
    private Spinner spinItemCat;


    public ItemAddActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoItemCat = new DaoItemCategory(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_add, container, false);

        setupSpinItemCategory(v);

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
