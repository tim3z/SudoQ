package de.sudoq.controller.sudoku.painter;

import android.graphics.Color;
import android.view.View;
import de.sudoq.model.sudoku.Field;

public class GameFieldViewState extends ViewState {
	
	private boolean connected;
	private boolean selected;
	private boolean input;
	private boolean fixed;
	private boolean wrong;
	private boolean darken;
	private Field field;
	

	public Field getField() {
		return field;
	}


	public void setField(Field field) {
		this.field = field;
		updateView();
	}


	public GameFieldViewState(View view) {
		super(view);
		
		this.connected = false;
		this.selected = false;
		this.input = false;
		this.fixed = false;
		this.wrong = false;
		this.darken = false;
	}
	
	
	public boolean isDarken() {
		return darken;
	}

	public void setDarken(boolean darken) {
		this.darken = darken;
		updateView();
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.selected = false;
		this.connected = connected;
		updateView();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.connected = false;
		this.selected = selected;
		updateView();
	}

	public boolean isInput() {
		return input;
	}

	public void setInput(boolean input) {
		this.input = input;
		updateView();
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
		updateView();
	}

	public boolean isWrong() {
		return wrong;
	}

	public void setWrong(boolean wrong) {
		this.wrong = wrong;
		updateView();
	}
	
	public int getBackgroundColor() {
		int result = Color.rgb(250, 250, 250);
		if (selected && input && !fixed) {
			result = Color.rgb(255, 100, 100);
		} else if (selected && !fixed) {
			result = Color.YELLOW;
		} else if (connected || (selected && fixed)) {
			result = Color.rgb(220, 220, 255);
		}
		return result;
	}
	
	public int getBorderColor() {
		return Color.DKGRAY;
	}
	
	public int getTextColor() {
		int result = Color.BLACK;
		if (wrong) {
			result = Color.RED;
		} else if (fixed) {
			result = Color.rgb(0, 100, 0);
		}
		return result;
		
	}
	
}
