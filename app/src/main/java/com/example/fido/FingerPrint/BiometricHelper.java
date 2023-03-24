package com.example.fido.FingerPrint;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;

public class BiometricHelper extends FragmentActivity{
    private Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;

    public BiometricHelper(Context context) {
        this.context = context;
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void authenticate() {
        BiometricManager biometricManager = BiometricManager.from(context);
        int result = biometricManager.canAuthenticate();
        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Title")
                    .setSubtitle("Subtitle")
                    .setDescription("Description")
                    .setNegativeButtonText("Cancel")
                    .build();
            biometricPrompt.authenticate(promptInfo);
        } else if (result == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            Snackbar.make(findViewById(android.R.id.content), "硬體不支援", Snackbar.LENGTH_LONG).show();
        } else if (result == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            Snackbar.make(findViewById(android.R.id.content), "硬體不可用", Snackbar.LENGTH_LONG).show();
        } else if (result == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            Snackbar.make(findViewById(android.R.id.content), "沒有任何註冊指紋", Snackbar.LENGTH_LONG).show();
        }
    }
}
