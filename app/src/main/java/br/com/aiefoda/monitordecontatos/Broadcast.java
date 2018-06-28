package br.com.aiefoda.monitordecontatos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("script","onReceive");
        context.startService(intent);
    }
}
