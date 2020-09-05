/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps;

import com.cjacob314.apps.utils.TextEncryptor;

/**
 * User non-editable constants
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class Constants {
    public static final String mainWindowName = "Jacob's Music Transfer";
    public static final String mainAppName = "JacobMusicTransfer";
    public static final byte[] encryptPass = TextEncryptor.hexStringToByteArray("6a256f574061657026495e435761244636786a5162656e4e");
    public static final byte[] encryptSalt = TextEncryptor.hexStringToByteArray("01afc41ad2d119674805bfeec83a8180");
    public static final String logAFilePrefix = "JMT-aout--";
    public static final String logEFilePrefix = "JMT-err--";
    public static final String logIFilePrefix = "JMT-info--";
    public static final String logFileExt = ".log";
    public static final String logIPrefix = "INFO ";
    public static final String logEPrefix = "ERROR ";
    public static boolean logCallingClasses = true;
    public static final String logDirNextToExe = "JMT-logs";


    // inherently static and final already :O
    public enum DLQuality {
        MASTER("HI_RES"), HIFI("LOSSLESS"), HIGH("HIGH"), LOW("LOW");
        private String str;
        public String getStr(){ return str; }
        DLQuality(String str){ this.str = str; }
    }
}
