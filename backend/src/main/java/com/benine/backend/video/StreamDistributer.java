package com.benine.backend.video;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class can distribute a stream read by StreamReader.
 */
public class StreamDistributer implements Observer {

  /**
   * The outputstream to write to the HTTP endpoint.
   */
  private OutputStream outputStream;

  public StreamDistributer(StreamReader reader) {
    outputStream = new ByteOutputStream();
    reader.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof byte[]) {
      try {
        outputStream.write((byte[]) arg);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns the output stream.
   * @return  The output stream bytes which can be sent by an HTTP endpoint.
   */
  public OutputStream getStream() {
    return outputStream;
  }
}
