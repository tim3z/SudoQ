/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.controller.sudoku.painter;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import de.sudoq.controller.sudoku.Symbol;

/**
 * Stellt eine Klasse zur Verfügung, welche für Animationen bzw. Markierungen
 * von Feldern zuständig ist.
 */
public class GameFieldViewPainter extends ConcreteViewPainter{
	/** Attributes */
	
	/**
	 * Mappt ein Field auf einen Animation-Wert, welcher beschreibt, wie das
	 * Feld zu zeichnen ist
	 */
	Map<View, GameFieldViewState> markings;

	/**
	 * Der zu diesem Painter zugehörige Symbolsatz
	 */
	Symbol symbolSet; 
	
	/** Constructors */

	/**
	 * Privater Konstruktor, da diese Klasse statisch ist.
	 */
	public GameFieldViewPainter() {
		this.markings = new HashMap<View, GameFieldViewState>();
	}

	public void setSymbolSet(Symbol symbolSet) {
		this.symbolSet = symbolSet; 
	}
	
	/**
	 * Gibt den FieldViewState zur entsprechenden FieldView zurück.
	 */
	public GameFieldViewState getState(View view) {
		if (this.markings.get(view) == null) {
			this.markings.put(view, new GameFieldViewState(view));
		}
		return this.markings.get(view);
	}

	/**
	 * Zeichnet die Notizen in dieses Feld
	 * 
	 * @param canvas
	 *            Das Canvas in das gezeichnet werde nsoll
	 */
	void drawNotes(Canvas canvas, View field) {
		Paint notePaint = new Paint();
		notePaint.setAntiAlias(true);
		int noteTextSize = field.getHeight() / symbolSet.getRasterSize();
		notePaint.setTextSize(noteTextSize);
		notePaint.setTextAlign(Paint.Align.CENTER);
		notePaint.setColor(Color.BLACK);
		for (int i = 0; i < symbolSet.getNumberOfSymbols(); i++) {
			if (markings.get(field).getField().isNoteSet(i)) {
				String note = symbolSet.getMapping(i);
				canvas.drawText(note + "", (i % symbolSet.getRasterSize()) * noteTextSize + noteTextSize / 2, (i / symbolSet.getRasterSize()) * noteTextSize + noteTextSize, notePaint);
			}
		}
	}

	/** Methods */
	@Override
	public void paintView(Canvas canvas, View view) {
		GameFieldViewState state = markings.get(view);
		
		if (canvas == null || view == null || state == null) {
			return;
		}
		
		drawBackground(canvas, view, state.getBackgroundColor(), true, state.isDarken());
		
		if (symbolSet != null) {
			if (!state.getField().isEmpty()) {
				drawText(canvas, view, state.getTextColor(), state.isFixed(), symbolSet.getMapping(state.getField().getCurrentValue()));
			} else {
				drawNotes(canvas, view);
			}
		}
		
	}


	@Override
	public void flushMarkings() {
		this.markings.clear();
	}
	
}
