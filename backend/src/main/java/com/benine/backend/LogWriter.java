package com.benine.backend;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to write to the log file.
 */
public class LogWriter {

  //TODO get this from config
  private int maxLogBufferSize = 25;

  private int minLogLevel = 4;

  private String logLocation;

  private PrintWriter writer;

  private ArrayList<LogEvent> buffer = new ArrayList<LogEvent>();

  /**
   * Creates a new LogWriter, should be deleted by calling the destoy method.
   * @param logLocation Writes to a file
   * @throws IOException Throws an IOException when the loglocation is available.
   */
  public LogWriter(String logLocation) throws IOException {
    this.logLocation = logLocation;
    writer = new PrintWriter(new FileWriter(logLocation));
  }
  /**
   * Writes LogEvent to file.
   */
  public void write(LogEvent event) throws IOException {
    if(event.getType().getValue()>minLogLevel) {
      return;
    }
    // If the log level is high write the buffer and write this event immidiately
    // because this might indicate a crash (soon).
    if (event.getType().getValue() < 3) {
      flush();
      writer.write(event.toString() + "\n");
    }else {
      buffer.add(event);
      if (buffer.size() > maxLogBufferSize) {
        flush();
      }
    }
  }
  /**
   * Writes a list of logEvents to the list.
   */
  public void write(List<LogEvent> eventList) throws IOException {
    for(LogEvent e : eventList) {
      write(e);
    }
  }
  /**
   * Flushes the buffer of this logwriter.
   */
  public void flush() {
    while (!buffer.isEmpty()) {
      LogEvent event = buffer.get(0);
      writer.write(event.toString()+ "\n");
      buffer.remove(0);
    }
    writer.flush();
  }
  /**
   * Sets the minimum log level.
   */
  public void setMinLogLevel(int newMinLevel) {
    this.minLogLevel = newMinLevel;
  }
  /**
   * Closes this Writer.
   */
  public void close() {
    flush();
  }

}
