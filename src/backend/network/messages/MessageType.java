package backend.network.messages;

/*Types of Messages which can be sent
 * @athor nilschae
 * @version 1.6*/
public enum MessageType {
  CONNECT, DISCONNECT, CONNECTION_REFUSED, SERVER_SHUTDOWN, SEND_ID, GET_ID, GAME_INFO,
  GAME_START, GAME_TURN, GAME_WAIT, GAME_OVER, GAME_WIN, GAME_LOOSE, GAME_PLACEMENT, GET_NEW_TILES,PASS,
  PLACE_TILES, SHUFFLE_TILES, RECEIVE_SHUFFLE_TILES,
  SEND_POINTS, SEND_RACK_POINTS, SEND_START_RACK,
  TIME_ALERT, TIME_SYNC, PLAY_FEEDBACK,


}
