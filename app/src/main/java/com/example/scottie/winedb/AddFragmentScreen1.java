package com.example.scottie.winedb;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * AddFragmentScreen1
 * <p>
 * Used by parent activity (AddActivity)
 * to allow user to enter in a new wine into app.
 * <p>
 * Provides interface for callback to parent activity
 *
 * @author Scott Garton
 * @version 1.0
 */
public class AddFragmentScreen1 extends Fragment {
    private Context activityContext;

    private EditText wineName;
    private EditText variety;
    private EditText vineyard;
    private EditText region;
    private EditText vintage;
    private Button btn;

    public AddFragmentScreen1() {
    }

    OnNextClickListenerScreen1 activityCallback;

    /**
     * OnNextClickListenerScreen1
     * <p>
     * Interface used to call-back to parent activity
     */
    public interface OnNextClickListenerScreen1 {
        void onNextButtonClickScreen1(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        // Attach listener
        try {
            activityCallback = (OnNextClickListenerScreen1) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement SearchClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_screen1, container, false);
        getViewsById(view);

        // callback for next button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClick();
            }
        });

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
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.overall_layout);
        wineName = (EditText) linearLayout.findViewById(R.id.wineNameField);
        variety = (EditText) linearLayout.findViewById(R.id.varietyField);
        vineyard = (EditText) linearLayout.findViewById(R.id.vineyardField);
        region = (EditText) linearLayout.findViewById(R.id.regionField);
        vintage = (EditText) linearLayout.findViewById(R.id.vintageField);
        btn = (Button) linearLayout.findViewById(R.id.nextButton);
    }

    /**
     * onNextButtonClick
     * <p>
     * method used to check required fields, then bundle data to callback to parent activity.
     */
    private void onNextButtonClick() {
        // check required fields
        if (Utils.isEmpty(wineName) || Utils.isEmpty(variety) || Utils.isEmpty(vineyard) || Utils.isEmpty(vintage)) {
            Toast.makeText(activityContext,
                    "Fields Missing: try again", Toast.LENGTH_SHORT).show();
            return;
        }

        // load bundle
        Bundle bundle = new Bundle();
        bundle.putString("WINE_NAME", wineName.getText().toString());
        bundle.putString("VARIETY", variety.getText().toString());
        bundle.putString("VINEYARD", vineyard.getText().toString());
        bundle.putString("REGION", region.getText().toString());
        bundle.putInt("VINTAGE", Integer.parseInt(vintage.getText().toString()));

        // activate callback
        activityCallback.onNextButtonClickScreen1(bundle);
    }
}
