package com.example.katevandonge.dejaphotoproject;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;
import static android.R.attr.button;
import static android.R.attr.content;
import static android.support.v4.content.ContextCompat.startActivity;

import android.Manifest;

import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    Gallery pList;
    WallpaperManager myWall;
    public static String WIDGET_BUTTON = "MY_PACKAGE_NAME.WIDGET_BUTTON";
    public static String WIDGET_NEXT = "NEXT_BUTTON";
    public static String WIDGET_PREV = "PREV_BUTTON";
    public static String WIDGET_RELEASE = "RELEASE_BUTTON";


    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        String but = "hello world!";
        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, but);


        Intent intentA = new Intent(WIDGET_BUTTON);
        // This button launches the app
        intentA.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        //OPEN APP BUTTON
        //Intent intent = new Intent(context, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //views.setOnClickPendingIntent(R.id.button6, pendingIntent);

        //KARMA BUTTON
        Intent intentKarma = new Intent(context, NewAppWidget.class);
        intentKarma.setAction(WIDGET_BUTTON);
        PendingIntent pendingIntentKarma = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
        views.setOnClickPendingIntent(R.id.Karma, pendingIntentKarma);

        //NEXT BUTTON
        intentKarma = new Intent(context, NewAppWidget.class);
        intentKarma.setAction(WIDGET_NEXT);
        pendingIntentKarma = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
        views.setOnClickPendingIntent(R.id.Next, pendingIntentKarma);

        //PREV BUTTON
        intentKarma = new Intent(context, NewAppWidget.class);
        intentKarma.setAction(WIDGET_PREV);
        pendingIntentKarma = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
        views.setOnClickPendingIntent(R.id.Back, pendingIntentKarma);

        //RELEASE
        intentKarma = new Intent(context, NewAppWidget.class);
        intentKarma.setAction(WIDGET_RELEASE);
        pendingIntentKarma = PendingIntent.getBroadcast(context, 0, intentKarma, 0);
        views.setOnClickPendingIntent(R.id.button6, pendingIntentKarma);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.v("for", "gfor");
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intentKarma) {
        super.onReceive(context, intentKarma);


        //KARMA BUTTON
        if (intentKarma.getAction().equals(NewAppWidget.WIDGET_BUTTON)) {
            Log.v("karmwidgetIFF", "karmawidgetIFF");
            Photo[] wallArr = Wall.photoArr;
            int counter = Wall.counter;
            Photo thisPhoto = wallArr[counter];
            thisPhoto.karma = true;
            wallArr[counter].karma = true;
        }

        //RELEASE BUTTON
        if (intentKarma.getAction().equals(NewAppWidget.WIDGET_RELEASE)) {
            Log.v("releaseIFF", "releaseIFF");
            Photo[] wallArr = Wall.photoArr;
            int counter = Wall.counter;
            Photo thisPhoto = wallArr[counter];
            if(thisPhoto!=null) {
                thisPhoto.release = true;
                wallArr[counter].release = true;
            }
            wallArr[counter] = null;
            mover(context);
       }

        //NEXT BUTTON
        if (intentKarma.getAction().equals(NewAppWidget.WIDGET_NEXT)) {
            Log.v("NEXTwidgetIFF", "NEXTwidgetIFF");
            mover(context);
        }

        //BACK BUTTON
        if (intentKarma.getAction().equals(NewAppWidget.WIDGET_PREV)) {
            Log.v("BACKwidgetIFF", "BACKwidgetIFF");
            WallpaperManager myWall = Wall.myWall;
            Photo[] wallArr = Wall.photoArr;
            int wallArrSize = wallArr.length;
            int counter = Wall.counter;
            int looper = 0;
            counter--;
            if (counter < 0) {
                counter = wallArrSize + counter;
            }
            if (wallArr[counter] == null) {
                for (looper = 0; looper <= wallArrSize; looper++) {
                    counter--;
                    if (counter < 0) {
                        counter = wallArrSize + counter;
                    }
                    if (wallArr[counter] != null) {
                        break;
                    }
                }
            }
            if (wallArr[counter] == null) {
                try {
                    myWall.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Photo picToSet = wallArr[counter];
                try {
                    Bitmap bm = picToSet.toBitmap(context.getContentResolver());
                    myWall.setBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Wall.counter = counter;
        }
    }

    public void mover(Context context){
        WallpaperManager myWall = Wall.myWall;
        Photo[] wallArr = Wall.photoArr;
        int wallArrSize = wallArr.length;
        int counter = Wall.counter;
        int looper = 0;
        counter++;

        if (counter >= wallArrSize) {
            counter = wallArrSize - counter;
            //or just make counter = 0 if this breaks!
        }
        if (wallArr[counter] == null) {
            for (looper = 0; looper <= wallArrSize; looper++) {
                counter++;
                if (counter >= wallArrSize) {
                    counter = wallArrSize - counter;
                }
                if (wallArr[counter] != null) {
                    break;
                }
            }
        }
        if (wallArr[counter] == null) {
            try {
                myWall.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Photo picToSet = wallArr[counter];
            try {
                Bitmap bm = picToSet.toBitmap(context.getContentResolver());
                myWall.setBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Wall.counter = counter;

    }

}

