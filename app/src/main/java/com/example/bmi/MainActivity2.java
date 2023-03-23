package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private TextView show_result;
    private Button button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initViews();
        showResults();
        setListensers();
    }
    private void initViews()
    {

        show_result = (TextView)findViewById(R.id.report_result);
        button_back=(Button) findViewById(R.id.button2);

    }
    private void showResults()
    {
        DecimalFormat nf = new DecimalFormat("0.00");
        Bundle bundle = this.getIntent().getExtras();
        show_result.setText(bundle.getString("KEY_RESULT"));
    }

    private void setListensers()
    {
        button_back.setOnClickListener(backtoMain);
    }

    private Button.OnClickListener backtoMain = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            System.out.println("MainActivity2 finish");
            MainActivity2.this.finish();
        }
    };
}