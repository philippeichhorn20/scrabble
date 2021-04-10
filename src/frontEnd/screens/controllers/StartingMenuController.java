package frontEnd.screens.controllers;

import frontEnd.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartingMenuController {

    // @author mkolinsk

    @FXML
    private Button newProfileButton;
    @FXML
    private Button existProfileButton;

    public void createNew(ActionEvent e) throws IOException {
        newProfile();

    }

    private void newProfile() throws IOException {

        Main m = new Main();
        m.changeScene("screens/gameScreen.fxml");
    }

    public void logInto(ActionEvent e) throws IOException {
        logIn();
    }

    private void logIn() throws IOException {
        Main m = new Main();
        m.changeScene("screens/existingProfile.fxml");

    }
}
