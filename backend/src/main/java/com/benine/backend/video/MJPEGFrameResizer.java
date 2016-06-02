package com.benine.backend.video;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created on 01-06-16.
 */
public class MJPEGFrameResizer {

  /**
   * Width this resizer should resize to.
   */
  private int width;

  /**
   * Height this resizer should resize to.
   */
  private int height;

  /**
   * Constructor for an MJPEGFrameResizer.
   * @param width     Width to resize to.
   * @param height    Height to resize to.
   */
  public MJPEGFrameResizer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Resizes the frame, updating the content-length in the header.
   * @param frame An MJPEGVideoFrame.
   * @return The updated MJPEGVideoFrame.
   */
  public VideoFrame resize(VideoFrame frame) {
    frame.setImage(rescaleImage(frame.getImage()));
    frame.getHeader().setContentLength(frame.getImage().length);

    return frame;
  }

  /**
   * Resizes the image.
   * @param image The byte array to resize.
   * @return If an error occurs, a non-resized image, else a resized image.
   */
  private byte[] rescaleImage(byte[] image) {
    try {
      BufferedImage inputimg = ImageIO.read(new ByteArrayInputStream(image));

      // Convert input image to a faster color model.
      BufferedImage img = new BufferedImage(inputimg.getWidth(), inputimg.getHeight(),
              BufferedImage.TYPE_INT_RGB);

      img.getGraphics().drawImage(inputimg, 0, 0, null);

      BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      Graphics2D graphics = resized.createGraphics();

      graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
              RenderingHints.VALUE_INTERPOLATION_BICUBIC);

      graphics.drawImage(img, 0, 0, width, height, 0, 0, img.getWidth(), img.getHeight(), null);
      graphics.dispose();

      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      ImageIO.write(resized, "jpg", buffer);

      return buffer.toByteArray();

    } catch (IOException e) {
      return image;
    }
  }
}
