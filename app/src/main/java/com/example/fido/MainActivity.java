package com.example.fido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.database.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fido.FingerPrint.BiometricHelper;
import com.example.fido.fidoapi.ConnAPI;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ImageView fingerprint_img;
    private Context context;

    private ConnAPI connAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;


        initViews();
        initData();
        setListeners();
        connAPI = new ConnAPI();

    }

    private void initViews()
    {
        fingerprint_img = (ImageView) findViewById(R.id.imageView);

    }

    private void initData()
    {
    }

    private void setListeners() {
        fingerprint_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView t_v = (ImageView)v;

                if(t_v == fingerprint_img) {
                    context = MainActivity.this;
                    BiometricHelper helper = new BiometricHelper(context);

                    Snackbar.make(t_v, "Image clicked!", Snackbar.LENGTH_SHORT).show();
                    boolean callAPI = helper.authenticate();
                    if(callAPI){
                        ConnAPI connAPI = new ConnAPI();
                        EditText editTextName = (EditText)findViewById(R.id.editTextName);
                        EditText editTextdisplayName = (EditText)findViewById(R.id.editTextdisplayName);
                        String Name = editTextName.getText().toString();
                        String displayName = editTextdisplayName.getText().toString();
                        String result = "";
                        try {
                            result = connAPI.start(Name,displayName);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

}
