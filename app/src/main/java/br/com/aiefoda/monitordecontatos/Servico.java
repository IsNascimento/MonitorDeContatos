package br.com.aiefoda.monitordecontatos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Servico extends Service{

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

        public Worker(int startID){
            this.startID = startID;
        }

        public void run(){
            while( ativo && count < 1000 ){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                Log.i("script","COUNT: "+count);
            }
            stopSelf(startID);
        }
    }
}
