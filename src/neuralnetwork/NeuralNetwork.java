package neuralnetwork;

import java.util.Vector;

import myutils.SqlServerInfo;

public class NeuralNetwork {
	private SqlServerInfo sqlServerInfo;
	private String mode;
	private int[] layers_ints;
	private Vector<Cell> cells;
	private Vector<Layer> layers;

	public NeuralNetwork(SqlServerInfo sqlServerInfo, String mode, int[] layers_ints) {
		if (mode != "BackNN") {
			System.out.println("[" + "mode" + "] Mode not supported");
			System.exit(1);
		}
		this.sqlServerInfo = (SqlServerInfo) sqlServerInfo.clone();
		this.mode = mode;
		this.layers_ints = layers_ints.clone();
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
	private void saveToDB() {
		// TODO Auto-generated method stub
	}

	private void loadFromDB() {
		// TODO Auto-generated method stub
	}

	/** neural network staff **/
	public void learn(Vector<Example> examples) {
		for (Example example : examples)
			learn(example);
	}

	private void learn(Example example) {
		example.checkOutput(layers.get(layers.size() - 1).cells.size());
		run(example.input);
		for (int i = 0; i < layers.get(layers.size() - 1).cells.size(); i++)

			for (int i = 0; i < results.size(); i++)
				deltaValues.add(example.output.get(i) - results.get(i));
	}

	public Vector<Double> run(Vector<Double> inputs) {
		Vector<Double> outputs = new Vector<Double>();
		/** init cell activation **/
		for (Layer layer : layers)
			for (Cell cell : layer.cells)
				cell.value = 0;
		/** set input layer **/
		while (inputs.size() < layers.get(0).cells.size())
			inputs.add(0d);
		for (int i = 0; i < layers.get(0).cells.size(); i++)
			layers.get(0).cells.get(i).value = inputs.get(i);
		/** calc **/
		for (Layer layer : layers)
			for (Cell cell : layer.cells)
				for (Connection connection : cell.connections)
					connection.dest.value += (connection.src.value - connection.src.delta)
							* connection.weight;
		/** export output layer **/
		for (Cell cell : layers.get(layers.size() - 1).cells)
			outputs.add(cell.value);
		return outputs;
	}
}
