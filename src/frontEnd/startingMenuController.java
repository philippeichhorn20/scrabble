import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class startingMenuController {

    // @author mkolinsk

    @FXML
    private Button newProfileButton;
    @FXML
    private Button existProfileButton;

    public void createNew(ActionEvent e) throws IOException{
        newProfile();

    }
    private void newProfile() throws IOException{

        Main m = new Main();
        m.changeScene("resources/createProfile.fxml");
    }
    public void logInto(ActionEvent e) throws IOException{
        logIn();
    }
    private void logIn() throws IOException{
        Main m = new Main();
        m.changeScene("resources/existingProfile.fxml");

    }
}
