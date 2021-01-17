package com.example.latihan12;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class form_barang extends AppCompatActivity {
    EditText teksKode, teksNama, hrgBrg;

    public static TextView tekcoba;
    Button tbSave, tblView, balik;
    //private String URL_ADD = "http://192.168.1.4/simpan_data.php";
    private String KEY_BRG_ID = "kode";
    private String KEY_BRG_NAMA = "nama";
    private String KEY_BRG_HRG = "harga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_barang);

        teksKode = (EditText)findViewById(R.id.xkode);
        teksNama = (EditText)findViewById(R.id.xnama);
        hrgBrg = (EditText)findViewById(R.id.xharga);
        tbSave = (Button)findViewById(R.id.tblsave);
        balik = (Button)findViewById(R.id.tblbalik);

        balik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(form_barang.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanData();
                Intent intent = new Intent(form_barang.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void simpanData() {
        final String kode = teksKode.getText().toString();
        final String nama = teksNama.getText().toString();
        final String harga = hrgBrg.getText().toString();

        class SimpanData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(KEY_BRG_ID, kode);
                params.put(KEY_BRG_NAMA, nama);
                params.put(KEY_BRG_HRG, harga);

                RequestHandler rh = new RequestHandler();
                String hs = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return hs;
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
            }
        }

        SimpanData sd = new SimpanData();
        sd.execute();

    }
}