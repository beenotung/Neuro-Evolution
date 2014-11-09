package neuralnetwork;

import java.util.Vector;

import myutils.Utils;
import neuralnetwork.databaseconnection.NeuralNetworkDatabaseConnector;

public class NeuralNetwork {
	private String mode;
	private int[] layers_ints;
	private NeuralNetworkDatabaseConnector databaseConnector;

	private Vector<Layer> layers;
	private double mse;

	/** parameter **/
	public final double eta, alpha, minErrorRate;

	private NeuralNetwork() {
		minErrorRate = 1 / 100.0 / 100.0;
		// eta = 0.03;
		eta = 10 / 100.0;
		alpha = 1 - eta;
		// alpha = 0.3;
	}

	/** static staff **/
	public static double derivative(double y) {
		return y * (1 - y);
	}

	private static void shuffle(Vector<Example> examples) {
		Vector<Object> tmp = new Vector<Object>();
		for (Object o : examples)
			tmp.add(o);
		tmp = Utils.getShuffled(tmp);
		examples.removeAllElements();
		for (Object o : tmp)
			examples.add((Example) o);
	}

	/** constructor **/
	public NeuralNetwork(String mode, int[] layers_ints,
			NeuralNetworkDatabaseConnector databaseConnector) {
		this();
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.mode = mode;
		this.layers_ints = layers_ints.clone();
		this.databaseConnector = databaseConnector;
	}

	/** java staff **/
	public void create() {
		createLayers();
		createConnections();
	}

	private void createLayers() {
		layers = new Vector<Layer>();
		for (int iLayer = 0; iLayer < layers_ints.length; iLayer++)
			layers.add(new Layer(iLayer, layers_ints[iLayer]));
	}

	private void createConnections() {
		for (int iLayer = 0; iLayer < layers.size() - 1; iLayer++)
			for (Cell srcCell : layers.get(iLayer).cells)
				for (Cell destCell : layers.get(iLayer + 1).cells)
					srcCell.connections.add(new Connection(srcCell, destCell));
	}

	/** sql staff **/
	private void loadFromDB() {
		// TODO loadFromDB()
	}

	private void saveToDB() {
		// TODO saveToDB()
	}

	public void learnFromDatabase(int NCycle) {
		Vector<Example> examples = databaseConnector.getExamples();
		for (int iCycle = 0; iCycle < NCycle; iCycle++) {
			mse = 0;
			shuffle(examples);
			learn(examples);
			System.out.printf("Training Cycle:%f%%(%d/%d)\tmse:%f%%\n", iCycle * 100.0
					/ NCycle, iCycle, NCycle, mse * 100.0);
		}
	}

	public void learnFromDatabase() {
		learnFromDatabase(minErrorRate);
	}

	public void learnFromDatabase(double minErrorRate) {
		Vector<Example> examples = databaseConnector.getExamples();
		do {
			mse = 0;
			shuffle(examples);
			learn(examples);
			System.out.printf("mse:%f%%\n", mse * 100.0);
		} while (mse > minErrorRate);
	}

	/** neural network staff **/
	public void learn(Vector<Example> examples) {
		// init training variables
		for (Layer layer : layers)
			for (Cell cell : layer.cells) {
				cell.deltaBias = 0;
				for (Connection connection : cell.connections)
					connection.deltaWeight = 0;
			}
		// Train
		for (Example example : examples)
			learn(example);
	}

	private void learn(Example example) {
		// check example
		example.checkOutput(layers.get(layers.size() - 1).cells.size());
		// run example
		run(example.input);
		// compute delta error
		for (int iCell = 0; iCell < example.output.size(); iCell++) {
			Cell cell = layers.get(layers.size() - 1).cells.get(iCell);
			cell.deltaError = derivative(cell.activation)
					* (example.output.get(iCell) - cell.activation);
		}
		for (int iLayer = layers.size() - 2; iLayer >= 0; iLayer--) {
			for (Cell cell : layers.get(iLayer).cells) {
				cell.deltaError = 0;
				for (Connection connection : cell.connections)
					cell.deltaError += connection.dest.deltaError * connection.weight;
				cell.deltaError *= derivative(cell.activation);
			}
		}
		// compute delta weight, delta bias
		for (Layer layer : layers)
			for (Cell cell : layer.cells) {
				cell.deltaBias = alpha * cell.deltaBias - eta * cell.deltaError;
				for (Connection connection : cell.connections)
					connection.deltaWeight = alpha * connection.deltaWeight + eta
							* cell.activation * connection.dest.deltaError;
			}
		// update weight, bias
		for (Layer layer : layers)
			for (Cell cell : layer.cells) {
				cell.bias += cell.deltaBias;
				for (Connection connection : cell.connections)
					connection.weight += connection.deltaWeight;
			}
		// mse
		Layer layer = layers.get(layers.size() - 1);
		for (int iCell = 0; iCell < example.output.size(); iCell++)
			mse += Math.pow(
					example.output.get(iCell) - layer.cells.get(iCell).activation, 2);
	}

	public Vector<Double> run(Vector<Double> inputs) {
		/* System.out.println("run"); */
		// check input
		while (inputs.size() < layers.get(0).cells.size())
			inputs.add(0d);
		/** init cell activation **/
		for (Layer layer : layers)
			for (Cell cell : layer.cells)
				cell.activation = 0;
		/** set input layer **/
		for (int iCell = 0; iCell < layers.get(0).cells.size(); iCell++)
			layers.get(0).cells.get(iCell).activation = inputs.get(iCell);
		/** calc **/
		Layer layer = layers.get(0);
		for (Cell cell : layer.cells) {
			/*
			 * System.out.println("--");
			 * System.out.println(cell.connections.size());
			 */
			for (Connection connection : cell.connections) {
				/*
				 * System.out.println();
				 * System.out.println(connection.dest.activation);
				 * System.out.println(connection.src.activation);
				 * System.out.println(connection.weight);
				 */
				connection.dest.activation += connection.src.activation
						* connection.weight;
			}
		}
		for (int iLayer = 1; iLayer < layers.size(); iLayer++) {
			layer = layers.get(iLayer);
			for (Cell cell : layer.cells) {
				cell.activate();
				for (Connection connection : cell.connections)
					connection.dest.activation += (connection.src.activation - connection.src.bias)
							* connection.weight;
			}
		}
		/** export output layer **/
		Vector<Double> outputs = new Vector<Double>();
		for (Cell cell : layers.get(layers.size() - 1).cells)
			outputs.add(cell.activation);
		return outputs;
	}
}
