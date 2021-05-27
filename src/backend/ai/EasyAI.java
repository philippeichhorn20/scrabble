package backend.ai;

import backend.basic.Tile;

import java.io.IOException;
import java.util.Random;
import backend.basic.ScrabbleBoard;

/*
@author vivanova
EasyAI uses Brain to find simple possible moves.
 */

public class EasyAI extends PlayerAI {

	private Brain easyBrain;
	private Random r = new Random(System.currentTimeMillis());
	public EasyAI(String name, String color, ScrabbleBoard board) {
		super(name);
		this.easyBrain = new Brain(board);
	}

	public Brain getEasyBrain() {
		return easyBrain;
	}

	@Override
	public void handleTurn() {
		updateScrabbleboard(tilesOnHand);
		// Make a random choice;
		int choice = r.nextInt(3);
		switch (choice) {
		case 0: {
			var moves = easyBrain.getPlayableWords(tilesOnHand);
			// If we can not make a move we request new Tiles
			if (moves.isEmpty()) {
				try {
					// ?Does it automatically end the Turn?
					requestNewTiles();
				} catch (IOException e) {
					// What is a good way to handle the Exception?
					System.err.println("Easy Ai could not request new tiles.");
					e.printStackTrace();
				}

			} else {
				PossibleWord pick = null;
				int counter = 0;
				int random = r.nextInt(moves.size() - 1) + 1;
				var it = moves.iterator();
				// Loop through words until we are finished with the array or until we hit the
				// random number.
				while (it.hasNext() && (counter != random)) {
					pick = it.next();
					counter++;
				}
				// We take the tiles of our current word and place them
				var word = pick.getTile();
				try {
					placeTiles((Tile[]) word.toArray());
				} catch (IOException e) {
					System.err.println("Easy Ai could not play tiles");
					e.printStackTrace();
				}
			}

		}
		case 1: {
			try {
				// Request new Tiles and end turn.
				requestNewTiles();
			} catch (IOException e) {
				System.err.println("Easy Ai could not request new tiles.");
				e.printStackTrace();
			}
		}
		case 2: {
			try {
				// Pass a turn because we did not have an interest to Play
				pass();
			} catch (IOException e) {
				System.err.println("Easy Ai could not pass turn");
				e.printStackTrace();
			}
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + choice);
		}

	}

}