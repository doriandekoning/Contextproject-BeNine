package com.benine.backend;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class used to write to the log file.
 */
public class LogWriter {

  private String logLocation;

  PrintWriter writer;

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
   * Closes this Writer.
   */
  public void close() {
    writer.close();
  }
  
}
