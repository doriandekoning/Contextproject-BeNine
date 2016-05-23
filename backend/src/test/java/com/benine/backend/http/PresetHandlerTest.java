package com.benine.backend.http;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import com.benine.backend.Preset;
import com.benine.backend.PresetController;
import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.Position;
import com.benine.backend.database.Database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraConnectionException;
import com.sun.net.httpserver.HttpExchange;

public class PresetHandlerTest {
  
  private ServerController serverController;
  private PresetHandler handler;
  private OutputStream out;
  private Database database = mock(Database.class);
  HttpExchange exchange = mock(HttpExchange.class);
  ArrayList<Preset> presets = new ArrayList<Preset>();
  
  @Before
  public void setUp() throws CameraConnectionException{
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setDatabase(database);
    handler = new PresetHandler();

    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    handler = new PresetHandler();
    presets.add(new Preset(new Position(0, 0), 0, 0, 0, true, 0, 0, true, 0));
    presets.get(0).addTag("Piano");
    PresetController presetcontroller = mock(PresetController.class);
    when(presetcontroller.getPresetsByTag("Piano")).thenReturn(presets); 
    serverController.setPresetController(presetcontroller);
    
    serverController.setDatabase(database);
    handler = new PresetHandler();  
    
    out = mock(OutputStream.class);
  }

  @Test
  public void testQueryByKeyWord() throws Exception {
    URI uri = new URI("http://localhost/camera/1/preset?tag=Piano");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    handler.handle(exchange);
    JSONObject obj = new JSONObject();
    JSONArray ar = new JSONArray();
    ar.add(presets.get(0).toJSON());
    obj.put("presets", ar);
    String expected = obj.toString();
    verify(out).write(expected.getBytes());
  }

}
