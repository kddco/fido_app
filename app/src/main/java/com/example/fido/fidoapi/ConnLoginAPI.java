package com.example.fido.fidoapi;

import static android.content.Context.MODE_PRIVATE;
import static com.example.fido.CRUD.connectinfo.gobal_selectedDevice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.fido.CRUD.connectinfo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnLoginAPI {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private String IP = connectinfo.ipAddress;
    private static Context appContext;
    private BluetoothSocket socket;
    public static void setAppContext_ConnLoginAPi(Context context) {
        appContext = context.getApplicationContext();
    }

    public String sendRequest(String name, String displayName) throws IOException {
        // 建立 Gson 物件
        Gson gson = new Gson();
    // 建立 JsonObject
        JsonObject jsonObject = new JsonObject();
    // 加入 "displayName" 與 "name" 欄位
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("displayName", displayName);
        String json = gson.toJson(jsonObject);
        // 建立URL對象
        URL url = new URL("http://"+ IP +":6677/login");
        // 建立HttpURLConnection對象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 設置請求方法為POST
        conn.setRequestMethod("POST");

        // 設置請求頭Content-Type為application/json
        conn.setRequestProperty("Content-Type", "application/json");

        // 向服務器寫入JSON數據
        conn.setDoOutput(true);
        conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

        // 讀取服務器的響應
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String response = br.readLine();



        // 關閉連接和讀寫器
        br.close();
        conn.disconnect();

        JsonObject jsonObject2 = gson.fromJson(response, JsonObject.class);
        String result = gson.toJson(jsonObject2);
        return result;

    }
    public String tobase64(String result){
        byte[] encodedBytes = Base64.getEncoder().encode(result.getBytes(StandardCharsets.UTF_8));
        String base64_result = new String(encodedBytes, StandardCharsets.UTF_8);
        System.out.println("Encoded: " + base64_result);
        return base64_result;
    }
    public void btConnect(String base64_result ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, InterruptedException {
        BluetoothSocket socket;
        BluetoothDevice selectedDevice;

        selectedDevice = gobal_selectedDevice;
        Method m = selectedDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
        socket = (BluetoothSocket) m.invoke(selectedDevice, 1);
        socket.connect();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(base64_result.getBytes());
        outputStream.flush();
        Thread.sleep(1500);
        socket.close();
        Log.d("MyTag", "SEND base64_result TO PC");




    }

    public String start(String name, String displayname) throws IOException {
        // send to server
        String result = sendRequest(name, displayname);
        String base64_result = tobase64(result);

        // send to PC by bluetooth
        executorService.submit(new Runnable() {
            @Override
            public void run() {

//                Context context = appContext;
//                SharedPreferences sharedPreferences2 = appContext.getSharedPreferences("btServer", MODE_PRIVATE);
//
//                String serverAddress = sharedPreferences2.getString("ServerAddress", "");

                ConnLoginAPI BT = new ConnLoginAPI();
                try {
                    BT.btConnect(base64_result);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        return "1";
    }
}
