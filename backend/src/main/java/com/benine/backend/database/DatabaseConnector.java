package com.benine.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

  /**
   * Connect with the database.
   * @throws SQLException thrown when sql query fails.
   * @throws ClassNotFoundException when class is not found.
   */
  public void connect() throws SQLException, ClassNotFoundException {

    Class.forName("com.mysql.jdbc.Driver");
    Connection connection = DriverManager
                 .getConnection("jdbc:mysql://localhost:3306/", "root", "password");
      
  }
}
