package de.sudoq.view;

/**
 * Ein Interface für eine zoombare View, die einem FullScrollLayout als Child übergeben werden kann.
 */
public interface ZoomableView {

	/**
	 * Setzt den aktuellen Zoom-Faktor für diese View und refresh sie.
	 * 
	 * @param factor
	 *            Der Zoom-Faktor
	 * @return Gibt zurück, ob das Zoom-Event verarbeitet wurde
	 */
	public boolean zoom(float factor);
}
