package com.benine.backend.video;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Arrays;

public class StreamTest {

  private Stream stream;

  @Before
  public void init() throws MalformedURLException {
    stream = new Stream(new File("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg").toURI().toURL().toString());
  }

  @Test
  public void testStream() throws IOException {
    byte[] expected = IOUtils.toByteArray(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg"));
    byte[] actual = IOUtils.toByteArray(stream.getInputStream());

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void testStreamUnequal() throws IOException {
    byte[] expected = IOUtils.toByteArray(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg_incorrect.mjpg"));
    byte[] actual = IOUtils.toByteArray(stream.getInputStream());

    Assert.assertFalse(Arrays.equals(expected, actual));
  }

  @Test
  public void testStreamIncorrectStream() throws MalformedURLException {
    Stream testStream = new Stream(new File("nonexistent").toURI().toURL().toString());

    Assert.assertNull(testStream.getInputStream());
  }
}
