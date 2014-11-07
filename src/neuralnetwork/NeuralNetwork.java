package neuralnetwork;

import java.util.Vector;

import neuralnetwork.databaseconnection.NeuralNetworkDatabaseConnector;

public class NeuralNetwork {
	private String mode;
	private int[] layers_ints;
	private NeuralNetworkDatabaseConnector databaseConnector;

	private Vector<Layer> layers;

	/** parameter **/
	public final double eta, alpha;

	private NeuralNetwork() {
		eta = 0.02;
		alpha = 1 - eta;
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

	/** static staff **/
	public static double derivative(double y) {
		return y * (1 - y);
	}

	/** java staff **/
	public void create() {
		createLayers();
	}

	private void createLayers() {
		layers = new Vector<Layer>();
		for (int iLayer = 0; iLayer < layers_ints.length; iLayer++)
			layers.add(new Layer(iLayer, layers_ints[iLayer]));
	}

	/** sql staff **/
	private void loadFromDB() {
		// TODO loadFromDB()
	}

	private void saveToDB() {
		// TODO saveToDB()
	}

	public void learnFromDatabase(int NCycle) {
		// TODO getexample
		Vector<Example> examples = databaseConnector.getExamples();
		for (int iCycle = 0; iCycle < NCycle; iCycle++) {
			learn(examples);
		}
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
		// mse
		double mse = 0;
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
		System.out.println(mse);
	}

	public Vector<Double> run(Vector<Double> inputs) {
		Vector<Double> outputs = new Vector<Double>();
		/** init cell activation **/
		for (Layer layer : layers)
			for (Cell cell : layer.cells)
				cell.activation = 0;
		/** set input layer **/
		while (inputs.size() < layers.get(0).cells.size())
			inputs.add(0d);
		for (int i = 0; i < layers.get(0).cells.size(); i++)
			layers.get(0).cells.get(i).activation = inputs.get(i);
		/** calc **/
		Layer layer = layers.get(0);
		for (Cell cell : layer.cells)
			for (Connection connection : cell.connections)
				connection.dest.activation += connection.src.activation
						* connection.weight;
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
		for (Cell cell : layers.get(layers.size() - 1).cells)
			outputs.add(cell.activation);
		return outputs;
	}
}
