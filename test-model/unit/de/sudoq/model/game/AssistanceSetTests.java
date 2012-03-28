package de.sudoq.model.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.sudoq.model.game.AssistanceSet;
import de.sudoq.model.game.Assistances;

public class AssistanceSetTests {
	@Test
	public void test() {
		AssistanceSet a = new AssistanceSet();
		a.setAssistance(Assistances.autoAdjustNotes);
		a.setAssistance(Assistances.markWrongSymbol);

		assertTrue("autoAdjustNotes has wrong value", a.getAssistance(Assistances.autoAdjustNotes));
		assertFalse("markRowColumn has wrong value", a.getAssistance(Assistances.markRowColumn));
		assertTrue("markWrongSymbol has wrong value", a.getAssistance(Assistances.markWrongSymbol));
		assertFalse("restrictCandidates has wrong value", a.getAssistance(Assistances.restrictCandidates));

		String t = a.convertToString();
		a = null;
		a = AssistanceSet.fromString(t);

		assertTrue("autoAdjustNotes has wrong value", a.getAssistance(Assistances.autoAdjustNotes));
		assertFalse("markRowColumn has wrong value", a.getAssistance(Assistances.markRowColumn));
		assertTrue("markWrongSymbol has wrong value", a.getAssistance(Assistances.markWrongSymbol));
		assertFalse("restrictCandidates has wrong value", a.getAssistance(Assistances.restrictCandidates));

		a.clearAssistance(Assistances.markWrongSymbol);
		assertFalse("markWrongSymbol has wrong value", a.getAssistance(Assistances.markWrongSymbol));
	}

	@Test
	public void testNull() {
		AssistanceSet a = new AssistanceSet();
		a.setAssistance(null);
		assertFalse(a.getAssistance(null));
		a.clearAssistance(null);
		assertFalse(a.getAssistance(null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFooString() {
		AssistanceSet.fromString("foo");
	}
}
