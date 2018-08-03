package com.example.scottie.winedb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;

public class SearchFragment extends ListFragment
{
    private static final int DELETE_ID = Menu.FIRST + 3;

    private Context activityContext;

    private SearchView searchView;
    private CheckBox inCellarCheckBox;
    private CheckBox tastedCheckBox;
    private Button getAllWinesButton;

    private DBHelper db = null;
    private Cursor cursor = null;

    public SearchFragment() {}

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        getAllWines();
        getViewsById(view);
        initialiseUI();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    private void getViewsById(View view)
    {
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.overall_layout);
        TableRow tableRow01 = (TableRow) tableLayout.findViewById(R.id.TableRow01);
        TableRow tableRow02 = (TableRow) tableLayout.findViewById(R.id.TableRow02);
        TableRow tableRow03 = (TableRow) tableLayout.findViewById(R.id.TableRow03);

        searchView = (SearchView) tableRow01.findViewById(R.id.searchView);
        inCellarCheckBox = (CheckBox) tableRow02.findViewById(R.id.inCellarCheckBox);
        tastedCheckBox = (CheckBox) tableRow02.findViewById(R.id.tastedCheckBox);
        getAllWinesButton = (Button) tableRow03.findViewById(R.id.showAllButton);
    }

    private void initialiseUI()
    {
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                querySubmit(query, inCellarCheckBox.isChecked(), tastedCheckBox.isChecked());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        inCellarCheckBox.setOnCheckedChangeListener(new CheckBoxChecker());
        tastedCheckBox.setOnCheckedChangeListener(new CheckBoxChecker());

        getAllWinesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                inCellarCheckBox.setChecked(false);
                tastedCheckBox.setChecked(false);
                getAllWines();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        db.close();
        cursor.close();
        Fragment newFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("ID", id);
        newFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_layout, newFragment)
                .commit();
    }

    class CheckBoxChecker implements CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (buttonView == inCellarCheckBox)
            {
                querySubmit(searchView.getQuery().toString(),
                        isChecked, tastedCheckBox.isChecked());
            }
            else if (buttonView == tastedCheckBox)
            {
                querySubmit(searchView.getQuery().toString(),
                        inCellarCheckBox.isChecked(), isChecked);
            }
        }
    }

    private void getAllWines()
    {
        db = DBHelper.getInstance(activityContext);

        if (cursor != null) cursor.close(); // close old cursor

        String selectAllQuery = "SELECT _ID, " +
                DBHelper.WINE_NAME + ", " + DBHelper.VINTAGE + ", " +
                DBHelper.VINEYARD + ", " + DBHelper.VARIETY +
                " FROM " + DBHelper.WINE_TBL + " ORDER BY " + DBHelper.VINEYARD;

        cursor = db.getReadableDatabase().rawQuery(selectAllQuery, null);

        String[] from = new String[]
                { DBHelper.WINE_NAME, DBHelper.VINTAGE, DBHelper.VINEYARD, DBHelper.VARIETY };
        int[] to = new int[]
                { R.id.listWineName, R.id.listVintage, R.id.listVineyard, R.id.listVariety };

        ListAdapter adapter = new SimpleCursorAdapter(activityContext,
                R.layout.search_listrow, cursor, from, to, 0);

        setListAdapter(adapter);
    }

    private void querySubmit(String query, Boolean inCellarOnly, Boolean tastedOnly)
    {
        // true = 1, false = 0
        db = DBHelper.getInstance(activityContext);

        if (cursor != null) cursor.close(); // close old cursor

        String[] fieldsToGetFrom = new String[]
                { DBHelper._ID, DBHelper.WINE_NAME, DBHelper.VINTAGE, DBHelper.VINEYARD, DBHelper.VARIETY };

        String selection = "(" + DBHelper.WINE_NAME + " LIKE" + "'%" + query + "%' OR " +
                                 DBHelper.VINTAGE + " LIKE" + "'%" + query + "%' OR " +
                                 DBHelper.VINEYARD + " LIKE" + "'%" + query + "%' OR " +
                                 DBHelper.VARIETY + " LIKE" + "'%" + query + "%' OR " +
                                 DBHelper.REGION + " LIKE" + "'%" + query + "%')";

        if (inCellarOnly && tastedOnly)
        {
            selection += " AND (" + DBHelper.IN_CELLAR + " = 1 AND " + DBHelper.TASTED + " = 1)";
        }
        if (inCellarOnly)
        {
            selection += " AND " + DBHelper.IN_CELLAR + " = 1";
        }
        else if (tastedOnly)
        {
            selection += " AND " + DBHelper.TASTED + " = 1";
        }

        cursor = db.getReadableDatabase().query(DBHelper.WINE_TBL, fieldsToGetFrom, selection,
                null, null, null, DBHelper.VINEYARD, null);

        String[] from = new String[]
                { DBHelper.WINE_NAME, DBHelper.VINTAGE, DBHelper.VINEYARD, DBHelper.VARIETY };
        int[] to = new int[]
                { R.id.listWineName, R.id.listVintage, R.id.listVineyard, R.id.listVariety };

        ListAdapter adapter = new SimpleCursorAdapter(activityContext,
                R.layout.search_listrow, cursor, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (cursor != null)
            cursor.close();

        if (db != null)
            db.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        // add(groupId, itemID, order, title)
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete")
                .setAlphabeticShortcut('d');
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                delete(info.id);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete(final long rowId)
    {
        if (rowId > 0)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.delete_wine)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton)
                                {
                                    processDelete(rowId);
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int whichButton) {}
                            }).show();
        }
    }

    private void processDelete(long rowId)
    {
        String[] args = { String.valueOf(rowId) };

        db = DBHelper.getInstance(getActivity());
        db.getWritableDatabase().delete(DBHelper.WINE_TBL, "_ID=?", args);

        String query = searchView.getQuery().toString();
        if (query.length() > 0)
        {
            querySubmit(query, inCellarCheckBox.isChecked(), tastedCheckBox.isChecked());
        }
        else
        {
            getAllWines();
        }
    }
}