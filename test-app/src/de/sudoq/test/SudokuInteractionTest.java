package de.sudoq.test;

import de.sudoq.R;
import de.sudoq.controller.game.SudokuActivity;
import de.sudoq.controller.menus.SudokuPreferencesActivity;
import de.sudoq.model.profile.Profile;
import de.sudoq.model.sudoku.Field;
import de.sudoq.view.PaintableView;
import de.sudoq.view.KeyboardLayout;

public class SudokuInteractionTest extends SudoqTestCase {

	// MT30-MT80, MT100
	public void testFieldSelection() {
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.sf_mainmenu_new_sudoku));
		solo.assertCurrentActivity("should be sudokupreferences", SudokuPreferencesActivity.class);
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.sf_sudokupreferences_start));
		solo.assertCurrentActivity("should be in sudoku", SudokuActivity.class);

		SudokuActivity a = (SudokuActivity) solo.getCurrentActivity();
		PaintableView[][] views = SudokuUtilities.getViewArray(a);
		solo.clickOnView(views[5][3]);
		assertTrue(a.getCurrentFieldView() == views[5][3]);

		boolean editable = false;
		boolean locked = false;
		int x = -1;
		int y = -1;
		for (int i = 0; i < views.length && (!editable || !locked); i++) {
			for (int j = 0; j < views[i].length && (!editable || !locked); j++) {
				solo.clickOnView(views[i][j]);
				assertTrue(a.getCurrentFieldView() == views[i][j]);
				if (a.getGameSudokuViewManager().getField(views[i][j]).isEditable()) {
					assertTrue(((KeyboardLayout) a.findViewById(R.id.virtual_keyboard)).isVisible());
					editable = true;
					x = i;
					y = j;
				} else {
					assertFalse(((KeyboardLayout) a.findViewById(R.id.virtual_keyboard)).isVisible());
					locked = true;
				}
			}
		}

		if (a.getCurrentFieldView() != views[x][y]) {
			solo.clickOnView(views[x][y]);
		}
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 5));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 4);
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 2));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 1);
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 2));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == Field.EMPTYVAL);
		solo.clickOnView(views[8][8]);

		x = -1;
		y = -1;
		for (int i = 0; i < views.length && (x == -1 || y == -1); i++) {
			for (int j = 0; j < views[i].length && (x == -1 || y == -1); j++) {
				if (a.getGameSudokuViewManager().getField(views[i][j]).isEditable() && a.getGameSudokuViewManager().getField(views[i][j]).isEmpty()
						&& a.getCurrentFieldView() != views[i][j]) {
					solo.clickOnView(views[i][j]);
					solo.clickOnView(views[i][j]);
					assertTrue(a.getCurrentFieldView() == views[i][j]);
					assertTrue(((KeyboardLayout) a.findViewById(R.id.virtual_keyboard)).isVisible());
					x = i;
					y = j;
				}
			}
		}

		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 5));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).isNoteSet(4));
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 6));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).isNoteSet(4));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).isNoteSet(5));
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 5));
		assertFalse(a.getGameSudokuViewManager().getField(views[x][y]).isNoteSet(4));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).isNoteSet(5));

		solo.clickOnView(a.findViewById(R.id.button_sudoku_toggle_gesture));
		assertTrue(Profile.getInstance().isGestureActive() || solo.waitForText(solo.getCurrentActivity().getString(R.string.error_gestures_not_complete)));
	}

	// AT100
	public void testUndoRedo() {
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.sf_mainmenu_new_sudoku));
		solo.assertCurrentActivity("should be sudokupreferences", SudokuPreferencesActivity.class);
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.sf_sudokupreferences_start));
		solo.assertCurrentActivity("should be in sudoku", SudokuActivity.class);

		SudokuActivity a = (SudokuActivity) solo.getCurrentActivity();
		PaintableView[][] views = SudokuUtilities.getViewArray(a);

		int x = -1;
		int y = -1;
		for (int i = 0; i < views.length && (x == -1 || y == -1); i++) {
			for (int j = 0; j < views[i].length && (x == -1 || y == -1); j++) {
				if (a.getGameSudokuViewManager().getField(views[i][j]).isEditable() && a.getGameSudokuViewManager().getField(views[i][j]).isEmpty()) {
					solo.clickOnView(views[i][j]);
					assertTrue(a.getCurrentFieldView() == views[i][j]);
					assertTrue(((KeyboardLayout) a.findViewById(R.id.virtual_keyboard)).isVisible());
					x = i;
					y = j;
				}
			}
		}

		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 9));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 8);
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 1));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 0);
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 7));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 6);

		solo.clickOnView(a.findViewById(R.id.button_sudoku_undo));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 0);
		solo.clickOnView(a.findViewById(R.id.button_sudoku_undo));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 8);
		solo.clickOnView(a.findViewById(R.id.button_sudoku_redo));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 0);
		solo.clickOnView(a.findViewById(R.id.button_sudoku_redo));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 6);
		solo.clickOnView(a.findViewById(R.id.button_sudoku_redo));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 6);
		solo.clickOnView(a.findViewById(R.id.button_sudoku_undo));
		solo.clickOnView(SudokuUtilities.getKeyboardButton(a, 3));
		assertTrue(a.getGameSudokuViewManager().getField(views[x][y]).getCurrentValue() == 2);

		solo.clickOnView(a.findViewById(R.id.button_sudoku_actionTree));
		solo.clickOnText(a.getString(R.string.sf_sudoku_button_bookmark));
		solo.clickOnText(a.getString(R.string.sf_sudoku_button_close));
	}
}
