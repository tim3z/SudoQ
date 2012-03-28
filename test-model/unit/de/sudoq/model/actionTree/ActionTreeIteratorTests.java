package de.sudoq.model.actionTree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import de.sudoq.model.actionTree.ActionFactory;
import de.sudoq.model.actionTree.ActionTree;
import de.sudoq.model.actionTree.ActionTreeElement;
import de.sudoq.model.actionTree.SolveActionFactory;
import de.sudoq.model.sudoku.Field;

public class ActionTreeIteratorTests {

	@Test
	public void testCompleteness() {
		ActionTree at = new ActionTree();
		ActionFactory factory = new SolveActionFactory();
		Field field = new Field(-1, 1);

		ActionTreeElement ate1 = at.add(factory.createAction(1, field), null);
		ActionTreeElement ate2 = at.add(factory.createAction(1, field), ate1);
		at.add(factory.createAction(1, field), ate2);
		at.add(factory.createAction(1, field), ate2);

		int i = 0;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (ActionTreeElement ate : at) {
			assertFalse(ids.contains(new Integer(ate.getId())));
			ids.add(ate.getId());
			i++;
		}

		assertEquals(i, 4);
	}

	@Test
	public void testExceptions() {
		ActionTree at = new ActionTree();
		Iterator<ActionTreeElement> iterator = at.iterator();

		try {
			iterator.next();
			fail("No Exception thrown");
		} catch (NoSuchElementException e) {
		}
		try {
			iterator.remove();
			fail("No Exception thrown");
		} catch (UnsupportedOperationException e) {
		}
	}

}
