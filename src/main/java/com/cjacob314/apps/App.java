/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps;

import com.cjacob314.apps.persistence.CredSaver;
import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.utils.logging.InAppLogView;
import com.cjacob314.apps.utils.logging.Logger;
import com.cjacob314.apps.utils.SystemInfo;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.cjacob314.apps.scenes.*;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The all-containing class (including the main())
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class App extends Application {

    private static LocalDateTime startTime;

    public static LocalDateTime getStartTime(){
        return startTime;
    }

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        Logger.logInfo(Constants.mainAppName + " running on Java " + javaVersion);

        stage.setTitle(Constants.mainWindowName + " v" + Constants.mainAppVersion);
        stage.setScene(DefaultScene.getMain());
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/icon.png")));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(final String[] args) {
        startTime = LocalDateTime.now();
        IniManager.loadIni();
        launch(args);
    }

    /**
     * Testing function for setting and getting the stored user credentials. Call wherever (I did in main) for testing!
     */
    private static boolean testCreds(String testUserStr, String testPassStr) {
        CredSaver.save(testUserStr, testPassStr);

        Logger.logInfo("Saved?");

        Optional<Pair<String,String>> resultOp = CredSaver.getSaved();

        if(resultOp.isPresent()){
            Pair<String,String> result = resultOp.get();
            Logger.logInfo("We got u: " + result.getKey() + " and p: " + result.getValue());
            return true;
        }
        return false;
    }
}