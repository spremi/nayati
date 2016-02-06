/*
 * [Nayati] ItemListActivity.java
 *
 * Implements activity to list items being tracked.
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


public class ItemListActivity extends AppCompatActivity {

    /**
     * Request code for 'ItemAdd' activity
     */
    public static final int REQUEST_ITEM_ADD = 101;

    /**
     * Request code for 'ItemDetail' activity
     */
    public static final int REQUEST_ITEM_DETAIL = 102;

    /**
     * Request code for 'ItemEdit' activity
     */
    public static final int REQUEST_ITEM_EDIT = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
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
                 * Create intent to start 'add' activity
                 */
                Intent i = new Intent(ItemListActivity.this, ItemAddActivity.class);

                startActivityForResult(i, REQUEST_ITEM_ADD);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean updateFlag = false;

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ITEM_ADD) ||
            (requestCode == REQUEST_ITEM_EDIT) ||
            (requestCode == REQUEST_ITEM_DETAIL)) {

            if (resultCode == RESULT_CANCELED) {
                updateFlag = data.getBooleanExtra("refresh", false);
            }
        }

        if (updateFlag) {
            ItemListActivityFragment f = (ItemListActivityFragment)
                                            getSupportFragmentManager()
                                                    .findFragmentById(R.id.fragment_item_list);

            f.refresh();
        }
    }
}
