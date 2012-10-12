/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.controller.menus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import de.sudoq.R;
import de.sudoq.controller.SudoqActivity;
import de.sudoq.controller.game.SudokuActivity;
import de.sudoq.model.game.AssistanceSet;
import de.sudoq.model.game.Game;
import de.sudoq.model.game.GameManager;
import de.sudoq.model.game.GameType;
import de.sudoq.model.profile.Profile;
import de.sudoq.model.sudoku.Constraint;
import de.sudoq.model.sudoku.ConstraintType;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.UniqueConstraintBehavior;
import de.sudoq.model.sudoku.complexity.Complexity;
import de.sudoq.model.sudoku.sudokuTypes.SudokuType;
import de.sudoq.model.sudoku.sudokuTypes.SudokuTypeNames;

/**
 * SudokuPreferences ermöglicht das Verwalten von Einstellungen eines zu
 * startenden Sudokus.
 */
public class SudokuPreferencesActivity extends SudoqActivity {
	/** Attributes */
	private Intent startGameIntent;

	/**
	 * Konstante für den Key des GameTypes
	 */
	public static final String DESIRED_GAME_TYPE = "desired_game_type";

	/**
	 * Konstante für den Wert des Game Typs
	 */
	public static final int NO_GAME_TYPE = 0;
	/**
	 * Konstante für den Wert des Game Typs
	 */
	public static final int LOCAL_GAME = 1;
	/**
	 * Konstante für den Wert des Game Typs
	 */
	public static final int MULTIPLAYER_GAME = 2;

	private static final String LOG_TAG = SudokuPreferencesActivity.class.getSimpleName();

	private int sudokuType;

	private GameType gameType;

	private Complexity complexity;
	
	public static AssistanceSet assistances;

	/** Constructors */

	/**
	 * Instanziiert ein neues SudokuPreferences-Objekt.
	 */

	/** Methods */

	/**
	 * Wird beim ersten Aufruf der SudokuPreferences aufgerufen. Die Methode
	 * inflated das Layout der Preferences.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sudokupreferences);

		Spinner typeSpinner = (Spinner) findViewById(R.id.spinner_sudokutype);
		Spinner complexitySpinner = (Spinner) findViewById(R.id.spinner_sudokucomplexity);

		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
				this, R.array.sudokutype_values, android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeSpinner.setAdapter(typeAdapter);

		ArrayAdapter<CharSequence> complexityAdapter = ArrayAdapter.createFromResource(
				this, R.array.sudokucomplexity_values, android.R.layout.simple_spinner_item);
		complexityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		complexitySpinner.setAdapter(complexityAdapter);

		// nested Listener for typeSpinner
		complexitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {
				String item = parent.getItemAtPosition(pos).toString();

				if (item.equals(getResources().getString(R.string.complexity_easy))) {
					setSudokuDifficulty(Complexity.easy);
				} else if (item.equals(getResources().getString(R.string.complexity_medium))) {
					setSudokuDifficulty(Complexity.medium);
				} else if (item.equals(getResources().getString(R.string.complexity_difficult))) {
					setSudokuDifficulty(Complexity.difficult);
				} else if (item.equals(getResources().getString(R.string.complexity_infernal))) {
					setSudokuDifficulty(Complexity.infernal);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});

		// nested Listener for complexitySpinner
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {
				String item = parent.getItemAtPosition(pos).toString();

				if (item.equals(getResources().getString(R.string.sudoku_type_standard_9x9))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_standard_16x16))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_standard_6x6))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_standard_4x4))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_squiggly_a_9x9))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_squiggly_b_9x9))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_stairstep_9x9))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_hyper))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_xsudoku))) {
					setSudokuType(0);
				} else if (item.equals(getResources().getString(R.string.sudoku_type_samurai))) {
					setSudokuType(0);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// do nothing
			}
		});
		
		// TODO ugly ugly ugly ugly, not static!!
		SudokuPreferencesActivity.assistances = AssistanceSet.fromString(Profile.getInstance().getAssistances().convertToString());
	}

	/**
	 * Wird aufgerufen, wenn die Activity in den Vordergrund gelangt. Die
	 * Preferences werden hier neu geladen.
	 */
	@Override
	public void onResume() {
		super.onResume();
		int desiredGameType = getIntent().getIntExtra(DESIRED_GAME_TYPE, NO_GAME_TYPE);
		switch (desiredGameType) {
		case LOCAL_GAME:
			this.startGameIntent = new Intent(this, SudokuActivity.class);
			this.gameType = GameType.LOCAL;
			break;
		// TODO: Enable MP in SudokuPreferences
		// case MULTIPLAYER_GAME:
		// this.startGameIntent = new Intent(this,
		// SudokuMultiplayerActivity.class);
		// this.gameType = GameType.MULTIPLAYER;
		// break;
		}
	}

	/*
	 * {@inheritDoc}
	 */
	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	// }

	/**
	 * Die Methode startet per Intent ein Sudokus mit den eingegebenen
	 * Einstellungen.
	 * 
	 * @param view
	 *            von android xml übergebene View
	 */
	public void startGame(View view) {
		
		/*startActivity(this.startGameIntent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
		if (this.sudokuType != -1 && this.complexity != null && this.gameType != null && this.assistances != null) {
			try {// this.sudokuType statt 0
				Game game = GameManager.getInstance().newGame(0, this.complexity, this.gameType, this.assistances);
				Profile.getInstance().setCurrentGame(game.getId());
				startActivity(this.startGameIntent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, getString(R.string.sf_sudokupreferences_copying), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.error_sudoku_preference_incomplete), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Setzt den Sudokutyp des zu startenden Sudokus. Ist dieser null oder
	 * ungültig, so wird nichts getan
	 * 
	 * @param type
	 *            Typ des zu startenden Sudokus
	 */
	public void setSudokuType(int type) {
		this.sudokuType = type;
		Log.d(LOG_TAG, "type changed to:" + SudokuTypeNames.getInstance().getName(type));
	}

	/**
	 * Setzt die Schwierigkeit des zu startenden Sudokus. Ist diese null, so
	 * wird nichts getan.
	 * 
	 * @param difficulty
	 *            Schwierigkeit des zu startenden Sudokus
	 */
	public void setSudokuDifficulty(Complexity difficulty) {
		this.complexity = difficulty;
		Log.d(LOG_TAG, "complexity changed to:" + difficulty.toString());
	}

	/**
	 * Ruft die PrefererencesActivityNew auf mit dem Intent, dort nur
	 * Assistances einzustellen.
	 * 
	 * @param view
	 *            von android xml übergebene View
	 */
	public void switchToAssistances(View view) {
		Intent assistancesIntent = new Intent(this, AssistancesPreferencesActivity.class);
//		assistancesIntent.putExtra(PlayerPreferencesActivity.INTENT_ONLYASSISTANCES, true);
		startActivity(assistancesIntent);
	}
}
