package com.example.fido.fidoapi;

import java.io.IOException;

public class ConnAPI {
    public static void main(String[] args) throws Exception {
        //初始化變數
        String preReg_challenge="";
        String pre_session="NULL pre_session";
        String hashedChallengeHex="";
        String hashedSignedMSGHex="";
        String publicKeyHex="";
        String privateKeyHex="";

        //前註冊 最後拿到challenge,session
        preregister preRegistrationManager = new preregister();
        preRegistrationManager.sendRequest();
        preReg_challenge =  preRegistrationManager.getChallenge();
        pre_session = preRegistrationManager.getSession();




        //生成hashed,hashedsigned,pubkey
        String challenge="";
        ECDSASignature_fromAPI ecdsa = new ECDSASignature_fromAPI();
        ecdsa.setChallenge(preReg_challenge);
        ecdsa.getkeypairhex();
        privateKeyHex =  ecdsa.privateKeyHex;
        publicKeyHex = ecdsa.publicKeyHex;
        hashedChallengeHex = ecdsa.getHashChallengeHex();
        hashedSignedMSGHex = ecdsa.signhashMessage();



        //註冊 設定session,hashedChallengeHex,hashedSignedMSGHex,pubkey,，傳送hashed,hashedsigned,pubkey
        register RegistrationManager = new register();
        System.out.println("pre_session: " + pre_session);
        RegistrationManager.setSession(pre_session);
        RegistrationManager.sethashedChallengeHex(hashedChallengeHex);
        RegistrationManager.sethashedSignedMSGHex(hashedSignedMSGHex);
        RegistrationManager.setpublicKeyHex(publicKeyHex);

        String hashedChallengeHex1 = RegistrationManager.hashedChallengeHex;
        System.out.println("publicKeyHex: " + publicKeyHex);


        RegistrationManager.sendRequest();




    }
    public void start() throws Exception {
        //初始化變數
        String preReg_challenge="";
        String pre_session="NULL pre_session";
        String hashedChallengeHex="";
        String hashedSignedMSGHex="";
        String publicKeyHex="";
        String privateKeyHex="";

        //前註冊 最後拿到challenge,session
        preregister preRegistrationManager = new preregister();
        preRegistrationManager.sendRequest();
        preReg_challenge =  preRegistrationManager.getChallenge();
        pre_session = preRegistrationManager.getSession();




        //生成hashed,hashedsigned,pubkey
        String challenge="";
        ECDSASignature_fromAPI ecdsa = new ECDSASignature_fromAPI();
        ecdsa.setChallenge(preReg_challenge);
        ecdsa.getkeypairhex();
        privateKeyHex =  ecdsa.privateKeyHex;
        publicKeyHex = ecdsa.publicKeyHex;
        hashedChallengeHex = ecdsa.getHashChallengeHex();
        hashedSignedMSGHex = ecdsa.signhashMessage();



        //註冊 設定session,hashedChallengeHex,hashedSignedMSGHex,pubkey,，傳送hashed,hashedsigned,pubkey
        register RegistrationManager = new register();
        System.out.println("pre_session: " + pre_session);
        RegistrationManager.setSession(pre_session);
        RegistrationManager.sethashedChallengeHex(hashedChallengeHex);
        RegistrationManager.sethashedSignedMSGHex(hashedSignedMSGHex);
        RegistrationManager.setpublicKeyHex(publicKeyHex);

        String hashedChallengeHex1 = RegistrationManager.hashedChallengeHex;
        System.out.println("publicKeyHex: " + publicKeyHex);


        String result = RegistrationManager.sendRequest();
    }
}