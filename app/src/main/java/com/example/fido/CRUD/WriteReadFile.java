package com.example.fido.CRUD;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class WriteReadFile {
    private static Context appContext;


    public static void setAppContext(Context context) {
        appContext = context.getApplicationContext();
    }
    public static void storePrivateKey(String privateKeyHex) {
        if (appContext == null) {
            throw new IllegalStateException("AppContext has not been set. Call setAppContext() first.");
        }
        File file = new File(appContext.getExternalFilesDir(null), "private_key.txt");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(privateKeyHex.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readPrivateKey() {
        if (appContext == null) {
            throw new IllegalStateException("AppContext has not been set. Call setAppContext() first.");
        }
        File file = new File(appContext.getExternalFilesDir(null), "private_key.txt");
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream inputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
