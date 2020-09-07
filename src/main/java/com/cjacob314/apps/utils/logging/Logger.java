/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils.logging;

import com.cjacob314.apps.App;
import com.cjacob314.apps.Constants;
import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.utils.SystemInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A quick little way to store the programs outputs and log things properly...
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class Logger {

    private static FileOutputStream allOuts;
    private static final String runTimeAppend;

    private static boolean firstLine = true;

    static{
        LocalDateTime start = App.getStartTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        runTimeAppend = dtf.format(start);

        try {
            new File(SystemInfo.exeDirPath() + "\\" + Constants.logDirNextToExe).mkdirs();
            File aOutFile = new File(SystemInfo.exeDirPath() + "\\" + Constants.logDirNextToExe + "\\" + Constants.logAFilePrefix + runTimeAppend + Constants.logFileExt);
            aOutFile.createNewFile();
            allOuts = new FileOutputStream(aOutFile, true);
        } catch (IOException e) {
            e.printStackTrace(); // going to be the only non logged-through-Logger log haha
        }
    }

    public static void logInfo(String info){
        String toPrint = Constants.logIPrefix + (IniManager.debugLogClassNames ? "[" + new Exception().getStackTrace()[1].getClassName() + "] " : "") + info;
        System.out.println(toPrint);
        try {
            String finalPrint = (firstLine ? "" : System.lineSeparator()) + toPrint;
            allOuts.write(finalPrint.getBytes());
            InAppLogView.write(finalPrint);
            // add just the info files too
        } catch (IOException e) {
            e.printStackTrace(); // okay these ones too lol
        } finally{
            firstLine = false;
        }
    }

    public static void logError(String error){
        String toPrint = Constants.logEPrefix + (IniManager.debugLogClassNames ? "[" + new Exception().getStackTrace()[1].getClassName() + "] " : "") + error;
        System.err.println(toPrint);
        try {
            String finalPrint = (firstLine ? "" : System.lineSeparator()) + toPrint;
            allOuts.write(finalPrint.getBytes());
            InAppLogView.write(finalPrint);
            // add just the error files too
        } catch (IOException e) {
            e.printStackTrace(); // yup this should be the last...
        } finally{
            firstLine = false;
        }
    }

    public static void logException(Exception e) {
        String toOut = Constants.logEPrefix + "caught exception: " + e.toString();
        System.err.println(toOut);
        try {
            String finalToOut = (firstLine ? "" : System.lineSeparator()) + toOut;
            allOuts.write(finalToOut.getBytes());
            InAppLogView.write(finalToOut);
            // add just the error files too
        } catch (IOException ioe) {
            ioe.printStackTrace(); // haha
        } finally{
            firstLine = false;
        }
    }
}
