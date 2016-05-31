package com.benine.backend.video;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Observable;

public abstract class StreamReader extends Observable implements Runnable {

  private boolean streamDisconnected;

  private BufferedInputStream bufferedStream;

  private Stream stream;

  /**
   * Constructor for a new StreamReader object.
   */
  public StreamReader(Stream stream) {
    this.stream = stream;
    this.streamDisconnected = false;
    this.bufferedStream = new BufferedInputStream(stream.getInputStream());
  }

  /**
   * Should return a valid image snapshot.
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from a buffer.
   */
  public abstract BufferedImage getSnapShot() throws IOException;

  /**
   * Returns true if the stream is disconnected, false otherwise.
   * @return  A boolean representing disconnected or note.
   */
  public boolean isStreamDisconnected() {
    return streamDisconnected;
  }

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

  /**
   * Returns the stream to which this streamreader is connected.
   * @return  a Stream object.
   */
  public Stream getStream() {
    return this.stream;
  }

}
