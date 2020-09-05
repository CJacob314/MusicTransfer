/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils;

import com.cjacob314.apps.Constants;
import com.cjacob314.apps.tidal.TidalUtils;
import com.mashape.unirest.request.GetRequest;
import com.hadas.krzysztof.models.Track;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * Utilities for the HTTP request(s)
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class HttpUtils {

    public static String getTidalTrackHifiOfflineUrl(Track track){
        //String url = getTidalTrackHifiUrl(track);
        Map<String,String> pParams = TidalUtils.getSessionParams();
        pParams.put("soundQuality", Constants.DLQuality.MASTER.getStr());
        pParams.remove("limit");
        return getHTTP("https://api.tidal.com/v1/tracks/" + track.getId().toString() + "/streamUrl", pParams).getBody().getObject().getString("url");
    }

    public static HttpResponse<JsonNode> getHTTP(String url, Map<String,String> params){
        GetRequest req = Unirest.get(url);
        params.forEach((k, v) -> req.queryString(k, v));
        try {
            //Logger.logInfo("JSON IS: " + req.asJson().getBody().getObject().toString());
            return req.asJson();
        } catch (UnirestException e) {
            Logger.logError("UnirestException! Returning null, not json data...");
            return null;
        }
    }

    // used to have a postHTTP method here. don't need it haha...
}
