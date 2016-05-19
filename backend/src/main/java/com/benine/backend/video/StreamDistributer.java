package com.benine.backend.video;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class can distribute a stream read by StreamReader.
 */
public class StreamDistributer implements Observer {

  /**
   * The outputstream to write to the HTTP endpoint.
   */
  private PipedOutputStream outputStream;

  private Observable reader;

  public StreamDistributer(StreamReader reader) {
    outputStream = new PipedOutputStream();
    this.reader = reader;
    reader.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof byte[]) {
      try {
        outputStream.write((byte[]) arg);
      } catch (IOException e) {
        deregister();
      }
    }
  }

  /**
   * Returns the output stream.
   * @return  The output stream bytes which can be sent by an HTTP endpoint.
   */
  public PipedOutputStream getStream() {
    return outputStream;
  }

  public void deregister() {
    reader.deleteObserver(this);
  }

}
