package de.sudoq.controller.builder;

import de.sudoq.R;
import de.sudoq.controller.SudoqActivity;
import android.os.Bundle;

public class BuilderActivity extends SudoqActivity {

	
	/**
	 * Wird beim ersten Anzeigen des Hauptmenüs aufgerufen. Inflated das Layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku_builder);
		
		//SudokuLayout layout = new SudokuLayout(this);
		
		
		//findViewById(R.id.sudoku_builder_field)
	}
	

}
