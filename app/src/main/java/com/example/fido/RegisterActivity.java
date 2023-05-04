package com.example.fido;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fido.FingerPrint.BiometricHelper;
import com.example.fido.fidoapi.ConnAPI;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity implements BiometricHelper.BiometricCallback {

    private ImageView fingerprint_img;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        initViews();
        setListeners();
    }

    private void initViews() {
        fingerprint_img = findViewById(R.id.imageView);
    }

    private void setListeners() {
        fingerprint_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView t_v = (ImageView) v;
                if (t_v == fingerprint_img) {
                    BiometricHelper helper = new BiometricHelper(context);
                    helper.authenticate(RegisterActivity.this);
                }
            }
        });
    }

    @Override
    public void onBiometricAuthenticationSuccess() {
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextdisplayName = findViewById(R.id.editTextdisplayName);
        String Name = editTextName.getText().toString();
        String displayName = editTextdisplayName.getText().toString();
        Single.fromCallable(() -> {
                    ConnAPI connAPI = new ConnAPI();
                    String result = "";
                    try {
                        result = connAPI.start(Name, displayName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return result;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // do nothing
                    }

                    @Override
                    public void onSuccess(String result) {
                        TextView resultText = findViewById(R.id.resultText);
                        resultText.setText("驗證成功");
                        Snackbar.make(resultText, "PASS", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBiometricAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(RegisterActivity.this, "Error: " + errString, Toast.LENGTH_SHORT).show();
    }
}

