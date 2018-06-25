package br.com.aiefoda.monitordecontatos;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback{

    private SharedPreferences preferencias;
    private int contatosParaGerarAlerta = 0;
    private TextView noAlerta;
    private Switch monitoramenswitchtoOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noAlerta = findViewById(R.id.noAlerta);
        monitoramenswitchtoOnOff = findViewById(R.id.monitoramentoOnOff);

        preferencias = getSharedPreferences("preferencias", 0);
        monitoramenswitchtoOnOff.setChecked(preferencias.getBoolean("monitoramento", false));
        contatosParaGerarAlerta = preferencias.getInt("alerta", 50);
        noAlerta.setText(String.valueOf(contatosParaGerarAlerta));

        Intent it = new Intent( this, Servico.class);
        if(monitoramenswitchtoOnOff.isChecked()) {
            startService(it);
        } else {
            stopService(it);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_CONTACTS}, 0);
        } else {
            this.setTotalDeContatos();
        }

        findViewById(R.id.btnMenos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contatosParaGerarAlerta > 0) {
                    contatosParaGerarAlerta --;
                    noAlerta.setText(String.valueOf(contatosParaGerarAlerta));
                }
            }
        });

        findViewById(R.id.btnMais).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contatosParaGerarAlerta ++;
                noAlerta.setText(String.valueOf(contatosParaGerarAlerta));
            }
        });

        findViewById(R.id.salvarConfiguracoes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferencias.edit().putInt("alerta", contatosParaGerarAlerta).apply();
                preferencias.edit().putBoolean("monitoramento", monitoramenswitchtoOnOff.isChecked()).apply();
                Intent it = new Intent(MainActivity.this, Servico.class);
                if(monitoramenswitchtoOnOff.isChecked()) {
                    startService(it);
                } else {
                    stopService(it);
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.setTotalDeContatos();
            }
        }
    }

    private void setTotalDeContatos() {
        TextView totalDeContatos = findViewById(R.id.noDeContatos);
        LeitorDeContatos leitorDeContatos = new LeitorDeContatos(this, getLoaderManager());
        totalDeContatos.setText(String.valueOf(leitorDeContatos.contaContatos()));
    }

}
