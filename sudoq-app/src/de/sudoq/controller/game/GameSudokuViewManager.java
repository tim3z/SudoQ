package de.sudoq.controller.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.sudoq.controller.sudoku.ObservableSelectedViewChanged;
import de.sudoq.controller.sudoku.SelectedViewChangedListener;
import de.sudoq.controller.sudoku.SudokuViewManager;
import de.sudoq.controller.sudoku.Symbol;
import de.sudoq.controller.sudoku.painter.GameFieldViewPainter;
import de.sudoq.controller.sudoku.painter.GameFieldViewState;
import de.sudoq.model.ModelChangeListener;
import de.sudoq.model.game.Assistances;
import de.sudoq.model.game.Game;
import de.sudoq.model.profile.Profile;
import de.sudoq.model.sudoku.Constraint;
import de.sudoq.model.sudoku.ConstraintType;
import de.sudoq.model.sudoku.Field;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.view.PaintableView;
import de.sudoq.view.RasterLayout;

public class GameSudokuViewManager extends SudokuViewManager implements ModelChangeListener<Field>, ObservableSelectedViewChanged {

	private List<SelectedViewChangedListener> selectedViewChangedListener;
	
	private Game game;

	private Map<PaintableView, Constraint> rows;
	private Map<PaintableView, Constraint> columns;
	private Map<PaintableView, Boolean> extra;

	private GameFieldViewPainter painter;
	
	private PaintableView selectedView;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param layout
	 * @param game
	 * @throws NullPointerException
	 *             thrown if game is null
	 */
	public GameSudokuViewManager(RasterLayout rasterLayout, Game game) {
		super(rasterLayout);
		
		if (game == null) {
			throw new IllegalArgumentException("Game was null");
		}
		
		this.selectedViewChangedListener = new ArrayList<SelectedViewChangedListener>();
		
		this.game = game;
		game.getSudoku().registerListener(this);
		
		this.painter = new GameFieldViewPainter();
		setSudoku(game.getSudoku(), painter);
		getSudokuLayout().registerListener(this);

		initialiseRowColumn();
		initialiseExtraConstraints();
		initialisePainter();
	}

	public void setSymbolSet(Symbol symbolSet) {
		painter.setSymbolSet(symbolSet);
	}
	
	
	/**
	 * Operation wird nicht unterstützt.
	 */
	public void setSudoku(Sudoku sudoku) {
	}

	private void initialiseRowColumn() {
		rows = new HashMap<PaintableView, Constraint>();
		columns = new HashMap<PaintableView, Constraint>();

		RasterLayout layout = getSudokuLayout();

		ArrayList<Constraint> allConstraints = this.getSudoku().getSudokuType()
				.getConstraints();
		ArrayList<Position> positions = null;
		for (int constrNum = 0; constrNum < allConstraints.size(); constrNum++) {
			if (allConstraints.get(constrNum).getType()
					.equals(ConstraintType.LINE)) {
				positions = allConstraints.get(constrNum).getPositions();
				for (int i = 0; i < positions.size(); i++) {
					if (rows.get(layout.getView(positions.get(i))) == null) {
						rows.put(layout.getView(positions.get(i)),
								allConstraints.get(constrNum));
					} else {
						columns.put(
								layout.getView(positions.get(i)),
								allConstraints.get(constrNum));
					}
				}
			}
		}
	}

	private void initialiseExtraConstraints() {
		this.extra = new HashMap<PaintableView, Boolean>();
		ArrayList<Constraint> constraints = game.getSudoku().getSudokuType()
				.getConstraints();
		ArrayList<Position> positions = null;
		RasterLayout layout = getSudokuLayout();
		for (int i = 0; i < constraints.size(); i++) {
			if (constraints.get(i).getType().equals(ConstraintType.EXTRA)) {
				positions = constraints.get(i).getPositions();
				for (int j = 0; j < positions.size(); j++) {
					extra.put(layout.getView(positions.get(j)), true);
				}
			}
		}
	}

	@Override
	public void onViewClicked(PaintableView view) {
		PaintableView oldView = selectedView;
		RasterLayout layout = getSudokuLayout();
		if (selectedView != view) {
			if (selectedView != null) {
				Constraint c = rows.get(selectedView);
				if (c != null) {
					for (Position p : c) {
						markFieldNormal(layout.getView(p));
					}
				}
				c = columns.get(selectedView);
				if (c != null) {
					for (Position p : c) {
						markFieldNormal(layout.getView(p));
					}
				}
			}

			selectedView = view;
			
			if (selectedView != null) {
				// Set initial mode
				painter.getState(selectedView).setInput(!Profile.getInstance().isGestureActive());
			
				// Mark connected fields if assistance available
				if (this.game.isAssistanceAvailable(Assistances.markRowColumn)) {
					for (Position p : rows.get(selectedView)) {
						painter.getState(layout.getView(p)).setConnected(true);
					}
					for (Position p : columns.get(selectedView)) {
						painter.getState(layout.getView(p)).setConnected(true);
					}
				}
				
				// Mark selected if not finished and not fixed
				if (!game.isFinished()) {
					painter.getState(selectedView).setSelected(true);
				}
			}
			
			
		} else if (!game.isFinished() && !Profile.getInstance().isGestureActive()) {
			// Toggle input / note mode
			painter.getState(selectedView).setInput(!painter.getState(selectedView).isInput());
			selectedView.invalidate();
		}
	
		notifyListener(oldView, selectedView);
	}
	
	private void initialisePainter() {
		PaintableView currentFieldView;
		for (int x = 0; x < this.game.getSudoku().getSudokuType().getSize().getX(); x++) {
			for (int y = 0; y < this.game.getSudoku().getSudokuType().getSize().getY(); y++) {
				currentFieldView = getSudokuLayout().getView(Position.get(x, y)); 
				if (currentFieldView != null) {
					GameFieldViewState state = painter.getState(currentFieldView);
					state.setField(getSudoku().getField(Position.get(x, y)));
					if (extra.get(currentFieldView) != null) {
						state.setDarken(true);
					}
					if (!state.getField().isEditable()) {
						state.setFixed(true);
					}
				}
			}
		}
		
	}
	
	
	private void markFieldNormal(PaintableView view) {
		GameFieldViewState state = painter.getState(view);
		state.setConnected(false);
		state.setSelected(false);
	}
	
	public Field getField(PaintableView view) {
		if (view == null) {
			return null;
		}
		return this.painter.getState(view).getField();
	}

	@Override
	public void onModelChanged(Field field) {
		getSudokuLayout().getView(game.getSudoku().getPosition(field.getId())).invalidate();
		
		if (game.isAssistanceAvailable(Assistances.markWrongSymbol)) {
			for (Constraint c : game.getSudoku().getSudokuType()) {
				if (c.includes(game.getSudoku().getPosition(field.getId()))) {
					for (Position p : c) {
						if (!game.getSudoku().getField(p).isNotWrong()) {
							if (checkConstraint(game.getSudoku().getField(p))) {
								painter.getState(getSudokuLayout().getView(p)).setWrong(true);
							} else {
								painter.getState(getSudokuLayout().getView(p)).setWrong(false);
							}
						} else {
							painter.getState(getSudokuLayout().getView(p)).setWrong(false);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gibt true zurück, falls der Wert dieses Feldes die Constraints verletzt.
	 * Überprüft werden nur UniqueConstraints. Befindet sich das Feld in einem
	 * anderen ConstraintTyp wird immer fals ezurückgegeben.
	 * 
	 * @return true, falls der Wert dieses Feldes die UniqueConstraints verletzt
	 *         oder sich in einem anderen ConstraintTyp befindet, false sonst
	 */
	private boolean checkConstraint(Field field) {
		ArrayList<Constraint> constraints = this.game.getSudoku().getSudokuType().getConstraints();
		ArrayList<Position> positions;
		for (int i = 0; i < constraints.size(); i++) {
			if (constraints.get(i).includes(this.game.getSudoku().getPosition(field.getId()))) {
				if (constraints.get(i).hasUniqueBehavior()) {
					positions = constraints.get(i).getPositions();
					for (int k = 0; k < positions.size(); k++) {
						if (positions.get(k) != this.game.getSudoku().getPosition(field.getId())
								&& this.game.getSudoku().getField(positions.get(k)).getCurrentValue() == field.getCurrentValue()) {
							return true;
						}
					}
				} else {
					return true;
				}
			}
		}

		return false;
	}
	
	
	
	public PaintableView getSelectedView() {
		return this.selectedView;
	}

	@Override
	public void notifyListener(PaintableView oldView, PaintableView newView) {
		for (SelectedViewChangedListener listener : selectedViewChangedListener) {
			listener.onSelectedViewChanged(oldView, newView);
		}
	}

	@Override
	public void registerListener(SelectedViewChangedListener listener) {
		if (listener != null) {
			this.selectedViewChangedListener.add(listener);
		}
	}

	@Override
	public void removeListener(SelectedViewChangedListener listener) {
		this.selectedViewChangedListener.remove(listener);
	}
	
}
