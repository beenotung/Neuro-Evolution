package neuralnetwork;

import java.util.Vector;

import myutils.Utils;

public class Cell {
	private static int NewID = 0;
	public int id;
	public int layerid;
	public Vector<Connection> connections;
	public double bias;
	public double activation;
	public double deltaBias;
	public double deltaError;

	/** Static methods **/
	public static int getID() {
		return NewID++;
	}

	/** constructor **/
	public Cell(int id, int layerid) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		bias = Utils.random.nextDouble();
		activation = Utils.random.nextDouble();
	}

	public Cell(int id, int layerid, double delta, double value) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		this.bias = delta;
		this.activation = value;
	}

	public Cell(int id, int layerid, Vector<Connection> connections, double delta,
			double value) {
		this.id = id;
		this.layerid = layerid;
		this.connections = connections;
		this.bias = delta;
		this.activation = value;
	}

	/** instance methods **/
	public void activate() {
		activation = 1 / (1 + Math.exp(bias - activation));
	}

}
