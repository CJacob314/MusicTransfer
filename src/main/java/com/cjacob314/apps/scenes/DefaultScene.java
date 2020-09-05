/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.scenes;

import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.tidal.TidalUtils;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * Singleton(?) class to manage&contain the project's default scene
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class DefaultScene {

    private static Scene instance = null;

    private static GridPane createGrid(){
        // will eventually do with fxml file, rather than this code DD:

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(25, 25, 25, 25));
        JFXButton connTidalBtn = new JFXButton("Connect to Tidal");
        connTidalBtn.setOnAction(TidalUtils::testConnect);
        connTidalBtn.setPrefHeight(60);
        connTidalBtn.setButtonType(JFXButton.ButtonType.RAISED);
        connTidalBtn.setRipplerFill(IniManager.windowRipplerColor);
        /*
        Button connTidalBtn = new Button("Connect To Tidal");
        connTidalBtn.setAlignment(Pos.TOP_LEFT);
        connTidalBtn.setOnAction(TidalUtils::testConnect);
         */
        grid.add(connTidalBtn, 0, 0);

        return grid;
    }

    public static Scene getMain(){
        if(instance == null) instance = new Scene(createGrid(), IniManager.windowWidth, IniManager.windowHeight);
        return instance;
    }
}
