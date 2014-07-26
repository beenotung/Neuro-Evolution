package core;

import java.sql.SQLException;
import java.util.ArrayList;

import myutils.Mysql;
import myutils.SqlServerInfo;

public class NeuralNetwork {
	private SqlServerInfo sqlServerInfo;

	public NeuralNetwork(SqlServerInfo sqlServerInfo) throws CloneNotSupportedException {
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
	}

	public void removeDatabase() {
		try {
			Mysql.sqlExecUpdate(sqlServerInfo.noDB(), "DROP DATABASE IF EXISTS " + sqlServerInfo.databaseName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println();
			System.out.println("failed to DROP DATABASE " + sqlServerInfo.databaseName);
			System.out.println("SQLException ErrorCode: " + e.getErrorCode());
			System.out.println("SQLException SQLState: " + e.getSQLState());
			System.out.println("SQLException Message: " + e.getMessage());
		}
	}

	public void createDatabase() {
		ArrayList<String> sqls = new ArrayList<String>();
		sqls.add("CREATE DATABASE IF NOT EXISTS " + sqlServerInfo.databaseName + ";");
		sqls.add("CREATE TABLE cells (id int, incomeid int, outcomeid int);");
		try {
			// Mysql.sqlExecBatch(sqlServerInfo, sqls);
			Mysql.sqlExec(sqlServerInfo.noDB(), sqls.get(0));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println();
			System.out.println("failed when creating database/table");
			System.out.println("SQLException ErrorCode: " + e.getErrorCode());
			System.out.println("SQLException SQLState: " + e.getSQLState());
			System.out.println("SQLException Message: " + e.getMessage());
		}
	}

	

	public void setRandomly() {
		// TODO Auto-generated method stub
		
	}

	public void save() {
		// TODO Auto-generated method stub
		
	}

}
