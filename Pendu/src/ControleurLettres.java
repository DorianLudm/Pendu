import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu){
        this.vuePendu = vuePendu;
        this.modelePendu = modelePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la partie est finie
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getTarget();
        String lettre = source.getText();
        if(this.modelePendu.essaiLettre(lettre.charAt(0)) == 0){
            this.vuePendu.majAffichage();
            if(this.modelePendu.perdu()){
                Optional<ButtonType> reponse = this.vuePendu.popUpMessagePerdu().showAndWait(); // on lance la fenêtre popup et on attends la réponse
                // si la réponse est oui
                if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)){
                    this.vuePendu.lancePartie();
                }
                else{
                    this.vuePendu.modeAccueil();
                }
            }
        }
        else{
            this.vuePendu.majAffichage();
            if(this.modelePendu.gagne()){
                Optional<ButtonType> reponse = this.vuePendu.popUpMessageGagne().showAndWait(); // on lance la fenêtre popup et on attends la réponse
                // si la réponse est oui
                if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)){
                    this.vuePendu.lancePartie();
                }
                else{
                    this.vuePendu.modeAccueil();
                }
            }
        }
        this.vuePendu.majAffichage();
    }
}
