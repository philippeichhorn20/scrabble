package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.network.client.AIProtocol;
import backend.network.messages.Message;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import java.io.IOException;

/*
@author jawinter
@description Superclass for AIs
 */
public class PlayerAI {
  protected Brain brain;
  protected String name;
  protected Player[] playerList;
  protected Tile[] tilesOnHand;
  protected AIProtocol aiProtocol;

  public PlayerAI(String name){
    this.name = name;
  }

  public void handleGameTurnMessage(int nowTurn){
    if(playerList[nowTurn].equals(name)) {
      handleTurn();
    }
  }

  public void handleTurn(){

  }

  public void updateScrabbleboard(Tile[] tiles){
    brain.updateScrabbleboard(tiles);
  }

  public void acceptNewTiles(Tile[] tiles){
    int count = 0;
    for(Tile t : this.tilesOnHand) {
      if(t.equals(null)) {
        t = tiles[count++];
      }
    }
  }

  public void requestNewTiles() throws IOException {
    Message shuffle = new ShuffleTilesMessage(name,this.tilesOnHand,new Tile[0]);
    aiProtocol.sendToServer(shuffle);
  }

  public void placeTiles(Tile[] tilesToPlay) throws IOException {
    Message place = new PlaceTilesMessage(name,tilesToPlay);
    aiProtocol.sendToServer(place);
  }

  public void pass() throws IOException {
    Message pass = new PassMessage(name);
    aiProtocol.sendToServer(pass);
  }

}
