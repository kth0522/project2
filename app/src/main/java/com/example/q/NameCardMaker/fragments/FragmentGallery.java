package com.example.q.NameCardMaker.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.q.NameCardMaker.GalleryClick;
import com.example.q.NameCardMaker.MainActivity;
import com.example.q.NameCardMaker.R;
import com.example.q.NameCardMaker.adapters.GalleryAdapter;
import com.example.q.NameCardMaker.models.ExifUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentGallery extends Fragment {

    static final int PICK_IMAGE = 1;
    public GridView gv;
    public static ArrayList<String> link = GalleryAdapter.getLinks();

    public FragmentGallery() {}

    public static Fragment newInstance() {
        FragmentFav fragment = new FragmentFav();
        return fragment;
    }
    public List<String> getCameraImages() {
        String[] projection = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Uri imageURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = getContext().getContentResolver().query(imageURI, projection, null, null, null);
        Log.i("ListingImages", " query count=" + cur.getCount());
        ArrayList<String> imagePaths = new ArrayList<>(cur.getCount());
        int rawCol = cur.getColumnIndex(MediaStore.Images.Media.DATA);
        if (cur.moveToFirst()) {
            do {
                imagePaths.add(cur.getString(rawCol));
            } while (cur.moveToNext());
        }
        return imagePaths;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_gallery, container, false);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }

        gv = view.findViewById(R.id.listview_gallery);

        dataSetting();

        //gridview item을 image와 link 보유.

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("img",link.get(position));
                Intent i = new Intent(getActivity().getApplicationContext(), GalleryClick.class);
                i.putExtra("id", link.get(position));
                startActivity(i);
            }
        });
        Button button = (Button)view.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSetting();
            }
        });

        return view;
    }

    public void dataSetting(){

        List<String> allimage = getCameraImages();
        int len = allimage.size();
        int index = 0;

        GalleryAdapter mAdapter = new GalleryAdapter();

        while(index < len){
            //ImageView iv = new ImageView(getActivity().getApplicationContext());
            String imgpath = allimage.get(index++);
            Log.d("img path is ", imgpath);
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            bm = ExifUtils.rotateBitmap(imgpath,bm);


            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm,300,300);
            mAdapter.addItem(thumbnail,imgpath);
            link.add(imgpath);


        }

        gv.setAdapter(mAdapter);
    }
}
