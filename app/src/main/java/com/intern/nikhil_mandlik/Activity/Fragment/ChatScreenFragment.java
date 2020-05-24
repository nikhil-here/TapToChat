package com.intern.nikhil_mandlik.Activity.Fragment;

import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.icu.text.UnicodeSetSpanner;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.nikhil_mandlik.Activity.Activity.MainActivity;
import com.intern.nikhil_mandlik.Activity.Util.Chats;
import com.intern.nikhil_mandlik.R;

import static com.intern.nikhil_mandlik.R.drawable.receiver_imageview_bg;
import static com.intern.nikhil_mandlik.R.drawable.sender_imageview_bg;

public class ChatScreenFragment extends Fragment implements View.OnClickListener {
    private LinearLayout linearLayout;
    private Handler handler;
    private TextView tvTapToStart;
    private ScrollView scrollView;
    public static int progress = 0;
    public static Chats[] chatArray;
    public ChatScreenFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_screen, container, false);
        if (savedInstanceState == null) {
            initViews(view);
            initChat();
            initHandler();
            initListeners();
        }
        return view;
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_chat_screen_ll:
                scrollView.post(new Runnable() { public void run() { scrollView.fullScroll(View.FOCUS_DOWN); } });
                tvTapToStart.setVisibility(View.GONE);
                ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (!isConnected)
                {
                    MainActivity.fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_left_to_right).hide(MainActivity.fragmentManager.findFragmentByTag("1")).commit();
                    MainActivity.fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_right_to_left).add(R.id.activity_main_fl_fragment_container,new NetworkUtilityFragment(),"2").commit();
                    break;
                }
                if(progress >= chatArray.length)
                {
                    Toast.makeText(getContext(),"Conversation Finish",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (chatArray[progress].getSender())
                {
                    if (chatArray[progress].getImage())
                    {
                        addSenderImageView();
                    }
                    else {
                        addSenderTextView();
                    }
                    scrollView.post(new Runnable() { public void run() { scrollView.fullScroll(View.FOCUS_DOWN); } });
                    progress++;
                    break;
                }
                else
                {
                    if (chatArray[progress].getImage()) {
                        addReceiverImageView();
                    }
                    else {
                        addReceiverTextView();
                    }
                    scrollView.post(new Runnable() { public void run() { scrollView.fullScroll(View.FOCUS_DOWN); } });
                    progress++;
                    break;
                }
        }
    }

    private void addReceiverImageView() {
        String name = chatArray[progress].getMessage();
        final int drawableResourceId = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams Imageparams = new LinearLayout.LayoutParams(500, 500);
                Imageparams.gravity = Gravity.LEFT;
                Imageparams.setMargins(12,12,0,0);
                imageView.setLayoutParams(Imageparams);
                imageView.setImageResource(drawableResourceId);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(10,10,10,10);
                imageView.setBackgroundResource(receiver_imageview_bg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.addView(imageView);
                    }
                });
            }
        }).start();
        scrollView.post(new Runnable() { public void run() { scrollView.fullScroll(View.FOCUS_DOWN); } });
    }
    private void addSenderImageView() {
        String name = chatArray[progress].getMessage();
        final int drawableResourceId = getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams Imageparams = new LinearLayout.LayoutParams(500, 500);
                Imageparams.gravity = Gravity.RIGHT;
                Imageparams.setMargins(0,12,12,0);
                imageView.setLayoutParams(Imageparams);
                imageView.setImageResource(drawableResourceId);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(10,10,10,10);
                imageView.setBackgroundResource(sender_imageview_bg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.addView(imageView);
                    }
                });
            }
        }).start();
        scrollView.post(new Runnable() { public void run() { scrollView.fullScroll(View.FOCUS_DOWN); } });
    }
    private void addSenderTextView() {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,12,12,0);
        params.gravity = Gravity.RIGHT;
        textView.setTextSize(20);
        textView.setLayoutParams(params);
        textView.setPadding(12,12,12,12);
        textView.setText(chatArray[progress].getMessage());
        textView.setBackgroundResource(R.drawable.sender_textview_bg);
        linearLayout.addView(textView);
    }
    private void addReceiverTextView() {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(12,12,0,0);
        params.gravity = Gravity.LEFT;
        textView.setTextSize(20);
        textView.setLayoutParams(params);
        textView.setPadding(12,12,12,12);
        textView.setText(chatArray[progress].getMessage());
        textView.setBackgroundResource(R.drawable.receiver_textview_bg);
        linearLayout.addView(textView);
    }
    private void initViews(View view) {
        linearLayout = view.findViewById(R.id.fragment_chat_screen_ll);
        tvTapToStart = view.findViewById(R.id.fragment_chat_screen_tv_tapToStart);
        scrollView = view.findViewById(R.id.fragment_chat_screen_sv);
        scrollView.getMeasuredHeight();
    }
    private void initListeners() {
        linearLayout.setOnClickListener(this);
    }
    private void initChat() {
        //initializing Chat
        chatArray = new Chats[31];
        chatArray[0] = new Chats(false,false,"Hi kalya");
        chatArray[1] = new Chats(true, false, "Hi");
        chatArray[2] = new Chats(false,false,"you want to play some Friday\nthe 13th on the xbox");
        chatArray[3] = new Chats(false,true,"xbox");
        chatArray[4] = new Chats(true, false, "yea");
        chatArray[5] = new Chats(false,false,"Ok my game is on");
        chatArray[6] = new Chats(true, false, "Me to");
        chatArray[7] = new Chats(false, false, "ill invite you to my game");
        chatArray[8] = new Chats(true, false, "Hold up i'll be right back");
        chatArray[9] = new Chats(true, true, "run");
        chatArray[10] = new Chats(false, false, "ok");
        chatArray[11] = new Chats(true, false, "I'm back");
        chatArray[12] = new Chats(false, false, "okay that was fast");
        chatArray[13] = new Chats(true, false, "I Know");
        chatArray[14] = new Chats(false, false, "Yes I shot jason");
        chatArray[15] = new Chats(false, false, "wait I hear knocking at my door");
        chatArray[16] = new Chats(true, false, "I'll wait you can go check");
        chatArray[17] = new Chats(false, false, "I Checked someone with a mask \nis at my door");
        chatArray[18] = new Chats(true, false, "can it be jason");
        chatArray[19] = new Chats(false, false, "Wait");
        chatArray[20] = new Chats(false, false, "HE BROKE MY DOOR DOWN");
        chatArray[21] = new Chats(true, false, "CALL THE POLICE");
        chatArray[22] = new Chats(false, false, "i'm hiding under my bed in my room");
        chatArray[23] = new Chats(true, false, "Okay let me go tell my mom\nand dad");
        chatArray[24] = new Chats(false, false, "i see his feet");
        chatArray[25] = new Chats(true, false, "my mom and dad\nare calling police");
        chatArray[26] = new Chats(false, false, "NO GOD HE SEE dsffsdasd");
        chatArray[27] = new Chats(false, false, "jwawsagsad");
        chatArray[28] = new Chats(true, false, "NOO STAY WITH ME");
        chatArray[29] = new Chats(false, false, "You are next");
        chatArray[30] = new Chats(true, false, "Who are u????");
    }
    private void initHandler() {
        handler = new Handler(getActivity().getMainLooper());
    }

}
