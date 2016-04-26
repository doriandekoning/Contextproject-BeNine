package com.benine.backend;

/**
 * Represents an event that happens and has to be logged.
 */
public class LogEvent {
  private String time, description;
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
     * Trace logs everything that happens in the system and could possibly be of any interest when debugging.
     */
    TRACE(5);

    private final int level;

    Type(int level) {
      this.level = level;
    }

    public int getValue() {
      return level;
    }

  }
  /**
   * Creates a new logEvent
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
   * Creates a new logEvent
   * @param time time the event happened
   * @param description description of the event
   * @param type indicating the log level
   */
  public LogEvent(String time, String description, Type type) {
    this(time, description, type, null);
  }
  /**
   * Creates a new Logevent with the default level (INFO)
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
      builder.append(exception);
    }
    return builder.toString();
  }
  /**
   * Getter for the type of this LogEvent.
   */
  public Type getType() {
    return this.type;
  }
  /**
   * Compares an object to this logevent, returns if both are equal.
   * @param other Object to compare to
   * @return true if this equals other, false otherwise
   */
  public boolean equals(Object other) {
    if(other instanceof LogEvent) {
      LogEvent that = (LogEvent)other;
      if(((this.exception == null
                && that.exception==null)
                || (that.exception!= null
                && that.exception.equals(this.exception))
              )
              && that.type.equals(this.type)
              && that.time.equals(this.time)
              && that.description.equals(this.description)) {
        return true;
      }
      return false;
    }
    return false;
  }
}
