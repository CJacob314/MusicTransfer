/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.persistence;

import com.cjacob314.apps.utils.Logger;
import com.cjacob314.apps.utils.SystemInfo;
import com.cjacob314.apps.utils.TextEncryptor;
import javafx.util.Pair;

import java.io.*;
import java.util.Optional;

/**
 * I know. Key still on client as well... Just to prevent really easy access by non-computer-savvy people as it would be plain text
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class CredSaver {

    static String pathToCredsDir = null;

    static{
        pathToCredsDir = SystemInfo.exeDirPath() + "\\" + "credentials"; // removed the + Constants.mainAppName
    }

    public static boolean isDataSaved(){
        File passFile = new File(pathToCredsDir + "\\p");
        File userFile = new File(pathToCredsDir + "\\u");
        return userFile.exists() && passFile.exists();
    }

    public static void removeSavedData(){
        File passFile = new File(pathToCredsDir + "\\p");
        File userFile = new File(pathToCredsDir + "\\u");
        passFile.delete();
        userFile.delete();
    }

    private static void byteArrToFile(byte[] arr, File file){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(arr);
        }
        catch (FileNotFoundException e) {
            Logger.logError("File not found" + e);
        }
        catch (IOException ioe) {
            Logger.logInfo("Exception while writing file " + ioe);
        }
        finally {
            try {
                if (fos != null) fos.close();
            }
            catch (IOException ioe) {
                Logger.logInfo("Error while closing stream: " + ioe);
            }
        }
    }

    private static byte[] fileToByteArr(File file){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return fis.readAllBytes();
        }
        catch (FileNotFoundException e) {
            Logger.logError("File not found" + e);
        }
        catch (IOException ioe) {
            Logger.logError("Exception while writing file " + ioe);
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            }
            catch (IOException ioe) {
                Logger.logError("Error while closing stream: " + ioe);
            }
        }

        Logger.logError("Will be returning null, something went wrong. See log.");
        return null;
    }

    public static void save(String username, String password){
        if(isDataSaved()){
            // return; // oh lord we should check for updates good lord // save hashes to tell if different data?
        }

        File dir = new File(pathToCredsDir);
        dir.mkdirs(); // yes I had to do this separately...

        File passOut = new File(pathToCredsDir + "\\p");
        byteArrToFile(TextEncryptor.encryptString(password), passOut);

        File userOut = new File(pathToCredsDir + "\\u");
        byteArrToFile(TextEncryptor.encryptString(username), userOut);
    }

    public static Optional<Pair<String,String>> getSaved(){
        File passIn = new File(pathToCredsDir + "\\p");
        File userIn = new File(pathToCredsDir + "\\u");
        if(!userIn.exists() || !passIn.exists()){
            return Optional.empty();
        }

        byte[] userArr = fileToByteArr(userIn);
        byte[] passArr = fileToByteArr(passIn);

        return Optional.of(new Pair<String,String>(TextEncryptor.decryptString(userArr), TextEncryptor.decryptString(passArr)));
    }
}
