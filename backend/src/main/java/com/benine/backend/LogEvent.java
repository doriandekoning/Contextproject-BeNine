package com.benine.backend;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Represents an event that happens and has to be logged.
 */
public class LogEvent {
  private String time;
  private String description;
  private Exception exception;
  private Type type;

  public enum Type {

    /**
     * Critical, program crashes or gets in an unrecoverable state.
     */
    CRITICAL(1),
    /**
     * Warns about an event that could possibly cause malfunction of the software.
     */
    WARNING(2),
    /**
     * Info about general events that happen in the system.
     */
    INFO(3),
    /**
     * Debug, used for debugging, more detailled info about events in the system.
     */
    DEBUG(4),
    /**
     * Trace logs everything that happens in the system
     * and could possibly be of any interest when debugging.
     */
    TRACE(5);

    private final int level;
    
    /**
     * Type of this log event.
     * @param level log level.
     */
    Type(int level) {
      this.level = level;
    }

    /**
     * Returns the value.
     * @return int representation of the level.
     */
    public int getValue() {
      return level;
    }

  }

  /**
   * Creates a new logEvent.
   * @param time time the event happened
   * @param description description of the event
   * @param type indicating the log level
   * @param except the associated exception
   */
  public LogEvent(String time, String description, Type type, Exception except) {
    this.time = time;
    this.description = description;
    this.type = type;
    this.exception = except;
  }

  /**
   * Creates a new logEvent.
   * @param time time the event happened
   * @param description description of the event
   * @param type indicating the log level
   */
  public LogEvent(String time, String description, Type type) {
    this(time, description, type, null);
  }

  /**
   * Creates a new Logevent with the default level (INFO).
   * @param time time the event happened
   * @param description description of the event
   */
  public LogEvent(String time, String description) {
    this(time, description, Type.INFO, null);
  }

  /**
   * Creates a string representation of this LogEvent.
   * @return String representation of this object.
   */
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append('[');
    builder.append(this.type);
    builder.append('|');
    builder.append(this.time);
    builder.append(']');
    builder.append(this.description);
    if (this.exception != null) {
      builder.append(", ");
      StackTraceElement[] stacktrace = exception.getStackTrace();
      for (StackTraceElement e : stacktrace) {
        builder.append(e.toString());
        builder.append(System.getProperty("line.separator"));
        builder.append("     ");
      }
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      exception.printStackTrace(pw);
      builder.append(sw.toString());
    }
    return builder.toString();
  }

  public Type getType() {
    return this.type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((exception == null) ? 0 : exception.hashCode());
    result = prime * result + ((time == null) ? 0 : time.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof LogEvent) {
      LogEvent that = (LogEvent)other;
      return (this.exception == null
                && that.exception == null
                || that.exception != null
                && that.exception.equals(this.exception)
              )
              && that.type.equals(this.type)
              && that.time.equals(this.time)
              && that.description.equals(this.description);
    }
    return false;
  } 
}
