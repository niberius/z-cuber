package org.zoltor.common;

import java.sql.*;
import java.util.*;

import static org.zoltor.common.Config.*;

/**
 * Created by zoltor on 22.10.14.
 */
public class DataBase {

    private static DataBase instance;
    private Connection connection;

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    private DataBase() {
        // protect from instantiating
    }

    /**
     * Get associative list with column names and values from database
     * @param sqlScript Sql script for prepared statements
     * @param args Arguments to scripts
     * @return
     */
    public List<Map<String, String>> get(String sqlScript, Object... args) throws SQLException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        ResultSet queryResult = (ResultSet)doPreparedStatement(sqlScript, true, args);
        if (queryResult != null) {
            try {
                ResultSetMetaData meta = queryResult.getMetaData();
                int colsCount = meta.getColumnCount();
                while (queryResult.next()){
                    Map<String, String> tmpMap = new HashMap<String, String>();
                    for (int i = 1; i <= colsCount; i++) {
                        tmpMap.put(meta.getColumnName(i), queryResult.getString(i));
                    }
                    result.add(tmpMap);
                }
            } catch (SQLException e) {
                logger.error("Can't get metadata of table");
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Prepare INSERT / DELETE / UPDATE statement and execute it
     * @param sqlScript Sql script for prepared statements
     * @param args Arguments to scripts
     * @return 0 - no affected rows
     *         -1 - error at script execution
     *         other int - number of affected rows
     */
    public int update(String sqlScript, Object... args) throws SQLException {
        return (Integer) doPreparedStatement(sqlScript, false, args);
    }

    ////////////////////
    // Private methods
    ////////////////////

    /**
     * Refresh connection or establish new.
     */
    private void reconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DB_DRIVER_NAME);
                this.connection = DriverManager.getConnection(DB_JDBC_URL);
            }
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            logger.error("Connection not established");
            e.printStackTrace();
        }
    }

    /**
     * Prepare statement and execute it
     * @param sqlScript Sql script for prepared statements
     * @param isSelect Type of sqlScript: True - if it SELECT ..., False - if INSERT ..., DELETE ..., UPDATE ...
     * @param args Arguments to scripts
     * @return ResultSet with select results or code of INSERT / DELETE / UPDATE query:
     *         0 - no affected rows
     *         -1 - error at script execution
     *         other int - number of affected rows
     * Return result should be casted to ResultSet type or to Integer
     */
    private Object doPreparedStatement(final String sqlScript, boolean isSelect, Object... args) throws SQLException{
        ResultSet resultSelect = null;
        Integer resultUpdate = -1;
        reconnect();
        PreparedStatement statement = connection.prepareStatement(sqlScript);
        int currIdx = 1;
        for (Object oneObj : args) {
            if (oneObj instanceof Integer) {
                statement.setInt(currIdx, (Integer)oneObj);
            } else if (oneObj instanceof Boolean) {
                statement.setInt(currIdx, ((Boolean)oneObj) ? 1 : 0);
            } else if (oneObj == null) {
                statement.setNull(currIdx, Types.NULL);
            } else {
                statement.setString(currIdx, (String) oneObj);
            }
            currIdx++;
        }
        if (isSelect) {
            resultSelect = statement.executeQuery();
        } else {
            resultUpdate = statement.executeUpdate();
        }
        return (resultSelect != null) ? resultSelect : resultUpdate;
    }

}
