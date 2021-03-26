package Basics;

/* @author nilschae
 * @version 1.0
 * @description a class which represents a player in the game scrabble*/
public class Player {
	double timer;
	String name;
	long score;
	String color;
	Tile[] rack = new Tile[7];
	Playerstatus status;
	
	
	public Player(String name, String color, double timer, Playerstatus status) {
		this.name = name;
		this.color = color;
		this.timer = timer;
		this.score = 0;
		this.status = status;
		
	}
	
	/*A methode to shuffle choosen tiles from the players rack and drawing new random tile instead*/
	public boolean ShuffleRack(Tile[] tilesToShuffel, TileBag bag) {
		Tile[] newTiles = new Tile[tilesToShuffel.length];
		
		if(bag.size() >= 7) {
			for(int i = 0; i < tilesToShuffel.length; i++) {
				newTiles[i] = bag.drawTile();
			}
			for(int i = 0; i < tilesToShuffel.length; i++) {
				bag.insertTileToBag(drawTileFromRack(tilesToShuffel[i]));
			}
			int count = 0;
			for(int i = 0; i < this.rack.length; i++) {
				if(this.rack[i] == null) {
					this.rack[i] = newTiles[count];
					count++;
				}
			}
		}
		return false;
	}
	
	/*Add points to the players score*/
	public void addPoints(long points) {
		this.score += points;
	}
	
	/*check if the timer is out of time. When timer out of time it return false, when time is left it returns true*/
	public boolean checkTimer() {
		if(this.timer > 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/*A methode which fills the rack bag to 7 tiles*/
	public Tile[] fillRack(TileBag bag) {
		if (!bag.isEmpty()) {
			for (int i = 0; i < rack.length; i++) {
				if(rack[i] == null) {
					if(bag.isEmpty()) {
						break;
					} else {
						rack[i] = bag.drawTile();
					}
				}
			}
		}
		return null;
	}
	
	/*draw a spezific tile from the players rack*/ 
	public Tile drawTileFromRack(Tile tileToDraw) {
		for(int i = 0; i < this.rack.length; i++) {
			if(rack[i].equals(tileToDraw)) {
				Tile foundTile = rack[i];
				rack[i] = null;
				return foundTile;
			}
		}
		return null;
	}
	
	public Tile getTileOnPositionInRack(int pos) {
		return this.rack[pos];
	}
	
	public void setStatus(Playerstatus status) {
		this.status = status;
	}
	
	public void setTimer(double time) {
		this.timer = time;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getTimer() {
		return this.timer;
	}
	
	public String getName() {
		return this.name;
	}
	
	public long getScore() {
		return this.score;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public Tile[] getRack() {
		return this.rack;
	}
	
	public Playerstatus getStatus() {
		return this.status;
	}
	
	public enum Playerstatus{
		OUTOFGAME,
		WAIT,
		TURN
	}
}
