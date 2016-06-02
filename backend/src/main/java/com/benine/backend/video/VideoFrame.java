package com.benine.backend.video;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created on 01-06-16.
 */
public class VideoFrame {

  /**
   * Header as a byte stream.
   */
  private MJPEGFrameHeader header;

  /**
   * Image as a byte stream.
   */
  private byte[] image;

  /**
   * Constructor for a VideoFrame.
   * @param header The HTTP header.
   * @param image  The Image in bytes.
   */
  public VideoFrame(MJPEGFrameHeader header, byte[] image) {
    this.header = header;
    this.image = Arrays.copyOf(image, image.length);
  }

  public MJPEGFrameHeader getHeader() {
    return this.header;
  }

  public byte[] getImage() {
    return Arrays.copyOf(image, image.length);
  }

  public byte[] getHeaderBytes() {
    return header.getString().getBytes(StandardCharsets.UTF_8);
  }

  public void setImage(byte[] image) {
    this.image = Arrays.copyOf(image, image.length);
  }
}
