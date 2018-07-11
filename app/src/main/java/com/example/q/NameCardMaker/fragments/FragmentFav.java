package com.example.q.NameCardMaker.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.NameCardMaker.FavClick;
import com.example.q.NameCardMaker.GalleryClick;
import com.example.q.NameCardMaker.MainActivity;
import com.example.q.NameCardMaker.R;
import com.example.q.NameCardMaker.models.CaptureUtil;
import com.example.q.NameCardMaker.models.ExifUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class FragmentFav extends Fragment {
    private View v;
    private View mv;
    private String link;
    private String name;
    private String mobile_num;
    private String home_num;
    private String email;

    public FragmentFav() {

    }
    public static Fragment newInstance(String link,String name,String mobile_num, String home_num, String email) {
        FragmentFav fragment = new FragmentFav();
        Bundle args = new Bundle();
        args.putString("link", link);
        args.putString("name", name);
        args.putString("mobile_num", mobile_num);
        args.putString("home_num",home_num);
        args.putString("email",email);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            link = getArguments().getString("link");
            name = getArguments().getString("name");
            mobile_num = getArguments().getString("mobile_num");
            home_num = getArguments().getString("home_num");
            email = getArguments().getString("email");
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_fav, container, false);

        TextView view_name =(TextView) v.findViewById(R.id.name);
        view_name.setText(name);

        TextView view_number =(TextView) v.findViewById(R.id.number);
        view_number.setText("Mobile: "+mobile_num);

        Bitmap bm = BitmapFactory.decodeFile(link);
        bm = ExifUtils.rotateBitmap(link,bm);

        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm,500,500);
        ImageView imageView = (ImageView) v.findViewById(R.id.picture);
        if(Build.VERSION.SDK_INT >= 21) {
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
        }
        imageView.setImageBitmap(thumbnail);
        ImageView setIcon = (ImageView) v.findViewById(R.id.icon);
        setIcon.setImageResource(R.drawable.android3);

        Button button = (Button)v.findViewById(R.id.button3);
        Button capture = (Button)v.findViewById(R.id.capture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)getActivity();
                activity.refresh();
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), FavClick.class);
                startActivity(i);
            }
        });

        return v;
    }

}
