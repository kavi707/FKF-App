package com.fkf.commercial.templates;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fkf.commercial.R;

/**
 * Created by kavi on 5/5/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class HomeFragment extends Fragment {

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_layout, container, false);

        return rootView;
    }
}
