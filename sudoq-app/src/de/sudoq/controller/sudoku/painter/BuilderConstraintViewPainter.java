package de.sudoq.controller.sudoku.painter;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.view.View;

public class BuilderConstraintViewPainter extends ConcreteViewPainter {
	
	/**
	 * Mappt ein Field auf einen Animation-Wert, welcher beschreibt, wie das
	 * Feld zu zeichnen ist
	 */
	Map<View, BuilderConstraintViewState> markings;

	public BuilderConstraintViewPainter() {
		this.markings = new HashMap<View, BuilderConstraintViewState>();
	}

	@Override
	public void paintView(Canvas canvas, View view) {
		BuilderConstraintViewState state = markings.get(view);
		
		if (canvas == null || view == null || state == null) {
			return;
		}
		
		drawBackground(canvas, view, state.getBackgroundColor(), true, false);
		
		drawText(canvas, view, state.getTextColor(), false, state.getText());
	}
	
	/**
	 * Gibt den FieldViewState zur entsprechenden FieldView zurück.
	 */
	public BuilderConstraintViewState getState(View view) {
		if (this.markings.get(view) == null) {
			this.markings.put(view, new BuilderConstraintViewState(view));
		}
		return this.markings.get(view);
	}

	@Override
	public void flushMarkings() {
		this.markings.clear();
	}

}
