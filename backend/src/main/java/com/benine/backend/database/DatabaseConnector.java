package com.benine.backend.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.ibatis.common.jdbc.ScriptRunner;

public class DatabaseConnector {

	/**
	 * Connect with the database.
	 */
	public static void main(String[] arg) {

//		try {
//			String line;
//			Process p = Runtime.getRuntime().exec
//					("mysql -u root -proot");
//			Process p1 = Runtime.getRuntime().exec
//					("backend/database/databasefile.sql");
//			BufferedReader input =
//					new BufferedReader
//							(new InputStreamReader(p.getInputStream()));
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//			input.close();
//		}
//		catch (Exception err) {
//			err.printStackTrace();
//		}
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
			if(connection != null) {
				System.out.println("Connected to database!");
			}
			try {
				// Initialize object for ScripRunner
				ScriptRunner sr = new ScriptRunner(connection, false, false);

				// Give the input file to Reader
				Reader reader = new BufferedReader(
						new FileReader("backend/database/databasefile.sql"));

				// Exctute script
				sr.runScript(reader);

			} catch (Exception e) {
				System.err.println("Failed to Execute"
						+ " The error is " + e.getMessage());
			}
			connection.close();
		} catch (SQLException e) {
			System.out.println("can't connect" + e);
		} catch (ClassNotFoundException e) {
			System.out.println("No driver found!");
		}

	}
}
