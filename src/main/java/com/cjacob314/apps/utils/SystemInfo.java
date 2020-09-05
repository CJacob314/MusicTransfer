/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils;

/**
 * Really self-explanatory :)
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class SystemInfo {

    public static String javaVersion() { return System.getProperty("java.version"); }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

    public static String exeDirPath() { return System.getProperty("user.dir"); }

}