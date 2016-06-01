package com.benine.backend.video;

/**
 * Created on 01-06-16.
 */
public class MJPEGVideoFrame {

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
  public MJPEGVideoFrame(MJPEGFrameHeader header, byte[] image) {
    this.header = header;
    this.image = image;
  }

  public MJPEGFrameHeader getHeader() {
    return this.header;
  }

  public byte[] getImage() {
    return this.image;
  }


  public byte[] getHeaderBytes() {
    return header.getString().getBytes();
  }

  public void setImage(byte[] image) {
    this.image = image;
  }
}
