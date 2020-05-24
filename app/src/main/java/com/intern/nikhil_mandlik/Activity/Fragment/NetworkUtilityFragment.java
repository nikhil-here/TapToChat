package com.intern.nikhil_mandlik.Activity.Fragment;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.intern.nikhil_mandlik.Activity.Activity.MainActivity;
import com.intern.nikhil_mandlik.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkUtilityFragment extends Fragment implements View.OnClickListener {
    private Button btnRetry;

    public NetworkUtilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_network_utility, container, false);
        if (savedInstanceState == null)
        {
            initViews(view);
            initListeners();
        }
        return view;
    }

    private void initListeners() {
        btnRetry.setOnClickListener(this);
    }

    private void initViews(View view) {
        btnRetry = view.findViewById(R.id.fragment_network_util_btn_retry);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fragment_network_util_btn_retry:
                ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected)
                {
                    MainActivity.fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left).remove(MainActivity.fragmentManager.findFragmentByTag("2")).commit();
                    if(MainActivity.fragmentManager.findFragmentByTag("1") != null) {
                        //if the fragment exists, show it.
                        MainActivity.fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_left_to_right).show(MainActivity.fragmentManager.findFragmentByTag("1")).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        MainActivity.fragmentManager.beginTransaction().add(R.id.activity_main_fl_fragment_container, new ChatScreenFragment(), "1").commit();
                    }
                }
                else {
                    Toast.makeText(getContext(),"Try Again",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
