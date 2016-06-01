package com.benine.backend.video;

import java.util.Observable;

/**
 * This class can distribute a stream read by StreamReader.
 */
public class ResizableStreamDistributer extends StreamDistributer {

  private MJPEGFrameResizer resizer;

  /**
   * Creates a new StreamDistributer object from a StreamReader.
   * It provides an outputstream and registrers the distributer to the reader.
   * @param reader  A StreamReader object.
   * @param width   The width to resize to.
   * @param height  The height to resize to.
   */
  public ResizableStreamDistributer(StreamReader reader, int width, int height) {
    super(reader);
    this.resizer = new MJPEGFrameResizer(width, height);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof VideoFrame) {
      VideoFrame frame = (VideoFrame) arg;

      frame = resizer.resize(frame);
      writeVideoFrame(frame);
    }
  }

}
