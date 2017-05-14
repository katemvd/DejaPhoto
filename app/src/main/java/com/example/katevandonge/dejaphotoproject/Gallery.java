package com.example.katevandonge.dejaphotoproject;

/**
 * Created by luujfer on 5/8/17.
 */

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Contains a uriList and PriorityQueue which holds photos.
 * Created by luujfer on 5/8/17.
 */

public class Gallery {

    Context con;
    int size;
    ArrayList<Uri> uriList;
    ArrayList<Long> dateList;
    ArrayList<Double> latList;
    ArrayList<Double> longList;
    PriorityQueue<Photo> photoQueue;
    static PriorityQueue<Photo> queueCopy;
    Comparator<Photo> photoComparator;


    /*
    * Constructor for Gallery class.
    * */
    @TargetApi(24)
    public Gallery(Context context){
        size = 0;
        con= context;
        uriList = new ArrayList<Uri>(size);
        dateList = new ArrayList<Long>(size);
        latList = new ArrayList<Double>(size);
        longList = new ArrayList<Double>(size);
        photoComparator= new PhotoComparator();
        photoQueue = new PriorityQueue<Photo>(photoComparator); //this isn't an error
        queueCopy = new PriorityQueue<Photo>(photoComparator);
    }

    public int getSize(){
        return size;
    }


    /*
    * Queries photos from Camera roll on Android, fills uriList with Uris of our photos.
    * */
    public void queryGallery(ContentResolver cr){
        Uri imagesURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] proj = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};
        Cursor cursor;
        cursor = cr.query(imagesURI,proj, null, null, MediaStore.Images.Media._ID);
        size = cursor.getCount();
        Log.v("cursor size", Integer.toString(size));

        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            final int longColumn= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE);
            final int latColumn= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE);
            //Log.v("latColumn", Integer.toString(latColumn));
            final int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
            do {
                final String data = cursor.getString(dataColumn);
                final Double lon= cursor.getDouble(longColumn);
                final Double lat= cursor.getDouble(latColumn);
                final long date= cursor.getLong(dateColumn);
                Uri uri = Uri.withAppendedPath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, data);
                dateList.add(date);
                latList.add(lat);
                longList.add(lon);
                uriList.add(uri);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //Log.v("dateList size", Integer.toString(dateList.size()));
        //Log.v("longList size", Integer.toString(longList.size()));
        //Log.v("latList size", Integer.toString(latList.size()));
        //Log.v("uriList size", Integer.toString(uriList.size()));
    }


    /*
    * Makes photo objects and fills with corresponding uris and fills photoQueue with photo objects.
    * */
    public void fillQueue (){
        for(int i = 0; i<size ; i++){
            Photo photo = new Photo(con);
            photo.setUri(uriList.get(i));
            photo.setTimeTotal(dateList.get(i));
            photo.setDate(dateList.get(i));
            photo.setLatitude(latList.get(i));
            photo.setLongitude(longList.get(i));
            photo.locScreenHelper();
            photo.setWeight();
            if(photo.release==true){}
            else {
                photoQueue.add(photo);
                queueCopy.add(photo);
            }
        }
        //Log.v("photo 1 weight", Integer.toString(photoQueue.peek().getWeight()));
        //Log.v("photo 1 info", ""+ uriList.get(1)+ "  "+ dateList.get(1)+" " +latList.get(1)+ " "+ longList.get(1));
        //Log.v("size of photo queue", Integer.toString(photoQueue.size()));
    }


    /*
    * To be called when we need to update the queue with a service.
    * */
    @TargetApi(24)
    public void updateQueue(){
        PriorityQueue<Photo> newQueue= new PriorityQueue<Photo>(photoComparator);
        PriorityQueue<Photo> newQcopy = new PriorityQueue<Photo>(photoComparator);
        Photo polled = photoQueue.poll();
        while(polled != null){

            polled.setWeight();
            newQueue.add(polled);
            newQcopy.add(polled);
            polled= photoQueue.poll();
        }
        photoQueue=newQueue;
        queueCopy = newQcopy;

    }

    /**
     * Reset recently shown every 24 hours
     */
    public void resetShown (){

    }



}
