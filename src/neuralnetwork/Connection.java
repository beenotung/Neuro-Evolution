package neuralnetwork;

import myutils.Utils;

public class Connection {
	public Cell src;
	public Cell dest;
	public double weight;

	public Connection(Cell src, Cell dest) {
		this.src = src;
		this.dest = dest;
		weight = Utils.random.nextDouble();
	}

	public Connection(Cell src, Cell dest, double weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}
}