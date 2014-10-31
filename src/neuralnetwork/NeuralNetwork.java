package neuralnetwork;

import java.util.Vector;

import myutils.SqlServerInfo;

public class NeuralNetwork {
	private SqlServerInfo sqlServerInfo;
	private String mode;
	private int[] layers;
	private Vector<Cell> cells;

	public NeuralNetwork(SqlServerInfo sqlServerInfo, String mode, int[] layers) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
		this.layers = layers.clone();
	}

	/** java staff **/
	public void create() {
		createCells();
		createConnections();
	}

	private void createCells() {
		// TODO Auto-generated method stub

	}

	private void createConnections() {
		// TODO Auto-generated method stub

	}

	/** sql staff **/
	private void saveToDB() {
		// TODO Auto-generated method stub
	}

	private void loadFromDB() {
		// TODO Auto-generated method stub
	}
}
