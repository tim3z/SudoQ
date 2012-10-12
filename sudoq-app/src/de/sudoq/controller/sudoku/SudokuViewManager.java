package de.sudoq.controller.sudoku;

import de.sudoq.controller.sudoku.painter.ViewPainter;
import de.sudoq.model.sudoku.Constraint;
import de.sudoq.model.sudoku.ConstraintType;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.view.RasterLayout;
import de.sudoq.view.ViewClickListener;

public abstract class SudokuViewManager implements ViewClickListener {

	private RasterLayout layout;
	private Sudoku sudoku;
	
	public SudokuViewManager(RasterLayout layout) {
		if (layout == null) {
			throw new IllegalArgumentException("SudokuLayout was null");
		}
		
		this.layout = layout;
	}
	
	public void setSudoku(Sudoku sudoku, ViewPainter painter) {
		this.sudoku = sudoku;
		layout.setDimension(sudoku.getSudokuType().getSize());
		layout.setPainter(painter);
		updateRaster();
	}
	
	/**
	 * Gibt das SudokuLayout zurück.
	 * @return Das SudokuLayout
	 */
	public RasterLayout getSudokuLayout() {
		return layout;
	}
	
	
	/**
	 * Gibt das Sudoku zurück. Kann null sein.
	 * @return Das Sudoku
	 */
	public Sudoku getSudoku() {
		return sudoku;
	}
	
	protected void updateRaster() {
		int counter = 0;
		for (Constraint c : sudoku.getSudokuType()) {
			if (c.getType().equals(ConstraintType.BLOCK)) {
				for (Position p : c.getPositions()) {
					layout.setBlockMembership(p, counter);
				}
			}
			counter++;
		}
	}
}
