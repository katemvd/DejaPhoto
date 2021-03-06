package com.example.katevandonge.dejaphotoproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//import static com.example.katevandonge.dejaphotoproject.R.layout.content_main;

/**
 * Activity to access the camera and take a picture within the app.
 */

public class AccessCamera extends Activity{

    static String pictureImagePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AccessCamera: ", "activity entered");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File fileDir = new File(Environment.getExternalStorageDirectory()+File.separator+".privPhotos");
        if(!fileDir.exists()){
            if(fileDir.mkdirs()) {
                Log.i("AccessCamera: ", "external didn't exist- made");
            }
            else{
                Log.i("AccessCamera: ", "external doesn't exist- NOT made");
            }
        }
        else{
            Log.i("AccessCamera: ", "external exists");
        }
        pictureImagePath = fileDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        Log.i("AccessCamera: ", "exiting with intent");
        startActivityForResult(cameraIntent, 1);
        finish();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*setContentView(content_main);
        if (requestCode == 1) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.testPhoto);
                myImage.setImageBitmap(myBitmap);

            }
        }*/
    }

}
