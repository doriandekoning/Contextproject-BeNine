package com.benine.backend.database;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabase implements Database{
    Connection connection;
    int presetId;
    int movingPresetId;

    public MySQLDatabase() {
        connection = null;
        presetId = 0;
        movingPresetId = 0;
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    @Override
    public void addPreset(int camera, int cameraPresetNumber, DatabasePreset preset) {
        int auto = 0;
        if(preset.isAutofocus()){
            auto = 1;
        }
        try {Statement statement = connection.createStatement();
            String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan() + "," + preset.getTilt() +
                    "," + preset.getZoom() + "," + preset.getFocus() + "," + preset.getIris() +"," + auto + ")";
            ResultSet resultset = statement.executeQuery(sql);
            sql = "INSERT INTO presetsdatabase.camerapresets VALUES(" + cameraPresetNumber + "," + camera + "," + presetId + ")";
            presetId++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deletePreset(int camera, int cameraPresetNumber) {
        try {Statement statement = connection.createStatement();
            String sql = "DELETE FROM presetsdatabase.camerapresets WHERE Camera_ID = " + camera + " AND CameraPresetID = " + cameraPresetNumber;
            ResultSet resultset = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePreset(int camera, int cameraPresetNumber, DatabasePreset preset) {
        int auto = 0;
        if(preset.isAutofocus()){
            auto = 1;
        }
        try {Statement statement = connection.createStatement();
            String sql = "INSERT INTO presetsdatabase.presets VALUES(" + presetId + "," + preset.getPan() + "," + preset.getTilt() +
                    "," + preset.getZoom() + "," + preset.getFocus() + "," + preset.getIris() +"," + auto + ")";
            ResultSet resultset = statement.executeQuery(sql);
            sql = "UPDATE presetsdatabase.camerapresets SET Presets_ID = " + presetId + "WHERE Camera_ID = " + camera +
                    "AND CameraPresetID = " + cameraPresetNumber;
            presetId++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DatabasePreset getPreset(int camera, int cameraPresetNumber) {
        DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID" +
                    "WHERE camerapresets.Camera_ID = " + camera + " AND camerapresets.CameraPresetID = "+ cameraPresetNumber;
            ResultSet resultset = statement.executeQuery(sql);
            if(resultset.next()){
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preset;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresets() {
        ArrayList<DatabasePreset> list = new ArrayList<DatabasePreset>();
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID";
            ResultSet resultset = statement.executeQuery(sql);
            while(resultset.next()){
                DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
                list.add(preset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<DatabasePreset> getAllPresetsCamera(int cameraID) {
        ArrayList<DatabasePreset> list = new ArrayList<DatabasePreset>();
        try {Statement statement = connection.createStatement();
            String sql = "SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN camerapresets ON camerapresets.Preset_ID = presets.ID" +
                    "WHERE camerapresets.Camera_ID = " + cameraID;
            ResultSet resultset = statement.executeQuery(sql);
            while(resultset.next()){
                DatabasePreset preset = new DatabasePreset(0, 0, 0, 0, 0, false);
                preset.setPan(resultset.getInt("pan"));
                preset.setTilt(resultset.getInt("tilt"));
                preset.setZoom(resultset.getInt("zoom"));
                preset.setFocus(resultset.getInt("focus"));
                preset.setIris(resultset.getInt("iris"));
                preset.setAutofocus(resultset.getInt("autofocus") == 1);
                list.add(preset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
