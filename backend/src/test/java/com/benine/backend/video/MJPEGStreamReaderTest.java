package com.benine.backend.video;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MJPEGStreamReaderTest {

  private Stream stream = mock(Stream.class);
  private MJPEGStreamReader mjpegstream;
  private byte[] firstframe;
  private byte[] secondframe;

  @Before
  public void init() {
    try {
      when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg.mjpg")));
      mjpegstream = new MJPEGStreamReader(stream);

      firstframe = IOUtils.toByteArray(new FileInputStream("resources" + File.separator + "test" + File.separator + "firstframe.jpg"));
      secondframe = IOUtils.toByteArray(new FileInputStream("resources" + File.separator + "test" + File.separator + "secondframe.jpg"));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testInitialSnapshotCorrect() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(ImageIO.read(new ByteArrayInputStream(firstframe)), "jpg", baos);
    baos.flush();
    byte[] expected = baos.toByteArray();
    baos.close();

    baos = new ByteArrayOutputStream();
    ImageIO.write(mjpegstream.getSnapShot(), "jpg", baos);
    baos.flush();
    byte[] actual = baos.toByteArray();
    baos.close();

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void testSecondSnapshotCorrect() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(ImageIO.read(new ByteArrayInputStream(secondframe)), "jpg", baos);
    baos.flush();
    byte[] expected = baos.toByteArray();
    baos.close();

    // Check if second frame is correct.
    // First frame is read on construction.
    mjpegstream.processStream();

    baos = new ByteArrayOutputStream();
    ImageIO.write(mjpegstream.getSnapShot(), "jpg", baos);
    baos.flush();
    byte[] actual = baos.toByteArray();
    baos.close();

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void testFirstIncorrectHeader() throws IOException {
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg_incorrect.mjpg")));
    mjpegstream = new MJPEGStreamReader(stream);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(ImageIO.read(new ByteArrayInputStream(firstframe)), "jpg", baos);
    baos.flush();
    byte[] expected = baos.toByteArray();
    baos.close();

    baos = new ByteArrayOutputStream();
    ImageIO.write(mjpegstream.getSnapShot(), "jpg", baos);
    baos.flush();
    byte[] actual = baos.toByteArray();
    baos.close();

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void testFirstNoHeaderNextImage() throws IOException {
    when(stream.getInputStream()).thenReturn(new BufferedInputStream(new FileInputStream("resources" + File.separator + "test" + File.separator + "testmjpeg_incorrect.mjpg")));
    mjpegstream = new MJPEGStreamReader(stream);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(ImageIO.read(new ByteArrayInputStream(secondframe)), "jpg", baos);
    baos.flush();
    byte[] expected = baos.toByteArray();
    baos.close();

    mjpegstream.processStream();

    baos = new ByteArrayOutputStream();
    ImageIO.write(mjpegstream.getSnapShot(), "jpg", baos);
    baos.flush();
    byte[] actual = baos.toByteArray();
    baos.close();

    Assert.assertArrayEquals(expected, actual);
  }

}

