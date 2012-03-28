/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Haiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.model.sudoku.complexity;

/**
 * Diese Klasse repräsentiert die Vorgaben, die ein Sudoku einer bestimmten Schwierigkeit erfüllen muss. Zu den Vorgaben
 * zählen minimal und maximale Anzahl vorgegebener Felder, die Schwierigkeit der Lösung notwendigen Vorgehensweisen,
 * sowie die Anzahl der überhaupt notwendigen verschiedenen Vorgehensweisen.
 */
public class ComplexityConstraint {
	/** Attributes */

	/**
	 * Der Schwierigkeitsgrade für den die Vorgaben gemacht werden
	 */
	private Complexity complexity;

	/**
	 * Die durchschnittliche Anzahl Felder, die bei dieser Schwierigkeit belegt sein sollte
	 */
	private int averageFields;

	/**
	 * Die minimale Höhe der Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalgorithmus als Ganzzahl) der zum
	 * Lösen eines Sudokus nötigen Vorgehensweisen
	 */
	private int minComplexityIdentifier;

	/**
	 * Die maximale Höhe der Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalgorithmus als Ganzzahl) der zum
	 * Lösen eines Sudokus nötigen Vorgehensweisen
	 */
	private int maxComplexityIdentifier;

	/**
	 * Die Anzahl der erlaubten Vorgehensweisen, wobei diese die im Lösungsalgorithmus mit der geringsten Schwierigkeit
	 * hinterlegten sind
	 */
	private int numberOfAllowedHelpers;

	/** Constructors */

	/**
	 * Instanziiert ein neues ComplexityConstraint-Objekt mit den spezifizierten Attributen.
	 * 
	 * @param complexity
	 *            Die Schwierigkeit eines Sudokus für welche die Vorgaben dieses Objektes gelten sollen
	 * @param averageFields
	 *            Die durchschnittliche Anzahl Felder, die bei dieser Schwierigkeit belegt sein sollte
	 * @param minComplexityIdentifier
	 *            Die minimale Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalsgorithmus als Ganzzahl) der zum
	 *            Lösen eines Sudokus nötigen Vorgehensweisen
	 * @param maxComplexityIdentifier
	 *            Die minimale Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalsgorithmus als Ganzzahl) der zum
	 *            Lösen eines Sudokus nötigen Vorgehensweisen
	 * @param numberOfAllowedHelpers
	 *            Die Anzahl der erlaubten Vorgehensweisen (die im Lösungsalgorithmus hinterlegten mit geringstem
	 *            Schwierigkeitsgrad)
	 * @throws IllegalArgumentException
	 *             Wird geworfen, falls complexity null bzw. averageFields, minComplexityIdentifier oder
	 *             numberOfAllowedHelpers kleiner oder gleich 0 ist oder einer der minComplexityIdentifier größer ist
	 *             als der zugehörige max-Wert
	 */
	public ComplexityConstraint(Complexity complexity, int averageFields, int minComplexityIdentifier,
			int maxComplexityIdentifier, int numberOfAllowedHelpers) {
		if (complexity == null)
			throw new IllegalArgumentException("Complexity was null");
		if (minComplexityIdentifier <= 0)
			throw new IllegalArgumentException("minComplexityIdentifier < 0: " + minComplexityIdentifier);
		if (averageFields <= 0)
			throw new IllegalArgumentException("averageFields < 0: " + averageFields);
		if (numberOfAllowedHelpers <= 0)
			throw new IllegalArgumentException("minComplexityIdentifier < 0: " + numberOfAllowedHelpers);
		if (minComplexityIdentifier > maxComplexityIdentifier)
			throw new IllegalArgumentException("minComplexityIdentifier > maxComplexityIdentifier: "
					+ minComplexityIdentifier + " > " + maxComplexityIdentifier);

		this.complexity = complexity;
		this.averageFields = averageFields;
		this.minComplexityIdentifier = minComplexityIdentifier;
		this.maxComplexityIdentifier = maxComplexityIdentifier;
		this.numberOfAllowedHelpers = numberOfAllowedHelpers;
	}

	/** Methods */

	/**
	 * Diese Methode gibt die Schwierigkeit zurück, auf die sich die Vorgaben dieses Constraints beziehen.
	 * 
	 * @return Die Schwierigkeit, auf die sich die Vorgaben dieses Constraints beziehen
	 */
	public Complexity getComplexity() {
		return complexity;
	}

	/**
	 * Gibt die durchschnittliche Anzahl belegter Felder bei Sudokus dieser Schwierigkeit zurück.
	 * 
	 * @return Die durchschnittliche Anzahl belegter Felder in dieser Schwierigkeit
	 */
	public int getAverageFields() {
		return this.averageFields;
	}

	/**
	 * Diese Methode gibt die minimale Höhe der Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalgorithmus als
	 * Ganzzahl) der zum Lösen eines Sudokus des vorgegebenen Schwierigkeitsgrades nötigen Vorgehensweisen.
	 * 
	 * @return Die minimale Summe der Schwierigkeitsgrade der zum Lösen eines Sudokus nötigen Vorgehensweisen
	 */
	public int getMinComplexityIdentifier() {
		return minComplexityIdentifier;
	}

	/**
	 * Diese Methode gibt die maximale Höhe der Summe der Schwierigkeitsgrade (vorgegeben vom Lösungsalgorithmus als
	 * Ganzzahl) der zum Lösen eines Sudokus des vorgegebenen Schwierigkeitsgrades nötigen Vorgehensweisen.
	 * 
	 * @return Die maximale Summe der Schwierigkeitsgrade der zum Lösen eines Sudokus nötigen Vorgehensweisen
	 */
	public int getMaxComplexityIdentifier() {
		return maxComplexityIdentifier;
	}

	/**
	 * Diese Methode gibt die Anzahl der erlaubten verschiedenen Vorgehensweisen zum Lösen eines Sudokus des
	 * vorgegebenen Schwierigkeitsgrades zurück. Genutzt werden dann diejenigen, die im Lösungsalgorithmus mit dem
	 * geringsten Schwierigkeitsgrad hinterlegt sind.
	 * 
	 * @return Die Anzahl der erlaubten verschiedenen Vorgehensweisen zum Lösen eines Sudokus
	 */
	public int getNumberOfAllowedHelpers() {
		return numberOfAllowedHelpers;
	}
}
