/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.controller.sudoku;

import java.util.ArrayList;
import java.util.List;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import de.sudoq.R;
import de.sudoq.controller.game.ActionListener;
import de.sudoq.controller.game.GameKeyboardLayoutManager;
import de.sudoq.controller.game.GameSudokuViewManager;
import de.sudoq.model.game.Assistances;
import de.sudoq.model.game.Game;
import de.sudoq.model.profile.Profile;
import de.sudoq.model.sudoku.Constraint;
import de.sudoq.model.sudoku.Field;
import de.sudoq.view.PaintableView;
import de.sudoq.view.RasterLayout;
import de.sudoq.view.KeyboardLayout;

/**
 * Ein Vermittler zwischen einem Sudoku und den verschiedenen
 * Eingabemöglichkeiten, also insbesondere Tastatur und Gesten-View.
 */
public class UserInteractionMediator implements OnGesturePerformedListener, InputListener, SelectedViewChangedListener, ObservableActionCaster {

	/**
	 * Flag für den Notizmodus.
	 */
	private boolean noteMode;


	/**
	 * Das aktuelle Spiel.
	 */
	private Game game;

	/**
	 * Eine Liste der ActionListener.
	 */
	private List<ActionListener> actionListener;

	/**
	 * Die Gesten-View.
	 */
	private GestureOverlayView gestureOverlay;

	/**
	 * Die Bibliothek für die Gesteneingabe.
	 */
	private GestureStore gestureStore;

	
	private GameSudokuViewManager gameViewManager;
	
	private GameKeyboardLayoutManager keyboardManager;
	
	/**
	 * Instanziiert einen neuen UserInteractionMediator.
	 * 
	 * @param virtualKeyboard
	 *            Das virtuelle Keyboard, auf dem der Benutzer Eingaben
	 *            vornehmen kann
	 * @param sudokuView
	 *            Die View des Sudokus
	 * @param game
	 *            Das aktuelle Spiel
	 * @param gestureOverlay
	 *            Die Gesten-View auf der der Benutzer Gesten eingeben kann
	 * @param gestureStore
	 *            Die Bibliothek der Gesten
	 */
	public UserInteractionMediator(GameSudokuViewManager gameViewManager, KeyboardLayout virtualKeyboard, RasterLayout sudokuView, Game game, GestureOverlayView gestureOverlay,
			GestureStore gestureStore, Symbol symbolSet) {
		this.actionListener = new ArrayList<ActionListener>();

		this.gameViewManager = gameViewManager;
		this.gameViewManager.setSymbolSet(symbolSet);
		this.gameViewManager.registerListener(this);
		this.game = game;
		
		this.keyboardManager = new GameKeyboardLayoutManager(virtualKeyboard);
		// DUMMY
		this.keyboardManager.setSymbolSet(symbolSet);
		this.keyboardManager.registerListener(this);
		
		this.gestureOverlay = gestureOverlay;
		this.gestureStore = gestureStore;
		this.gestureOverlay.addOnGesturePerformedListener(this);
		this.gestureOverlay.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
	}

	public void onInput(int symbol) {
		PaintableView currentField = this.gameViewManager.getSelectedView();
		for (ActionListener listener : actionListener) {
			if (this.noteMode) {
				if (gameViewManager.getField(currentField).isNoteSet(symbol)) {
					listener.onNoteDelete(gameViewManager.getField(currentField), symbol);
				} else {
					listener.onNoteAdd(gameViewManager.getField(currentField), symbol);
				}
			} else {
				if (symbol == gameViewManager.getField(currentField).getCurrentValue()) {
					listener.onDeleteEntry(gameViewManager.getField(currentField));
				} else {
					listener.onAddEntry(gameViewManager.getField(currentField), symbol);
				}
			}
		}

		updateKeyboard();
	}
	
	public void updateKeyboard() {
		keyboardManager.setNoteMode(noteMode);
		this.keyboardManager.setSelectedField(gameViewManager.getField(gameViewManager.getSelectedView()));
		if (gameViewManager.getSelectedView() != null && gameViewManager.getField(gameViewManager.getSelectedView()).isEditable() && !game.isFinished()) {
			restrictCandidates();
			this.keyboardManager.setActivated(true);
		} else {
			this.keyboardManager.setActivated(false);
		}
	}

	public void onSelectedViewChanged(PaintableView oldView, PaintableView newView) {
		if (oldView != newView) {
			this.noteMode = Profile.getInstance().isGestureActive() && !game.isFinished();
		} else if (!game.isFinished()) {
			if (Profile.getInstance().isGestureActive() && gameViewManager.getField(newView) != null && gameViewManager.getField(newView).isEditable()) {
				this.gestureOverlay.setVisibility(View.VISIBLE);
				restrictCandidates();
				final TextView textView = new TextView(gestureOverlay.getContext());
				textView.setTextColor(Color.YELLOW);
				textView.setText(" " + gestureOverlay.getContext().getString(R.string.sf_sudoku_title_gesture_input) + " ");
				textView.setTextSize(18);
				this.gestureOverlay.addView(textView, new GestureOverlayView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));
			} else {
				this.noteMode = !this.noteMode;
			}
		}
		updateKeyboard();
	}

	public void notifyListener() {

	}

	public void registerListener(ActionListener listener) {
		this.actionListener.add(listener);
	}

	public void removeListener(ActionListener listener) {
		this.actionListener.remove(listener);
	}

	/**
	 * Setzt den Zustand der Tastatur. Diese wird entsprechend (nicht)
	 * angezeigt.
	 * 
	 * @param activated
	 *            Gibt den zu setzenden Zustand an
	 */
	public void setKeyboardState(boolean activated) {
		this.keyboardManager.setActivated(activated);
	}

	/**
	 * Wird aufgerufen, sobald der Benutzer eine Geste eingibt.
	 * 
	 * @param overlay
	 *            GestureOverlay, auf welchem die Geste eingegeben wurde
	 * @param gesture
	 *            Geste, die der Benutzer eingegeben hat
	 * @throws IllegalArgumentException
	 *             Wird geworfen, falls eines der Argumente null ist
	 */
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = this.gestureStore.recognize(gesture);
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.5) {
				for (ActionListener listener : this.actionListener) {
					if (prediction.name.equals(String.valueOf(keyboardManager.getSymbolSet().getMapping(gameViewManager.getField(this.gameViewManager.getSelectedView()).getCurrentValue())))) {
						listener.onDeleteEntry(gameViewManager.getField(this.gameViewManager.getSelectedView()));
					} else {
						int number = keyboardManager.getSymbolSet().getAbstract(prediction.name);
						int save = gameViewManager.getField(this.gameViewManager.getSelectedView()).getCurrentValue();
						if (number >= this.game.getSudoku().getSudokuType().getNumberOfSymbols())
							number = -1;
						if (number != -1 && this.game.isAssistanceAvailable(Assistances.restrictCandidates)) {
							gameViewManager.getField(this.gameViewManager.getSelectedView()).setCurrentValue(number, false);
							for (Constraint c : this.game.getSudoku().getSudokuType()) {
								if (!c.isSaturated(this.game.getSudoku())) {
									number = -2;
									break;
								}
							}
							gameViewManager.getField(this.gameViewManager.getSelectedView()).setCurrentValue(save, false);
						}
						if (number != -1 && number != -2) {
							listener.onAddEntry(gameViewManager.getField(this.gameViewManager.getSelectedView()), number);
							this.gestureOverlay.setVisibility(View.INVISIBLE);
						} else if (number == -1) {
							Toast.makeText(this.gameViewManager.getSudokuLayout().getContext(),
									this.gameViewManager.getSudokuLayout().getContext().getString(R.string.toast_invalid_symbol), Toast.LENGTH_SHORT).show();
						} else if (number == -2) {
							Toast.makeText(this.gameViewManager.getSudokuLayout().getContext(),
									this.gameViewManager.getSudokuLayout().getContext().getString(R.string.toast_restricted_symbol), Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		}
	}

	/**
	 * Schränkt die Kandidaten auf der Tastatur ein.
	 */
	private void restrictCandidates() {
		this.keyboardManager.enableAllButtons();
		if (this.game.isAssistanceAvailable(Assistances.restrictCandidates)) {
			int save = gameViewManager.getField(this.gameViewManager.getSelectedView()).getCurrentValue();
			for (int i = 0; i < this.game.getSudoku().getSudokuType().getNumberOfSymbols(); i++) {
				gameViewManager.getField(this.gameViewManager.getSelectedView()).setCurrentValue(i, false);
				for (Constraint c : this.game.getSudoku().getSudokuType()) {
					if (!c.isSaturated(this.game.getSudoku())) {
						this.keyboardManager.disableButton(i);
						break;
					}
				}
				gameViewManager.getField(this.gameViewManager.getSelectedView()).setCurrentValue(Field.EMPTYVAL, false);
			}
			gameViewManager.getField(this.gameViewManager.getSelectedView()).setCurrentValue(save, false);
		}
	}
	
	public void setSymbolSet(Symbol symbol) {
		this.gameViewManager.setSymbolSet(symbol);
		this.keyboardManager.setSymbolSet(symbol);
	}
	
}
