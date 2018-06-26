package br.com.aiefoda.monitordecontatos;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.Service;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Servico extends Service{

    private SharedPreferences preferencias;

    public List<Worker> threads = new ArrayList<Worker>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("script","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Log.i("script","onStartCommand");

        Worker w = new Worker(startID);
        w.start();
        threads.add(w);

        return(super.onStartCommand(intent, flags, startID));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        for(int i=0, tam = threads.size(); i < tam; i++){
            threads.get(i).ativo= false;
        }
    }

    class Worker extends Thread{
        public int count = 0;
        public int startID;
        public boolean ativo = true;
        private Handler handler;

        public Worker(int startID){
            this.startID = startID;
        }

        public void run(){
            handler = new Handler(Looper.getMainLooper());
            while(ativo){
                try {
                    Thread.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID},null, null, null);
                            Cursor cursor = cursorLoader.loadInBackground();
                            int contatos = cursor.getCount();
                            Log.i("script","COUNT: "+contatos);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(startID);
        }
    }
}
