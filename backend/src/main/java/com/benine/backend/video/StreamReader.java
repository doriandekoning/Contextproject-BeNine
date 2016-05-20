package com.benine.backend.video;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import javax.imageio.ImageIO;

public abstract class StreamReader extends Observable implements Runnable {

  private byte[] snapShot;

  /**
   * Should return a valid image snapshot.
   * @return a BufferedImage containing the image.
   * @throws IOException when the image cannot be read from a buffer.
   */
  public BufferedImage getSnapShot() throws IOException {
    return ImageIO.read(new ByteArrayInputStream(this.snapShot));
  }

  /**
   * Returns the snapshot frame in byte array form.
   * @return  A byte array containing header + jpeg.
   */
  public byte[] getSnapShotBytes() {
    return Arrays.copyOf(snapShot, snapShot.length);
  }

  /**
   * Sets the current latest snapshot image.
   * @param newSnapshot new SnapShot byte array.
   */
  public void setSnapShot(byte[] newSnapshot) {
    this.snapShot = Arrays.copyOf(newSnapshot, newSnapshot.length);
  }

}
