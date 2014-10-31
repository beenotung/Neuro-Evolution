package neuralnetwork;

import java.util.Vector;

import com.mysql.jdbc.Util;

import myutils.Utils;

public class Cell {
	private static int NewID = 0;
	public int id;
	public int layerid;
	public Vector<Connection> connections;
	public double delta;
	public double value;

	/** Static methods **/
	public static int getID() {
		return NewID++;
	}

	/** instance methods **/
	public Cell(int id, int layerid) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		delta = Utils.random.nextDouble();
		value = Utils.random.nextDouble();
	}

	public Cell(int id, int layerid, double delta, double value) {
		this.id = id;
		this.layerid = layerid;
		connections = new Vector<Connection>();
		this.delta = delta;
		this.value = value;
	}

	public Cell(int id, int layerid, Vector<Connection> connections, double delta,
			double value) {
		this.id = id;
		this.layerid = layerid;
		this.connections = connections;
		this.delta = delta;
		this.value = value;
	}

}
