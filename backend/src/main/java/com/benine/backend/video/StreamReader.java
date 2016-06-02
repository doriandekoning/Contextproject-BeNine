package com.benine.backend.video;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Observable;

public abstract class StreamReader extends Observable implements Runnable {

  private BufferedInputStream bufferedStream;

  private Stream stream;

  /**
   * Constructor for a new StreamReader object.
   * @param stream The stream to create the streamreader for.
   * @throws IOException if there is an error getting the stream, rendering the object useless.
   */
  public StreamReader(Stream stream) throws IOException {
    this.stream = stream;
    this.bufferedStream = new BufferedInputStream(stream.getInputStream());
  }

  /**
   * Should return a valid image snapshot.
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from a buffer.
   */
  @Override
  public void run() {
    while (!Thread.interrupted()) {
      processStream();
    }
  }

  /**
   * Processes the stream.
   */
  public abstract void processStream();

  public BufferedInputStream getBufferedStream() {
    return this.bufferedStream;
  }

  public void setBufferedStream(BufferedInputStream bufferedStream) {
    this.bufferedStream = bufferedStream;
  }

  /**
   * Returns the stream to which this streamreader is connected.
   * @return  a Stream object.
   */
  public Stream getStream() {
    return this.stream;
  }

  /**
   * Returns a videoframe snapshot.
   * @return  A videoframe object representing the snapshot.
   * @throws IOException  If the snapshot cannot be fetched.
   */
  public abstract VideoFrame getSnapShot() throws IOException;
}
