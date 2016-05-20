package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.ServerController;
import com.benine.backend.camera.*;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class HttpControllerTest {

  private CameraController cameraController;
  private ServerController serverController = mock(ServerController.class);
  private HttpController controller;
  private HttpServer mockserver;

  @Before
  public void setUp() throws IOException {
    ServerController.setConfigPath("resources" + File.separator + "configs" + File.separator + "maintest.conf");
    serverController = ServerController.getInstance();
    
    mockserver = mock(HttpServer.class);
    cameraController = new CameraController();
    serverController.setCameraController(cameraController);
  }

  private void setUpCamera(Camera cam) throws IOException {
    cameraController.addCamera(cam);
    controller = new HttpController(mockserver);

  }

  @After
  public void tearDown() throws IOException {
    controller.destroy();
  }

  @Test
  public void testCreateFocusHandler() throws IOException {
    FocussingCamera cam = mock(FocussingCamera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/focus"), any());
  }

  @Test
  public void testCreateIrisHandler() throws IOException {
    IrisCamera cam =  mock(IrisCamera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/iris"), any());
  }

  @Test
  public void testCreateMovingHandler() throws IOException {
    MovingCamera cam =  mock(MovingCamera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/move"), any());
  }

  @Test
  public void testCreateZoomHandler() throws IOException {
    ZoomingCamera cam = mock(ZoomingCamera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/zoom"), any());
  }

  @Test
  public void testCreateStaticHandler() throws IOException {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);

    Mockito.verify(mockserver).createContext(eq("/static"), any());
  }

  @Test
  public void testCreatePresetHandler() throws IOException {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);

    Mockito.verify(mockserver).createContext(eq("/presets/"), any());
  }

  @Test
  public void testCreateRecallPresetHandler() throws IOException  {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);
    Mockito.verify(mockserver).createContext(eq("/presets/recallpreset"), any());
  }

  @Test
  public void testCreateCreatePresetHandler() throws IOException {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/presets/createpreset"), any());
  }

  @Test
  public void testDestroy() {
    mockserver = mock(HttpServer.class);
    controller = new HttpController(mockserver);

    controller.destroy();
    Mockito.verify(mockserver).stop(0);
  }

}
