/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Haiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.model.sudoku.sudokuTypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.sudoq.model.files.FileManager;
import de.sudoq.model.sudoku.Constraint;
import de.sudoq.model.sudoku.ConstraintType;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.Sudoku;
import de.sudoq.model.sudoku.UniqueConstraintBehavior;
import de.sudoq.model.sudoku.complexity.Complexity;
import de.sudoq.model.sudoku.complexity.ComplexityConstraint;
import de.sudoq.model.sudoku.complexity.ComplexityFactory;
import de.sudoq.model.xml.XmlAttribute;
import de.sudoq.model.xml.XmlHelper;
import de.sudoq.model.xml.XmlTree;
import de.sudoq.model.xml.Xmlable;

/**
 * Ein SudokuType repräsentiert die Eigenschaften eines spezifischen Sudoku-Typs. Dazu gehören insbesondere die
 * Constraints, die einen Sudoku-Typ beschreiben.
 */
public class SudokuType implements Iterable<Constraint>, ComplexityFactory, Xmlable {
	/** Attributes */

	protected int id;
	
	/**
	 * Ein Positionobjekt das in seinen Koordinaten die Anzahl an Spalten und Zeilen hält
	 */
	protected Position dimensions;

	/**
	 * Die Anzahl von Symbolen die in die Felder eines Sudokus dieses Typs eingetragen werden können.
	 */
	private int numberOfSymbols;

	/**
	 * Die Liste der Constraints, die diesen Sudoku-Typ beschreiben
	 */
	protected List<Constraint> constraints;

	/**
	 * eine List die zulässige Transformationen am Sudokutyp hält
	 */
	protected List<PermutationProperties> permutationProperties;

	/**
	 * Konstruktor für einen SudokuTyp
	 * 
	 * @param length
	 *            die maximale Kantenlänge des SudokuTyps
	 * @param numberOfSymbols
	 *            die Anzahl an Symbolen die dieses Sudoku verwendet
	 */
	public SudokuType(int width, int height, int numberOfSymbols) {
		if (numberOfSymbols < 0)
			throw new IllegalArgumentException("Number of symbols < 0 : " + numberOfSymbols);
		if (width < 0)
			throw new IllegalArgumentException("Sudoku width < 0 : " + width);
		if (height < 0)
			throw new IllegalArgumentException("Sudoku height < 0 : " + height);
		this.id = -1;
		this.numberOfSymbols = numberOfSymbols;
		this.dimensions = Position.get(width, height);
		this.constraints = new ArrayList<Constraint>();
		
		this.permutationProperties = new ArrayList<PermutationProperties>();
	}

	/** Methods */

	/**
	 * Gibt die Größe eines Sudokus dieses Typs zurück. Die Größe wird durch ein Position-Objekt repräsentiert, wobei
	 * die x-Koordinate die maximale Anzahl horizontaler Felder eines Sudokus dieses Typs beschreibt, die y-Koordinaten
	 * die maximale Anzahl vertikaler Felder.
	 * 
	 * @return Ein Position-Objekt, welches die maximale Anzahl horizontaler bzw. vertikaler Felder eines Sudokus dieses
	 *         Typs beschreibt
	 */
	public Position getSize() {
		return dimensions;
	}

	/**
	 * Gibt eine Liste mit zulässigen Transformationen an diesem Sudoku aus.
	 * 
	 * @return eine Liste mit zulässigen Transformationen an diesem Sudoku
	 */
	public List<PermutationProperties> getPermutationProperties() {
		return permutationProperties;
	}

	/**
	 * Überprüft, ob das spezifizierte Sudoku die Vorgaben aller Constraints dieses SudokuTyps erfüllt. Ist dies der
	 * Fall, so wir true zurückgegeben. Erfüllt es die Vorgaben nicht, oder wird null übergeben, so wird false
	 * zurückgegeben.
	 * 
	 * @param sudoku
	 *            Das Sudoku, welches auf Erfüllung der Constraints überprüft werden soll
	 * @return true, falls das Sudoku alle Constraints erfüllt, false falls es dies nicht tut oder null ist
	 */
	public boolean checkSudoku(Sudoku sudoku) {
		if (sudoku == null)
			return false;

		boolean allSaturated = true;

		for (int i = 0; i < this.constraints.size(); i++) {
			if (!this.constraints.get(i).isSaturated(sudoku))
				allSaturated = false;
		}

		return allSaturated;
	}

	/**
	 * Gibt einen Iterator für die Constraints dieses Sudoku-Typs zurück.
	 * 
	 * @return Einen Iterator für die Constraints dieses Sudoku-Typs
	 */
	public Iterator<Constraint> iterator() {
		return constraints.iterator();
	}

	/**
	 * Gibt die Anzahl der Symbole eines Sudokus dieses Typs zurück.
	 * 
	 * @return Die Anzahl der Symbole in einem Sudoku dieses Typs
	 */
	public int getNumberOfSymbols() {
		return this.numberOfSymbols;
	}

	/**
	 * Gibt einen ComplexityContraint für eine Schwierigkeit complexity zurück.
	 * 
	 * @param complexity
	 *            eine Schwierigkeit zu der ein ComplexityConstraint erzeugt werden soll
	 */
	public ComplexityConstraint buildComplexityConstraint(Complexity complexity) {
		ComplexityConstraint ret = null;

		if (complexity != null) {
			switch (complexity) {
			case easy:
				ret = new ComplexityConstraint(Complexity.easy, 40, 600, 1100, 2);
				break;
			case medium:
				ret = new ComplexityConstraint(Complexity.medium, 32, 1100, 2050, 3);
				break;
			case difficult:
				ret = new ComplexityConstraint(Complexity.difficult, 28, 1600, 3000, Integer.MAX_VALUE);
				break;
			case infernal:
				ret = new ComplexityConstraint(Complexity.infernal, 27, 2400, 25000, Integer.MAX_VALUE);
				break;
			case arbitrary:
				ret = new ComplexityConstraint(Complexity.arbitrary, 32, 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
				break;
			}
		}
		return ret;
	}

	/**
	 * Gibt den Standard Belegungsfaktor zurück
	 */
	public float getStandardAllocationFactor() {
		return 0.35f;
	}

	/**
	 * Gibt den Sudoku-Typ als String zurück.
	 * 
	 * @return Sudoku-Typ als String
	 */
	public String toString() {
		return "" + this.id;
	}
	
	/**
	 * Setzt die ID dieses Typs auf den spezifizierten Wert.
	 * @param id die neue Id für diesen Typ
	 */
	public void setId(int id) {
		if (id >= 0) 
			this.id = id;
	}
	
	/**
	 * Gibt die Id dieses Typs zurück.
	 * @return die Id dieses Typs
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gibt eine Liste der Constraints, welche zu diesem Sudokutyp gehören zurück. Hinweis: Wenn möglich stattdessen den
	 * Iterator benutzen.
	 * 
	 * @return Eine Liste der Constraints dieses Sudokutyps.
	 */
	public ArrayList<Constraint> getConstraints() {
		return (ArrayList<Constraint>) this.constraints;
	}
	
	
	public void addConstraint(Constraint c) {
		if (c != null) {
			this.constraints.add(c);
		}
	}
	
	public static SudokuType getSudokuType(int id) {
		if (id < 0 ) {
			return null;
		}
		File f = FileManager.getSudokuTypeFile(id);
		if (!f.exists()) {
			return null;
		}
		XmlHelper helper = new XmlHelper();
		try {
			SudokuType t = new SudokuType(9, 9, 9);
			t.fillFromXml(helper.loadXml(f));
			return t;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Integer> getSudokuTypeIds() {
		List<Integer> ids = new ArrayList<Integer>();
		File f = FileManager.getSudokuDir();
		for (File id : f.listFiles()) {
			if (id.isDirectory()) {
				System.out.println(id.getName());
				ids.add(Integer.parseInt(id.getName()));
			}
		}
		return ids;
	}

	@Override
	public XmlTree toXmlTree() {
		XmlTree representation = new XmlTree("sudokutype");
		representation.addAttribute(new XmlAttribute("id", "" + id));
		representation.addChild(dimensions.toXmlTree());
		representation.addAttribute(new XmlAttribute("numberOfSymbols", "" + numberOfSymbols));
		for (Constraint c : constraints) {
			representation.addChild(c.toXmlTree());
		}
		
		// TODO permutation properties
		return representation;
	}

	@Override
	public void fillFromXml(XmlTree xmlTreeRepresentation)
			throws IllegalArgumentException {
		id = Integer.parseInt(xmlTreeRepresentation.getAttributeValue("id"));
		numberOfSymbols = Integer.parseInt(xmlTreeRepresentation.getAttributeValue("numberOfSymbols")); 
		for (Iterator<XmlTree> iterator = xmlTreeRepresentation.getChildren(); iterator.hasNext();) {
			XmlTree sub = iterator.next();
			if (sub.getName().equals("position")) {
				dimensions = Position.fillFromXmlStatic(sub);
			} else if (sub.getName().equals("constraint")) {
				Constraint c = new Constraint(new UniqueConstraintBehavior(), ConstraintType.LINE);
				c.fillFromXml(sub);
				constraints.add(c);
			}
		}
	}
}