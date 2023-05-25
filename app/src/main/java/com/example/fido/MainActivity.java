package com.example.fido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.widget.ImageButton;

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
                animateButton(REGbutton);
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(Loginbutton);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ImageButton imageButton5 = findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButton(imageButton5);
                openRfcommClientActivity();
            }
        });
    }

    private void animateButton(View view) {
        AnimatorSet scaleAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.button_scale);
        scaleAnimation.setTarget(view);
        scaleAnimation.start();
    }

    private void openRfcommClientActivity() {
        Intent intent = new Intent(MainActivity.this, RfcommClientActivity.class);
        startActivity(intent);
    }
}
