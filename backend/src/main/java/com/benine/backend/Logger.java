package com.benine.backend;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dorian on 2-5-16.
 */
public class Logger {

  private LogWriter writer;

  /**
   * Creates a new logger object and logs to given output.
   * @param writer the logwriter
   */
  public Logger(LogWriter writer) {
    this.writer = writer;
  }
  /**
   * Creates a new Logger object with standard logwriter.
   * @throws IOException if the default log file is for some reason tot accesible
   */
  public Logger() throws IOException {
    this.writer = new LogWriter(Main.getConfig().getValue("standardloglocation"));
  }
}
