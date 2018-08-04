package com.example.scottie.winedb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * AddFragmentScreen2
 * <p>
 * Used by parent activity (AddActivity)
 * to allow user to enter in a new wine into app.
 * <p>
 * Has optional fields only
 * <p>
 * Provides interface for callback to parent activity
 *
 * @author Scott Garton
 * @version 1.0
 */
public class AddFragmentScreen2 extends Fragment {
    private Context activityContext;

    private CheckBox tastedCheckBox;
    private Button addToCellarButton;
    private Button clearCellarButton;

    private TableLayout addToCellarTableLayout;

    private EditText noBottles;
    private EditText boxNo;

    private Button nextButton;

    private Boolean addToCellarClicked = false;

    public AddFragmentScreen2() {
    }

    OnNextClickListenerScreen2 activityCallback;

    /**
     * OnNextClickListenerScreen2
     * <p>
     * Interface used to call-back to parent activity
     */
    public interface OnNextClickListenerScreen2 {
        void onNextButtonClickScreen2(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        // attach listener
        try {
            activityCallback = (OnNextClickListenerScreen2) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement SearchClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_screen2, container, false);
        getViewsById(view);
        initialiseUI();
        return view;
    }

    /**
     * getViewsById
     * <p>
     * Method to assign Views for each UI component in Fragment
     *
     * @param view View after fragment inflate
     */
    private void getViewsById(View view) {
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.overall_layout);

        TableRow tableRow01 = (TableRow) tableLayout.findViewById(R.id.TableRow01);
        addToCellarTableLayout = (TableLayout) tableLayout.findViewById(R.id.add_to_cellar_table);
        TableRow tableRowLast = (TableRow) tableLayout.findViewById(R.id.TableRowLast);

        addToCellarButton = (Button) tableRow01.findViewById(R.id.addToCellarButton);
        clearCellarButton = (Button) tableRow01.findViewById(R.id.clearCellarButton);
        tastedCheckBox = (CheckBox) tableRow01.findViewById(R.id.tastedcheckBox);

        nextButton = (Button) tableRowLast.findViewById(R.id.nextButton);
    }

    /**
     * initialiseUI
     * <p>
     * Method used to set callback for buttons, as part of initializing the UI
     */
    private void initialiseUI() {
        //Remove clear cellar button (as add cellar button has not been pressed yet)
        clearCellarButton.setVisibility(View.GONE);

        addToCellarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCellarClick();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClick();
            }
        });
    }

    /**
     * onAddToCellarClick
     * <p>
     * Method used to rejig the view within the Fragment to include new text fields for data entry
     */
    private void onAddToCellarClick() {
        // Toggle buttons that are displayed (from add cellar button, to clear cellar button)
        addToCellarTableLayout.removeAllViews();
        addToCellarClicked = true;
        addToCellarButton.setVisibility(View.GONE);
        clearCellarButton.setVisibility(View.VISIBLE);

        // --- Add new text/number fields to screen ---
        LayoutInflater inflater = LayoutInflater.from(activityContext);

        TableRow tableRow1 = (TableRow) inflater.inflate(R.layout.add_to_cellar_labels, null);
        TableRow tableRow2 = (TableRow) inflater.inflate(R.layout.add_to_cellar_fields, null);

        noBottles = (EditText) tableRow2.findViewById(R.id.noBottlesField);
        boxNo = (EditText) tableRow2.findViewById(R.id.boxNoField);

        // append new rows to table view
        addToCellarTableLayout.addView(tableRow1, new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        addToCellarTableLayout.addView(tableRow2, new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        // set callback for clear cellar button
        clearCellarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearCellarButtonClick();
            }
        });
    }

    /**
     * onClearCellarButtonClick
     * <p>
     * Method used to reset view back to initial state
     */
    private void onClearCellarButtonClick() {
        // Toggle button visibility within Fragment screen
        addToCellarTableLayout.removeAllViews();
        addToCellarClicked = false;
        clearCellarButton.setVisibility(View.GONE);
        addToCellarButton.setVisibility(View.VISIBLE);

        // setup callback function
        addToCellarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCellarClick();
            }
        });
    }

    /**
     * onNextButtonClick
     * <p>
     * method used to check required fields, then bundle data to callback to parent activity.
     */
    private void onNextButtonClick() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("TASTED", tastedCheckBox.isChecked());
        bundle.putBoolean("IN_CELLAR", addToCellarClicked);

        if (addToCellarClicked && (Utils.isEmpty(noBottles) || Utils.isEmpty(boxNo))) {
            Toast.makeText(activityContext,
                    "Fields Missing: try again", Toast.LENGTH_SHORT).show();
            return;
        } else if (addToCellarClicked) {
            bundle.putInt("NO_BOTTLES", Integer.parseInt(noBottles.getText().toString()));
            bundle.putInt("BOX_NO", Integer.parseInt(boxNo.getText().toString()));
        }

        activityCallback.onNextButtonClickScreen2(bundle);
    }
}
