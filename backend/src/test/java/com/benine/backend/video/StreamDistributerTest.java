package com.benine.backend.video;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.PipedOutputStream;

import static org.mockito.Mockito.mock;

public class StreamDistributerTest {

  private Stream stream;
  private StreamReader reader;
  private StreamDistributer distributer;
  private PipedOutputStream testStream;

  @Before
  public void init() throws IOException {
    reader = mock(StreamReader.class);
    stream = new Stream(new File("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg").toURI().toURL().toString());

    testStream = mock(PipedOutputStream.class);
    distributer = new StreamDistributer(reader);
    distributer.setStream(testStream);
  }

  @Test
  public void testConstructor() throws IOException {
    Mockito.verify(reader).addObserver(distributer);
  }

  @Test
  public void testUpdate() throws IOException {
    byte[] image = {1, 2, 3, 4};
    VideoFrame test = new VideoFrame(new MJPEGFrameHeader(image), image);
    distributer.update(reader, test);

    Mockito.verify(testStream).write(image);
  }

  @Test
  public void testDeregister() throws IOException {
    distributer.deregister();
    Mockito.verify(reader).deleteObserver(distributer);
  }
}
