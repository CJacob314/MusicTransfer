/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.scenes;

import com.cjacob314.apps.tidal.TidalUtils;
import com.cjacob314.apps.utils.HttpUtils;
import com.hadas.krzysztof.models.Track;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * For the rows of search results in the table view !
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class TrackTreeObj extends RecursiveTreeObject<TrackTreeObj> {
    private Track track;

    public StringProperty trackName;
    public StringProperty trackArtists;
    public SimpleObjectProperty<ImageView> image;

    public Track getTrack(){ return track; }

    public TrackTreeObj(Track track){
        this.track = track;
        trackName = new SimpleStringProperty(track.getTitle());
        trackArtists = new SimpleStringProperty(TidalUtils.formArtistString(track.getArtists()));
        image = new SimpleObjectProperty<>(new ImageView(new Image(HttpUtils.getTrackCoverUrl(track),35d, 35d,true,true)));
    }

}
