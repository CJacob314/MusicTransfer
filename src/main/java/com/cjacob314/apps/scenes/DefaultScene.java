/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.scenes;

import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.tidal.TidalUtils;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Singleton(?) class to manage&contain the project's default scene
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class DefaultScene {

    private static Scene instance = null;

    private static JFXTreeTableView<TrackTreeObj> tableView;

    public static ObservableList<TrackTreeObj> tracks = FXCollections.observableArrayList();

    private static void initTableView() {
        tableView = new JFXTreeTableView<>();
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
    }

    private static GridPane createGrid(){
        // will eventually do with fxml file, rather than this code DD:

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 25, 25, 25));
        JFXButton connTidalBtn = new JFXButton("Connect to Tidal");
        connTidalBtn.setOnAction(TidalUtils::testConnect);
        connTidalBtn.setPrefHeight(60);
        connTidalBtn.setButtonType(JFXButton.ButtonType.RAISED);
        connTidalBtn.setRipplerFill(IniManager.windowRipplerColor);

        JFXTextField searchField = new JFXTextField();
        searchField.setPrefWidth(200d);
        searchField.setPromptText("Song Search");
        searchField.setOnAction(actionEvent -> {
            List<Track> results = TidalUtils.searchTrackAndGetList(searchField.getText());
            tracks.clear();
            results.forEach(track -> tracks.add(new TrackTreeObj(track)));
        });

        grid.setVgap(10d);
        grid.setHgap(10d);
        grid.add(connTidalBtn, 0, 0);

        initTableView();

        //grid.setGridLinesVisible(true);
        grid.add(searchField, 1, 0);
        grid.add(tableView, 0, 1, 1, 5);

        return grid;
    }

    public static Scene getMain(){
        if(instance == null) instance = new Scene(createGrid(), IniManager.windowWidth, IniManager.windowHeight);
        return instance;
    }
}
