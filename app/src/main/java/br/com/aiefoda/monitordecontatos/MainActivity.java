package br.com.aiefoda.monitordecontatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view){
        Intent it = new Intent( this, Servico.class);
        startService(it);
    }
    public void stopService(View view){
        Intent it = new Intent(this, Servico.class);
        stopService(it);
    }

}
