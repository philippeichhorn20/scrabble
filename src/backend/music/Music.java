package backend.music;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * 
 * @author vivanova
 *
 */
public class Music {


	public static MediaPlayer player;

	
	/**
	 * We set the path to our music file in URI, set a media and play it
	 */
	public Music() {
		String path = "src\\lib\\music\\Somewhere_Off_Jazz_Street.mp3";
		Media media = new Media(Paths.get(path).toUri().toString());
		player = new MediaPlayer(media);
		player.play();
		// player.setAutoPlay(true);
		player.setVolume(0.3);

	}
	
	public MediaPlayer getPlayer() {
		return player;
	}
}
