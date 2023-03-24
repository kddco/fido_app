package com.example.fido.fidoapi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;

public class ECDSASignature_local {
    public String challenge="";
    public String hashedChallengeHex="";
    public String signatureHex="";

    public String publicKeyHex="";
    public static void main(String[] args) throws Exception {


    }


    public String gethashedChallengeHex() throws Exception {

        byte[] challengeBytes = challenge.getBytes(StandardCharsets.UTF_8);

        // 使用 Bouncy Castle 實現 SHA-256 雜湊
        SHA256Digest digest = new SHA256Digest();
        digest.update(challengeBytes, 0, challengeBytes.length);
        byte[] hashedChallengeBytes = new byte[digest.getDigestSize()];
        digest.doFinal(hashedChallengeBytes, 0);

        // 將 16 進位 byte[] 轉為字串回傳
        return Hex.toHexString(hashedChallengeBytes);
    }

    public String getsignatureHex() throws Exception {
        if (signatureHex == null || signatureHex.isEmpty()) {
            throw new Exception("call signhashMessage first");
        }
        return signatureHex;
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

    // 对指定的消息进行签名
//    public String signhashMessage() throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
//        String message=challenge;
//        // 將訊息轉換為字元陣列
//        byte[] messageBytes = message.getBytes("UTF-8");
//
//        // 使用 SHA-256 雜湊訊息
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] messageHash = md.digest(messageBytes);
//
//        // 產生 ECDSA 密鑰對
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
//        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
//        keyGen.initialize(ecSpec);
//        KeyPair keyPair = keyGen.generateKeyPair();
//
//        // 從 ECDSA 密鑰對中取出私鑰
//        PrivateKey privateKey = keyPair.getPrivate();
//        publicKeyHex = keyPair.getPublic().toString();
//        // 使用 secp256k1 橢圓曲線生成簽名物件
//        Signature ecdsa = Signature.getInstance("SHA256withECDSA", "BC");
//        ecdsa.initSign(privateKey);
//
//        // 將雜湊值放入簽名物件並簽章
//        ecdsa.update(messageHash);
//        byte[] signatureBytes = ecdsa.sign();
//
//        // 將簽名轉換為字串回傳
//        BigInteger signatureInt = new BigInteger(1, signatureBytes);
//        return signatureInt.toString(16);
//    }
}
