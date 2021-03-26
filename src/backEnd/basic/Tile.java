package basic;

/*
 * @author Nils Schäfer
 * @version 1.0
 * */

public class Tile {
	
	private char letter;  		//A holder for a letter of a tile
	private int value;	  		//A holder for the value of a tile
	private Tilestatus status;	//A holder for the status of a tile
	private boolean joker;  	//A holder if the tile is a joker or not
	
	/*The main constructor of a tile
	 * @param letter letter of the tile 
	 * @param value  value of the tile
	 * @param status represents where the tile is
	 * */
	public Tile(char letter, int value, Tilestatus status) {
		this.letter = letter;
		this.value = value;
		this.status = status;
	}
	
	/*A constructor which set the status automatic to INBAG
	 * @param letter letter of the tile 
	 * @param value  value of the tile
	 * */
	public Tile(char letter, int value) {
		this.letter = letter;
		this.value = value;
		this.status = Tilestatus.INBAG;
	}
	
	/*A constructor which creates a joker tile
	 * @param isJoker defines if the tile is a Joker what have to be if this constructor get called
	 * @param status set the Location where the tile is at the moment
	 * */
	public Tile(boolean isJoker, Tilestatus status) {
		this.joker = isJoker;
		this.value = 0;
		this.status = status;
	}
	
	/*Copy constructor for a tile
	 * @param tile the tile to copy
	 * */
	public Tile(Tile tile) {
		this.letter = tile.letter;
		this.value = tile.value;
		this.status = tile.status;
		this.joker = tile.joker;
	}
	
	/*@return letter of the tile
	 * */
	public char getLetter() {
		return this.letter;
	}
	
	/*@return the value of the letter
	 * */
	public int getValue() {
		return this.value;
	}
	
	/*@return the status of a tile
	 * */
	public Tilestatus getStatus(){
		return this.status;
	}
	
	/*@return if the tile is a joker*/
	public boolean isJoker() {
		return this.joker;
	}
	
	/*@param letter set the letter of a tile
	 * */
	public void setLetter(char letter) {
		this.letter = letter;
	}
	
	/*@param value set the value of a tile
	 * */
	public void setValue(int value) {
		this.value = value;
	}
	
	/*@param status set the status of a tile
	 * */
	public void setStatus(Tilestatus status) {
		this.status = status;
	}
	
	/*@param isJoker set if the tile is a Joker or not*/
	public void setJoker(boolean isJoker) {
		this.joker = isJoker;
	}
	
	/*A methode which compares a tile by it's joker status or letter*/
	public boolean equals(Tile tile) {
		if(this.joker && tile.joker) {
			return true;
		} else {
			if(this.letter != 0 && tile.letter != 0) {
				if(this.letter == tile.letter) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*Enum to set a status for a tile*/
	public enum Tilestatus {
		INBAG,				
		ONPLAYERRACK,
		ONBOARD,
		ONSWITCHPANEL		//Status for switching tiles back into the tilebag
	}
}
