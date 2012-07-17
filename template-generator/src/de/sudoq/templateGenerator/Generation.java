package de.sudoq.templateGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.sudoq.model.files.FileManager;
import de.sudoq.model.solverGenerator.Generator;
import de.sudoq.model.solverGenerator.GeneratorCallback;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.model.sudoku.complexity.Complexity;
import de.sudoq.model.sudoku.sudokuTypes.SudokuType;
import de.sudoq.model.xml.SudokuXmlHandler;

public class Generation implements GeneratorCallback {

	private static final int NUMBER_OF_EACH_TYPE = 10;

	private int wholeCounter;
	private int counter[][];
	private int currentCount;

	public static void main(String[] arg) {
		new Generation().generateTemplates();
	}

	@Override
	public synchronized void generationFinished(Sudoku sudoku) {
		new SudokuXmlHandler().saveAsXml(sudoku);

		wholeCounter++;
		currentCount--;
		counter[sudoku.getSudokuType().getId()][sudoku.getComplexity()
				.ordinal()]++;

		notifyAll();
		System.out
				.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("Number of sudokus generated: " + wholeCounter);
		for (Integer type : SudokuType.getSudokuTypeIds()) {
			StringBuilder str = new StringBuilder();
			str.append(type.toString() + ": ");
			for (Complexity c : Complexity.values()) {
				str.append(c.toString() + ": " + counter[type][c.ordinal()]
						+ ", ");
			}
			System.out.println(str.toString());
		}
	}

	public void generateTemplates() {
		Generator generator = new Generator();
		this.wholeCounter = 0;
		this.currentCount = 0;

		File sudokus = new File(".." + File.separator + "sudoq-app" + File.separator
				+ "assets" + File.separator + "sudokus");
		sudokus.mkdir();
		FileManager.initialize(sudokus, sudokus);
		this.counter = new int[SudokuType.getSudokuTypeIds().size()][Complexity
				.values().length];

		for (Integer id : SudokuType.getSudokuTypeIds()) {
			for (Complexity c : Complexity.values()) {
				if (c != Complexity.arbitrary) {
					for (int i = 0; i < NUMBER_OF_EACH_TYPE; i++) {
						generator.generate(id, c, this);
						this.currentCount += 1;
						while (this.currentCount > 20) {
							synchronized (this) {
								try {
									wait();
									currentCount--;
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}

		while (wholeCounter < 10 * 4 * NUMBER_OF_EACH_TYPE) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
