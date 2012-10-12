package de.sudoq.controller.sudoku;

import java.util.ArrayList;
import java.util.List;
import de.sudoq.controller.sudoku.painter.KeyboardFieldViewPainter;
import de.sudoq.controller.sudoku.painter.KeyboardFieldViewState;
import de.sudoq.view.KeyboardLayout;
import de.sudoq.view.PaintableView;
import de.sudoq.view.ViewClickListener;

public class KeyboardLayoutManager implements ViewClickListener, ObservableInput {

	private List<InputListener> inputListener;
	
	protected KeyboardLayout layout;
	
	protected KeyboardFieldViewPainter painter;
	
	protected Symbol symbolSet;
	
	public KeyboardLayoutManager(KeyboardLayout layout) {
		this.layout = layout;
		layout.registerListener(this);
		
		this.inputListener = new ArrayList<InputListener>();
		
		this.painter = new KeyboardFieldViewPainter();
		layout.setPainter(painter);
	}
	
	public void setSymbolSet(Symbol symbolSet) {
		this.symbolSet = symbolSet;
		
		if (symbolSet == null) {
			layout.refresh(0);
		} else {
			layout.refresh(symbolSet.getNumberOfSymbols());
			painter.setSymbolSet(symbolSet);
			initialisePainter();
		}
	}
	
	public Symbol getSymbolSet() {
		return this.symbolSet;
	}
	
	private void initialisePainter() {
		PaintableView currentView;
		for (int i = 0; i < symbolSet.getNumberOfSymbols(); i++) {
			currentView = layout.getView(i); 
			if (currentView != null) {
				KeyboardFieldViewState state = painter.getState(currentView);
				state.setValue(i);
			}
		}
	}

	@Override
	public void onViewClicked(PaintableView view) {
		notifyListener(painter.getState(view).getValue());
	}

	@Override
	public void notifyListener(int value) {
		for (InputListener listener : inputListener) {
			listener.onInput(value);
		}
		
	}

	@Override
	public void registerListener(InputListener listener) {
		if (listener != null) {
			this.inputListener.add(listener);
		}
		
	}

	@Override
	public void removeListener(InputListener listener) {
		this.inputListener.remove(listener);
	}
	
	public void setActivated(boolean activated) {
		layout.setVisible(activated);
	}
	
	public void enableAllButtons() {
		layout.enableAllButtons();
	}
	
	public void disableButton(int symbol) {
		layout.disableButton(symbol);
	}
	
}
