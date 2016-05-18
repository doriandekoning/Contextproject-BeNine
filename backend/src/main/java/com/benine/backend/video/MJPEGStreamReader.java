package com.benine.backend.video;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class MJPEGStreamReader implements Runnable {

  private BufferedInputStream bufferedStream;
  private byte[] snapShot;

  /**
   * Creates a new MJPEGStreamReader.
   *
   * @param url The url fo the stream.
   * @throws IOException if the inputstream cannot be read.
   */
  public MJPEGStreamReader(URL url) throws IOException {
    this(new Stream(url));
  }

  /**
   * Creates a new MJPEGStreamReader.
   *
   * @param stream A stream object.
   * @throws IOException If the inputstream cannot be read.
   */
  public MJPEGStreamReader(Stream stream) throws IOException {
    this.bufferedStream = new BufferedInputStream(stream.getInputStream());
    this.snapShot = getImage();
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      processStream();
    }
  }

  /**
   * Processes a stream by fetching an image
   * from the stream and updating the latest snapshot.
   */
  public void processStream() {
    try {
      snapShot = getImage();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Peeks into the next amount of bytes and returns them.
   *
   * @param amount The amount of bytes to check.
   * @return An int[] array containing the checked bytes in order.
   * @throws IOException when the next bytes cannot be read from the stream.
   */
  private int[] checkNextBytes(int amount) throws IOException {
    int[] byteArray = new int[amount];

    bufferedStream.mark(amount);
    for (int i = 0; i < amount; i++) {
      byteArray[i] = bufferedStream.read();
    }
    bufferedStream.reset();

    return byteArray;
  }

  /**
   * Checks the next 2 bytes and checks if they are the JPEG header (FF D8).
   *
   * @return true if header, false if not header.
   * @throws IOException when the next bytes cannot be read from the stream.
   */
  private boolean checkJPEGHeader() throws IOException {
    int[] byteArray = checkNextBytes(2);
    return (byteArray[0] == 255 && byteArray[1] == 216);
  }

  /**
   * Fetches the header and gets the jpeg file according to the content length.
   * @return  a byte[] representing the image.
   * @throws IOException when an error occurs fetching the header or reading the jpeg image.
   */
  private byte[] getImage() throws IOException {
    // Fetch the header and get the content length.
    String header = getHeader();
    int contentLength = getContentLength(header);

    // Now use the content length to extract the jpeg.
    byte[] image = new byte[contentLength];

    int offset = 0;
    int readByte;

    for (int i = 0; i < contentLength; i++) {
      readByte = bufferedStream.read(image, offset, contentLength - offset);
      offset += readByte;
    }

    return image;
  }

  /**
   * Returns a string representation of the header.
   *
   * @return String representation of the header.
   * @throws IOException if the header cannot be read from the buffered stream.
   */
  private String getHeader() throws IOException {
    StringWriter header = new StringWriter(128);

    while (!checkJPEGHeader()) {
      header.write(bufferedStream.read());
    }

    return header.toString();
  }

  /**
   * Looks for the Content-Length: tag in the header, and extracts the value.
   *
   * @param header A header string.
   * @return -1 if content-length not found, else content length.
   */
  private int getContentLength(String header) {
    Pattern contentLength = Pattern.compile("Content-Length: \\d+");
    Matcher matcher = contentLength.matcher(header);

    // On a match, remove all non-digits and parse it to an integer.
    if (matcher.find()) {
      return Integer.parseInt(matcher.group().replaceAll("[^0-9]", ""));
    } else {
      return -1;
    }
  }

  /**
   * Changes the byte[] snapshot into a bufferedimage which can be written to file.
   *
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from the byte[] image.
   */
  public BufferedImage getSnapShot() throws IOException {
    return ImageIO.read(new ByteArrayInputStream(this.snapShot));
  }
}