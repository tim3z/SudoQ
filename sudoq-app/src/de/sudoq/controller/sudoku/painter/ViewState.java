package de.sudoq.controller.sudoku.painter;

import android.view.View;

public abstract class ViewState {

	/**
	 * The view this state belongs to
	 */
	private View view;
	
	/**
	 * Initializes a new view state for the defined view
	 * @param view the view this state belongs to, must be not null
	 * @throws IllegalArgumentException thrown if view is null
	 */
	public ViewState(View view) {
		if (view == null) {
			throw new IllegalArgumentException("View was null.");
		}
		this.view = view;
	}
	
	/**
	 * Returns the view's current background color
	 * @return the current background color
	 */
	public abstract int getBackgroundColor();
	
	/**
	 * Returns the view's current border color
	 * @return the current border color
	 */
	public abstract int getBorderColor();
	
	/**
	 * Returns the view's current text color
	 * @return the current text color
	 */
	public abstract int getTextColor();
	
	/**
	 * Updates the view this state belongs to.
	 * Should be called every time the state changes.
	 */
	protected void updateView() {
		view.invalidate();
	}
}
