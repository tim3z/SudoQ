package de.sudoq.model.sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.sudoq.model.sudoku.Field;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.model.sudoku.SudokuBuilder;
import de.sudoq.model.sudoku.sudokuTypes.SudokuTypes;

public class SudokuBuilderTests {

	Field field;

	@Test
	public void testInitialisation() {
		for (SudokuTypes t : SudokuTypes.values()) {

			if (t != SudokuTypes.samurai && t != SudokuTypes.standard16x16 && t != SudokuTypes.standard4x4
					&& t != SudokuTypes.standard6x6)
				testBuildergeneric(t, 9);
			testBuildergeneric(SudokuTypes.samurai, 21);
			testBuildergeneric(SudokuTypes.standard16x16, 16);
			testBuildergeneric(SudokuTypes.standard6x6, 6);
			testBuildergeneric(SudokuTypes.standard4x4, 4);

		}
		assertEquals(SudokuBuilder.createType(null), null);
	}

	private void testBuildergeneric(SudokuTypes t, int length) {
		Sudoku sudoku = new SudokuBuilder(t).createSudoku();
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				field = sudoku.getField(Position.get(i, j));
				if (field != null)
					assertEquals(field.getCurrentValue(), Field.EMPTYVAL);
			}
		}
	}

	@Test
	public void testBuilderWithSolutions() {
		SudokuBuilder sb = new SudokuBuilder(SudokuTypes.standard9x9);
		sb.addSolution(Position.get(0, 0), 5);
		sb.setFixed(Position.get(0, 0));
		sb.addSolution(Position.get(0, 1), 3);
		Sudoku s = sb.createSudoku();

		assertEquals(s.getField(Position.get(0, 0)).getSolution(), 5);
		assertEquals(s.getField(Position.get(0, 0)).getCurrentValue(), 5);
		assertEquals(s.getField(Position.get(0, 1)).getSolution(), 3);
		assertEquals(s.getField(Position.get(0, 1)).getCurrentValue(), Field.EMPTYVAL);

		try {
			sb.addSolution(Position.get(1, 3), -5);
			fail("no exception");
		} catch (IllegalArgumentException e) {
			// great
		}
		try {
			sb.addSolution(Position.get(1, 3), 9);
			fail("no exception");
		} catch (IllegalArgumentException e) {
			// great
		}
	}

}