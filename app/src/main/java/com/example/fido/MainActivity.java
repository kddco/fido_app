package com.example.fido;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fido.FingerPrint.BiometricHelper;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ImageView fingerprint_img;
//    private BiometricHelper helper;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;


        initViews();
        initData();
        setListeners();
    }

    private void initViews()
    {
        fingerprint_img = (ImageView) findViewById(R.id.imageView);

    }

    private void initData()
    {
    }

    private void setListeners()
    {
        fingerprint_img.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                ImageView t_v = (ImageView)v;

                if(t_v == fingerprint_img) {
                    context = MainActivity.this;
                    BiometricHelper helper = new BiometricHelper(context);

//                    Toast.makeText(context, "Image clicked!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(t_v, "Image clicked!", Snackbar.LENGTH_SHORT).show();
                    helper.authenticate();

                }
            }
        });
    }
}
