/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.persistence;

import com.cjacob314.apps.Constants;
import com.cjacob314.apps.utils.logging.Logger;
import com.cjacob314.apps.utils.SystemInfo;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.ini4j.Wini;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Class to manage the .ini files for this application
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class IniManager {

    public static Double windowWidth = null;
    public static Double windowHeight = null;
    public static String windowThemeColor = null;
    public static Boolean debugLogClassNames = null;

    public static void loadIni() {
        // for org.ini4j.Wini
        File iniFile = new File(SystemInfo.exeDirPath() + "\\" + Constants.mainAppName + "_Settings.ini");
        try {
            if(!iniFile.exists()){
                iniFile.createNewFile();
            }
            Wini ini = new Wini(iniFile);

            for(Field f : IniManager.class.getDeclaredFields()){
                String[] splitName = f.getName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])", 2); // the limit param here is interesting!

                if(ini.get(splitName[0], splitName[1]) == null){
                    ini.put(splitName[0], splitName[1], IniDefaults.class.getDeclaredField(f.getName()).get(null));
                }
                if(f.getType() == Double.class){
                    f.set(IniManager.class, Double.parseDouble(ini.get(splitName[0], splitName[1])));
                }

                if(f.getType() == String.class){
                    f.set(IniManager.class, ini.get(splitName[0], splitName[1]));
                }

                if(f.getType() == Boolean.class){
                    f.set(IniManager.class, ini.get(splitName[0], splitName[1], Boolean.class));
                }
            }

            ini.store();
        }
        catch(Exception e){
            Platform.runLater(()->{new Alert(Alert.AlertType.ERROR, "There was a problem opening the settings file (\".ini\" extension)").showAndWait();}); // not sure why I needed the runLater...
            Logger.logError(e.getMessage());
            System.exit(-1);
        }

    }
}
