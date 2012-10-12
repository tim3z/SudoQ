package de.sudoq.controller.builder;

import de.sudoq.R;
import de.sudoq.controller.SudoqActivity;
import de.sudoq.controller.sudoku.painter.BuilderConstraintViewPainter;
import de.sudoq.controller.sudoku.painter.ViewPainter;
import de.sudoq.model.sudoku.ConstraintType;
import de.sudoq.model.sudoku.Position;
import de.sudoq.view.FullScrollLayout;
import de.sudoq.view.PaintableView;
import de.sudoq.view.RasterLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BuilderActivity extends SudoqActivity implements OnClickListener {

	private FullScrollLayout sudokuScrollView;
	
	private RasterLayout sudokuView;
	
	private Button lines;
	private Button blocks;
	private Button extras;
	private LinearLayout constraintContainer;
	
	private BuilderConstraintViewPainter linesPainter;
	private BuilderConstraintViewPainter blocksPainter;
	private BuilderConstraintViewPainter extrasPainter;
	
	private PaintableView[][] views;
	
	private static final int ROWS = 3;
	private static final int COLUMNS = 10;
	
	private BuilderSudokuViewManager sudokuViewManager;
	
	/**
	 * Wird beim ersten Anzeigen des Hauptmenüs aufgerufen. Inflated das Layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku_builder);
		
		this.sudokuScrollView = (FullScrollLayout) findViewById(R.id.sudoku_builder_field);
		this.sudokuView = new RasterLayout(this);
		this.sudokuView.setGravity(Gravity.CENTER);
		this.sudokuScrollView.addView(this.sudokuView);

		int height = getIntent().getIntExtra(BuilderPreferencesActivity.HEIGHT + "", 9);
		int width = getIntent().getIntExtra(BuilderPreferencesActivity.WIDTH + "", 9);
		this.sudokuViewManager = new BuilderSudokuViewManager(sudokuView, Position.get(width, height), 9);
		
		ViewTreeObserver vto = sudokuView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				sudokuView.optiZoom(sudokuScrollView.getMeasuredWidth(), sudokuScrollView.getMeasuredHeight());
				ViewTreeObserver obs = sudokuView.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
			}
		});
		
		
		this.lines = (Button) findViewById(R.id.sudoku_builder_lines);
		lines.setOnClickListener(this);
		this.blocks = (Button) findViewById(R.id.sudoku_builder_blocks);
		blocks.setOnClickListener(this);
		this.extras = (Button) findViewById(R.id.sudoku_builder_extra);
		extras.setOnClickListener(this);
		this.constraintContainer = (LinearLayout) findViewById(R.id.sudoku_builder_constraints);
		
		initialiseConstraintLayout(constraintContainer);
		initialisePainter();
	}
	
	
	private void initialiseConstraintLayout(LinearLayout layout) {
		views = new PaintableView[ROWS][COLUMNS];
		TableLayout t = new TableLayout(getBaseContext());
		
		LayoutParams viewParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
		viewParams.setMargins(1, 1, 1, 1);
		
		for (int y = 0; y < ROWS; y++) {
			TableRow row = new TableRow(getBaseContext());
			for (int x = 0; x < COLUMNS; x++) {
				views[y][x] = new PaintableView(getBaseContext());
				row.addView(views[y][x], viewParams);
			}
			t.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
		}
		
		layout.addView(t, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	
	private void initialisePainter() {
		linesPainter = new BuilderConstraintViewPainter();
		blocksPainter = new BuilderConstraintViewPainter();
		extrasPainter = new BuilderConstraintViewPainter();
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLUMNS; x++) {
				linesPainter.getState(views[y][x]).setText((y * COLUMNS + x) + "");
				blocksPainter.getState(views[y][x]).setText((y * COLUMNS + x + COLUMNS * ROWS) + "");
				extrasPainter.getState(views[y][x]).setText((y * COLUMNS + x + 2 * COLUMNS * ROWS) + "");
			}
		}
	}

	public void setConstraintType(ConstraintType type) {
		switch (type) {
		case LINE:
			setPainter(linesPainter);
			break;
		case BLOCK:
			setPainter(blocksPainter);
			break;
		case EXTRA:
			setPainter(extrasPainter);
		}
	}

	public void setPainter(ViewPainter painter) {
		for (int y = 0; y < ROWS; y++) {
			for (int x = 0; x < COLUMNS; x++) {
				views[y][x].setPainter(painter);
			}
		}
	}
	
	@Override
	public void onClick(View view) {
		if (view == lines) {
			setConstraintType(ConstraintType.LINE);
		} else if (view == blocks) {
			setConstraintType(ConstraintType.BLOCK);
		} else if (view == extras) {
			setConstraintType(ConstraintType.EXTRA);
		}
	}
	
	
	
	

}
