package com.benine.backend.database;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Ege on 4-5-2016.
 */
public class MySQLDatabaseTest extends BasicJDBCTestCaseAdapter {

    private void prepareEmptyResultSet()
    {
        MockConnection connection =
                getJDBCMockObjectFactory().getMockConnection();
        StatementResultSetHandler statementHandler =
                connection.getStatementResultSetHandler();
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
    }

    @Test
    public final void testConnection() throws SQLException {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        database.connectToDatabaseServer();
        assertTrue(database.isConnected());
        database.closeConnection();
        verifyConnectionClosed();
    }

    @Test
    public final void testResetDatabase() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.closeConnection();
        verifySQLStatementExecuted("CREATE DATABASE presetsDatabase");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testAddPreset() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(1,1,preset);
        database.closeConnection();
        verifySQLStatementExecuted("insert into presetsdatabase.presets");
        verifySQLStatementExecuted("insert into presetsdatabase.camerapresets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testDeletePreset() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(1,1,preset);
        database.deletePreset(1,1);
        database.closeConnection();
        verifySQLStatementExecuted("DELETE FROM presetsdatabase.camerapresets WHERE Camera_ID = 1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testUpdatePreset() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        DatabasePreset preset2 = new DatabasePreset(1,1,1,1,1,false);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(1,1,preset);
        database.updatePreset(1,1, preset2);
        database.closeConnection();
        verifySQLStatementExecuted("UPDATE presetsdatabase.camerapresets");
        verifySQLStatementExecuted("WHERE Camera_ID = 1");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetPreset() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1,1,"name");
        database.addPreset(1,1,preset);
        DatabasePreset result = database.getPreset(1,1);
        database.closeConnection();
        verifySQLStatementExecuted("SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetAllPreset() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addPreset(1,1,preset);
        ArrayList<DatabasePreset> result = database.getAllPresets();
        database.closeConnection();
        verifySQLStatementExecuted("SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets JOIN");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testGetPresetsCamera() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        DatabasePreset preset = new DatabasePreset(1,1,1,1,1,true);
        database.connectToDatabaseServer();
        database.resetDatabase();
        database.addCamera(1,1,"name");
        database.addPreset(1,1,preset);
        ArrayList<DatabasePreset> result = database.getAllPresetsCamera(1);
        database.closeConnection();
        verifySQLStatementExecuted("SELECT pan, tilt, zoom, focus, iris, autofocus FROM presetsDatabase.presets");
        verifyCommitted();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

    @Test
    public final void testCheckDatabase() {
        prepareEmptyResultSet();
        Database database = new MySQLDatabase();
        database.connectToDatabaseServer();
        assertFalse(database.checkDatabase());
        database.resetDatabase();
        assertFalse(database.checkDatabase());
        database.closeConnection();
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }

}