package de.sudoq.controller.game;

import de.sudoq.controller.sudoku.KeyboardLayoutManager;
import de.sudoq.controller.sudoku.painter.KeyboardFieldViewState;
import de.sudoq.model.ModelChangeListener;
import de.sudoq.model.sudoku.Field;
import de.sudoq.view.KeyboardLayout;

public class GameKeyboardLayoutManager extends KeyboardLayoutManager implements ModelChangeListener<Field> {

	private Field field;
	
	private boolean noteMode;
	
	
	public GameKeyboardLayoutManager(KeyboardLayout layout) {
		super(layout);
	}

	
	public void setSelectedField(Field field) {
		this.field = field;
		updateState();
		
	}
	
	public void setNoteMode(boolean noteMode) {
		this.noteMode = noteMode;
		updateState();
	}


	@Override
	public void onModelChanged(Field field) {
		if (field == this.field) {
			updateState();
		}
	}
	
	private void updateState() {
		if (symbolSet == null)
			return;
		for (int i = 0; i < symbolSet.getNumberOfSymbols(); i++) {
			KeyboardFieldViewState state = painter.getState(layout.getView(i));
			state.setSelected(false);
			if (field != null) {
				state.setInput(!noteMode);
				state.setSelected((!noteMode && field.getCurrentValue() == i) 
					|| (noteMode && field.isNoteSet(i)));
			}
		}
	}
		
}
