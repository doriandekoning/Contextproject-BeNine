package com.benine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	/**
	 * Connect with the database.
	 * @param argv
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void connect() throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "password");

	}
}
