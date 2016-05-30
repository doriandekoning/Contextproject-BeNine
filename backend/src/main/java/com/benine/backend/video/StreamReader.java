package com.benine.backend.video;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;

public abstract class StreamReader extends Observable implements Runnable {

  private boolean streamDisconnected;

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


}
