package myutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Mysql {

	public static ResultSet sqlExecQuery(SqlServerInfo sqlServerInfo, String sqlQuery) throws SQLException {
		ResultSet resultSet = null;

		String path;
		if (sqlServerInfo.databaseName == "") {
			path = "";
		} else {
			path = "/";
		}
		path = sqlServerInfo.sqlServerUrl + path + sqlServerInfo.databaseName;

		Connection connection = DriverManager.getConnection(path, sqlServerInfo.user, sqlServerInfo.password);
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(sqlQuery);

		return resultSet;
	}

	public static int sqlExecUpdate(SqlServerInfo sqlServerInfo, String sqlQuery) throws SQLException {
		int sqlStatus = 0;

		String path;
		if (sqlServerInfo.databaseName == "") {
			path = "";
		} else {
			path = "/";
		}
		path = sqlServerInfo.sqlServerUrl + path + sqlServerInfo.databaseName;

		Connection connection = DriverManager.getConnection(path, sqlServerInfo.user, sqlServerInfo.password);
		Statement statement = connection.createStatement();
		sqlStatus = statement.executeUpdate(sqlQuery);

		return sqlStatus;
	}

	public static boolean sqlExec(SqlServerInfo sqlServerInfo, String sqlQuery) throws SQLException {
		boolean hasResultSet;

		String path;
		if (sqlServerInfo.databaseName == "") {
			path = "";
		} else {
			path = "/";
		}
		path = sqlServerInfo.sqlServerUrl + path + sqlServerInfo.databaseName;

		Connection connection = DriverManager.getConnection(path, sqlServerInfo.user, sqlServerInfo.password);
		Statement statement = connection.createStatement();
		hasResultSet = statement.execute(sqlQuery);

		return hasResultSet;
	}

	public static int[] sqlExecBatch(SqlServerInfo sqlServerInfo, ArrayList<String> sqls) throws SQLException {
		int[] updateCount;

		String path;
		if (sqlServerInfo.databaseName == "") {
			path = "";
		} else {
			path = "/";
		}
		path = sqlServerInfo.sqlServerUrl + path + sqlServerInfo.databaseName;

		Connection connection = DriverManager.getConnection(path, sqlServerInfo.user, sqlServerInfo.password);
		Statement statement = connection.createStatement();
		for (String sqlQuery : sqls) {
			statement.addBatch(sqlQuery);
		}
		updateCount = statement.executeBatch();

		return updateCount;
	}
}
