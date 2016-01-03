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

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment to view list of items being tracked.
 */
public class ItemListActivityFragment extends Fragment {

    /**
     * Recycler view for the activity.
     */
    private RecyclerView rv = null;

    /**
     * Linear layout manager for the recycler view
     */
    LinearLayoutManager llm = null;


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

        rv.setLayoutManager(llm);

        return v;
    }
}
