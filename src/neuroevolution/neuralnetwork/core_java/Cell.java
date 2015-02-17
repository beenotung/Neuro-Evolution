package neuroevolution.neuralnetwork.core_java;

import myutils.Utils;

import java.util.Vector;

public class Cell {
    private static int NewID = 0;
    public int id;
    public int layerid;
    public Vector<Connection> connections;
    public double bias;
    public double activation;
    public double deltaBias;
    public double deltaError;

    /**
     * constructor *
     */
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

    /**
     * Static methods *
     */
    public static int getID() {
        return NewID++;
    }

    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * instance methods *
     */
    public void activate() {
        activation = Cell.sigmoid(activation - bias);
        // activation = (Math.signum(Cell.sigmoid(activation - bias))+1)/2.0;
    }

}
