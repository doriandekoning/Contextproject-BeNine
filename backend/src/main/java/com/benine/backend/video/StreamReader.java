package com.benine.backend.video;

import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class StreamReader implements Runnable {

  /**
   * Should return a valid image snapshot.
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from a buffer.
   */
  public abstract BufferedImage getSnapShot() throws IOException;

}
