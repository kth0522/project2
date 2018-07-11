package com.example.q.NameCardMaker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.NameCardMaker.models.ExifUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FavClick extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_fav);

        // get intent data
        SharedPreferences pref = getSharedPreferences("Variable", 0);
        String link = pref.getString("link", null);
        String name = pref.getString("name", null);
        String mobile_num = pref.getString("mobile_num", null);
        String home_num = pref.getString("home_num", null);
        String email = pref.getString("email", null);

        TextView view_name = (TextView) findViewById(R.id.name);
        view_name.setText(name);

        TextView view_number = (TextView) findViewById(R.id.number);
        view_number.setText("Mobile: "+mobile_num);

        Bitmap bm = BitmapFactory.decodeFile(link);
        bm = ExifUtils.rotateBitmap(link, bm);

        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 500, 500);
        ImageView imageView = (ImageView) findViewById(R.id.picture);
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
        }
        imageView.setImageBitmap(thumbnail);
        ImageView setIcon = (ImageView) findViewById(R.id.icon);
        setIcon.setImageResource(R.drawable.android3);

        final Button button_save = (Button) findViewById(R.id.button4);
        button_save.setOnClickListener(new View.OnClickListener() {
            int file_count = 100;
            @Override
            public void onClick(View view) {
                View rootView = getWindow().getDecorView();
                button_save.setVisibility(View.GONE);

                File screenShot = ScreenShot(rootView, file_count);
                if(screenShot!=null){
                    //갤러리에 추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                }
                button_save.setVisibility(View.VISIBLE);
                file_count++;
            }
        });


    }




    //화면 캡쳐하기
    public File ScreenShot(View view,int name){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = "screenshot"+String.valueOf(name)+".png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;
    }
}