package com.jv.sms.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jv.sms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    public ContactsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }

}
