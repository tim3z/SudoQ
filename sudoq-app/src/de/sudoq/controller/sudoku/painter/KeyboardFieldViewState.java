package de.sudoq.controller.sudoku.painter;

import android.graphics.Color;
import android.view.View;

public class KeyboardFieldViewState extends ViewState {
	private boolean input;
	private boolean selected;
	private int value;

	public KeyboardFieldViewState(View view) {
		super(view);
		
		this.input = false;
		this.selected = false;
		this.value = -1;
	}
	
	public boolean isInput() {
		return input;
	}
	public void setInput(boolean input) {
		this.input = input;
		updateView();
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
		updateView();
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
		updateView();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getBackgroundColor() {
		int result = Color.rgb(250, 250, 250);
		if (selected && input) {
			result = Color.rgb(255, 100, 100);
		} else if (selected) {
			result = Color.YELLOW;
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getBorderColor() {
		return Color.DKGRAY;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getTextColor() {
		int result = Color.BLACK;
		
		return result;
		
	}
}
