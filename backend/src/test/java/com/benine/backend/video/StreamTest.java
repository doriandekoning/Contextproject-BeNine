package com.benine.backend.video;

import org.junit.Before;

import java.io.File;
import java.io.IOException;

public class StreamTest {

  private Stream stream;

  @Before
  public void init() throws IOException {
    stream = new Stream(new File("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg").toURI().toURL().toString());
  }
}
