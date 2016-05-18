package com.benine.backend.http;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

import com.benine.backend.Preset;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.database.Database;
import com.sun.net.httpserver.HttpExchange;

public class PresetHandlerTest {
  
  private ServerController serverController;
  private PresetHandler handler;
  private CameraController camController;
  private OutputStream out;
  private Logger logger;
  private Database database = mock(Database.class);
  HttpExchange exchange = mock(HttpExchange.class);
  
  @Before
  public void setUp() throws CameraConnectionException{
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    camController = mock(CameraController.class);
    serverController = ServerController.getInstance();
    serverController.setDatabase(database);
    serverController.setCameraController(camController);
    logger = mock(Logger.class);
    handler = new PresetHandler(logger);  
    
    out = mock(OutputStream.class);
  }

  @Test
  public void testQueryByKeyWord() throws Exception {
    URI uri = new URI("http://localhost/camera/1/preset?tag=piano");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    Camera cam = mock(Camera.class);
    when(camController.getCameraById(1)).thenReturn(cam);
    Preset[] presets = {new Preset(new Position(0, 0), 0, 0, 0, true, 0, 0, true),
                      new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, true)};
    presets[0].addTag("Piano");
    presets[1].addTag("Violin");
    when(cam.getPresets()).thenReturn(presets);
    handler.handle(exchange);
    JSONObject obj = new JSONObject();
    JSONArray ar = new JSONArray();
    ar.add(presets[0].toJSON());
    obj.put("presets", ar);
    String expected = obj.toString();
    verify(out).write(expected.getBytes());
  }

}
