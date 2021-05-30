package backend.music;

import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
	@FXML
	private Button backButton;
	@FXML
	private static Slider volumeKey;
	@FXML
	private RadioButton radioButton;

	public static MediaPlayer player;

	public Music() {
		String path = "src\\lib\\music\\Somewhere_Off_Jazz_Street.mp3";
		Media media = new Media(Paths.get(path).toUri().toString());
		player = new MediaPlayer(media);
		player.play();
		// player.setAutoPlay(true);
		player.setVolume(0.1);

	}
}
