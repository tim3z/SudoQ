package de.sudoq.controller.sudoku.painter;

import android.graphics.Color;
import android.view.View;

public class BuilderSudokuViewState extends ViewState {

	private boolean used;
	
	private boolean selected;
	
	private boolean connected;
	
	public BuilderSudokuViewState(View view) {
		super(view);
		
		this.used = false;
		this.selected = false;
		this.connected = false;
	}

	@Override
	public int getBackgroundColor() {
		int result = Color.rgb(30, 30, 30);
		if (selected) {
			result = Color.rgb(255, 100, 100);
		} else if (connected) {
			result = Color.rgb(220, 220, 255);
		} else if (used) {
			result = Color.rgb(250, 250, 250);
		}
		return result;
	}

	@Override
	public int getBorderColor() {
		return Color.DKGRAY;
	}

	@Override
	public int getTextColor() {
		return Color.BLACK;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
		updateView();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		updateView();
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
		updateView();
	}

}
