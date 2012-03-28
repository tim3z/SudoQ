/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Haiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.model.sudoku;

import de.sudoq.model.sudoku.sudokuTypes.HyperSudoku;
import de.sudoq.model.sudoku.sudokuTypes.SamuraiSudokuType;
import de.sudoq.model.sudoku.sudokuTypes.SquigglyASudokuType9x9;
import de.sudoq.model.sudoku.sudokuTypes.SquigglyBSudokuType9x9;
import de.sudoq.model.sudoku.sudokuTypes.StairStepSudokuType9x9;
import de.sudoq.model.sudoku.sudokuTypes.StandardSudokuType;
import de.sudoq.model.sudoku.sudokuTypes.StandardSudokuType16x16;
import de.sudoq.model.sudoku.sudokuTypes.StandardSudokuType4x4;
import de.sudoq.model.sudoku.sudokuTypes.StandardSudokuType6x6;
import de.sudoq.model.sudoku.sudokuTypes.SudokuTypes;
import de.sudoq.model.sudoku.sudokuTypes.TypeBasic;
import de.sudoq.model.sudoku.sudokuTypes.XSudoku;

/**
 * Der SudokuBuilder stellt Methoden zur Verfügung, um einen Sudoku-Typ oder ein
 * komplettes, leeres Sudoku zu erzeugen.
 */
public class SudokuBuilder {

	private PositionMap<Integer> solutions;
	private PositionMap<Boolean> setValues;
	private TypeBasic type;

	/** Constructors */

	/**
	 * Erstellt einen Builder fuer ein Sudoku des spezifizierten Typs.
	 * 
	 * @param type
	 *            der Enum-Typ des zu erstellenden Sudokus
	 * @throws NullPointerException
	 *             falls type invalid ist.
	 */
	public SudokuBuilder(SudokuTypes type) {
		this(createType(type));
	}

	/**
	 * Erstellt einen Builder fuer ein Sudoku des spezifizierten Typs.
	 * 
	 * @param type
	 *            Das SudokuType-Objekt für das zu erstellende Sudokus
	 * @throws NullPointerException
	 *             falls type null ist.
	 */
	public SudokuBuilder(TypeBasic type) {
		this.type = type;
		solutions = new PositionMap<Integer>(type.getSize());
		setValues = new PositionMap<Boolean>(type.getSize());
	}

	/** Methods */

	/**
	 * Erstellt ein Sudoku mit dem Sudoku Typ dieses Builders und den
	 * eingetragenen Loesungen.
	 * 
	 * @return Das passende Sudoku
	 */
	public Sudoku createSudoku() {
		return new Sudoku(type, solutions, setValues);
	}

	/**
	 * Fügt dem aktuellen Sudoku die gegebene Lösung hinzu
	 * 
	 * @param pos
	 *            die Position der Lösung
	 * @param value
	 *            der Wert der Lösung
	 * @throws IllegalArgumentException
	 *             falls der gegebene Wert kleiner 0 oder größer dem maximalen
	 *             für diesen Typ zugelassen Wert ist
	 */
	public void addSolution(Position pos, int value) {
		if (value < 0 || value >= type.getNumberOfSymbols()) {
			throw new IllegalArgumentException("Unvalid value for given Sudoku Type");
		}
		solutions.put(pos, value);
	}

	/**
	 * Setzt die gegebene Position als Vorgabe für das Sudoku
	 * 
	 * @param pos
	 *            die Position der Vorgabe
	 */
	public void setFixed(Position pos) {
		setValues.put(pos, true);
	}

	/**
	 * Erstellt ein SudokuType-Objekt entsprechend dem spezifizierten Typ-Namen
	 * und gibt dieses zurück. Ist der übergebene Name null oder kann er nicht
	 * zugeordnet werden, so wird null zurückgegeben.
	 * 
	 * @param type
	 *            Der Enum-Typ des zu erstellenden Sudoku Types
	 * @return Ein SudokuType-Objekt entsprechend dem spezifizierten Typnamen
	 *         oder null, falls der Name null ist oder nicht zugeordnet werden
	 *         kann
	 */
	public static TypeBasic createType(SudokuTypes type) {
		if (type == null)
			return null;

		switch (type) {
		case standard9x9:
			return new StandardSudokuType();
		case standard16x16:
			return new StandardSudokuType16x16();
		case standard4x4:
			return new StandardSudokuType4x4();
		case standard6x6:
			return new StandardSudokuType6x6();
		case Xsudoku:
			return new XSudoku();
		case HyperSudoku:
			return new HyperSudoku();
		case squigglya:
			return new SquigglyASudokuType9x9();
		case squigglyb:
			return new SquigglyBSudokuType9x9();
		case stairstep:
			return new StairStepSudokuType9x9();
		case samurai:
			return new SamuraiSudokuType();
		default:
			return null;
		}
	}
}
