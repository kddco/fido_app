package com.example.fido.FingerPrint;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.fido.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;

public class BiometricHelper {
    private Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;

    public interface BiometricCallback {
        void onBiometricAuthenticationSuccess();
        void onBiometricAuthenticationError(int errorCode, CharSequence errString);
    }

    public BiometricHelper(Context context) {
        this.context = context;
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                BiometricCallback callback = (BiometricCallback) context;
                callback.onBiometricAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                BiometricCallback callback = (BiometricCallback) context;
                callback.onBiometricAuthenticationError(errorCode, errString);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean authenticate(BiometricCallback callback){
        BiometricManager biometricManager = BiometricManager.from(context);
        int result = biometricManager.canAuthenticate();
        View view = ((Activity) context).findViewById(R.id.imageView);


        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Title")
                    .setSubtitle("Subtitle")
                    .setDescription("Description")
                    .setNegativeButtonText("Cancel")
                    .build();
            biometricPrompt.authenticate(promptInfo);
            Snackbar.make(view, "PASS", Snackbar.LENGTH_LONG).show();
            return true;

        } else if (result == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            Snackbar.make(view, "硬體不支援", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (result == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            Snackbar.make(view, "硬體不可用", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (result == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            Snackbar.make(view, "沒有任何註冊指紋", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
