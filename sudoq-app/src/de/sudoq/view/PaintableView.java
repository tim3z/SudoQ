/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import de.sudoq.controller.sudoku.painter.ViewPainter;

/**
 * Diese Subklasse des von der Android API bereitgestellten Views stellt ein
 * einzelnes Feld innerhalb eines Sudokus dar. Es erweitert den Android View um
 * Funktionalität zur Benutzerinteraktion und Färben.
 */
public class PaintableView extends View implements ObservableViewClicked {

	/** Attributes */

	/**
	 * List der Selektions-Listener
	 */
	private ArrayList<ViewClickListener> fieldSelectListener;
	
	/**
	 * Der Painter, welcher dieses Feld bemalt
	 */
	private ViewPainter painter;
	
	
	/** Constructors */

	/**
	 * Erstellt einen SudokuFieldView und initialisiert die Attribute der
	 * Klasse.
	 * 
	 * @param context
	 *            der Applikationskontext
	 * @throws IllegalArgumentException
	 *             Wird geworfen, falls der Kontext null ist
	 */
	public PaintableView(Context context) {
		super(context);
		
		this.fieldSelectListener = new ArrayList<ViewClickListener>();
	}
	
	
	/**
	 * 
	 * @param painter
	 */
	public void setPainter(ViewPainter painter) {
		this.painter = painter;
		invalidate();
	}
	
	
	/** Methods */

	/**
	 * Zeichnet den Inhalt des Feldes auf das Canvas dieses SudokuFieldViews.
	 * Sollte den AnimationHandler nutzen um vorab Markierungen/Färbung an dem
	 * Canvas Objekt vorzunehmen.
	 * 
	 * @param canvas
	 *            Das Canvas Objekt auf das gezeichnet wird
	 * @throws IllegalArgumentException
	 *             Wird geworfen, falls das übergebene Canvas null ist
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (painter != null) {
			painter.paintView(canvas, this);
		}
		
		if (!this.isEnabled()) {
			canvas.drawARGB(100, 10, 10, 10);
		}
	}

	/**
	 * Diese Methode verarbeitet alle Touch Inputs, die der Benutzer macht und
	 * leitet sie an den ShowViewListener weiter.
	 * 
	 * @param touchEvent
	 *            Das TouchEvent das von der API kommt und diese Methode
	 *            aufgerufen hat
	 * @return true falls das TouchEvent behandelt wurde, false falls nicht
	 * @throws IllegalArgumentException
	 *             Wird geworfen, falls das übergebene MotionEvent null ist
	 */
	@Override
	public boolean onTouchEvent(MotionEvent touchEvent) {
		if (this.isEnabled())
			notifyListener();

		return false;
	}

	/**
	 * Benachrichtigt alle Registrierten Listener über Interaktion mit diesem
	 * SudokuFieldView.
	 */
	public void notifyListener() {
		for (ViewClickListener listener : fieldSelectListener) {
			listener.onViewClicked(this);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public void registerListener(ViewClickListener listener) {
		this.fieldSelectListener.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(ViewClickListener listener) {
		this.fieldSelectListener.remove(listener);
	}

}
