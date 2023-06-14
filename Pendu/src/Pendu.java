import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.Set;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private Pane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    
    /**
     * le bouton qui permet de retourner au menu d'acceuil
     */ 
    private Button boutonMaison;

    /**
     * le bouton qui permet de lancer ou relancer une partie
     */ 
    private Button bJouer;
    private boolean jeuxInProgress = false;
    private BorderPane fenetre;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pBar;
    private Color color;


    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        this.modelePendu.setnbEerreursMax(10);
        this.leNiveau = new Text("FACILE");
        this.fenetre = new BorderPane();

        ImageView homeImg = new ImageView("home.png");
        ImageView settingsImg = new ImageView("parametres.png");
        homeImg.setFitHeight(40);
        homeImg.setFitWidth(40);
        settingsImg.setFitHeight(40);
        settingsImg.setFitWidth(40);
        this.bJouer = new Button("Lancer une partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        this.boutonMaison = new Button("", homeImg);
        this.boutonMaison.setOnAction(new RetourAccueil(this.modelePendu, this));
        this.boutonParametres = new Button("", settingsImg);
        this.boutonParametres.setOnAction(new ControleurParam(this));
        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(this.modelePendu, this));
        this.motCrypte = new Text();
        this.dessin = new ImageView(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
        this.pBar = new ProgressBar();
        this.chrono = new Chronometre();
        this.color = Color.LIGHTBLUE;

        this.fenetre.setTop(this.titre());
        this.panelCentral = new Pane();
        this.modeAccueil();
        // A terminer d'implementer
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        return new Scene(this.fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private BorderPane titre(){        
        BorderPane banniere = new BorderPane();
        banniere.setBackground(new Background(new BackgroundFill(this.color, null, new Insets(0))));
        Label nomJeu = new Label("Jeu du Pendu");
        nomJeu.setFont(Font.font("Arial",FontWeight.BOLD,40));
        nomJeu.setPadding(new Insets(15));
        banniere.setLeft(nomJeu);
        ImageView infoImg = new ImageView("info.png");
        infoImg.setFitHeight(40);
        infoImg.setFitWidth(40);
        Button info = new Button("", infoImg);
        info.setOnAction(new ControleurInfos(this));
        HBox boxBoutons = new HBox();
        boxBoutons.getChildren().addAll(this.boutonMaison,this.boutonParametres, info);
        boxBoutons.setPadding(new Insets(15));
        banniere.setRight(boxBoutons);
        return banniere;
    }

    // /**
     // * @return le panel du chronomètre
     // */
    private TitledPane leChrono(){
        TitledPane res = new TitledPane("Chronomètre", this.chrono);
        res.setCollapsible(false);
        this.chrono.start();
        return res;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     */
    private VBox fenetreAccueil(){
        VBox res = new VBox();
        this.bJouer.setText("Lancer une partie");
        ToggleGroup difficulte = new ToggleGroup();
        RadioButton button1 = new RadioButton("Facile");
        button1.setToggleGroup(difficulte);
        button1.setSelected(true);
        button1.setOnAction(new ControleurNiveau(this.modelePendu));
        RadioButton button2 = new RadioButton("Medium");
        button2.setToggleGroup(difficulte);
        button2.setOnAction(new ControleurNiveau(this.modelePendu));
        RadioButton button3 = new RadioButton("Difficile");
        button3.setToggleGroup(difficulte);
        button3.setOnAction(new ControleurNiveau(this.modelePendu));
        RadioButton button4 = new RadioButton("Expert");
        button4.setToggleGroup(difficulte);
        button4.setOnAction(new ControleurNiveau(this.modelePendu));
        VBox box2 = new VBox();
        TitledPane box = new TitledPane("Choisissez un niveau de difficulté", box2);
        box.setCollapsible(false);
        box2.getChildren().addAll(button1,button2,button3,button4);
        res.getChildren().addAll(this.bJouer,box);
        VBox.setMargin(box, new Insets(15));
        VBox.setMargin(this.bJouer, new Insets(15));
        return res;
    }

    private BorderPane fenetreJeu(){
        BorderPane res = new BorderPane();
        //VBox centrale
        VBox mid = new VBox();
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin = new ImageView(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
        double progress = this.modelePendu.getProgressBar();
        this.pBar.setProgress(progress);
        mid.getChildren().addAll(this.motCrypte, this.dessin, this.pBar, this.clavier);
        this.clavier.setMaxWidth(380);

        mid.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(this.motCrypte, new Insets(5));
        VBox.setMargin(this.dessin, new Insets(5));
        VBox.setMargin(this.pBar, new Insets(5));
        VBox.setMargin(this.clavier, new Insets(5));


        //VBox droite
        VBox right = new VBox();
        this.bJouer.setText("Recommencer!");
        this.leNiveau = new Text(this.modelePendu.getDifficultéToString());
        right.getChildren().addAll(this.leNiveau, this.leChrono(), this.bJouer);

        right.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(this.leNiveau, new Insets(10));
        VBox.setMargin(this.leChrono(), new Insets(10));
        VBox.setMargin(this.bJouer, new Insets(10));
        BorderPane.setMargin(right, new Insets(5,15,5,15));
        //Ajout à la boite résultat
        res.setCenter(mid);
        res.setRight(right);
        return res;
    }

    private VBox fenetreParam(){
        VBox res = new VBox();
        ToggleGroup couleur = new ToggleGroup();
        RadioButton button1 = new RadioButton("LIGHTBLUE");
        button1.setToggleGroup(couleur);
        button1.setSelected(true);
        button1.setOnAction(new ControleurCouleur(this));
        RadioButton button2 = new RadioButton("LIGHTGREEN");
        button2.setToggleGroup(couleur);
        button2.setOnAction(new ControleurCouleur(this));
        RadioButton button3 = new RadioButton("ORANGERED");
        button3.setToggleGroup(couleur);
        button3.setOnAction(new ControleurCouleur(this));
        RadioButton button4 = new RadioButton("DIMGRAY");
        button4.setToggleGroup(couleur);
        button4.setOnAction(new ControleurCouleur(this));
        VBox box2 = new VBox();
        TitledPane box = new TitledPane("Choisissez une couleur pour la bannière du jeu", box2);
        box.setCollapsible(false);
        box2.getChildren().addAll(button1,button2,button3,button4);
        res.getChildren().addAll(box);
        VBox.setMargin(box, new Insets(15));
        VBox.setMargin(this.bJouer, new Insets(15));
        return res;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil(){
        this.jeuxInProgress = false;
        this.panelCentral = this.fenetreAccueil();
        this.boutonMaison.setDisable(true);
        this.boutonParametres.setDisable(false);
        this.majAffichage();
    }
    
    public void modeJeu(){
        this.panelCentral = this.fenetreJeu();
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);
        this.majAffichage();
    }
    
    public void modeParametres(){
        this.panelCentral = this.fenetreParam();
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);
        this.majAffichage();
    }

    /** lance une partie */
    public void lancePartie(){
        this.clavier.resetTouches();
        this.jeuxInProgress = true;
        this.modelePendu.setMotATrouver();
        this.dessin = new ImageView(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
        System.out.println(this.modelePendu.getMotATrouve());
        System.out.println(this.modelePendu.getMotCrypte());
        this.modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        // this.chrono = this.leChrono();
        this.clavier.desactiveTouches(this.modelePendu.getLettresEssayees());
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
        this.pBar.setProgress(this.modelePendu.getProgressBar());
        this.fenetre.setCenter(this.panelCentral);
        this.fenetre.setTop(this.titre());
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\nEtes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Un mot caché doit être retrouver, pour cela appuyer sur les lettres pour faire des essais!\nSi vous choisissez une lettre qui n'est pas dans le mot, un bout du personnage apparait!\nSi le personnage entier est pendu, c'est PERDU!\nSi tout le mot est trouvé, c'est GAGNER!");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vous avez gagner!\nVoulez vous relancer une partie ?", ButtonType.YES, ButtonType.NO);        
        return alert;
    }
    
    public Alert popUpMessagePerdu(){   
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Vous avez perdu!\nVoulez vous relancer une partie ?", ButtonType.YES, ButtonType.NO);
        return alert;
    }

    public boolean getEnCours(){
        return this.jeuxInProgress;
    }

    public void gameOff(){
        this.jeuxInProgress = false;
    }

    public void setColor(Color color){
        this.color = color;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}