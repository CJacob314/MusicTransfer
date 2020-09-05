/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils.tmp;

import com.cjacob314.apps.utils.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Quick little class to create a salt for the encryption algorithm. Does not matter that it is all the same for the obvious reasons + there is only one stored password at a time anyway...
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class SaltMaker {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static void printASaltAsHexString() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        SecureRandom sr = SecureRandom.getInstanceStrong();
        sr.nextBytes(salt);
        Logger.logInfo(bytesToHex(salt));
    }
}
