/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.tidal;

import com.cjacob314.apps.utils.logging.Logger;
import com.hadas.krzysztof.TidalApi;
import com.hadas.krzysztof.favorites.Favorites;
import com.hadas.krzysztof.items.Items;
import com.hadas.krzysztof.models.*;
import com.hadas.krzysztof.playlist.UserPlaylists;
import com.hadas.krzysztof.search.Search;
import com.hadas.krzysztof.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A custom implementation of com.hadas.krzysztof.TidalApi, allowing for easier access to the Session itself and the http params
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class TidalApiImplCustom implements TidalApi {
    private Favorites favorites;
    private Search search;
    private UserPlaylists userPlaylists;
    private Items items;
    private Session session;

    public Map<String,String> getSessionParams() {
        if(session == null){
            Logger.logError("TIDAL session has not yet been created! Use GUI and input username & password then try again.");
            return null; // could use Optional<> but this is better imo
        }

        // Tried with Map.of(), but forgot that it returns an immutable object...
        return new HashMap<>(){{
            put("sessionId", session.getSessionId());
            put("countryCode", session.getCountryCode());
            put("limit", "1000"); // SFMS
        }};

    }

    public Session getSession(){
        if(session == null){
            Logger.logError("TIDAL session has not yet been created! Use GUI and input username & password then try again.");
            return null; // could use Optional<> but this is better imo
        }
        return session;
    }

    public void login(String username, String password) {
        session = Session.login(username, password);
        favorites = new Favorites(session);
        search = new Search(session);
        userPlaylists = new UserPlaylists(session);
        items = new Items(session);
    }

    public void addTrackToPlaylist(List<String> trackId, String playlistId) { userPlaylists.addTrackToPlaylist(trackId, playlistId); }

    public List<Playlist> getUserPlaylists() {
        return userPlaylists.getUserPlaylists();
    }

    public Playlist createPlaylist(String title, String description) { return userPlaylists.createPlaylist(title, description); }

    public void deletePlaylist(String playlistId) {
        userPlaylists.deletePlaylist(playlistId);
    }

    public void deleteTrackFromPlaylist(String playlistId, int index) { userPlaylists.deleteTrackFromPlaylist(playlistId, index); }

    public List<Track> searchTrack(String query) {
        return search.searchTrack(query);
    }

    public List<Album> searchAlbum(String query) {
        return search.searchAlbum(query);
    }

    public List<Playlist> searchPlaylist(String query) {
        return search.searchPlaylist(query);
    }

    public List<Artist> searchArtist(String query) {
        return search.searchArtist(query);
    }

    public SearchResult search(String query, String types) {
        return search.search(query, types);
    }

    public Album getAlbum(String albumId) {
        return items.getAlbum(albumId);
    }

    public Artist getArtist(String artistId) {
        return items.getArtist(artistId);
    }

    public Track getTrack(String trackId) {
        return items.getTrack(trackId);
    }

    public Playlist getPlaylist(String playlistId) {
        return items.getPlaylist(playlistId);
    }

    public List<Album> getFavoriteAlbums() {
        return favorites.getFavoriteAlbums();
    }

    public List<Artist> getFavoriteArtists() {
        return favorites.getFavoriteArtists();
    }

    public List<Playlist> getFavoritePlaylists() {
        return favorites.getFavoritePlaylists();
    }

    public List<Track> getFavoriteTracks() {
        return favorites.getFavoriteTracks();
    }

    public void addAlbumToFavorite(String albumId) {
        favorites.addAlbumToFavorite(albumId);
    }

    public void addArtistToFavorite(String artistId) {
        favorites.addArtistToFavorite(artistId);
    }

    public void addTrackToFavorite(String trackId)  {
        favorites.addTrackToFavorite(trackId);
    }

    public void addPlaylistToFavorite(String playlistId) {
        favorites.addPlaylistToFavorite(playlistId);
    }

    public void removeTrackFromFavorites(String trackId) {
        favorites.removeTrackFromFavorites(trackId);
    }

    public void removeAlbumFromFavorites(String albumId) {
        favorites.removeAlbumFromFavorites(albumId);
    }

    public void removeArtistFromFavorites(String artistId) {
        favorites.removeArtistFromFavorites(artistId);
    }

    public void removePlaylistFromFavorites(String playlistId) {
        favorites.removePlaylistFromFavorites(playlistId);
    }

}