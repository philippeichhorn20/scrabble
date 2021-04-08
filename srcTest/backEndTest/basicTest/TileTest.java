package backEndTest.basicTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import backEnd.basic.Tile;
import backEnd.basic.Tile.Tilestatus;

class TileTest {

	@Test
	void test() {
		Tile testTile = new Tile('A', 1);
		assertEquals(testTile.getLetter(), 'A');
		assertEquals(testTile.getValue(), 1);
		assertEquals(testTile.getStatus(), Tilestatus.INBAG);
	}

}
