package self.premi.sanjeev.nayati;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import self.premi.sanjeev.nayati.db.DbConst;
import self.premi.sanjeev.nayati.db.DbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    /**
     * Did we check for presence of database?
     */
    private boolean dbCheck = false;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("dbCheck", dbCheck);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            dbCheck = savedInstanceState.getBoolean("dbCheck");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!dbCheck) {
            new CreateDb().execute();
        }
    }


    /**
     * Asynchronous to create database if it wasn't found.
     */
    public class CreateDb extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            boolean dbExists = false;

            SQLiteDatabase db = null;

            try {
                /*
                 * Check if the database file exists.
                 */
                File dbFile = getActivity().getApplicationContext().getDatabasePath(DbConst.DB_FILE);

                if (dbFile.exists()) {
                    /*
                     * Attempt to open the database.
                     */
                    String path = dbFile.getAbsolutePath();

                    db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
                }

            }
            catch (SQLiteException e) {
            }
            finally {
                if (db != null) {
                    db.close();
                    dbExists = true;
                }
            }

            if (dbExists) return DbConst.DB_EXISTS_ALREADY;

            /*
             * Setup database - with defaults
             */
            DbHelper dbh = DbHelper.getInstance(getContext());

            db = dbh.getWritableDatabase();

            if (db == null) return DbConst.DB_CREATE_FAILURE;

            return DbConst.DB_CREATE_SUCCESS;
        }


        @Override
        protected void onPostExecute(Integer result) {

            int resId;

            switch (result) {
                case DbConst.DB_CREATE_SUCCESS:
                    resId = R.string.msg_db_created;
                    break;

                case DbConst.DB_EXISTS_ALREADY:
                    resId = R.string.msg_db_already;
                    break;

                default:
                    resId = R.string.msg_db_failure;
            }

            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

            dbCheck = true;
        }
    }
}
