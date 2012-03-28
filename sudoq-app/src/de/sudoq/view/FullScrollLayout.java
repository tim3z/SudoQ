/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Haiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Eine ScrollView, welche sowohl horizontales, als auch vertikales Scrollen
 * ermöglicht.
 */
public class FullScrollLayout extends LinearLayout {
	/**
	 * Der maximale Zoom Faktor.
	 */
	private static final float MAX_ZOOM = 3.0f;

	/**
	 * Der Log-Tag
	 */
	private static final String LOG_TAG = FullScrollLayout.class.getSimpleName();

	/**
	 * Der aktuelle Zoom Faktor.
	 */
	private float zoomFactor;

	/**
	 * Flag, ob seit dem letzten Aufruf der isZoomPerformed-Methode gezoomt
	 * wurde.
	 */
	private boolean zoomPerformed;

	/**
	 * Der View, der horizontales Scrollen erlaubt und im View für vertikales
	 * Scrollen enthalten ist.
	 */
	private HorizontalScroll horizontalScrollView;

	/**
	 * Der View, der vertikales Scrollen erlaubt und den View für horizontales
	 * Scrollen enthällt.
	 */
	private VerticalScroll verticalScrollView;

	/**
	 * Aktueller X- und Y-Wert der ScrollViews.
	 */
	private float currentX, currentY;

	/**
	 * Der Zoom-Gesten-Detektor
	 */
	private ScaleGestureDetector scaleGestureDetector;

	/**
	 * Instanziiert ein neues ScrollLayout mit den gegebenen Parametern
	 * 
	 * @param context
	 *            der Applikationskontext
	 * @param set
	 *            das Android AttributeSet
	 */
	public FullScrollLayout(Context context, AttributeSet set) {
		super(context, set);
		initialize();
		this.scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
	}

	/**
	 * Instanziiert ein neues FullScrollLayout, welches auf Wunsch als
	 * qudratisch festgelegt wird.
	 * 
	 * @param context
	 *            Der Kontext, in dem dieses Layout angelegt wird
	 */
	public FullScrollLayout(Context context) {
		super(context);
		initialize();
		this.scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
	}

	/**
	 * Initialisiert ein neues Layout.
	 */
	private void initialize() {
		this.removeAllViews();

		if (this.zoomFactor == 0) {
			this.zoomFactor = 1.0f;
		}
		this.zoomPerformed = false;

		this.verticalScrollView = new VerticalScroll(getContext());
		this.horizontalScrollView = new HorizontalScroll(getContext());

		this.verticalScrollView.addView(this.horizontalScrollView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.addView(this.verticalScrollView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	/**
	 * Fügt eine View zu diesem Layout hinzu. Ist bereits eine vorhanden, so
	 * wird diese gelöscht.
	 */
	@Override
	public void addView(View v) {
		this.horizontalScrollView.removeAllViews();
		this.horizontalScrollView.addView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	/**
	 * Verarbeitet TouchEvents mit einem Finger, also klicken und scrollen.
	 * 
	 * @param event
	 *            Das MotionEvent
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			this.horizontalScrollView.performTouch(event);
			this.verticalScrollView.performTouch(event);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scrollTo(int x, int y) {
		// Has to be changed because the tree algo uses different coords.
		currentX = x - getWidth() / 2;
		currentY = y - getHeight() / 2;

		this.verticalScrollView.post(new Runnable() {
			public void run() {
				verticalScrollView.scrollTo((int) currentX, (int) currentY);
				currentY = verticalScrollView.getScrollY();
			}
		});
		this.horizontalScrollView.post(new Runnable() {
			public void run() {
				horizontalScrollView.scrollTo((int) currentX, (int) currentY);
				currentX = horizontalScrollView.getScrollX();
			}
		});
	}

	/**
	 * Verarbeitet ZoomEvents, also solche mit zwei Fingern.
	 * 
	 * @param event
	 *            Das MotionEvent
	 */
	public void performZoomEvent(MotionEvent event) {
		this.scaleGestureDetector.onTouchEvent(event);
	}

	/**
	 * Setzt den Zoom zurück.
	 */
	public void resetZoom() {
		setZoomFactor(1.0f);
		scrollTo(0, 0);
	}

	/**
	 * Gibt zurück, ob ein Zoom performt wurde und setzt den Zustand zurück.
	 * 
	 * @return true, falls ein Zoom performt wurde, false falls nicht
	 */
	public boolean isZoomPerformed() {
		boolean res = this.zoomPerformed;
		this.zoomPerformed = false;
		return res;
	}

	/**
	 * Scrollt an die korrekte Position.
	 */
	public void scrollCorrect() {
		scrollTo((int) (currentX * zoomFactor) + getWidth() / 2, (int) (currentY * zoomFactor) + getHeight() / 2);
	}

	/**
	 * Gibt den aktuellen Zoom-Faktor dieses Layouts zurück.
	 * 
	 * @return Der aktuelle Zoom-Faktor
	 */
	public float getZoomFactor() {
		return this.zoomFactor;
	}

	/**
	 * Setzt den aktuellen Zoom-Faktor des Layouts.
	 * 
	 * @param newZoom
	 *            Der neue Zoomfaktor
	 */
	public void setZoomFactor(float newZoom) {
		this.zoomFactor = newZoom;
	}

	/**
	 * Gibt den aktuell gescrollten X-Wert zurück.
	 * 
	 * @return der aktuell gescrollte X-Wert
	 */
	public float getScrollValueX()
	{
		return this.currentX;
	}

	/**
	 * Gibt den aktuell gescrollten Y-Wert zurück.
	 * 
	 * @return der aktuell gescrollte Y-Wert
	 */
	public float getScrollValueY()
	{
		return this.currentY;
	}

	/**
	 * Diese Klasse überschreibt das onTouch-Event der ScrollView, sodass dieses
	 * an dieses FullScrollLayout weitergereicht wird. Durch die
	 * performTouch-Methode kann das Event zurückgereicht werden.
	 */
	private class VerticalScroll extends ScrollView {
		/**
		 * Instanziiert eine neue vertikale ScrollView.
		 * 
		 * @param context
		 *            Der Kontext
		 */
		public VerticalScroll(Context context) {
			super(context);
			setVerticalScrollBarEnabled(false);
			setHorizontalScrollBarEnabled(false);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return false;
		}

		/**
		 * Für das übergebene Touch-Event aus.
		 * 
		 * @param event
		 *            Das auszuführende Touch-Event
		 */
		public void performTouch(MotionEvent event) {
			try {
				event.getX();
				event.getY();
				super.onTouchEvent(event);
				currentY = this.getScrollY();
			} catch (Exception e) {
				// Old android versions sometimes throw an exception when
				// putting and Event of one view in the onTouch of
				// another view. We just catch that and do nothing
			}
		}
	}

	/**
	 * Diese Klasse überschreibt das onTouch-Event der HorizontalScrollView,
	 * sodass dieses an dieses FullScrollLayout weitergereicht wird. Durch die
	 * performTouch-Methode kann das Event zurückgereicht werden.
	 */
	private class HorizontalScroll extends HorizontalScrollView {
		/**
		 * Instanziiert eine neue horizontale ScrollView.
		 * 
		 * @param context
		 *            Der Kontext
		 */
		public HorizontalScroll(Context context) {
			super(context);
			setVerticalScrollBarEnabled(false);
			setHorizontalScrollBarEnabled(false);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return false;
		}

		/**
		 * Für das übergebene Touch-Event aus.
		 * 
		 * @param event
		 *            Das auszuführende Touch-Event
		 */
		public void performTouch(MotionEvent event) {
			try {
				event.getX();
				event.getY();
				super.onTouchEvent(event);
				currentX = this.getScrollX();
			} catch (Exception e) {
				// Old android versions sometimes throw an exception when
				// putting and Event of one view in the onTouch of
				// another view. We just catch that and do nothing
			}
		}
	}

	private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float oldZoom = zoomFactor;
			float scaleFactor = detector.getScaleFactor();
			zoomFactor *= scaleFactor;
			// Don't let the object get too large.
			zoomFactor = Math.max(Math.min(zoomFactor, MAX_ZOOM), 1.0f);

			// if (Math.abs(zoomFactor - oldZoom) > 0.0001f)
			zoomPerformed = true;

			currentX += detector.getFocusX() - detector.getFocusX() / scaleFactor;
			currentY += detector.getFocusY() - detector.getFocusY() / scaleFactor;

			// currentX = currentX < 0 ? 0 :
			// (currentX > getMeasuredWidth() * zoomFactor - getMeasuredWidth()
			// ? currentX = getMeasuredWidth() * zoomFactor - getMeasuredWidth()
			// : currentX);
			//
			// currentY = currentY < 0 ? 0 :
			// (currentY > getMeasuredHeight() * zoomFactor -
			// getMeasuredHeight() ? currentY = getMeasuredHeight() * zoomFactor
			// - getMeasuredHeight() : currentY);

			scrollTo((int) currentX + getWidth() / 2, (int) currentY + getHeight() / 2);
			// invalidate();
			Log.d(LOG_TAG, "Scaled");
			return true;
		}
	}
}
