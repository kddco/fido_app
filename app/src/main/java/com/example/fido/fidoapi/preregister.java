package com.example.fido.fidoapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class preregister {
    String challenge = "";
    String reqid = "123456B";
    String type = "http";
    String app = "example.com";
    String name = "john.doe@example.com";
    String displayName = "user1";

    String session="";
    public static void main(String[] args) throws Exception {
        //最終拿到challenge
        preregister test = new preregister();
        test.sendRequest();
        System.out.println(test.getChallenge());
        String pre_session = test.getSession();
    }
    public preregister(){

    }
    public preregister(String challenge,String reqid,String type,String app,String name,String displayName ){
        this.challenge=challenge;
        this.reqid=reqid;
        this.type=type;
        this.app=app;
        this.name=name;
        this.displayName=displayName;

    }
    String getChallenge(){
        return challenge;
    }
    String getSession() {
        return session;
    }
    public void sendRequest() throws IOException {

        // 要發送的JSON數據

        String json = "{\n" +
                "    \"rp\": {\n" +
                "        \"reqid\": \"" + reqid + "\",\n" +
                "        \"type\": \"" + type + "\",\n" +
                "        \"app\": \"" + app + "\"\n" +
                "    },\n" +
                "    \"user\": {\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"displayName\": \"" + displayName + "\"\n" +
                "    },\n" +
                "    \"pubKeyCredParams\": {\n" +
                "        \"type\": \"public-key\",\n" +
                "        \"alg\": -7\n" +
                "    },\n" +
                "    \"timeout\": 60000,\n" +
                "    \"attestation\": \"direct\",\n" +
                "    \"authenticatorSelection\": {\n" +
                "        \"authenticatorAttachment\": \"platform\",\n" +
                "        \"userVerification\": \"preferred\"\n" +
                "    }\n" +
                "}";

        // 建立URL對象
        URL url = new URL("http://127.0.0.1:6677/preregister");

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

        //設定並儲存session
        String session = conn.getHeaderField("Set-Cookie");
        this.session = session;


        // 印出challenge

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String challenge = jsonObject.get("challenge").getAsString();
//        System.out.println("challenge" + challenge);

        this.challenge=challenge;
    }
}