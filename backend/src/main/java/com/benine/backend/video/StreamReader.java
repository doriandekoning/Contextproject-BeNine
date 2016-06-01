package com.benine.backend.video;

import java.io.IOException;
import java.util.Observable;

public abstract class StreamReader extends Observable implements Runnable {

  /**
   * Should return a valid image snapshot.
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from a buffer.
   */
  public abstract VideoFrame getSnapShot() throws IOException;
}
