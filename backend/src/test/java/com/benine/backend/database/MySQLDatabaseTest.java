package com.benine.backend.database;

import com.benine.backend.Preset;
import com.benine.backend.camera.Position;
import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created on 4-5-2016.
 */
public class MySQLDatabaseTest extends BasicJDBCTestCaseAdapter {
  
    private Database database;
  
    @Before
    public void prepareEmptyResultSet() {
        MockConnection connection =
                getJDBCMockObjectFactory().getMockConnection();
        StatementResultSetHandler statementHandler =
                connection.getStatementResultSetHandler();
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
        database = new MySQLDatabase("root", "root");
    }

    @Test
    public final void testConnection() throws SQLException {
        database.connectToDatabaseServer();
        assertTrue(database.isConnected());
        database.closeConnection();
        verifyConnectionClosed();
    }

    @Test
    public final void testResetDatabase() {
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("CREATE SCHEMA IF NOT EXISTS `presetsDatabase`");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddPreset() throws SQLException {
        Preset preset = new Preset(new Position(1,1), 1, 1,1,true,1,1,false, 0);;
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("insert into presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeletePreset() throws SQLException {
        Preset preset = new Preset(new Position(1,1), 1, 1,1,true,1,1,false, 0);;
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.deletePreset(1);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE ID = 1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testUpdatePreset() throws SQLException {
        Preset preset = new Preset(new Position(1,1), 1, 1,1,true,1,1,false, 0);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        database.updatePreset(preset);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presets WHERE ID = 1");
        verifySQLStatementExecuted("insert into presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetAllPreset() throws SQLException {
        Preset preset = new Preset(new Position(1,1), 1, 1,1,true,1,1,false, 0);;
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(preset);
        ArrayList<Preset> result = database.getAllPresets();
        database.closeConnection();
        verifySQLStatementExecuted("SELECT id, pan, tilt, zoom, focus, iris, autofocus");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetPresetsCamera() throws SQLException {
        Preset preset = new Preset(new Position(1,1), 1, 1,1,true,1,1,false, 0);;
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1,"ip");
        database.addPreset(preset);
        ArrayList<Preset> result = database.getAllPresetsCamera(1);
        database.closeConnection();
        verifySQLStatementExecuted("SELECT id, pan, tilt, zoom, focus, iris, autofocus");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

}