package com.benine.backend.http.jetty;

import com.benine.backend.http.CameraInfoHandler;
import com.benine.backend.http.CameraRequestHandler;

/**
 * Created on 22-05-16.
 */
public class CameraInfoHandlerTest extends CameraRequestHandlerTest {

  @Override
  public CameraRequestHandler supplyHandler() {
    return new CameraInfoHandler();
  }

}
