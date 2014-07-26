package myutils;

public class SqlServerInfo implements Cloneable {
	public String sqlServerUrl;
	public String databaseName;
	public String user;
	public String password;

	public SqlServerInfo(String sqlServerUrl, String user, String password) {
		this.sqlServerUrl = sqlServerUrl;
		this.databaseName = "";
		this.user = user;
		this.password = password;
	}

	public SqlServerInfo(String sqlServerUrl, String databaseName, String user, String password) {
		this.sqlServerUrl = sqlServerUrl;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public SqlServerInfo noDB() {
		return new SqlServerInfo(sqlServerUrl, user, password);
	}
}
