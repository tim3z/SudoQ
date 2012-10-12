package de.sudoq.controller.sudoku;

import de.sudoq.view.PaintableView;

public interface ObservableSelectedViewChanged {

	/**
	 * Benachrichtigt die Listener.
	 */
	void notifyListener(PaintableView oldView, PaintableView newView);

	/**
	 * Registriert einen Listener. Ist dieser null, so wird nichts getan.
	 * 
	 * @param listener
	 *            Der Listener der hinzugefügt werden soll.
	 */
	void registerListener(SelectedViewChangedListener listener);

	/**
	 * Entfernt einen Listener. Ist dieser nicht registriert, so wird nichts
	 * getan.
	 * 
	 * @param listener
	 *            Der Listener der entfernt werden soll.
	 */
	void removeListener(SelectedViewChangedListener listener);
}
