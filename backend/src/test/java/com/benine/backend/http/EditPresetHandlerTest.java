package com.benine.backend.http;

import java.io.IOException;

import org.junit.Before;

public class EditPresetHandlerTest extends RequestHandlerTest {

  @Override
  public RequestHandler supplyHandler() {
    return new EditPresetHandler();
  }

  
  @Before
  public void initialize() throws IOException{
    super.initialize();
  }
}
