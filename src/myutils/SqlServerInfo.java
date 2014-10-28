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

	public SqlServerInfo(String sqlServerUrl, String databaseName, String user,
			String password) {
		this.sqlServerUrl = sqlServerUrl;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			SqlServerInfo info = new SqlServerInfo(sqlServerUrl, databaseName, user,
					password);
			return info;
		}
	}

	public SqlServerInfo noDB() {
		return new SqlServerInfo(sqlServerUrl, user, password);
	}
}
