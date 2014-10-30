package myutils.connection;

/**
 * @author beenotung
 */
public class MySqlServerInfo {
	private String protocol;
	private String host;
	private int port;
	private String username;
	private String password;
	private String databasename;

	public MySqlServerInfo(String protocol, String host, int port,
			String databasename, String username, String password) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.databasename = databasename;
		this.username = username;
		this.password = password;
	}

	public String getUrlWithoutDB() {
		return protocol + "://" + host + ":" + port;
	}

	public String getUrlWithDB() {
		return getUrlWithoutDB() + "/" + databasename;
	}

	public String getMysqlusername() {
		return username;
	}

	public String getMysqlpassword() {
		return password;
	}

}
