package com.example.scottie.winedb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailsFragment extends Fragment
{
    private TextView wineNameField;
    private TextView vineyardField;
    private TextView varietyField;
    private TextView regionField;
    private TextView vintageField;
    private RatingBar ratingField;
    private TextView boxNoField;
    private TextView noOfBottlesField;
    private TextView tastedField;
    private TextView yearBoughtField;
    private TextView drinkFromField;
    private TextView drinkToField;
    private TextView pricePaidField;
    private TextView percentAlcoholField;
    private TextView notesField;

    private TextView bottleImageLabel;
    private ImageView bottleImage;

    private TextView wineNameLabel;
    private TextView vineyardLabel;
    private TextView varietyLabel;
    private TextView regionLabel;
    private TextView vintageLabel;
    private TextView boxNoLabel;
    private TextView noOfBottlesLabel;
    private TextView tastedLabel;
    private TextView yearBoughtLabel;
    private TextView drinkFromLabel;
    private TextView drinkToLabel;
    private TextView pricePaidLabel;
    private TextView percentAlcoholLabel;
    private TextView notesLabel;

    private long id;

    public DetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle bundle = getArguments();
        id = bundle.getLong("ID");

        getViewById(view);
        initialiseUI();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void getViewById(View view)
    {
        GridLayout overallLayout = (GridLayout) view.findViewById(R.id.overall_layout);
        GridLayout gridLayout01 = (GridLayout) overallLayout.findViewById(R.id.GridLayout01);

        wineNameField = (TextView) overallLayout.findViewById(R.id.wineNameField);
        vineyardField = (TextView) overallLayout.findViewById(R.id.vineyardField);
        varietyField = (TextView) overallLayout.findViewById(R.id.varietyField);
        regionField = (TextView) overallLayout.findViewById(R.id.regionField);
        vintageField = (TextView) gridLayout01.findViewById(R.id.vintageField);
        ratingField = (RatingBar) gridLayout01.findViewById(R.id.ratingBar);
        boxNoField = (TextView) overallLayout.findViewById(R.id.cellarBoxNoField);
        noOfBottlesField = (TextView) overallLayout.findViewById(R.id.noBottlesField);
        tastedField = (TextView) overallLayout.findViewById(R.id.tastedField);
        yearBoughtField = (TextView) overallLayout.findViewById(R.id.yearBoughtField);
        drinkFromField = (TextView) overallLayout.findViewById(R.id.drinkFromField);
        drinkToField = (TextView) overallLayout.findViewById(R.id.drinkToField);
        pricePaidField = (TextView) overallLayout.findViewById(R.id.pricePaidField);
        percentAlcoholField = (TextView) overallLayout.findViewById(R.id.percentAlcoholField);
        notesField = (TextView) overallLayout.findViewById(R.id.notesField);

        bottleImageLabel = (TextView) overallLayout.findViewById(R.id.bottleImageLabel);
        bottleImage = (ImageView) overallLayout.findViewById(R.id.bottleImageView);

        wineNameLabel = (TextView) overallLayout.findViewById(R.id.wineNameLabel);
        vineyardLabel = (TextView) overallLayout.findViewById(R.id.vineyardLabel);
        varietyLabel = (TextView) overallLayout.findViewById(R.id.varietyLabel);
        regionLabel = (TextView) overallLayout.findViewById(R.id.regionLabel);
        vintageLabel = (TextView) gridLayout01.findViewById(R.id.vintageLabel);
        boxNoLabel = (TextView) overallLayout.findViewById(R.id.cellarBoxNoLabel);
        noOfBottlesLabel = (TextView) overallLayout.findViewById(R.id.noBottlesLabel);
        tastedLabel = (TextView) overallLayout.findViewById(R.id.tastedLabel);
        yearBoughtLabel = (TextView) overallLayout.findViewById(R.id.yearBoughtLabel);
        drinkFromLabel = (TextView) overallLayout.findViewById(R.id.drinkFromLabel);
        drinkToLabel = (TextView) overallLayout.findViewById(R.id.drinkToLabel);
        pricePaidLabel = (TextView) overallLayout.findViewById(R.id.pricePaidLabel);
        percentAlcoholLabel = (TextView) overallLayout.findViewById(R.id.percentAlcoholLabel);
        notesLabel = (TextView) overallLayout.findViewById(R.id.notesLabel);
    }

    private void initialiseUI()
    {
        DBHelper db = DBHelper.getInstance(getActivity());
        String[] args = { String.valueOf(id) };
        Cursor cursor = db.getReadableDatabase().query(DBHelper.WINE_TBL, null, "_id= ?", args, null, null, null);

        if (cursor.moveToFirst())
        {
            int columnIndex = cursor.getColumnIndex(DBHelper.WINE_NAME);

            if (columnIndex != -1)
            {
                wineNameField.setText(cursor.getString(columnIndex));
            }
            else
            {
                wineNameField.setVisibility(View.GONE);
                wineNameLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.VINEYARD);

            if (columnIndex != -1)
            {
                vineyardField.setText(cursor.getString(columnIndex));
            }
            else
            {
                vineyardField.setVisibility(View.GONE);
                vineyardLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.VARIETY);

            if (columnIndex != -1)
            {
                varietyField.setText(cursor.getString(columnIndex));
            }
            else
            {
                varietyField.setVisibility(View.GONE);
                varietyLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.REGION);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex).equals("")))
            {
                regionField.setText(cursor.getString(columnIndex));
            }
            else
            {
                regionField.setVisibility(View.GONE);
                regionLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.VINTAGE);

            if (columnIndex != -1)
            {
                vintageField.setText(cursor.getString(columnIndex));
            }
            else
            {
                vintageField.setVisibility(View.GONE);
                vintageLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.RATING);

            if ((columnIndex != -1) && (cursor.getFloat(columnIndex) != 0))
            {
                ratingField.setRating(cursor.getFloat(columnIndex));
            }
            else
            {
                ratingField.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.IMAGE);

            if ((columnIndex != -1) && !(cursor.getBlob(columnIndex) == null))
            {
                byte[] imageBytes = cursor.getBlob(columnIndex);
                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                bottleImage.setImageBitmap(image);
            }
            else
            {
                bottleImage.setVisibility(View.GONE);
                bottleImageLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.BOX_NO);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                boxNoField.setText(cursor.getString(columnIndex));
            }
            else
            {
                boxNoField.setVisibility(View.GONE);
                boxNoLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.NO_BOTTLES);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                noOfBottlesField.setText(cursor.getString(columnIndex));
            }
            else
            {
                noOfBottlesField.setVisibility(View.GONE);
                noOfBottlesLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.TASTED);

            if (columnIndex != -1)
            {
                if (cursor.getInt(columnIndex) == 1)
                    tastedField.setText(R.string.yes);
                else
                    tastedField.setText(R.string.no);
            }
            else
            {
                tastedField.setVisibility(View.GONE);
                tastedLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.YEAR_BOUGHT);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                yearBoughtField.setText(cursor.getString(columnIndex));
            }
            else
            {
                yearBoughtField.setVisibility(View.GONE);
                yearBoughtLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.DRINK_FROM);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                drinkFromField.setText(cursor.getString(columnIndex));
            }
            else
            {
                drinkFromField.setVisibility(View.GONE);
                drinkFromLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.DRINK_TO);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                drinkToField.setText(cursor.getString(columnIndex));
            }
            else
            {
                drinkToField.setVisibility(View.GONE);
                drinkToLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.PRICE_PAID);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                pricePaidField.setText("$" + cursor.getString(columnIndex));
            }
            else
            {
                pricePaidField.setVisibility(View.GONE);
                pricePaidLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.PERCENT_ALCOHOL);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                percentAlcoholField.setText(cursor.getString(columnIndex) + "%");
            }
            else
            {
                percentAlcoholField.setVisibility(View.GONE);
                percentAlcoholLabel.setVisibility(View.GONE);
            }

            columnIndex = cursor.getColumnIndex(DBHelper.NOTES);

            if ((columnIndex != -1) && !(cursor.getString(columnIndex) == null))
            {
                notesField.setText(cursor.getString(columnIndex));
            }
            else
            {
                notesField.setVisibility(View.GONE);
                notesLabel.setVisibility(View.GONE);
            }
        }

        cursor.close();
        db.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.delete_wine)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichButton)
                                    {
                                        processDelete();
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichButton) {}
                                }).show();
                return true;
            case R.id.action_back:
                Fragment newFragment = new SearchFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                //fragmentManager.popBackStack(); ** Used to remove details fragment from back stack.
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_layout, newFragment)
                        .commit();
        }

        return super.onOptionsItemSelected(item);
    }


    private void processDelete()
    {
        String[] args = { String.valueOf(id) };

        DBHelper db = DBHelper.getInstance(getActivity());
        db.getWritableDatabase().delete(DBHelper.WINE_TBL, "_ID=?", args);
        Fragment newFragment = new SearchFragment();

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.main_layout, newFragment).commit();
    }
}
