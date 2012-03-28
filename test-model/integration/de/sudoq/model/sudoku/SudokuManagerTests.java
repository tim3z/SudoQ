package de.sudoq.model.sudoku;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.sudoq.model.files.FileManager;
import de.sudoq.model.profile.Profile;
import de.sudoq.model.sudoku.complexity.Complexity;
import de.sudoq.model.sudoku.sudokuTypes.SudokuTypes;

public class SudokuManagerTests {

	private static File profiles = new File("res/tmp_profiles");
	private static File sudokus = new File("res/tmp_sudokus");

	@BeforeClass
	public static void init() throws IOException {
		profiles.mkdir();
		sudokus.mkdir();
		copyDirectory(new File(".." + File.separator + "sudokus"), sudokus);
		FileManager.initialize(profiles, sudokus);
		Profile.getInstance();
	}

	@AfterClass
	public static void clean() throws IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		java.lang.reflect.Field f = FileManager.class.getDeclaredField("profiles");
		f.setAccessible(true);
		f.set(null, null);
		java.lang.reflect.Field s = FileManager.class.getDeclaredField("sudokus");
		s.setAccessible(true);
		s.set(null, null);
		java.lang.reflect.Field p = Profile.class.getDeclaredField("instance");
		p.setAccessible(true);
		p.set(null, null);
		FileManager.deleteDir(profiles);
		FileManager.deleteDir(sudokus);
	}

	private static void copyDirectory(File src, File target) throws IOException {
		for (File subFile : src.listFiles()) {
			File targetSubFile = new File(target, subFile.getName());
			if (!targetSubFile.exists() && !subFile.isDirectory()) {
				InputStream in = null;
				OutputStream out = null;
				in = new FileInputStream(subFile);
				out = new FileOutputStream(targetSubFile);
				copyFile(in, out);
				in.close();
				out.flush();
				out.close();
			} else {
				targetSubFile.mkdir();
				copyDirectory(subFile, targetSubFile);
			}
		}
	}

	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Test
	public void test() {
		assertEquals(10, FileManager.getSudokuCountOf(SudokuTypes.standard9x9, Complexity.infernal));
		Sudoku s = SudokuManager.getNewSudoku(SudokuTypes.standard9x9, Complexity.infernal);
		for (int i = 0; i < 10; i++) {
			s.increaseTransformCount();
		}
		SudokuManager sm = new SudokuManager() {
			public void generationFinished(Sudoku sudoku) {
				synchronized (SudokuManagerTests.this) {
					super.generationFinished(sudoku);
					SudokuManagerTests.this.notifyAll();
				}
			}
		};
		sm.usedSudoku(s);
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertEquals(10, FileManager.getSudokuCountOf(SudokuTypes.standard9x9, Complexity.infernal));
	}

}
