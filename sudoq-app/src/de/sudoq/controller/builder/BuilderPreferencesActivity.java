package de.sudoq.controller.builder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import de.sudoq.R;
import de.sudoq.controller.SudoqActivity;

public class BuilderPreferencesActivity extends SudoqActivity {

	public static final String WIDTH = "WIDTH";
	public static final String HEIGHT = "HEIGHT";
	
	/**
	 * Wird beim ersten Anzeigen des Hauptmenüs aufgerufen. Inflated das Layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku_builder_preferences);
	}
	
	public void startBuilder(View button) {
		switch(button.getId()) {
		case R.id.sudoku_builder_start:
			Intent builderIntent = new Intent(this, BuilderActivity.class);
			builderIntent.putExtra(WIDTH, Integer.parseInt(((EditText)findViewById(R.id.sudoku_builder_size_x)).getText().toString()));
			builderIntent.putExtra(HEIGHT, Integer.parseInt(((EditText)findViewById(R.id.sudoku_builder_size_y)).getText().toString()));
			startActivity(builderIntent);
			break;
		}
	}
}
