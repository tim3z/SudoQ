package de.sudoq.controller.menus;

import de.sudoq.controller.sudoku.KeyboardLayoutManager;
import de.sudoq.controller.sudoku.painter.KeyboardFieldViewState;
import de.sudoq.view.KeyboardLayout;

public class GestureKeyboardLayoutManager extends KeyboardLayoutManager {

	public GestureKeyboardLayoutManager(KeyboardLayout layout) {
		super(layout);
	}
	
	public void setAssigned(int i) {
		KeyboardFieldViewState state = painter.getState(layout.getView(i));
		state.setSelected(true);
		state.setInput(false);
	}
	
}
