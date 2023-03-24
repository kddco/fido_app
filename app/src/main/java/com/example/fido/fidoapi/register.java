package com.example.fido.fidoapi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class register {
    String challenge = "";
    String reqid = "123456B";
    String type = "http";
    String app = "example.com";
    String name = "john.doe@example.com";
    String displayName = "user1";

    String session="";
    //
    String hashedChallengeHex="";
    String hashedSignedMSGHex ="";
    String publicKeyHex ="";
    public static void main(String[] args) throws Exception {
        register test = new register();
        test.sendRequest();
        test.setSession("1");

    }
    public register(){

    }
    public register(String challenge,String reqid,String type,String app,String name,String displayName ){
        this.challenge=challenge;
        this.reqid=reqid;
        this.type=type;
        this.app=app;
        this.name=name;
        this.displayName=displayName;

    }
    public void setSession(String session){
        this.session=session;
    }
    public void sethashedChallengeHex(String hashedChallengeHex) throws Exception {
        if (hashedChallengeHex == null || hashedChallengeHex.isEmpty()) {
            throw new Exception("hashedChallengeHex cannot be empty.");
        }
        this.hashedChallengeHex=hashedChallengeHex;
    }
    public void sethashedSignedMSGHex(String hashedSingedMSG) throws Exception {
        if (hashedSingedMSG == null || hashedSingedMSG.isEmpty()) {
            throw new Exception("hashedSingedMSG cannot be empty.");
        }
        hashedSignedMSGHex =hashedSingedMSG;
    }

    public void setpublicKeyHex(String publicKeyhex) throws Exception {
        if (publicKeyhex == null || publicKeyhex.isEmpty()) {
            throw new Exception("publicKeyhex cannot be empty.");
        }
        this.publicKeyHex =publicKeyhex;
    }
    public void setChallenge(String Challenge){
        this.challenge=challenge;
    }



    public String sendRequest() throws IOException {
        String result = "";

        // 要發送的JSON數據
        // 使用GSON建立JSON物件
        JsonObject jsonObject = new JsonObject();

        // 建立rp欄位
        JsonObject rpObject = new JsonObject();
        rpObject.addProperty("reqid", reqid);
        rpObject.addProperty("type", type);
        rpObject.addProperty("app", app);
        jsonObject.add("rp", rpObject);

        // 建立user欄位
        JsonObject userObject = new JsonObject();
        userObject.addProperty("name", name);
        userObject.addProperty("displayName", displayName);
        jsonObject.add("user", userObject);

        // 建立pubKeyCredParams欄位
        JsonObject pubKeyCredParamsObject = new JsonObject();
        pubKeyCredParamsObject.addProperty("type", "public-key");
        pubKeyCredParamsObject.addProperty("alg", -7);
        jsonObject.add("pubKeyCredParams", pubKeyCredParamsObject);

        // 填入其他欄位
        jsonObject.addProperty("timeout", 60000);
        jsonObject.addProperty("attestation", "direct");

        JsonObject authenticatorSelectionObject = new JsonObject();
        authenticatorSelectionObject.addProperty("authenticatorAttachment", "platform");
        authenticatorSelectionObject.addProperty("userVerification", "preferred");
        jsonObject.add("authenticatorSelection", authenticatorSelectionObject);

        jsonObject.addProperty("hashedChallengeHex", hashedChallengeHex);
        jsonObject.addProperty("hashedSignedMSGHex", hashedSignedMSGHex);
        jsonObject.addProperty("publicKeyHex", publicKeyHex);

        // 輸出JSON字串
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonObject);
//        System.out.println(json);

        URL url = new URL("http://127.0.0.1:6677/register");

        // 建立HttpURLConnection對象
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Cookie", session);

        // 設置請求方法為POST
        conn.setRequestMethod("POST");

        // 設置請求頭Content-Type為application/json
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        // 向服務器寫入JSON數據
        conn.setDoOutput(true);
        conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

        int statusCode = conn.getResponseCode();

        // 讀取服務器的響應



        if (statusCode != 200) {
            System.out.println("Error: " + statusCode);
        }else {

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            String response = sb.toString();
            System.out.println("response: "+ response);
            response= result;
            br.close();
        }






        // 關閉連接和讀寫器

        conn.disconnect();
        return result;

    }
}