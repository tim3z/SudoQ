/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.model.sudoku;

import de.sudoq.model.files.FileManager;
import de.sudoq.model.solverGenerator.Generator;
import de.sudoq.model.solverGenerator.GeneratorCallback;
import de.sudoq.model.solverGenerator.transformations.Transformer;
import de.sudoq.model.sudoku.complexity.Complexity;
import de.sudoq.model.xml.SudokuXmlHandler;

/**
 * Ist fuer die Verwaltung der vorhandenen Sudokus zuständig. Setzt das
 * Singleton Pattern um.
 */
public class SudokuManager implements GeneratorCallback {

	// private static final int[][] neccessaryCounts = new
	// int[SudokuTypes.values().length][Complexity.values().length];
	private Generator generator = new Generator();
	private Sudoku used = null;

	// neccessaryCounts[SudokuTypes.standard9x9.ordinal()][Complexity.easy.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard9x9.ordinal()][Complexity.medium.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard9x9.ordinal()][Complexity.difficult.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard9x9.ordinal()][Complexity.infernal.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard16x16.ordinal()][Complexity.easy.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard16x16.ordinal()][Complexity.medium.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard16x16.ordinal()][Complexity.difficult.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.standard16x16.ordinal()][Complexity.infernal.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.Xsudoku.ordinal()][Complexity.easy.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.Xsudoku.ordinal()][Complexity.medium.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.Xsudoku.ordinal()][Complexity.difficult.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.Xsudoku.ordinal()][Complexity.infernal.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.HyperSudoku.ordinal()][Complexity.easy.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.HyperSudoku.ordinal()][Complexity.medium.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.HyperSudoku.ordinal()][Complexity.difficult.ordinal()]
	// = 10;
	// neccessaryCounts[SudokuTypes.HyperSudoku.ordinal()][Complexity.infernal.ordinal()]
	// = 10;

	/**
	 * Das Callback fuer den Generator
	 */
	public void generationFinished(Sudoku sudoku) {
		new SudokuXmlHandler().saveAsXml(sudoku);
		FileManager.deleteSudoku(used);
	}

	/**
	 * Markiert ein Sudoku als benutzt. Falls möglich wird es transformiert,
	 * andernfalls gelöscht und ein neues generiert.
	 * 
	 * @param sudoku
	 *            das genutzte Sudoku
	 */
	public void usedSudoku(Sudoku sudoku) {
		if (sudoku.getTransformCount() >= 10) {
			used = sudoku;
			generator.generate(sudoku.getSudokuType().getId(), sudoku.getComplexity(), this);
		} else {
			Transformer.transform(sudoku);
			new SudokuXmlHandler().saveAsXml(sudoku);
		}
	}

	/**
	 * Gibt ein neues Sudoku des gewünschten Typs und der gewünschten
	 * Schwierigkeit zurück
	 * 
	 * @param t
	 *            Typ des Sudokus
	 * @param c
	 *            Schwierigkeit des Sudokus
	 * @return das neue Sudoku
	 */
	public static Sudoku getNewSudoku(int typeId, Complexity c) {
		Sudoku sudoku = getEmptySudokuToFillWithXml();
		new SudokuXmlHandler(typeId, c).createObjectFromXml(sudoku);
		return sudoku;
	}

	/**
	 * Erzeugt ein vollständig leeres Sudoku, welches noch gefüllt werden muss.
	 * DO NOT USE THIS METHOD (if you are not from us)
	 * 
	 * @return das neue Sudoku
	 */
	public static Sudoku getEmptySudokuToFillWithXml() {
		return new Sudoku();
	}

}
