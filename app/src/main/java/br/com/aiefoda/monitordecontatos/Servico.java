package br.com.aiefoda.monitordecontatos;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
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
        createNotificationChannel();
        preferencias = getSharedPreferences("preferencias", 0);
        //Log.i("script","onCreate");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Nome do canal";
            String description = "Monitor de Contatos";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("canal", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void criarNotificacao() {
        Intent intent = new Intent(this, Servico.class);
        PendingIntent intentPendente = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder construtorDeNotificao =
                new NotificationCompat.Builder(this, "canal")
                        .setSmallIcon(R.drawable.ic_report_black_24dp)
                        .setContentTitle("Monitor de Contatos")
                        .setContentText("Número de contatos atingiu o limite")
                        .setContentIntent(intentPendente)
                        .addAction(R.drawable.ic_report_black_24dp, "IR", intentPendente)
                        .setAutoCancel(true);

        NotificationManager gerenciadorDeNotificacoes =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        gerenciadorDeNotificacoes.notify(1, construtorDeNotificao.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        //Log.i("script","onStartCommand");

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
                    Thread.sleep(10000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID},null, null, null);
                            Cursor cursor = cursorLoader.loadInBackground();
                            int contatos = cursor.getCount();
                            Log.i("script","NContatos: "+contatos);
                            if (contatos == preferencias.getInt("alerta", 50)){
                                criarNotificacao();
                            }
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
