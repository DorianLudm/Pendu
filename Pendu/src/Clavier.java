import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle ;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane{
    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     * @param tailleLigne nombre de touches par ligne
     */
    public Clavier(String touches, EventHandler<ActionEvent> actionTouches){
        super();
        clavier = new ArrayList<>();
        for(int i = 0; i < touches.length(); i++){
            Button leboutton = new Button("" + touches.charAt(i));
            leboutton.setOnAction(actionTouches);
            leboutton.setPadding(new Insets(5,12,5,12));
            clavier.add(leboutton);
            this.getChildren().add(leboutton);
        }
        this.setHgap(3);
        this.setVgap(5);
        this.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(Set<String> touchesDesactivees){
        for(Button buttonElem: clavier){
            for(String elem: touchesDesactivees){
                if(!elem.equals(buttonElem.getText())){
                    buttonElem.setDisable(false);
                }
                else{
                    buttonElem.setDisable(true);
                    break;
                }
            }
        }
    }

    public void resetTouches(){
        for(Button buttonElem: clavier){
            buttonElem.setDisable(false);
        }
    }
}