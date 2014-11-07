package neuralnetwork;

import myutils.SqlServerInfo;

public class NeuralNetworkDatabaseConnector {
	private SqlServerInfo sqlServerInfo;
	private String mode;

	public NeuralNetworkDatabaseConnector(SqlServerInfo sqlServerInfo, String mode) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
			}
}
