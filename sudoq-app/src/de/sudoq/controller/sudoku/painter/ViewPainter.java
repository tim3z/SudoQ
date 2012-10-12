package de.sudoq.controller.sudoku.painter;

import android.graphics.Canvas;
import android.view.View;

public interface ViewPainter {

	/**
	 * Bemalt das spezifizierte Canvas entsprechend der in den SudokuFieldViewStates definierten Eigenschaften.
	 * Ist eines der beiden Argumente null, so wird nichts getan.
	 * 
	 * @param canvas
	 *            Das Canvas, welches bemalt werden soll
	 * @param view
	 *            Das Feld, anhand dessen Animations-Einstellung das Canvas
	 *            bemalt werden soll
	 * @param justText
	 *            Definiert, dass nur Text geschrieben wird
	 */
	public abstract void paintView(Canvas canvas, View view);

	/**
	 * Löscht alle hinzugefügten Markierungen auf Default.
	 */
	public abstract void flushMarkings();

}