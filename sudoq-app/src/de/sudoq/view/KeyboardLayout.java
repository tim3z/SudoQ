/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import de.sudoq.controller.sudoku.painter.ViewPainter;


/**
 * Dieses Layout stellt ein virtuelles Keyboard zur Verfügung, in dem sich die
 * Buttons möglichst quadratisch ausrichten.
 */
public class KeyboardLayout extends LinearLayout implements ObservableViewClicked {

	/**
	 * Die Buttons des VirtualKeyboard
	 */
	private PaintableView[][] buttons;

	/**
	 * Beschreibt, ob die Tastatur deaktiviert ist.
	 */
	private boolean deactivated;

	private ViewPainter painter;
	
	private List<ViewClickListener> viewClickListener;
	
	/**
	 * Instanziiert ein neues VirtualKeyboardLayout mit den gegebenen Parametern
	 * 
	 * @param context
	 *            der Applikationskontext
	 * @param attrs
	 *            das Android AttributeSet
	 */
	public KeyboardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
		
		this.viewClickListener = new ArrayList<ViewClickListener>();
	}
	
	
	public void setPainter(ViewPainter painter) {
		this.painter = painter;
		
		if (buttons == null) {
			return;
		}
		
		int buttonsPerRow = this.buttons.length;
		if (buttonsPerRow == 0) 
			return;
		int buttonsPerColumn = this.buttons[0].length;
		
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				buttons[x][y].setPainter(painter);
				buttons[x][y].invalidate();
			}
		}
	}

	/**
	 * Aktualisiert das Keyboard, sodass für das angegebene Game die korrekten
	 * Buttons dargestellt werden.
	 * 
	 * @param numberOfButtons
	 *            Die Anzahl der Buttons für dieses Keyboard
	 */
	public void refresh(int numberOfButtons) {
		if (numberOfButtons < 0) {
			return;
		}
		this.deactivated = false;
		inflate(numberOfButtons);
		setPainter(painter);
		invalidate();
		reRegisterListenerOnChild();
	}

	/**
	 * Inflatet das Keyboard.
	 * 
	 * @param numberOfButtons
	 *            Anzahl der Buttons dieser Tastatur
	 */
	private void inflate(int numberOfButtons) {
		this.removeAllViews();
		
		int buttonsPerColumn = (int) Math.floor(Math.sqrt(numberOfButtons));
		int buttonsPerRow = (int) Math.ceil(Math.sqrt(numberOfButtons));
		this.buttons = new PaintableView[buttonsPerRow][buttonsPerColumn];

		for (int y = 0; y < buttonsPerColumn; y++) {
			LinearLayout la = new LinearLayout(getContext());
			for (int x = 0; x < buttonsPerRow; x++) {
				this.buttons[x][y] = new PaintableView(getContext());
				this.buttons[x][y].setVisibility(View.INVISIBLE);
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
				params.leftMargin = 2;
				params.bottomMargin = 2;
				params.topMargin = 2;
				params.rightMargin = 2;
				la.addView(this.buttons[x][y], params);
			}
			addView(la, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		FieldViewPainter.getInstance().markField(canvas, this, ' ', false);
	}

	/**
	 * Aktiviert bzw. deaktiviert dieses Keyboard.
	 * 
	 * @param activated
	 *            Spezifiziert, ob das Keyboard aktiviert oder deaktiviert sein
	 *            soll
	 */
	public void setVisible(boolean activated) {
		this.deactivated = !activated;
		if (this.buttons == null || this.buttons.length == 0)
			return;

		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				if (deactivated) {
					this.buttons[x][y].setVisibility(View.INVISIBLE);
				} else {
					this.buttons[x][y].setVisibility(View.VISIBLE);
				}
			}
		}
	}

	/**
	 * Unbenutzt.
	 * 
	 * @throws UnsupportedOperationException
	 *             Wirft immer eine UnsupportedOperationException
	 */
	public void notifyListener() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerListener(ViewClickListener listener) {
		if (listener == null) {
			return;
		}
		
		viewClickListener.add(listener);
		if (this.buttons == null || this.buttons.length == 0)
			return;
		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				this.buttons[x][y].registerListener(listener);
			}
		}
	}
	
	public void reRegisterListenerOnChild() {
		if (this.buttons == null || this.buttons.length == 0)
			return;

		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				for (ViewClickListener listener : viewClickListener) {
					this.buttons[x][y].registerListener(listener);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(ViewClickListener listener) {
		viewClickListener.remove(listener);
		if (this.buttons == null || this.buttons.length == 0)
			return;
		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				this.buttons[x][y].removeListener(listener);
			}
		}
	}


	/**
	 * Aktiviert alle Buttons dieses Keyboards.
	 */
	public void enableAllButtons() {
		if (this.buttons == null || this.buttons.length == 0)
			return;
		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				this.buttons[x][y].setEnabled(true);
			}
		}
	}

	/**
	 * Deaktiviert den spezifizierten Button.
	 * 
	 * @param symbol
	 *            Das Symbol des zu deaktivierenden Button
	 */
	public void disableButton(int symbol) {
		if (this.buttons == null || this.buttons.length == 0)
			return;
		int buttonsPerRow = this.buttons.length;
		this.buttons[symbol % buttonsPerRow][symbol / buttonsPerRow].setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		if (this.buttons == null) {
			return;
		}

		if (this.buttons == null || this.buttons.length == 0)
			return;
		int buttonsPerRow = this.buttons.length;
		int buttonsPerColumn = this.buttons[0].length;
		for (int x = 0; x < buttonsPerRow; x++) {
			for (int y = 0; y < buttonsPerColumn; y++) {
				if (this.buttons[x][y] != null)
					this.buttons[x][y].invalidate();
			}
		}
	}

	/**
	 * Gibt zurueck ob die view angezeigt wird
	 * 
	 * @return true falls aktive andernfalls false
	 */
	public boolean isVisible() {
		return !deactivated;
	}

	public PaintableView getView(int index) {
		if (this.buttons == null || this.buttons.length == 0 || index > this.buttons[0].length * this.buttons.length){
			return null;
		} else {
			return buttons[index % this.buttons.length][index / this.buttons.length];
		}
	}
}
