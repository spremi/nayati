/*
 * [Nayati] ItemDetailActivity.java
 *
 * Implements activity to view tracking information for an item being tracked.
 *
 * (c) 2015 Sanjeev Premi (spremi@ymail.com)
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *                          (http://spdx.org/licenses/BSD-3-Clause.html)
 *
 */

package self.premi.sanjeev.nayati;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ItemDetailActivity extends AppCompatActivity {

    /**
     * Tracking number of the item
     */
    private String trackNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Get our intent and extract the tracking number
         */
        Intent i = getIntent();

        trackNum = i.getExtras().getString("trackNum");

        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * Return to previous activity
                 */
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        // Inflate the menu and add to the action bar
        //
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);

        return true;
    }


    public String getTrackNum() {
        return trackNum;
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
