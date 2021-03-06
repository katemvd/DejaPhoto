package com.example.katevandonge.dejaphotoproject;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateQueueIntentService extends IntentService {

    int rate;
    String Srate;
    int Qrate;
    Intent intent2;
    String WIDGET_NEXT;
    NewAppWidget widget;
    boolean keepRunning = true;
    UpdateQueueIntentService updateQueueIntentService = this;

    public UpdateQueueIntentService() {
        super("worker");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(UpdateQueueIntentService.this, "SERVICE STARTED", Toast.LENGTH_SHORT).show();
        keepRunning = true;
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy(){
        Toast.makeText(UpdateQueueIntentService.this, "SERVICE STOPPED", Toast.LENGTH_SHORT).show();
        keepRunning = false;
        super.onDestroy();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //onDestroy();
        if (intent != null && keepRunning) {
            Srate = (String)intent.getExtras().get("myrate");
            Qrate = (Integer.parseInt(Srate));

            intent2 = new Intent(UpdateQueueIntentService.this, NewAppWidget.class);
            WIDGET_NEXT = "NEXT_BUTTON";
            intent2.setAction(WIDGET_NEXT);
            intent2.putExtra("From", "service");
            widget = new NewAppWidget();
            widget.onReceive(getApplicationContext(), intent2);

            synchronized (this){
                try {
                    wait(Qrate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            onHandleIntent(intent);
        }
    }
}
