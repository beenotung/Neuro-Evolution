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
	public class Example {
		public Vector<Double> input;
		public Vector<Double> output;

		public Example(Vector<Double> input, Vector<Double> output) {
			this.input = input;
			this.output = output;
		}

		public void checkInput(int n) {
			while (input.size() < n)
				input.add(0d);
		}

		public void checkOutput(int n) {
			while (output.size() < n)
				output.add(0d);
		}
	}

	public void learn(Vector<Example> examples) {
		for (Example example : examples)
			learn(example);
	}

	private void learn(Example example) {
		example.checkInput(layers.get(0).cells.size());
		example.checkOutput(layers.get(layers.size()).cells.size());
	}

}
