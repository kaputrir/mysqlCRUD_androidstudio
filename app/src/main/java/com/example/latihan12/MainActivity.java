package com.example.latihan12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button tblAdd;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listdata);
        tblAdd = (Button)findViewById(R.id.btnAdd);
        //tampilkanBarang("http://192.168.1.4/servicedata.php");

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(MainActivity.this, update_barang.class);
                HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
                String kode = map.get(konfigurasi.TAG_KODE).toString();
                String nama_barang = map.get(konfigurasi.TAG_NAMA_BARANG).toString();
                String harga = map.get(konfigurasi.TAG_HARGA).toString();

                intent.putExtra(konfigurasi.TAG_KODE, kode);
                intent.putExtra(konfigurasi.TAG_NAMA_BARANG, nama_barang);
                intent.putExtra(konfigurasi.TAG_HARGA, harga);
                startActivity(intent);
            }
        });

        getJSON();

        tblAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, form_barang.class);
                startActivity(intent);
            }
        });

    }

    private void showProduct(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>>list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String kode = jo.getString(konfigurasi.TAG_KODE);
                String nama_barang = jo.getString(konfigurasi.TAG_NAMA_BARANG);
                String harga = jo.getString(konfigurasi.TAG_HARGA);

                HashMap<String, String> stokbarang = new HashMap<>();
                stokbarang.put(konfigurasi.TAG_KODE, kode);
                stokbarang.put(konfigurasi.TAG_NAMA_BARANG, nama_barang);
                stokbarang.put(konfigurasi.TAG_HARGA, harga);
                list.add(stokbarang);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_KODE, konfigurasi.TAG_NAMA_BARANG, konfigurasi.TAG_HARGA},
                new int[]{R.id.kode, R.id.nama_barang, R.id.harga});
        listView.setAdapter(adapter);

    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Retrieving Data", "Please Wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showProduct();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//        Intent intent = new Intent(this, update_barang.class);
//        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
//        String kode = map.get(konfigurasi.TAG_KODE).toString();
//        String nama_barang = map.get(konfigurasi.TAG_NAMA_BARANG).toString();
//        String harga = map.get(konfigurasi.TAG_HARGA).toString();
//
//        intent.putExtra(konfigurasi.TAG_KODE, kode);
//        intent.putExtra(konfigurasi.TAG_NAMA_BARANG, nama_barang);
//        intent.putExtra(konfigurasi.TAG_HARGA, harga);
//        startActivity(intent);
//    }
}
