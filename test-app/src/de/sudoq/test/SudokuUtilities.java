package de.sudoq.test;

import android.widget.LinearLayout;
import de.sudoq.R;
import de.sudoq.controller.game.SudokuActivity;
import de.sudoq.model.sudoku.Position;
import de.sudoq.view.PaintableView;
import de.sudoq.view.KeyboardLayout;

public class SudokuUtilities {

	public static PaintableView[][] getViewArray(SudokuActivity a) {
		Position dim = a.getGame().getSudoku().getSudokuType().getSize();
		PaintableView[][] views = new PaintableView[dim.getX()][dim.getY()];
		for (int i = 0; i < dim.getX(); i++) {
			for (int j = 0; j < dim.getY(); j++) {
				views[i][j] = a.getSudokuLayout().getView(Position.get(i, j));;
			}
		}
		return views;
	}
	
	/**
	 * Gets a Button of the VirtualKeyboard with the given symbol. It expects a real value and the transformation to abstract is done automatically.
	 * 
	 * @param a The current {@link SudokuActivity}
	 * @param value The value, of the Button (Not the abstract symbol!)
	 * @return The View of the {@link VirtualKeyboardButtonView}
	 */
	public static PaintableView getKeyboardButton(SudokuActivity a, int value) {
		if (value > 0) {
			KeyboardLayout view = (KeyboardLayout) a.findViewById(R.id.virtual_keyboard);
			int columns = (int) Math.ceil(Math.sqrt(a.getGame().getSudoku().getSudokuType().getNumberOfSymbols()));
			LinearLayout linearLayout = (LinearLayout) view.getChildAt((value - 1) / columns);
			PaintableView virtualKeyboardButtonView = (PaintableView) linearLayout.getChildAt((value - 1) % columns);
			return virtualKeyboardButtonView;
		} else {
			return null;
		}
	}
}
