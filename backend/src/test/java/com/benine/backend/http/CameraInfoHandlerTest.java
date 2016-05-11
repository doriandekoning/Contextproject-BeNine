package com.benine.backend.http;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ZoomingCamera;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.OutputStream;
import java.net.URI;

import static org.mockito.Mockito.*;

/**
 * Created by dorian on 4-5-16.
 */
public class CameraInfoHandlerTest {

  @Test
  public void testGetInfo() throws Exception {
    // Setup mocks
    HttpExchange exchange = mock(HttpExchange.class);
    OutputStream out = mock(OutputStream.class);
    ZoomingCamera cam = mock(ZoomingCamera.class);
    CameraController camController = new CameraController();
    camController.addCamera(cam);
    // Setup request uri
    URI uri = new  URI("http://localhost/camera/");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    CameraInfoHandler cHandler = new CameraInfoHandler(camController);
    try {
      cHandler.handle(exchange);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Create expected json object
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    for(Camera camera : camController.getCameras()) {
      array.add(camera.toJSON());
    }
    json.put("cameras", array);
    // Test if correct json string is written to the output
    verify(out).write(json.toString().getBytes());
  }
}
