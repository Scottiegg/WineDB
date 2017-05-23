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

public class AddFragmentScreen1 extends Fragment
{
    private Context activityContext;

    private EditText wineName;
    private EditText variety;
    private EditText vineyard;
    private EditText region;
    private EditText vintage;
    private Button btn;

    public AddFragmentScreen1() {}

    OnNextClickListenerScreen1 activityCallback;

    public interface OnNextClickListenerScreen1
    {
        void onNextButtonClickScreen1(Bundle savedData);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        activityContext = activity.getBaseContext();

        try
        {
            activityCallback = (OnNextClickListenerScreen1) activity;
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
        View view = inflater.inflate(R.layout.fragment_add_screen1, container, false);
        getViewsById(view);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onNextButtonClick();
            }
        });

        return view;
    }

    private void getViewsById(View view)
    {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.overall_layout);
        wineName = (EditText) linearLayout.findViewById(R.id.wineNameField);
        variety = (EditText) linearLayout.findViewById(R.id.varietyField);
        vineyard = (EditText) linearLayout.findViewById(R.id.vineyardField);
        region = (EditText) linearLayout.findViewById(R.id.regionField);
        vintage = (EditText) linearLayout.findViewById(R.id.vintageField);
        btn = (Button) linearLayout.findViewById(R.id.nextButton);
    }

    private void onNextButtonClick()
    {
        if (isEmpty(wineName) || isEmpty(variety) || isEmpty(vineyard) || isEmpty(vintage))
        {
            Toast.makeText(activityContext,
                    "Fields Missing: try again", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("WINE_NAME", wineName.getText().toString());
        bundle.putString("VARIETY", variety.getText().toString());
        bundle.putString("VINEYARD", vineyard.getText().toString());
        bundle.putString("REGION", region.getText().toString());
        bundle.putInt("VINTAGE", Integer.parseInt(vintage.getText().toString()));

        activityCallback.onNextButtonClickScreen1(bundle);
    }

    private boolean isEmpty(EditText text)
    {
        String s = text.getText().toString().trim();
        return s.matches("");
    }
}
