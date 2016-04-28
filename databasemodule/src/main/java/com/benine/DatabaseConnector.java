package com.benine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	/**
	 * Connect with the database.
	 * @param argv
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void connect(String[] argv) throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/contextprojectdatabase", "root", "password");

	}
}
