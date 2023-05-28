package com.example.fido.fidoapi;
import static com.example.fido.CRUD.WriteReadFile.*;
import com.example.fido.CRUD.WriteReadFile.*;


public class ConnRegisterAPI {
    public static void main(String input_name, String input_displayName) throws Exception {
        //初始化變數
        String name = input_name;
        String displayName = input_displayName;
        String preReg_challenge = "";
        String pre_session = "NULL pre_session";
        String hashedChallengeHex = "";
        String hashedSignedMSGHex = "";
        String publicKeyHex = "";
        String privateKeyHex = "";

        //前註冊 最後拿到challenge,session
        preregister preRegistrationManager = new preregister(name, displayName);
        preRegistrationManager.sendRequest();
        preReg_challenge = preRegistrationManager.getChallenge();
        pre_session = preRegistrationManager.getSession();


        //生成hashed,hashedsigned,pubkey
        String challenge = "";
        ECDSASignature_fromAPI ecdsa = new ECDSASignature_fromAPI();
        ecdsa.setChallenge(preReg_challenge);
        ecdsa.getkeypairhex();
        privateKeyHex = ecdsa.privateKeyHex;
        storePrivateKey(privateKeyHex);
        readPrivateKey();




        publicKeyHex = ecdsa.publicKeyHex;
        hashedChallengeHex = ecdsa.getHashChallengeHex();
        hashedSignedMSGHex = ecdsa.signhashMessage();


        //註冊 設定session,hashedChallengeHex,hashedSignedMSGHex,pubkey,，傳送hashed,hashedsigned,pubkey
        register RegistrationManager = new register(name, displayName);
        System.out.println("pre_session: " + pre_session);
        RegistrationManager.setSession(pre_session);
        RegistrationManager.sethashedChallengeHex(hashedChallengeHex);
        RegistrationManager.sethashedSignedMSGHex(hashedSignedMSGHex);
        RegistrationManager.setpublicKeyHex(publicKeyHex);

        String hashedChallengeHex1 = RegistrationManager.hashedChallengeHex;
        System.out.println("publicKeyHex: " + publicKeyHex);


        RegistrationManager.sendRequest();


    }



    public String start(String input_name, String input_displayName) throws Exception {
        // 初始化变量
        String name = input_name;
        String displayName = input_displayName;
        String preReg_challenge = "";
        String pre_session = "NULL pre_session";
        String hashedChallengeHex = "";
        String hashedSignedMSGHex = "";
        String publicKeyHex = "";
        String privateKeyHex = "";

        // 前注册 最后拿到 challenge,session
        preregister preRegistrationManager = new preregister(name, displayName);
        preRegistrationManager.sendRequest();
        preReg_challenge = preRegistrationManager.getChallenge();
        pre_session = preRegistrationManager.getSession();

        // 生成 hashed, hashedsigned, pubkey
        ECDSASignature_fromAPI ecdsa = new ECDSASignature_fromAPI();
        ecdsa.setChallenge(preReg_challenge);
        ecdsa.getkeypairhex();
        privateKeyHex = ecdsa.privateKeyHex;
        storePrivateKey(privateKeyHex);
        String FromFilePrivateKey = readPrivateKey();

        publicKeyHex = ecdsa.publicKeyHex;
        hashedChallengeHex = ecdsa.getHashChallengeHex();
        hashedSignedMSGHex = ecdsa.signhashMessage();

        // 注册 設定 session, hashedChallengeHex, hashedSignedMSGHex, pubkey, 傳送 hashed, hashedsigned, pubkey
        register RegistrationManager = new register(name,displayName);
        RegistrationManager.setSession(pre_session);
        RegistrationManager.sethashedChallengeHex(hashedChallengeHex);
        RegistrationManager.sethashedSignedMSGHex(hashedSignedMSGHex);
        RegistrationManager.setpublicKeyHex(publicKeyHex);

        String result = RegistrationManager.sendRequest();
        return result;
    }
}