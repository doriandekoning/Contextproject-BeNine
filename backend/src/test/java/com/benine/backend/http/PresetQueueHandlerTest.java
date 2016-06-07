package com.benine.backend.http;

public class PresetQueueHandlerTest extends RequestHandlerTest {

  @Override
  public RequestHandler supplyHandler() {
    return new PresetQueueHandler(httpserver);
  }

}
