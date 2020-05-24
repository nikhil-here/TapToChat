package com.intern.nikhil_mandlik.Activity.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intern.nikhil_mandlik.Activity.Fragment.ChatScreenFragment;
import com.intern.nikhil_mandlik.Activity.Fragment.NetworkUtilityFragment;
import com.intern.nikhil_mandlik.Activity.Util.Chats;
import com.intern.nikhil_mandlik.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private View decorView;
    public static FragmentManager fragmentManager;
    private TextView tvReset;
    private ImageView ivBack;
    private LinearLayout linearLayout;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            initViews();
            initListeners();
            ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            fragmentManager = getSupportFragmentManager();
            if (isConnected) {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left).add(R.id.activity_main_fl_fragment_container, new ChatScreenFragment(), "1").commit();
            }
            else {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left).add(R.id.activity_main_fl_fragment_container, new NetworkUtilityFragment(), "2").commit();

            }
        }
    }

    private void initListeners() {
        tvReset.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }


    private void initViews()
    {
        decorView = getWindow().getDecorView();
        tvReset = findViewById(R.id.activity_main_tv_reset);
        ivBack = findViewById(R.id.activity_main_iv_back);
    }


    //removing navigation buttons (on-screen buttons)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }
    public void onSystemUiVisibilityChange(int visibility) {
        if(visibility == 0)
        {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }
    private int hideSystemBars()
    {
        int i = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        return i;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_main_tv_reset:
                ChatScreenFragment.progress = 0;
                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                fragmentManager = getSupportFragmentManager();
                if (isConnected) {
                    fragmentManager.beginTransaction().replace(R.id.activity_main_fl_fragment_container, new ChatScreenFragment(), "1").commit();
                }
                else {
                    fragmentManager.beginTransaction().replace(R.id.activity_main_fl_fragment_container, new NetworkUtilityFragment(), "2").commit();
                }
                break;

            case R.id.activity_main_iv_back:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Exit ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
        }
    }
}
