/*
 * SudoQ is a Sudoku-App for Adroid Devices with Version 2.2 at least.
 * Copyright (C) 2012  Haiko Klare, Julian Geppert, Jan-Bernhard Kordaß, Jonathan Kieling, Tim Zeitz, Timo Abele
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
 * You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.sudoq.controller.tutorial;

import de.sudoq.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TutorialActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);

		TabHost tabHost = getTabHost();

		// Tab for sudokus
		TabSpec sudokuspec = tabHost.newTabSpec(getString(R.string.sf_tutorial_sudoku_title));
		// setting Title and Icon for the Tab
		sudokuspec.setIndicator(getString(R.string.sf_tutorial_sudoku_title), getResources().getDrawable(R.drawable.sudoku_tutorial));
		Intent sudokuIntent = new Intent(this, TutorialSudokuActivity.class);
		sudokuspec.setContent(sudokuIntent);

		// Tab for assistances
		TabSpec assistancesspec = tabHost.newTabSpec(getString(R.string.sf_tutorial_assistances_title));
		// setting Title and Icon for the Tab
		assistancesspec.setIndicator(getString(R.string.sf_tutorial_assistances_title), getResources().getDrawable(R.drawable.help_tutorial));
		Intent assistancesIntent = new Intent(this, TutorialAssistancesActivity.class);
		assistancesspec.setContent(assistancesIntent);

		// Tab for actiontree
		TabSpec actiontreespec = tabHost.newTabSpec(getString(R.string.sf_tutorial_action_title));
		// setting Title and Icon for the Tab
		actiontreespec.setIndicator(getString(R.string.sf_tutorial_action_title), getResources().getDrawable(R.drawable.action_tree_tutorial));
		Intent actiontreeIntent = new Intent(this, TutorialActionTreeActivity.class);
		actiontreespec.setContent(actiontreeIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(sudokuspec); // Adding sudoku tab
		tabHost.addTab(assistancesspec); // Adding assistances tab
		tabHost.addTab(actiontreespec); // Adding actiontree tab
	}

}
