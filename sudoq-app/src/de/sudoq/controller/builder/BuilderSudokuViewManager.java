package de.sudoq.controller.builder;

import de.sudoq.controller.sudoku.SudokuViewManager;
import de.sudoq.controller.sudoku.painter.BuilderSudokuViewPainter;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.model.sudoku.sudokuTypes.SudokuType;
import de.sudoq.view.PaintableView;
import de.sudoq.view.RasterLayout;

public class BuilderSudokuViewManager extends SudokuViewManager {

	private Position dimension;
	private SudokuType type;
	private BuilderSudokuViewPainter painter;
	
	
	
	public BuilderSudokuViewManager(RasterLayout layout, Position dimension, int numberOfSymbols) {
		super(layout);
		
		this.dimension = dimension;
		this.type = new SudokuType(dimension.getX(), dimension.getY(), numberOfSymbols);
				
		this.painter = new BuilderSudokuViewPainter();
		
		setSudoku(new Sudoku(type), painter);
		
		layout.registerListener(this);
		
		// Remove this for memory game ;)
		initialisePainter();
	}
	
	/**
	 * DUMMY!!!
	 * @param dimension
	 */
	public void setDimension(Position dimension) {
		if (dimension != null && dimension.getX() > 0 && dimension.getY() > 0) {
			this.dimension = dimension;
		}
	}

	private void initialisePainter() {
		PaintableView currentFieldView;
		for (int x = 0; x < getSudoku().getSudokuType().getSize().getX(); x++) {
			for (int y = 0; y < getSudoku().getSudokuType().getSize().getY(); y++) {
				currentFieldView = getSudokuLayout().getView(Position.get(x, y)); 
				if (currentFieldView != null) {
					painter.getState(currentFieldView);
				}
			}
		}
	}
	
	@Override
	public void onViewClicked(PaintableView view) {
		painter.getState(view).setSelected(true);
	}

}
