package com.benine.backend.http;

import com.benine.backend.Logger;
import com.benine.backend.camera.CameraController;
import com.benine.backend.camera.ipcameracontrol.IPCamera;
import com.sun.net.httpserver.HttpServer;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by dorian on 10-5-16.
 */

public class httpControllerTest {

  Logger logger = mock(Logger.class);
  CameraController cameraController = mock(CameraController.class);


  @Before
  public void init() {

  }


    //TODO add test

}
