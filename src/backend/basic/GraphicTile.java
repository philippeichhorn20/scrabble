package backend.basic;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GraphicTile extends Rectangle {
  private boolean highlighted = false;
  private Rectangle tile;
  private Text letter;

  public GraphicTile(Rectangle rec, Text letter){
    this.tile = rec;
    this.letter = letter;


  }
  public boolean isHighlighted(){
    return highlighted;
  }
  public void highlight(boolean b){
    highlighted = b;
  }

  public Text getLetter() {
    return letter;
  }

  public void setLetter(Text letter) {
    this.letter = letter;
  }
  public void setLetter(char c){
    Text let = new Text(String.valueOf(c));
    let.setFont(this.letter.getFont());
    this.letter = let;
  }
  public Rectangle getRec() {
    return tile;
  }

  public void setRec(Rectangle tile) {
    this.tile = tile;
  }
  public void setXY(double x,double y){
    tile.setLayoutX(x);
    letter.setLayoutX(x+13);
    tile.setLayoutY(y);
    letter.setLayoutY(y+25);

  }
  public void setVisiblee(boolean b){
    tile.setVisible(b);
    letter.setVisible(b);
  }
  public boolean isVisiblee(){
    return this.letter.isVisible() && this.tile.isVisible();
  }

  public String toString(){
    if (this != null) {

      return "Tile: " + this.tile.toString() + "Letter: " + this.letter.getText();
    }else{
      return "baba";
    }
  }
}
