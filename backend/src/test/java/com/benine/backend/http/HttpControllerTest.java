package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.*;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class HttpControllerTest {

  private Logger logger = mock(Logger.class);
  private CameraController cameraController = new CameraController();
  private HttpController controller;
  private HttpServer mockserver;

  @Before
  public void setUp() throws IOException {
    mockserver = mock(HttpServer.class);
    controller = new HttpController(mockserver, logger, cameraController);
  }

  private void setUpCamera(Camera cam) throws IOException {
    cameraController.addCamera(cam);
    controller = new HttpController(mockserver, logger, cameraController);

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
  public void testCreatePresetHandler() throws IOException {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/preset"), any());
  }

  @Test
  public void testCreateRecallPresetHandler() throws IOException  {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/recallpreset"), any());
  }

  @Test
  public void testCreateCreatePresetHandler() throws IOException {
    Camera cam = mock(Camera.class);
    setUpCamera(cam);
    int camId = cam.getId();

    Mockito.verify(mockserver).createContext(eq("/camera/" + camId + "/createpreset"), any());
  }

  @Test
  public void testDestroy() {
    mockserver = mock(HttpServer.class);
    controller = new HttpController(mockserver, logger, cameraController);

    controller.destroy();
    Mockito.verify(mockserver).stop(0);
  }

}
