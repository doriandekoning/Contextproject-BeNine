package com.benine.backend.http;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;

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
  private OutputStream out;
  private Database database = mock(Database.class);
  HttpExchange exchange = mock(HttpExchange.class);
  
  @Before
  public void setUp() throws CameraConnectionException{
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    serverController.setDatabase(database);
    handler = new PresetHandler();  
    
    out = mock(OutputStream.class);
  }
  
  @Test
  public void testFailingPresetRequest() throws Exception {   
    URI uri = new  URI("http://localhost/camera/1/public/test.jpg");
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);
    handler.handle(exchange);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("presets", new JSONArray());
    String expected = jsonObject.toString();
    verify(out).write(expected.getBytes());
  }
  
  @Test
  public void testDatabaseThrowingException() throws Exception {   
    URI uri = new  URI("http://localhost/camera/1/public/test.jpg");
    Database database = mock(Database.class);
    when(exchange.getRequestURI()).thenReturn(uri);
    when(exchange.getResponseBody()).thenReturn(out);

    doThrow(new SQLException("test exception")).when(database).getAllPresetsCamera(1);
    serverController.setDatabase(database);
    handler.handle(exchange);

    String expected = "{\"succes\":\"false\"}";
    verify(out).write(expected.getBytes());
  }
}
