package de.sudoq.controller.sudoku.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public abstract class ConcreteViewPainter implements ViewPainter {

	public ConcreteViewPainter() {
		super();
	}

	/**
	 * Schreibt den Text
	 * 
	 * @param canvas
	 *            Das Canvas
	 * @param field
	 *            Das Field, das gezeichnet wird
	 * @param color
	 *            Die Farbe des Textes
	 * @param bold
	 *            Definiert, ob der Text fett ist
	 * @param symbol
	 *            Das Symbol, welches geschrieben wird
	 */
	protected void drawText(Canvas canvas, View field, int color, boolean bold, String symbol) {
		Paint paint = new Paint();
		paint.setColor(color);
		if (bold) {
			paint.setTypeface(Typeface.DEFAULT_BOLD);
		}
		paint.setAntiAlias(true);
		paint.setTextSize(Math.min(field.getHeight() * 3 / 4, field.getWidth() * 3 / 4));
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(symbol + "", field.getWidth() / 2, field.getHeight() / 2 + Math.min(field.getHeight() / 4, field.getWidth() / 4), paint);
	}
	
	/**
	 * Zeichnet den Hintergrund.
	 * 
	 * @param canvas
	 *            Das Canvas
	 * @param view
	 *            Die View, deren Daten gezeichnet werden
	 * @param color
	 *            Die Hintergrundfarbe
	 * @param round
	 *            Gibt an, ob die Ecken rund gezeichnet werden sollen
	 * @param darken
	 *            Gibt an, ob das Feld verdunkelt werden soll
	 */
	protected void drawBackground(Canvas canvas, View view, int color,
			boolean round, boolean darken) {
		Paint mainPaint = new Paint();
		Paint darkenPaint = null;
		if (darken) {
			darkenPaint = new Paint();
			darkenPaint.setARGB(60, 0, 0, 0);
		}
		mainPaint.setColor(color);
		RectF rect = new RectF(0, 0, view.getWidth(), view.getHeight());
		if (round) {
			canvas.drawRoundRect(rect, view.getWidth() / 20.0f,
					view.getHeight() / 20.0f, mainPaint);
			if (darken) {
				canvas.drawRoundRect(rect, view.getWidth() / 20.0f,
						view.getHeight() / 20.0f, darkenPaint);
			}
		} else {
			canvas.drawRect(rect, mainPaint);
			if (darken) {
				canvas.drawRect(rect, darkenPaint);
			}
		}
	}

	/**
	 * Malt den inneren Bereich (lässt einen Rahmen).
	 * 
	 * @param canvas
	 *            Das Canvas
	 * @param view
	 *            Die View, deren Daten gezeichnet werden
	 * @param color
	 *            Die Farbe
	 * @param round
	 *            Gibt an, ob die Ecken rund gezeichnet werden sollen
	 * @param darken
	 *            Gibt an, ob das Feld verdunkelt werden soll
	 */
	protected void drawInner(Canvas canvas, View view, int color,
			boolean round, boolean darken) {
		Paint mainPaint = new Paint();
		Paint darkenPaint = null;
		if (darken) {
			darkenPaint = new Paint();
			darkenPaint.setARGB(60, 0, 0, 0);
		}
		mainPaint.setColor(color);
		RectF rect = new RectF(2, 2, view.getWidth() - 2,
				view.getHeight() - 2);
		if (round) {
			canvas.drawRoundRect(rect, view.getWidth() / 20.0f,
					view.getHeight() / 20.0f, mainPaint);
			if (darken) {
				canvas.drawRoundRect(rect, view.getWidth() / 20.0f,
						view.getHeight() / 20.0f, darkenPaint);
			}
		} else {
			canvas.drawRect(rect, mainPaint);
			if (darken) {
				canvas.drawRect(rect, darkenPaint);
			}
		}
	}

}