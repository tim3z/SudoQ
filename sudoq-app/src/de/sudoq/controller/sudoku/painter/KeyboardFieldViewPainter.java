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

import de.sudoq.controller.sudoku.Symbol;

import android.graphics.Canvas;
import android.view.View;

/**
 * Stellt eine Klasse zur Verfügung, welche für Animationen bzw. Markierungen
 * von Feldern zuständig ist.
 */
public class KeyboardFieldViewPainter extends ConcreteViewPainter{
	/** Attributes */
	
	/**
	 * Mappt ein Field auf einen Animation-Wert, welcher beschreibt, wie das
	 * Feld zu zeichnen ist
	 */
	private Map<View, KeyboardFieldViewState> markings;
	
	private Symbol symbolSet;

	
	/** Constructors */

	/**
	 * Privater Konstruktor, da diese Klasse statisch ist.
	 */
	public KeyboardFieldViewPainter() {
		this.markings = new HashMap<View, KeyboardFieldViewState>();
	}

	
	public void setSymbolSet(Symbol symbolSet) {
		this.symbolSet = symbolSet;
	}
	
	
	/**
	 * Gibt 
	 */
	public KeyboardFieldViewState getState(View view) {
		if (this.markings.get(view) == null) {
			this.markings.put(view, new KeyboardFieldViewState(view));
		}
		return this.markings.get(view);
	}

	
	/** Methods */
	@Override
	public void paintView(Canvas canvas, View view) {
		KeyboardFieldViewState state = markings.get(view);
		
		drawBackground(canvas, view, state.getBorderColor(), true, false);
		drawInner(canvas, view, state.getBackgroundColor(), true, false);
		drawText(canvas, view, state.getTextColor(), false, symbolSet.getMapping(markings.get(view).getValue()));
	}


	@Override
	public void flushMarkings() {
		this.markings.clear();
	}
	
}
