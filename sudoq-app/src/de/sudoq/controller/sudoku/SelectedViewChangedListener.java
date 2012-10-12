package de.sudoq.controller.sudoku;

import de.sudoq.view.PaintableView;

public interface SelectedViewChangedListener {

	public void onSelectedViewChanged(PaintableView oldView, PaintableView newView);
}
