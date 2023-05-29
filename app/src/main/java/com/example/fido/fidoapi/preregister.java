package com.example.fido.fidoapi;
import static com.example.fido.CRUD.WriteReadFile.setAppContext;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.example.fido.CRUD.connectinfo;
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

    private String IP = connectinfo.ipAddress;

    public static void main(String[] args) throws Exception {
        //最終拿到challenge
//        preregister test = new preregister();
//        test.sendRequest();
//        System.out.println(test.getChallenge());
//        String pre_session = test.getSession();
    }
    public preregister(String name,String displayName){
        this.name=name;
        this.displayName=displayName;

    }
    public preregister(String reqid,String type,String app,String name,String displayName ){

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

        JsonObject jsonObject = new JsonObject();

// 构建 rp 对象
        JsonObject rpObject = new JsonObject();
        rpObject.addProperty("reqid", reqid);
        rpObject.addProperty("type", type);
        rpObject.addProperty("app", app);
        jsonObject.add("rp", rpObject);

// 构建 user 对象
        JsonObject userObject = new JsonObject();
        userObject.addProperty("name", name);
        userObject.addProperty("displayName", displayName);
        jsonObject.add("user", userObject);

// 构建 pubKeyCredParams 对象
        JsonObject pubKeyCredParamsObject = new JsonObject();
        pubKeyCredParamsObject.addProperty("type", "public-key");
        pubKeyCredParamsObject.addProperty("alg", -7);
        jsonObject.add("pubKeyCredParams", pubKeyCredParamsObject);

// 填充其他属性
        jsonObject.addProperty("timeout", 60000);
        jsonObject.addProperty("attestation", "direct");

// 构建 authenticatorSelection 对象
        JsonObject authenticatorSelectionObject = new JsonObject();
        authenticatorSelectionObject.addProperty("authenticatorAttachment", "platform");
        authenticatorSelectionObject.addProperty("userVerification", "preferred");
        jsonObject.add("authenticatorSelection", authenticatorSelectionObject);

// 输出 JSON 字符串
        String json = new Gson().toJson(jsonObject);
        // 建立URL對象

        URL url = new URL("http://"+ IP +":6677/preregister");

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
        JsonObject jsonObject2 = gson.fromJson(response, JsonObject.class);
        String challenge = jsonObject2.get("challenge").getAsString();
//        System.out.println("challenge" + challenge);

        this.challenge=challenge;
    }
}