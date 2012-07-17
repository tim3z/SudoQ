package de.sudoq.model.sudoku.sudokuTypes;

import java.util.HashMap;
import java.util.Map;

public class SudokuTypeNames {
	private Map<Integer, String> names;
	private static SudokuTypeNames instance;
	
	private SudokuTypeNames() {
		names = new HashMap<Integer, String>();
	}
	
	public static SudokuTypeNames getInstance() {
		if (instance == null) {
			synchronized(SudokuTypeNames.class) {
				if (instance == null) {
					instance = new SudokuTypeNames();		
				}
			}
		}
		return instance;
	}
	
	public String getName(int id) {
		return names.get(id);
	}
	
}
