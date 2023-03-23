package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button result_Button;
    private  ImageView fingerprint_img;

    private  TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        setListeners();
    }
    private void initViews()
    {
        result_Button = (Button)findViewById(R.id.button);
        fingerprint_img=(ImageView) findViewById(R.id.fingerprint_img);
    }
    private void initData()
    {
    }

    private void setListeners()
    {
        //呼叫一個新的class
        result_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DecimalFormat nf = new DecimalFormat("0.00");
                EditText fieldheight = (EditText)findViewById(R.id.hight_input);
                EditText fieldweight = (EditText)findViewById(R.id.weight_input);
                //身高
                double height = Double.parseDouble(fieldheight.getText().toString())/100;
                //體重
                double weight = Double.parseDouble(fieldweight.getText().toString());
                //計算出BMI值
                double BMI = weight / (height*height);
                System.out.println(BMI);

                //結果
                result = (TextView)findViewById(R.id.result);

                result.setText(nf.format(BMI));
                return false;
            }
        });


        fingerprint_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView t_v = (ImageView)v;
                if(t_v == fingerprint_img)
                {
//                    Toast.makeText(getApplicationContext(), "您好!Android!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MainActivity2.class);

                    Bundle bundle = new Bundle();
                    result = (TextView)findViewById(R.id.result);
                    bundle.putString("KEY_RESULT", result.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                return false;
            }
        });
    }


}