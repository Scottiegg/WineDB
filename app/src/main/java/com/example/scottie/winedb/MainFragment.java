package com.example.scottie.winedb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

/**
 * MainFragment
 * <p>
 * Fragment seen when app is first opened.
 *
 * @author Scott Garton
 * @version 1.0
 */
public class MainFragment extends Fragment {
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.overall_layout);
        ImageButton btn = (ImageButton) gridLayout.findViewById(R.id.imageButton);

        /**
         * replaces main fragment with search fragment when user click search
         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SearchFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.main_layout, newFragment)
                        .commit();
            }
        });

        return view;
    }
}
