package de.sudoq.model.game;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.sudoq.model.game.GameType;

public class GameTypeTests {

	@Test
	public void test() {
		GameType[] types = GameType.values();
		for (GameType type : types) {
			assertTrue(GameType.valueOf(type.toString()).equals(type));
		}
	}

}
