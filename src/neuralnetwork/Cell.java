package neuralnetwork;

import java.util.Vector;

import myutils.Utils;

public class Cell {
	public int id;
	public int layerid;
	public Vector<Connection> connections;
	public double delta;

	public Cell(int id, int layerid) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		delta = Utils.random.nextDouble();
	}

	public Cell(int id, int layerid, double delta) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		this.delta = delta;
	}

	public Cell(int id, int layerid, Vector<Connection> connections, double delta) {
		this.id = id;
		this.layerid = layerid;
		this.connections = connections;
		this.delta = delta;
	}
}
