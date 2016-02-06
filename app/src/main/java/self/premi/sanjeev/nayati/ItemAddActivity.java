/*
 * [Nayati] ItemAddActivity.java
 *
 * Implements activity to add an item to be tracked.
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


public class ItemAddActivity extends AppCompatActivity {

    /**
     * Flag indicates if database was changed in the fragment.
     */
    private boolean dbChanged = false;

    /**
     * Tracking number for item to be edited.
     */
    String editTrackNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Get our intent and extract the tracking number (when editing existing item)
         */
        Intent i = getIntent();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            editTrackNum = i.getExtras().getString("trackNum");
        } else {
            editTrackNum = "";
        }

        setContentView(R.layout.activity_item_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (editTrackNum != "") {
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(R.string.title_activity_item_edit);
            }
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


    public void setDbChanged() {
        dbChanged = true;
    }


    /**
     * Return tracking number for editing.
     */
    public String getEditTrackNum() {
        return editTrackNum;
    }
}
