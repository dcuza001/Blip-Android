package com.Blipster.horay.Blip;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Blipster.horay.Blip.R;

/**
 * Created by Daniel on 5/3/2016.
 */
public class GroupsFragment extends Fragment{
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.groups_layout, container, false);
        return myView;
    }
}
