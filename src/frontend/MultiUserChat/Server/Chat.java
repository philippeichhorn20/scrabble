package frontend.MultiUserChat.Server;

import backend.basic.GameInformation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * @author vivanova
 *
 */
public class Chat {
	private TextArea textArea;
	private Stage window;
	
	public Chat(String test) {
		
	}

	public Chat() {
		window = new Stage();
		window.getIcons().add(new Image("frontend/screens/resources/gameIcon.png"));
		window.initModality(Modality.WINDOW_MODAL);
		window.initStyle(StageStyle.UTILITY);
		window.setTitle("Chat");
		window.setMinHeight(500);
		window.setMaxHeight(500);
		window.setMinWidth(250);
		window.setMaxWidth(260);
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setMaxWidth(220);
		textArea.setMinHeight(300);
		TextField inputField = new TextField();
		inputField.setPrefWidth(220);
		inputField.setMaxHeight(50);

		Button btnClose = new Button("Close");
		btnClose.setOnAction(e -> window.close());
		Button btnSend = new Button("Send");
		btnSend.setOnAction(e -> {
			String chatMessage = inputField.getText();
			if (chatMessage != "") {
				GameInformation.getInstance().getClientmatch().sendChatMessage(chatMessage);
				inputField.setText("");
			}
		});
		VBox layout = new VBox(10);

		layout.setPadding(new Insets(10.0d));
		layout.setBackground(
				new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0), new CornerRadii(0), new Insets(0))));

		((VBox) layout)
				.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(0), new Insets(0))));

		btnClose.setMaxWidth(60);
		btnClose.setMinWidth(60);
		btnClose.setMaxHeight(50);
		btnClose.setMinHeight(30);
		btnSend.setMinWidth(60);
		btnSend.setMaxWidth(60);
		btnSend.setMaxHeight(50);
		btnSend.setMinHeight(30);
		btnClose.setStyle(".button {\n" + "    -fx-padding: 8 15 15 15;\n"
				+ "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" + "    -fx-background-radius: 8;\n"
				+ "    -fx-background-color:\n"
				+ "            linear-gradient(from 0% 93% to 0% 100%, #e09c1d 0%, #e09c1d 100%),\n"
				+ "            #7a7c7d,\n" + "            #9b9d9e,\n"
				+ "            radial-gradient(center 50% 50%, radius 100%, #e09c1d, #e09c1d);\n"
				+ "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n"
				+ "    -fx-font-weight: bold;\n" + "    -fx-font-size: 1.3em;\n" + "}\n" + ".button:hover {\n"
				+ "    -fx-background-color:\n"
				+ "            linear-gradient(from 0% 93% to 0% 100%, #d4be94 0%,#d4be94 100%),\n"
				+ "            #7a7c7d,\n" + "            #9b9d9e,\n"
				+ "            radial-gradient(center 50% 50%, radius 100%, #d4be94, #d4be94);\n" + "}\n"
				+ ".button:pressed {\n" + "    -fx-padding: 10 15 13 15;\n"
				+ "    -fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;\n" + "}\n" + ".button Text {\n"
				+ "    -fx-fill: white;\n" + "    -fx-effect: dropshadow( gaussian , #a30000 , 0,0,0,2 );\n" + "}");
		btnSend.setStyle(".button {\n" + "    -fx-padding: 8 15 15 15;\n"
				+ "    -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;\n" + "    -fx-background-radius: 8;\n"
				+ "    -fx-background-color:\n"
				+ "            linear-gradient(from 0% 93% to 0% 100%, #e09c1d 0%, #e09c1d 100%),\n"
				+ "            #7a7c7d,\n" + "            #9b9d9e,\n"
				+ "            radial-gradient(center 50% 50%, radius 100%, #e09c1d, #e09c1d);\n"
				+ "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );\n"
				+ "    -fx-font-weight: bold;\n" + "    -fx-font-size: 1.3em;\n" + "}\n" + ".button:hover {\n"
				+ "    -fx-background-color:\n"
				+ "            linear-gradient(from 0% 93% to 0% 100%, #d4be94 0%,#d4be94 100%),\n"
				+ "            #7a7c7d,\n" + "            #9b9d9e,\n"
				+ "            radial-gradient(center 50% 50%, radius 100%, #d4be94, #d4be94);\n" + "}\n"
				+ ".button:pressed {\n" + "    -fx-padding: 10 15 13 15;\n"
				+ "    -fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;\n" + "}\n" + ".button Text {\n"
				+ "    -fx-fill: white;\n" + "    -fx-effect: dropshadow( gaussian , #a30000 , 0,0,0,2 );\n" + "}");
		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					btnSend.fire();
				}

			}

		});
		layout.getChildren().addAll(textArea, inputField, btnSend, btnClose);
		layout.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
	}
	
	public void emptyTextArea() {
		this.textArea.clear();
	}

	public void fillTextArea(String from, String text) {
		this.textArea.appendText(from.substring(0,1).toUpperCase()+from.substring(1).toLowerCase() + ": " + text + "\n");
		System.out.println();
	}
	public String readOutTextArea() {
		return textArea.getText();
	}
	public void setTextArea() {
		this.textArea = new TextArea();
		 
	}

	public void display() {
		window.show();
	}
	public void close(){
		window.close();
	}

}
