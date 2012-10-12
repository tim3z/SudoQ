package de.sudoq.controller.sudoku.painter;

import android.graphics.Color;
import android.view.View;
import de.sudoq.model.sudoku.Constraint;

public class BuilderConstraintViewState extends ViewState {
	public BuilderConstraintViewState(View view) {
		super(view);
		this.constraint = null;
		this.active = false;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	private Constraint constraint;
	
	private boolean active;
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int getBackgroundColor() {
		if (isActive()) {
			return Color.RED;
		} else if (getConstraint() != null) {
			return Color.WHITE;
		} else {
			return Color.DKGRAY;
		}
	}

	@Override
	public int getBorderColor() {
		return 0;
	}

	@Override
	public int getTextColor() {
		return Color.BLACK;
	}
	
}
