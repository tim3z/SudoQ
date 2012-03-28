package de.sudoq.test;

import android.app.Activity;
import de.sudoq.R;
import de.sudoq.controller.menus.MainActivity;
import de.sudoq.controller.menus.SudokuPreferencesActivity;
import de.sudoq.controller.sudoku.SudokuActivity;

public class CrossActivityTests extends SudoqTestCase {

	// AT10
	public void testSudokuStarting() {
		Activity a = getActivity();

		String continueSudoku = a.getString(R.string.sf_mainmenu_continue);
		String newSudoku = a.getString(R.string.sf_mainmenu_new_sudoku);

		assertTrue(solo.searchText(continueSudoku));
		assertTrue(solo.searchText(newSudoku));
		assertFalse(solo.getButton(continueSudoku).isEnabled());

		solo.clickOnText(newSudoku);
		solo.assertCurrentActivity("should be sudokupreferences", SudokuPreferencesActivity.class);
		assertTrue(solo.searchText(a.getString(R.string.complexity_easy)));
		assertTrue(solo.searchText(a.getString(R.string.sudoku_type_standard_9x9)));
		assertTrue(solo.searchText(a.getString(R.string.sf_sudokupreferences_start)));

		solo.clickOnText(a.getString(R.string.sf_sudokupreferences_start));
		solo.assertCurrentActivity("should be in sudoku", SudokuActivity.class);

		solo.goBack();
		solo.goBack();
		solo.assertCurrentActivity("should be mainactivity", MainActivity.class);

		assertTrue(solo.getButton(continueSudoku).isEnabled());
		solo.clickOnText(continueSudoku);
		solo.assertCurrentActivity("should be in sudoku", SudokuActivity.class);
	}
}
