package com.benine.backend.video;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * StreamReader for Motion JPEG streams.
 */
public class MJPEGStreamReader extends StreamReader {

  private BufferedInputStream bufferedStream;

  private String boundary;

  private VideoFrame snapshot;

  /**
   * Creates a new MJPEGStreamReader.
   *
   * @param url The url fo the stream.
   * @throws IOException if the inputstream cannot be read.
   */
  public MJPEGStreamReader(String url) throws IOException {
      this(new Stream(url));
  }

  /**
   * Creates a new MJPEGStreamReader.
   *
   * @param stream A stream object.
   */
  public MJPEGStreamReader(Stream stream) {
    this.bufferedStream = new BufferedInputStream(stream.getInputStream());
    this.snapshot = null;

    setMJPEGBoundary();
    processStream();
  }

  @Override
  public void run() {
    while (!Thread.interrupted()) {
      processStream();
    }
  }

  /**
   * Sets the MJPEG boundary by parsing the first header.
   */
  private void setMJPEGBoundary() {
    try {
      this.boundary = new MJPEGFrameHeader(getHeader()).getBoundary();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Processes a stream by fetching an image
   * from the stream and updating the snapshot if possible.
   */
  public void processStream() {
    try {
      MJPEGFrameHeader header = new MJPEGFrameHeader(getHeader());
      VideoFrame frame = new VideoFrame(header, getImage(header));

      sendToDistributers(frame);
      snapshot = frame;

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Notify the observers about the header and the image.
   * @param frame The VideoFrame to send to the distributers.
   */
  private void sendToDistributers(VideoFrame frame) {
    setChanged();
    notifyObservers(frame);
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
  private boolean isJPEGHeader() throws IOException {
    int[] byteArray = checkNextBytes(2);
    return byteArray[0] == 255 && byteArray[1] == 216;
  }

  /**
   * Checks the next 2 bytes and checks if they are the JPEG trailer (FF D9).
   *
   * @return true if trailer, false if not trailer.
   * @throws IOException when the next bytes cannot be read from the stream.
   */
  private boolean isJPEGTrailer() throws IOException {
    int[] byteArray = checkNextBytes(2);
    return byteArray[0] == 255 && byteArray[1] == 217;
  }

  /**
   * Fetches the header and gets the jpeg file according to the content length.
   * @param   header A string representation of the header belonging to the image.
   * @return  a byte[] representing the image.
   * @throws IOException when an error occurs fetching the header or reading the jpeg image.
   */
  private byte[] getImage(MJPEGFrameHeader header) throws IOException {
    int contentLength = header.getContentlength();

    if (contentLength != -1) {
      return readJPEG(contentLength);
    } else {
      return readJPEG();
    }
  }

  /**
   * Reads JPEG bytes if the content length is known.
   * This is more efficent than looking for the trailer.
   * @param contentLength Amount of bytes to read.
   * @return A byte[] containing the jpeg bytes.
   * @throws IOException If the bufferedstream cannot be read.
   */
  private byte[] readJPEG(int contentLength) throws IOException {
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
   * Reads JPEG bytes by reading until the JPEGTrailer is found.
   * @return A byte[] containing the jpeg bytes.
   * @throws IOException If the bufferedstream cannot be read.
   */
  private byte[] readJPEG() throws IOException {
    ByteArrayOutputStream jpeg = new ByteArrayOutputStream();

    while (!isJPEGTrailer()) {
      // If stream has not ended, add to jpeg stream.
      if (bufferedStream.available() != 0) {
        jpeg.write(bufferedStream.read());
      }
    }

    jpeg.close();
    return jpeg.toByteArray();
  }

  /**
   * Returns a byte representation of the header.
   *
   * @return Byte representation of the header.
   * @throws IOException if the header cannot be read from the buffered stream.
   */
  private byte[] getHeader() throws IOException {
    ByteArrayOutputStream header = new ByteArrayOutputStream(128);

    while (!isJPEGHeader()) {
      // If stream has not ended, add to header stream.
      if (bufferedStream.available() != 0) {
        header.write(bufferedStream.read());
      }
    }

    header.close();
    return header.toByteArray();
  }

  /**
   * Returns the MJPEG boundary.
   * @return a boundary of preferably of format '--[BOUNDARY]'
   */
  public String getBoundary() {
    return boundary;
  }

  @Override
  public VideoFrame getSnapShot() throws IOException {
    return this.snapshot;
  }
}

