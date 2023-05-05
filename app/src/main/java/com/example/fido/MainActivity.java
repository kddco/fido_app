package com.example.fido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button REGbutton;
    private Button Loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        REGbutton = findViewById(R.id.button1);
        Loginbutton = findViewById(R.id.button2);

        REGbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(REGbutton); // 執行按鈕動畫
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "LOGIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateButton(Button button) {
        AnimatorSet scaleAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.button_scale);
        scaleAnimation.setTarget(button);
        scaleAnimation.start();
    }

}
