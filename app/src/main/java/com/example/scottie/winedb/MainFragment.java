package com.example.scottie.winedb;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class MainFragment extends Fragment
{
    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.overall_layout);
        ImageButton btn = (ImageButton) gridLayout.findViewById(R.id.imageButton);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment newFragment = new SearchFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_layout, newFragment)
                        .commit();
            }
        });

        return view;
    }
}
