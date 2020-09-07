/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.tidal;

import com.cjacob314.apps.persistence.CredSaver;
import com.cjacob314.apps.scenes.DefaultScene;
import com.cjacob314.apps.utils.ColorUtils;
import com.cjacob314.apps.utils.HttpUtils;
import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.utils.logging.Logger;
import com.cjacob314.apps.utils.SystemInfo;
import com.hadas.krzysztof.models.Playlist;
import com.hadas.krzysztof.models.Track;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * A class of custom utils for Tidal... Should really be a package with multiple classes...
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class TidalUtils {
    private static final TidalApiImplCustom api;

    static{
        api = new TidalApiImplCustom();
    }

    public static TidalApiImplCustom getCustomTidalApiInst(){
        if(api.getSession() == null){
            Logger.logError("TIDAL session has not yet been created! Use GUI and input username & password then try again.");
            return null; // could use Optional<> but this is better imo
        }

        return api;
    }

    public static void testDownload(){
        //download(api.searchTrack("Shoot Sideways").get(0));
        download(api.searchTrack("Ooh La La").get(0));
    }

    private static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    public static String artistNameFromObject(Object artist){
        // so much was attempted here. Turns out the Object instance passed by HieronimB's wrapper was just a HashMap
        return (String)((HashMap)artist).get("name");
    }

    public static String formArtistString(List<Object> artists){
        // yeah...

        StringBuilder strb = new StringBuilder(); // using this because str concat in loop bad or something (https://rules.sonarsource.com/java/RSPEC-1643)
        int sz = artists.size();
        if(sz > 0)
            strb.append(artistNameFromObject(artists.get(0)));
        if(sz > 2)
            for(int i = 0; i < sz - 1; i++)
                strb.append(", ").append(artistNameFromObject(artists.get(i)));

        strb.append(" & ").append(artistNameFromObject(artists.get(sz - 1)));

        return strb.toString();
    }

    public static void download(Track track){
        Logger.logInfo("Downloading track id: " + track.getId().toString());
        String url = HttpUtils.getTidalTrackHifiOfflineUrl(track);
        try {
            // Apparently + str concat gets compiled to StringBuilder, which is a lot faster than my String.format old way...
            downloadUsingStream(url, SystemInfo.exeDirPath() + "\\" + track.getTitle() + " - " + formArtistString(track.getArtists()) + ".flac");
        } catch (IOException e) {
            Logger.logException(e);
        }
    }

    public static void initTidalConn(ActionEvent actionEvent) {
        // Really need to move to the fxml things smh...

        JFXAlert<Pair<String, String>> alert = new JFXAlert<>();
        alert.setResult(null);
        alert.setTitle("Tidal Login");
        alert.setSize(325, 180);
        double fieldWidth = 200d;
        JFXPasswordField pwd = new JFXPasswordField();
        JFXTextField username = new JFXTextField();
        pwd.setPrefWidth(fieldWidth);
        username.setPrefWidth(fieldWidth);
        username.setPromptText("Tidal Email Address");
        pwd.setPromptText("Tidal Password");

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5, 5, 5, 5));
        HBox hBox = new HBox();
        JFXButton submitBtn = new JFXButton("Submit");
        submitBtn.setRipplerFill(Paint.valueOf(IniManager.windowThemeColor));
        submitBtn.setButtonType(JFXButton.ButtonType.RAISED);
        //submitBtn.setDefaultButton(true); // I think just close down below. It seems to auto choose this btn when pressing exit as well...
        JFXCheckBox savePCheck = new JFXCheckBox("Save password");
        savePCheck.setCheckedColor(Paint.valueOf(IniManager.windowThemeColor));
        savePCheck.setUnCheckedColor(Paint.valueOf(ColorUtils.darkenColorStr(IniManager.windowThemeColor, 0.3d)));
        if(CredSaver.isDataSaved()){
            Optional<Pair<String,String>> pairOp = CredSaver.getSaved();
            if(pairOp.isPresent()){
                Pair<String,String> pair = pairOp.get();
                username.setText(pair.getKey());
                pwd.setText(pair.getValue());
                savePCheck.setSelected(true);
            }
        }
        submitBtn.setOnAction(addEvent -> {
            String usernameStr = username.getText();
            String pwdStr = pwd.getText();
            alert.setResult(new Pair<>(usernameStr, pwdStr));
            alert.hideWithAnimation();
            if(savePCheck.isSelected()){
                CredSaver.save(usernameStr, pwdStr);
            }
            else{
                CredSaver.removeSavedData();
            }
        });

        Region spacer = new Region(); // good ol' spacers
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox wrap = new HBox(savePCheck);
        wrap.setFillHeight(true);
        wrap.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(wrap, spacer, submitBtn);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.setBottom(hBox);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(username, pwd);
        pane.setTop(vBox);

        JFXDialogLayout content = new JFXDialogLayout();

        content.setBody(pane);
        alert.setContent(content);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(TidalUtils.class.getClassLoader().getResourceAsStream("images/icon.png")));

        //alert.setOnCloseRequest(closeEvent -> alert.setResult(Optional.empty())); // event is NOT only called from exit button. will do at start

        Optional<Pair<String, String>> resultOp = alert.showAndWait();

        if (resultOp.isPresent()) {
            /* The following code is a really annoying workaround D: */
            Object resultObj = resultOp.get();
            if(!(resultObj instanceof Pair)) return; // it will be a ButtonType. thanks jfx...
            Pair<String, String> result = (Pair<String, String>) resultObj;

            if(result.getKey().isBlank() || result.getValue().isBlank()) return;
            api.login(result.getKey(), result.getValue());

            String sessionId = api.getSession().getSessionId();
            if(sessionId != null && !sessionId.isBlank()){
                Logger.logInfo("You have successfully logged in!");
                DefaultScene.enableTidalFunc();
                List<Playlist> userPlaylists = api.getUserPlaylists();
                Logger.logInfo("Here's a random one of your playlists' name: " + userPlaylists.get(new Random().nextInt(userPlaylists.size())).getTitle());
            }
            else{
                Logger.logError("The login was unsuccessful :(");
            }
        }
    }

    public static Map<String,String> getSessionParams() {
        return api.getSessionParams();
    }

    public static List<Track> searchTrackAndGetList(String searchStr) {
        if(api.getSession() == null){
            Logger.logError("TIDAL session has not yet been created! Use GUI and input username & password then try again.");
            return null;
        }

        return api.searchTrack(searchStr);
    }
}
