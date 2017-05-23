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

public class AddFragmentScreen2 extends Fragment
{
    private Context activityContext;

    private CheckBox tastedCheckBox;
    private Button addToCellarButton;
    private Button clearCellarButton;

    private TableLayout addToCellarTableLayout;

    private EditText noBottles;
    private EditText boxNo;

    private Button nextButton;

    private Boolean addToCellarClicked = false;

    public AddFragmentScreen2() {}

    OnNextClickListenerScreen2 activityCallback;

    public interface OnNextClickListenerScreen2
    {
        void onNextButtonClickScreen2(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        try
        {
            activityCallback = (OnNextClickListenerScreen2) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "must implement SearchClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_screen2, container, false);
        getViewsById(view);
        initialiseUI();
        return view;
    }

    private void getViewsById(View view)
    {
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.overall_layout);

        TableRow tableRow01 = (TableRow) tableLayout.findViewById(R.id.TableRow01);
        addToCellarTableLayout = (TableLayout) tableLayout.findViewById(R.id.add_to_cellar_table);
        TableRow tableRowLast = (TableRow) tableLayout.findViewById(R.id.TableRowLast);

        addToCellarButton = (Button) tableRow01.findViewById(R.id.addToCellarButton);
        clearCellarButton = (Button) tableRow01.findViewById(R.id.clearCellarButton);
        tastedCheckBox = (CheckBox) tableRow01.findViewById(R.id.tastedcheckBox);

        nextButton = (Button) tableRowLast.findViewById(R.id.nextButton);
    }

    private void initialiseUI()
    {
        clearCellarButton.setVisibility(View.GONE);

        addToCellarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAddToCellarClick();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onNextButtonClick();
            }
        });
    }

    private void onAddToCellarClick()
    {
        addToCellarTableLayout.removeAllViews();
        addToCellarClicked = true;
        addToCellarButton.setVisibility(View.GONE);
        clearCellarButton.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(activityContext);

        TableRow tableRow1 = (TableRow) inflater.inflate(R.layout.add_to_cellar_labels, null);
        TableRow tableRow2 = (TableRow) inflater.inflate(R.layout.add_to_cellar_fields, null);

        noBottles = (EditText) tableRow2.findViewById(R.id.noBottlesField);
        boxNo = (EditText) tableRow2.findViewById(R.id.boxNoField);

        addToCellarTableLayout.addView(tableRow1, new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        addToCellarTableLayout.addView(tableRow2, new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        clearCellarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClearCellarButtonClick();
            }
        });
    }

    private void onClearCellarButtonClick()
    {
        addToCellarTableLayout.removeAllViews();
        addToCellarClicked = false;
        clearCellarButton.setVisibility(View.GONE);
        addToCellarButton.setVisibility(View.VISIBLE);

        addToCellarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onAddToCellarClick();
            }
        });
    }

    private void onNextButtonClick()
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean("TASTED", tastedCheckBox.isChecked());
        bundle.putBoolean("IN_CELLAR", addToCellarClicked);

        if (addToCellarClicked && (isEmpty(noBottles) || isEmpty(boxNo)))
        {
            Toast.makeText(activityContext,
                    "Fields Missing: try again", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (addToCellarClicked)
        {
            bundle.putInt("NO_BOTTLES", Integer.parseInt(noBottles.getText().toString()));
            bundle.putInt("BOX_NO", Integer.parseInt(boxNo.getText().toString()));
        }

        activityCallback.onNextButtonClickScreen2(bundle);
    }

    private boolean isEmpty(EditText text)
    {
        String s = text.getText().toString().trim();
        return s.matches("");
    }
}
