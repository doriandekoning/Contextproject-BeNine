package com.benine.backend.video;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class can distribute a stream read by StreamReader.
 */
public class StreamDistributer implements Observer {

  OutputStream outputStream;

  public StreamDistributer(StreamReader reader) {
    outputStream = new PipedOutputStream().;
    reader.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof StreamReader && arg instanceof byte[]) {
      StreamReader reader = (StreamReader) o;
      try {
        outputStream.write((byte[]) arg);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
