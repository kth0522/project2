package com.example.q.NameCardMaker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.q.NameCardMaker.models.ExifUtils;


public class GalleryClick extends Activity {
    private Bitmap imgRotate(Bitmap bm){
        int width = bm.getWidth();
        int height = bm.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        bm.recycle();

        return resizedBitmap;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_image_select);

        // get intent data
        final Intent i = getIntent();

        // Selected image id
        final String link = i.getExtras().getString("id");

        Log.d("position is here", link);
        Bitmap bm = BitmapFactory.decodeFile(link);
        bm = ExifUtils.rotateBitmap(link,bm);

        ImageView imageView = (ImageView) findViewById(R.id.picture);
        imageView.setImageBitmap(bm);
        Button button1 = (Button) findViewById(R.id.selectButton);
        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences pref = getSharedPreferences("Variable", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("link", link);
                editor.commit();
                Toast toast = Toast.makeText(GalleryClick.this, "선택되었습니다",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
