/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.scenes;

import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.tidal.TidalUtils;
import com.cjacob314.apps.utils.logging.InAppLogView;
import com.cjacob314.apps.utils.logging.Logger;
import com.hadas.krzysztof.models.Track;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.List;

/**
 * Singleton(?) class to manage&contain the project's default scene
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class DefaultScene {

    private static Scene instance = null;

    private static JFXTextField searchField;

    public static ObservableList<TrackTreeObj> tracks = FXCollections.observableArrayList();

    public static void enableTidalFunc(){
        searchField.setDisable(false);
    }

    private static JFXTreeTableView<TrackTreeObj> initTableView() {
        JFXTreeTableView<TrackTreeObj> tableView = new JFXTreeTableView<>();
        tableView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        TreeTableColumn<TrackTreeObj,ImageView> trackCover = new TreeTableColumn<>("Image");
        trackCover.setPrefWidth(50d);
        trackCover.setCellValueFactory(param -> param.getValue().getValue().image);
        trackCover.setReorderable(false);

        TreeTableColumn<TrackTreeObj,String> trackTitle = new TreeTableColumn<>("Track");
        trackTitle.setPrefWidth(350d);
        trackTitle.setCellValueFactory(param -> param.getValue().getValue().trackName);

        TreeTableColumn<TrackTreeObj,String> trackArtists = new TreeTableColumn<>("Artists");
        trackArtists.setPrefWidth(450d);
        trackArtists.setCellValueFactory(param -> param.getValue().getValue().trackArtists);

        final TreeItem<TrackTreeObj> root = new RecursiveTreeItem<>(tracks, RecursiveTreeObject::getChildren);
        tableView.getColumns().setAll(trackCover, trackTitle, trackArtists);
        tableView.setRoot(root);
        tableView.setShowRoot(false);
        tableView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                if(mouseEvent.getClickCount() == 2){
                    TidalUtils.download(tableView.getSelectionModel().getSelectedItem().getValue().getTrack());
                }
        });
        tableView.setPlaceholder(new Label("Double click anything appearing here to download it at the highest quality!"));
        return tableView;
    }

    private static GridPane createGrid(){
        // will eventually do with fxml file, rather than this code DD:

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 25, 25, 25));
        JFXButton connTidalBtn = new JFXButton("Connect to Tidal");
        connTidalBtn.setOnAction(TidalUtils::initTidalConn);
        connTidalBtn.setPrefHeight(60);
        connTidalBtn.setButtonType(JFXButton.ButtonType.RAISED);
        connTidalBtn.setRipplerFill(Paint.valueOf(IniManager.windowThemeColor));

        searchField = new JFXTextField();
        searchField.setPrefWidth(200d);
        searchField.setPromptText("Song Search");
        searchField.setDisable(true);
        searchField.setOnAction(actionEvent -> {
            String toSearch = searchField.getText();
            List<Track> results = TidalUtils.searchTrackAndGetList(toSearch);
            tracks.clear();
            results.forEach(track -> tracks.add(new TrackTreeObj(track)));
            Logger.logInfo(toSearch.isBlank() ? "Cleared Tidal search" : "Searched Tidal for \"" + toSearch + "\"");
        });

        grid.setVgap(10d);
        grid.setHgap(10d);
        grid.add(connTidalBtn, 0, 0);

        //grid.setGridLinesVisible(true);
        grid.add(searchField, 1, 0);
        grid.add(initTableView(), 0, 1, 1, 5);
        grid.add(InAppLogView.getMain(), 0, 10, 1, 3);

        return grid;
    }

    public static Scene getMain(){
        if(instance == null) instance = new Scene(createGrid(), IniManager.windowWidth, IniManager.windowHeight);
        return instance;
    }
}
