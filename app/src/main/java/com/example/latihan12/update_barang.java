package com.example.latihan12;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.mbms.DownloadProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

public class update_barang extends AppCompatActivity implements View.OnClickListener {

    private TextView editTextKode;
    private EditText editTextNamaBarang;
    private EditText editTextHarga;

    private Button buttonUpdate;
    private Button buttonDelete;
    private Button buttonCancel;

    private String Skode;
    private String Snama_barang;
    private String Sharga;

    private String PRODUCT_KODE="kode";
    private String PRODUCT_NAMA_BARANG="nama_barang";
    private String PRODUCT_HARGA="harga";

    //private TextView tkode, tnama, tharga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_barang);

        Intent intent = getIntent();

        Skode = intent.getStringExtra(PRODUCT_KODE);
        Snama_barang = intent.getStringExtra(PRODUCT_NAMA_BARANG);
        Sharga = intent.getStringExtra(PRODUCT_HARGA);

        editTextKode = (TextView) findViewById(R.id.xkodeEdit);
        editTextNamaBarang = (EditText)findViewById(R.id.xnamaEdit);
        editTextHarga = (EditText)findViewById(R.id.xhargaEdit);

        buttonUpdate = (Button)findViewById(R.id.tblupdate);
        buttonDelete = (Button)findViewById(R.id.tbldelete);
        buttonCancel = (Button)findViewById(R.id.tblback);

//        tkode = (TextView)findViewById(R.id.tampilKode);
//        tnama = (TextView)findViewById(R.id.tampilNama);
//        tharga = (TextView)findViewById(R.id.tampilHarga);
//
//        tkode.setText(Skode);
//        tnama.setText(Snama_barang);
//        tharga.setText(Sharga);

        editTextKode.setText(Skode);
        editTextNamaBarang.setText(Snama_barang, TextView.BufferType.EDITABLE);
        editTextHarga.setText(Sharga, TextView.BufferType.EDITABLE);

        getProduct();

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }


    private void getProduct() {
        class GetProduct extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_barang.this,"Fetching Data...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                // RETURN FIELD YANG DICARI BERDASARKAN ID TERPILIH DALAM BENTUK JSON
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_PRODUCT,Skode);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProduct(s);
            }
        }
        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void showProduct(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            //JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = jsonObject.getJSONObject(konfigurasi.TAG_JSON_ARRAY);
            String kode = c.getString(PRODUCT_KODE);
            String nama_barang = c.getString(PRODUCT_NAMA_BARANG);
            String harga = c.getString(PRODUCT_HARGA);

            editTextKode.setText(kode);
            editTextNamaBarang.setText(nama_barang);
            editTextHarga.setText(harga);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void updateProduct(){
        final String kode = editTextKode.getText().toString();
        final String nama_barang = editTextNamaBarang.getText().toString();
        final String harga = editTextHarga.getText().toString();

        class UpdateProduct extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(update_barang.this,"Updating Data...","Wait...",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_PRODUCT_KODE, kode);
                params.put(konfigurasi.KEY_PRODUCT_NAMA_BARANG, nama_barang);
                params.put(konfigurasi.KEY_PRODUCT_HARGA, harga);

                RequestHandler rh = new RequestHandler();
                String hs = rh.sendPostRequest(konfigurasi.URL_UPDATE_PRODUCT, params);
                return hs;
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(update_barang.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        UpdateProduct up = new UpdateProduct();
        up.execute();
        startActivity(new Intent(update_barang.this, MainActivity.class));
        finish();
    }

    private void deleteProduct(){
        class DeleteProduct extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(update_barang.this,"Delete Data", "Please Wait...", false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_PRODUCT, Skode);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(update_barang.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        DeleteProduct dp = new DeleteProduct();
        dp.execute();
    }

    private void confirmDeleteProduct(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteProduct();
                        startActivity(new Intent(update_barang.this, MainActivity.class));
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateProduct();
        }

        if(v == buttonDelete){
            confirmDeleteProduct();
        }
        if (v == buttonCancel) {
            startActivity(new Intent(update_barang.this, MainActivity.class));
            finish();
        }
    }
}