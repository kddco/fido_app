package com.example.fido.fidoapi;

import android.content.Context;

import com.example.fido.CRUD.connectinfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ECDSASignature_fromAPI {
    public String challenge="";
    public String hashedChallengeHex="";
    public String hashedSignedMSGHex ="";

    public String publicKeyHex="";
    public String privateKeyHex="";

    private String IP = connectinfo.ipAddress;

    public String Hash(String challenge) throws IOException {

        // 使用GSON建立JSON物件
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("challenge_text", challenge);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonObject);


        // 建立 HTTP 連線
        URL url = new URL("http://"+ IP +":6677/hash");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        // 向服務器寫入JSON數據
        conn.setDoOutput(true);
        conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        //讀取伺服器回傳的資料
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String response = br.readLine();

        // 關閉連接和讀寫器
        br.close();
        conn.disconnect();

        System.out.println(response);
        System.out.println("hashed: "+ response);
        br.close();


        hashedChallengeHex = response;


        return response;
    }
    public String getHashChallengeHex() throws IOException {
        return Hash(challenge);
    }


    public void setpublicKeyHex() throws Exception {

        this.publicKeyHex=publicKeyHex;
    }
    public String getpublicKeyHex() throws Exception {
        if (publicKeyHex == null || publicKeyHex.isEmpty()) {
            throw new Exception("Call signhashMessage first");
        }
        return publicKeyHex;
    }
    public void setChallenge(String challenge) throws Exception {
        if (challenge == null || challenge.isEmpty()) {
            throw new Exception("Challenge cannot be empty.");
        }

        this.challenge = challenge;
    }
    public void getkeypairhex() throws Exception {
        String apiUrl = "http://"+IP+":6677/getkeypairhex";
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
//        connection.setDoOutput(true);

        // 讀取 API 回傳的資料
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = br.read()) != -1) {
            sb.append((char) cp);
//            System.out.println((char)cp);
        }
        String response = sb.toString();
//        System.out.println(response);
//        System.out.println("response: "+ response);
        br.close();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        privateKeyHex = jsonObject.get("privateKeyHex").getAsString();
        publicKeyHex = jsonObject.get("publicKeyHex").getAsString();



    }
    public String signhashMessage() throws IOException {


        //create JSON ojbect
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("privateKeyHex", privateKeyHex);
        jsonObject.addProperty("hashedChallengeHex", hashedChallengeHex);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonObject);

        //create connection
        String apiUrl = "http://"+ IP +":6677/sign";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        // 設置請求頭Content-Type為application/json
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        //傳送json給伺服器
        connection.setDoOutput(true);
        connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));


        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = br.read()) != -1) {
            sb.append((char) cp);
        }
        String response = sb.toString();
        System.out.println("response: "+ response);
        br.close();

        // 關閉連接和讀寫器
        br.close();
        connection.disconnect();




        System.out.println(response);
        hashedSignedMSGHex = response;
//        hashedSignedMSGHex
        return response;
    }
}
