/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Heiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import de.sudoq.controller.sudoku.painter.ViewPainter;
import de.sudoq.model.sudoku.Position;
import de.sudoq.model.sudoku.PositionMap;

/**
 * Eine View als RealativeLayout, die eine Sudoku-Anzeige verwaltet.
 */
public class RasterLayout extends RelativeLayout implements ObservableViewClicked, ZoomableView {
	
	/**
	 * Das Log-Tag für den LogCat
	 */
	private static final String LOG_TAG = RasterLayout.class.getSimpleName();

	/**
	 * Der Kontext dieser View
	 */
	private Context context;

	/**
	 * Die Standardgröße eines Feldes
	 */
	private int defaultViewSize;

	private float zoomFactor;

	private Position dimension;
	
	/**
	 * Ein Array aller FieldViews
	 */
	private PositionMap<PaintableView> rasterViews;
	
	private PositionMap<Integer> rasterBlocks;

	/**
	 * Der linke Rand, verursacht durch ein zu niedriges Layout
	 */
	private int leftMargin;

	/**
	 * Der linke Rand, verursacht durch ein zu schmales Layout
	 */
	private int topMargin;

	/**
	 * Der Platz zwischen 2 Blöcken
	 */
	private static int spacing = 2;

	private Paint borderPaint;
	
	private ViewPainter painter;
	
	/**
	 * Instanziiert eine neue SudokuView in dem spezifizierten Kontext.
	 * 
	 * @param context
	 *            Der Kontext, in dem diese View angezeigt wird
	 */
	public RasterLayout(Context context) {
		super(context);
		this.context = context;

		this.defaultViewSize = 40;
		this.zoomFactor = 1.0f;
		this.borderPaint = new Paint();
		this.borderPaint.setColor(Color.BLACK);
		// this.currentFieldViewSize = this.defaultFieldViewSize;
		this.setWillNotDraw(false);
	}

	public void setDimension(Position dimension) {
		this.dimension = dimension;
		if (this.painter != null) {
			painter.flushMarkings();
		}
		inflateRaster();
		setPainter(painter);
	}
	
	public void setPainter(ViewPainter painter) {
		this.painter = painter;
		
		if (dimension == null) {
			return;
		}
		
		for (int x = 0; x <= dimension.getX(); x++) {
			for (int y = 0; y <= dimension.getY(); y++) {
				if (rasterViews.get(Position.get(x,y)) != null) {
					rasterViews.get(Position.get(x,y)).setPainter(painter);
				}
			}
		}
	}
	
	/**
	 * Erstellt die Anzeige des Sudokus.
	 */
	private void inflateRaster() {
		this.removeAllViews();
		rasterViews = null;
		
		if (dimension == null) {
			return;
		}

		this.rasterViews = new PositionMap<PaintableView>(Position.get(dimension.getX() + 1, dimension.getY() + 1));
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) this.getCurrentFieldViewSize(), (int) this.defaultViewSize);
				params.topMargin = (y * (int) this.getCurrentFieldViewSize()) + y;
				params.leftMargin = (x * (int) this.getCurrentFieldViewSize()) + x;
				rasterViews.put(Position.get(x, y), new PaintableView(context));
				this.addView(rasterViews.get(Position.get(x, y)), params);
			}
		}
		
		// Dummy view for margin
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) this.getCurrentFieldViewSize(), (int) this.defaultViewSize);
		params.topMargin = ((dimension.getY() - 1) * (int) this.getCurrentFieldViewSize()) + (dimension.getY() - 1) + getCurrentTopMargin();
		params.leftMargin = ((dimension.getX() - 1) * (int) this.getCurrentFieldViewSize()) + (dimension.getX() - 1) + getCurrentLeftMargin();
		rasterViews.put(dimension, new PaintableView(context));
		this.addView(rasterViews.get(dimension), params);
		
		rasterBlocks = new PositionMap<Integer>(dimension);
	}

	/**
	 * Berechnet das aktuelle Spacing (gem. dem aktuellen ZoomFaktor) und gibt
	 * es zurück.
	 * 
	 * @return Das aktuelle Spacing
	 */
	private int getCurrentSpacing() {
		return (int) (spacing * this.zoomFactor);
	}

	/**
	 * Berechnet das aktuelle obere Margin (gem. dem aktuellen ZoomFaktor) und
	 * gibt es zurück.
	 * 
	 * @return Das aktuelle obere Margin
	 */
	public int getCurrentTopMargin() {
		return (int) (this.topMargin * this.zoomFactor);
	}

	/**
	 * Berechnet das aktuelle linke Margin (gem. dem aktuellen ZoomFaktor) und
	 * gibt es zurück.
	 * 
	 * @return Das aktuelle linke Margin
	 */
	public int getCurrentLeftMargin() {
		return (int) (this.leftMargin * this.zoomFactor);
	}

	/**
	 * Aktualisiert die Sudoku-Anzeige bzw. der enthaltenen Felder.
	 */
	private void refresh() {
		if (rasterViews == null) {
			return;
		}
		
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rasterViews.get(Position.get(x, y)).getLayoutParams();
				params.width = (int) this.getCurrentFieldViewSize();
				params.height = (int) this.getCurrentFieldViewSize();
				params.topMargin = (int) (getCurrentTopMargin() + (y * (this.getCurrentFieldViewSize() + getCurrentSpacing())));
				params.leftMargin = (int) (getCurrentLeftMargin() + (x * (this.getCurrentFieldViewSize() + getCurrentSpacing())));
				rasterViews.get(Position.get(x, y)).setLayoutParams(params);
				rasterViews.get(Position.get(x, y)).invalidate();
			}
		}
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) this.getCurrentFieldViewSize(), (int) this.defaultViewSize);
		params.width = (int) this.getCurrentFieldViewSize();
		params.height = (int) this.getCurrentFieldViewSize();
		params.topMargin = (int) (2 * getCurrentTopMargin() + ((dimension.getY() - 1) * (this.getCurrentFieldViewSize() + getCurrentSpacing())));
		params.leftMargin = (int) (2 * getCurrentLeftMargin() + ((dimension.getX() - 1) * (this.getCurrentFieldViewSize() + getCurrentSpacing())));
		rasterViews.get(dimension).setLayoutParams(params);
		rasterViews.get(dimension).invalidate();

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float edgeRadius = getCurrentFieldViewSize() / 20.0f;
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				Position p = Position.get(x, y);
				if (rasterBlocks.get(p) != null) {
					boolean isLeft = x == 0 || !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x - 1, y)));
					boolean isRight = x == dimension.getX() - 1 || !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x + 1, y)));
					boolean isTop = y == 0 || !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x, y - 1)));
					boolean isBottom = y == dimension.getY() - 1 || !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x, y + 1)));
					for (int i = 1; i <= getCurrentSpacing(); i++) {
						if (isLeft) {
							canvas.drawLine(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									borderPaint);
						}
						if (isRight) {
							canvas.drawLine(getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									borderPaint);
						}
						if (isTop) {
							canvas.drawLine(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									borderPaint);
						}
						if (isBottom) {
							canvas.drawLine(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									borderPaint);
						}

						if (isLeft && isTop) {
							canvas.drawCircle(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									edgeRadius + i, borderPaint);
						} else if (isLeft && !isTop && (x == 0 || (y > 0 && !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x - 1, y - 1)))))) {
							canvas.drawLine(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - edgeRadius,
									getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									borderPaint);
						} else if (!isLeft && isTop && (y == 0 || (x > 0 && !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x - 1, y - 1)))))) {
							canvas.drawLine(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - i,
									borderPaint);
						}

						if (isRight && isTop) {
							canvas.drawCircle(getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - edgeRadius,
									getCurrentTopMargin() + p.getY() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									edgeRadius + i, borderPaint);
						}

						if (isLeft && isBottom) {
							canvas.drawCircle(getCurrentLeftMargin() + p.getX() * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									edgeRadius + i, borderPaint);
						}
						if (isRight && isBottom) {
							canvas.drawCircle(getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									edgeRadius + i, borderPaint);
						} else if (isRight && !isBottom && (x == dimension.getX() - 1 || (y < dimension.getY() && !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x + 1, y + 1)))))) {
							canvas.drawLine(getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - edgeRadius,
									getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									borderPaint);
						} else if (!isRight && isBottom && (y == dimension.getY() - 1 || (x < dimension.getX() && !rasterBlocks.get(p).equals(rasterBlocks.get(Position.get(x + 1, y + 1)))))) {
							canvas.drawLine(getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - edgeRadius - getCurrentSpacing(),
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									getCurrentLeftMargin() + (p.getX() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) + edgeRadius,
									getCurrentTopMargin() + (p.getY() + 1) * (int) (this.getCurrentFieldViewSize() + getCurrentSpacing()) - getCurrentSpacing() - 1 + i,
									borderPaint);
						}
					}
				}
			}
		}
	}

	/**
	 * Zoom so heraus, dass ein diese View optimal in ein Layout der
	 * spezifizierte Größe passt
	 * 
	 * @param width
	 *            Die Breite auf die optimiert werden soll
	 * @param height
	 *            Die Höhe auf die optimiert werden soll
	 */
	public void optiZoom(int width, int height) {
		Log.d(LOG_TAG, "SudokuView height intern: " + this.getMeasuredHeight());
		int size = width < height ? width : height;
		int numberOfFields = width < height ? dimension.getX() : dimension.getY();
		this.defaultViewSize = (size - (numberOfFields + 1) * spacing) / numberOfFields;
		// this.currentFieldViewSize = this.defaultFieldViewSize;

		this.leftMargin = (int) (width - dimension.getX() *
				(this.getCurrentFieldViewSize() + spacing) + spacing)
				/ 2;
		this.topMargin = (int) (height - dimension.getY() *
				(this.getCurrentFieldViewSize() + spacing) + spacing)
				/ 2;
		Log.d(LOG_TAG, "Sudoku width: " + width);
		Log.d(LOG_TAG, "Sudoku height: " + height);
		this.refresh();
	}

	/**
	 * Touch-Events werden nicht verarbeitet.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	/**
	 * Gibt das Array der SudokuFieldViews zurück.
	 * 
	 * @return Das Array der SudokuFieldViews
	 */
	public PaintableView getView(Position position) {
		if (rasterViews == null) {
			return null;
		} else {
			return rasterViews.get(position);
		}
	}

	/**
	 * Setzt den aktuellen Zoom-Faktor für diese View und refresh sie.
	 * 
	 * @param factor
	 *            Der Zoom-Faktor
	 */
	public boolean zoom(float factor) {
		this.zoomFactor = factor;
		refresh();
		return true;
	}

	/**
	 * Gibt die aktuelle Größe einer FieldView zurück.
	 * 
	 * @return die aktuelle Größe einer FieldView
	 */
	private int getCurrentFieldViewSize() {
		return (int) (this.defaultViewSize * zoomFactor);
	}

	/**
	 * Unbenutzt.
	 * 
	 * @throws UnsupportedOperationException
	 *             Wirft immer eine UnsupportedOperationException
	 */
	public void notifyListener() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerListener(ViewClickListener listener) {
		if (rasterViews == null) {
			return;
		}
		
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				rasterViews.get(Position.get(x, y)).registerListener(listener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(ViewClickListener listener) {
		if (rasterViews == null) {
			return;
		}
		
		for (int x = 0; x < dimension.getX(); x++) {
			for (int y = 0; y < dimension.getY(); y++) {
				rasterViews.get(Position.get(x, y)).removeListener(listener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMinZoomFactor() {
		return 1.0f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMaxZoomFactor() {
		return Math.max(dimension.getX(), dimension.getY()) / 2.0f;
	}

	/**
	 * 
	 * @param position
	 * @param block block number, null for no membership
	 */
	public void setBlockMembership(Position position, Integer block) {
		if (position.getX() < dimension.getX() && position.getY() < dimension.getY()) {
			rasterBlocks.put(position, block);
		}	
		invalidate();
	}
}
