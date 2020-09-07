/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils;

import com.cjacob314.apps.Constants;
import com.cjacob314.apps.utils.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * I know. Key still on client as well... Just to prevent really easy access by non-computer-savvy people as it would be plain text
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class TextEncryptor {
    private static Cipher cipher = null;
    private static Boolean lastDecryptMode = null;

    public static Cipher getCipher(){ return getCipher(false); } // seriously java? no default parameters? well this works...

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static Cipher getCipher(boolean decryptMode){
        if(cipher == null || (lastDecryptMode != null && decryptMode != lastDecryptMode)){
            SecretKeyFactory scf = null;

            try {
                //cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                //cipher = Cipher.getInstance("AES/CBC/NoPadding");
                //cipher = Cipher.getInstance("AES/CTR/NoPadding");
                cipher = Cipher.getInstance("AES"); // Yeah I know it is not secure. Oh well. It obfuscates enough for my purposes...
                scf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); // maybe 256? no
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                Logger.logException(e);
            }

            byte[] salt = Constants.encryptSalt; // I explain why only one predefined salt elsewhere...

            PBEKeySpec spec = new PBEKeySpec(new String(Constants.encryptPass, StandardCharsets.UTF_8).toCharArray(), salt, 65536, 256); // maybe change i count

            SecretKeySpec key;
            try {
                 key = new SecretKeySpec(scf.generateSecret(spec).getEncoded(), "AES");
                 cipher.init(decryptMode ? Cipher.DECRYPT_MODE : Cipher.ENCRYPT_MODE, key);
            } catch (InvalidKeySpecException | InvalidKeyException e) {
                Logger.logException(e);
            }
        }

        lastDecryptMode = decryptMode;
        return cipher;
    }

    public static String decryptString(byte[] byteArr){
        Cipher cipher = getCipher(true);

        try {
            return new String(cipher.doFinal(byteArr), StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            Logger.logException(e);
        }

        Logger.logError("Returning null... See log.");
        return null;
    }

    public static byte[] encryptString(String string){
        Cipher cipher = getCipher();

        byte[] strBytes = string.getBytes(StandardCharsets.UTF_8); // using the StandardCharsets instead of "UTF-8" string as then do not have to worry about catching UnsupportedEncodingException

        byte[] encrypted = null; // was String
        try {
            encrypted = cipher.doFinal(strBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            Logger.logException(e);
        }

        return encrypted;
    }
}
