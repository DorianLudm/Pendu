import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurCouleur implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private Pendu vuePendu;


    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurCouleur(Pendu vuePendu) {
        this.vuePendu = vuePendu;
    }

    /**
     * gère le changement de niveau
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent){
        RadioButton radiobouton = (RadioButton) actionEvent.getTarget();
        String colorString = radiobouton.getText();
        Color c = Color.web(colorString);
        this.vuePendu.setColor(c);
        this.vuePendu.majAffichage();
    }
}
