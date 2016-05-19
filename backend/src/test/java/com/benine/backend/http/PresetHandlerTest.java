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
  HttpExchange exchange = mock(HttpExchange.class);
  ArrayList<Preset> presets = new ArrayList<Preset>();
  Database database = mock(Database.class);
  
  @Before
  public void setUp() throws Exception{
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "serverControllertest.conf");
    serverController = ServerController.getInstance();
    camController = mock(CameraController.class);
    serverController.setCameraController(camController);
    logger = mock(Logger.class);
    handler = new PresetHandler(logger);
    presets.add(new Preset(new Position(0, 0), 0, 0, 0, true, 0, 0, true, 0));
    presets.add(new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0));
    presets.get(0).addTag("Piano");
    presets.get(1).addTag("Violin");
    when(database.getAllPresets()).thenReturn(presets);
    serverController.setDatabase(database);
    out = mock(OutputStream.class);
  }

  @Test
  public void testQueryByKeyWord() throws Exception {
    URI uri = new URI("http://localhost/camera/1/preset?tag=Piano");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    Camera cam = mock(Camera.class);
    camController.addCamera(cam);
    handler.handle(exchange);
    JSONObject obj = new JSONObject();
    JSONArray ar = new JSONArray();
    ar.add(presets.get(0).toJSON());
    obj.put("presets", ar);
    String expected = obj.toString();
    verify(out).write(expected.getBytes());
    //System.out.println(out.toString());
  }

}
