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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ItemDetailActivity extends AppCompatActivity {

    /**
     * Tracking number of the item
     */
    private String trackNum;

    /**
     * Flag indicates if database was changed in the fragment.
     */
    private boolean dbChanged = false;


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

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setLogo(R.drawable.ic_nayati);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * Return to previous activity
                 */
                Intent ri = new Intent();
                ri.putExtra("refresh", dbChanged);

                setResult(RESULT_CANCELED, ri);

                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent ri = new Intent();
        ri.putExtra("refresh", dbChanged);

        setResult(RESULT_CANCELED, ri);

        super.onBackPressed();
    }


    public String getTrackNum() {
        return trackNum;
    }


    public void setDbChanged() {
        dbChanged = true;
    }
}
