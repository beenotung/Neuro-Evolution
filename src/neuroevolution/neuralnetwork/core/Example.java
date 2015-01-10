package neuroevolution.neuralnetwork.core;

import java.util.Vector;

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
