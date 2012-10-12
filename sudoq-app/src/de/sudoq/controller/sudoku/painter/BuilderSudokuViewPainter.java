package de.sudoq.controller.sudoku.painter;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.view.View;

public class BuilderSudokuViewPainter extends ConcreteViewPainter {

	private Map<View, BuilderSudokuViewState> markings;
	
	
	public BuilderSudokuViewPainter() {
		markings = new HashMap<View, BuilderSudokuViewState>();
	}
	
	@Override
	public void paintView(Canvas canvas, View view) {
		BuilderSudokuViewState state = markings.get(view);
		
		if (canvas == null || view == null || state == null) {
			return;
		}
		
		drawBackground(canvas, view, state.getBackgroundColor(), true, false);

	}

	@Override
	public void flushMarkings() {
		markings.clear();
	}
	
	public BuilderSudokuViewState getState(View view) {
		if (this.markings.get(view) == null) {
			this.markings.put(view, new BuilderSudokuViewState(view));
		}
		return this.markings.get(view);
	}

}
