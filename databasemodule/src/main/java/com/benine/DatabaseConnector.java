package com.benine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	public static void main(String[] argv) throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "tavsan");

	}
}
