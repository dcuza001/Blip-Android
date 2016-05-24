package com.example.horay.login;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBlipDialog extends DialogFragment {

    TextView usernameText;

    Blip blip;



    public ViewBlipDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        blip = (Blip) bundle.getSerializable("BlipObj");
        View view =  inflater.inflate(R.layout.fragment_view_blip, container, false);
        usernameText = (TextView) view.findViewById(R.id.usernameView);
        usernameText.setText(blip.owner);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
