package com.benine.backend.http;

import com.benine.backend.Main;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandler {
  public void handle(HttpExchange exchange) throws IOException {
    //TODO add logging stuff
    JSONObject jsonObj = new JsonObject();
    jsonObj.put(Main.getCameraHandler().getCameras());
  }
}
