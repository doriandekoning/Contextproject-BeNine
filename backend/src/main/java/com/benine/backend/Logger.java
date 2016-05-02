package com.benine.backend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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

  /**
   * Logs item.
   * @param time the time to register the logevent at.
   * @param message the description of the logevent
   * @param level An int specifying the level of the logevent.
   */
  public void log(String time, String message, int level) {
    LogEvent event = new LogEvent(time, message, LogEvent.Type.values()[level]);
    System.out.println(event.toString());
    writer.write(event);
  }

  /**
   * Logs item.
   * @param time the time to register the logevent at.
   * @param message the description of the logevent
   * @param type The type of the logevent.
   */
  public void log(String time, String message, LogEvent.Type type) {
    LogEvent event = new LogEvent(time, message, type);
    System.out.println(event.toString());
    writer.write(event);
  }
  /**
   * Logs item at current time
   * @param message the description of the logevent
   * @param type The type of the logevent.
   */
  public void log(String message, LogEvent.Type type) {
    LogEvent event = new LogEvent(new Date().toString(), message, type);
    System.out.println(event.toString());
    writer.write(event);
  }
}
