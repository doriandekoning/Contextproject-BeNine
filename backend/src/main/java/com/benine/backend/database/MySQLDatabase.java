package com.benine.backend.database;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabase implements Database{
    Connection connection;

    public MySQLDatabase() {
        connection = null;
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    @Override
    public void addPreset(int camera, int cameraPresetNumber, DatabasePreset preset) {

    }

    @Override
    public void deletePreset(int camera, int cameraPresetNumber) {

    }

    @Override
    public void updatePreset(int camera, int cameraPresetNumber, DatabasePreset preset) {

    }

    @Override
    public void addMovingPreset(int camera, int cameraPresetNumber, DatabaseMovingPreset mPreset) {

    }

    @Override
    public void updateMovingPreset(int camera, int cameraPresetNumber, DatabaseMovingPreset mPreset) {

    }

    @Override
    public void deleteMovingPreset(int camera, int cameraMovingPresetNumber) {

    }

    @Override
    public DatabasePreset getPreset(int camera, int cameraPresetNumber) {
        return null;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresets() {
        return null;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresetsCamera(int cameraID) {
        return null;
    }

    @Override
    public DatabaseMovingPreset getMovingPreset(int camera, int cameraPresetNumber) {
        return null;
    }

    @Override
    public ArrayList<DatabaseMovingPreset> getAllMovingPresets() {
        return null;
    }

    @Override
    public ArrayList<DatabaseMovingPreset> getAllMovingPresetsCamera(int cameraID) {
        return null;
    }

    @Override
    public boolean connectToDatabaseServer() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
        } catch (SQLException e) {
            System.out.println("Can't connect to server with error message: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found with error message: " + e.getMessage());
        }
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkDatabase() {
        try{
            ResultSet databaseNames = connection.getMetaData().getCatalogs();
            while (databaseNames.next()) {
                String databaseName = databaseNames.getString(1);
                if(databaseName.equals("presetsDatabase")) return true;
            }
            databaseNames.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void resetDatabase() {
        try {
            ScriptRunner sr = new ScriptRunner(connection, false, false);
            Reader reader = new BufferedReader(
                    new FileReader("backend/database/databasefile.sql"));
            sr.runScript(reader);

        } catch (Exception e) {
            System.err.println("Failed to Execute"
                    + " The error is " + e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
