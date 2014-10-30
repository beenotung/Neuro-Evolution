package myutils.connection;

/**
 * @author beenotung
 */
public class MyPortforwardInfo {
	private int localPort;
	private String remoteHost;
	private int remotePort;

	public MyPortforwardInfo(int localPort, String remoteHost, int remotePort) {
		this.localPort = localPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	public int getLocalPort() {
		return localPort;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

}
