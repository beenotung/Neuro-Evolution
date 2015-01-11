package neuroevolution.neuralnetwork.core_java;

import java.util.Vector;

public class Layer {
	public int layerid;
	public Vector<Cell> cells;

	public Layer(int layerid) {
		this.layerid = layerid;
		cells = new Vector<Cell>();
	}

	public Layer(int layerid, Vector<Cell> cells) {
		this.layerid = layerid;
		this.cells = cells;
	}

	public Layer(int layerid,int NCell) {
		this(layerid);
		for(int iCell=0;iCell
				<NCell;iCell++)
			cells.add(new Cell(Cell.getID(), layerid));
	}}
