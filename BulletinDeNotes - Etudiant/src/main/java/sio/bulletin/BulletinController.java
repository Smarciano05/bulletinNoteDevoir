package sio.bulletin;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sio.bulletin.Model.Etudiant;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class BulletinController implements Initializable
{
    DecimalFormat df;
    @FXML
    private AnchorPane apBulletin;
    @FXML
    private ListView lvMatieres;
    @FXML
    private ListView lvDevoirs;
    @FXML
    private ComboBox cboTrimestres;
    @FXML
    private TextField txtNomEtudiant;
    @FXML
    private TextField txtNote;
    @FXML
    private Button btnValider;
    @FXML
    private AnchorPane apMoyenne;
    @FXML
    private Button btnMenuBulletin;
    @FXML
    private Button btnMenuMoyenne;
    @FXML
    private ListView lvMatieresMoyenne;
    @FXML
    private TreeView tvMoyennesParDevoirs;
    @FXML
    private TextField txtMajor;
    @FXML
    private TextField txtNoteMaxi;
    HashMap<String,HashMap<String, HashMap<String,ArrayList<Etudiant>>>> lesBulletins;
    //1ere hashmap : cle = nomMatiere valeur = hashmap
    // 2e sous hashmap : cle = nomDevoir valeur = hashmap
    //3e sous sous hashmap : cle = trimestre valeur = arraylist Etudiant

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apBulletin.toFront();
        df = new DecimalFormat("#.##");
        lesBulletins = new HashMap<>();
        lvMatieres.getItems().addAll("Maths","Anglais","Economie");
        lvDevoirs.getItems().addAll("Devoir n°1","Devoir n°2","Devoir n°3","Devoir n°4");
        cboTrimestres.getItems().addAll("Trim 1","Trim 2","Trim 3");
        cboTrimestres.getSelectionModel().selectFirst();


    }

    @FXML
    public void btnMenuClicked(Event event)
    {
        if(event.getSource()==btnMenuBulletin)
        {
            apBulletin.toFront();
        }
        else if(event.getSource()==btnMenuMoyenne)
        {
            apMoyenne.toFront();

            // A vous de jouer
            //on affiche uniquement les matières pour lesquelles des devoirs ont été réalisés
            if(lesBulletins.containsKey(lvMatieres.getSelectionModel().getSelectedItem().toString()))
            {
                TreeItem root = new TreeItem<>();
                for (String nomMatiere : lesBulletins.keySet())
                {
                    TreeItem noeudMatiere = new TreeItem<>(lvMatieres.getSelectionModel().getSelectedItems());
                    noeudMatiere.getChildren().add(root);
                }
                lvMatieres.setItems(root.getChildren());
            }


        }
    }

    @FXML
    public void btnValiderClicked(Event event)
    {
        if(lvMatieres.getSelectionModel().getSelectedItems().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText("selectionner une matiere");
            alert.showAndWait();
        } else if (lvDevoirs.getSelectionModel().getSelectedItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText("selectionner un devoir ");
            alert.showAndWait();
        } else if (txtNomEtudiant.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText("saisir un etudiant");
            alert.showAndWait();
        } else if (txtNote.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText("saisir une note");
            alert.showAndWait();
        }
        else {
            //Tout est ok
            int note = 0;
            Etudiant etudiant = new Etudiant(txtNomEtudiant.getText(), note);

            //verification de la presence du nom de la matiere dans la hashmap
            if (!lesBulletins.containsKey(lvMatieres.getSelectionModel().getSelectedItems().toString())) {
                //si le nom de la matiere pas present , creer la matiere
                ArrayList<Etudiant> lesEtudiant = new ArrayList<>();
                lesEtudiant.add(etudiant);
                HashMap<String, ArrayList<Etudiant>> leTrimestre = new HashMap<>();
                HashMap<String, HashMap<String, ArrayList<Etudiant>>> lesDevoirs = new HashMap<>();
                leTrimestre.put(cboTrimestres.getSelectionModel().getSelectedItem().toString(), lesEtudiant);
                lesDevoirs.put(lvDevoirs.getSelectionModel().getSelectedItem().toString(), leTrimestre);
                lesBulletins.put(lvMatieres.getSelectionModel().getSelectedItem().toString(), lesDevoirs);

            } else if (!lesBulletins.get(lvMatieres.getSelectionModel().getSelectedItem().toString()).containsKey(lvDevoirs.getSelectionModel().getSelectedItem().toString())) {
                //si le nom de la matiere est presente mais le devoir n'est pas = creer devoir
                HashMap<String, HashMap<String, ArrayList<Etudiant>>> lesDevoirs = new HashMap<>();
                ArrayList<Etudiant> lesEtudiant = new ArrayList<>();
                lesEtudiant.add(etudiant);
                HashMap<String, ArrayList<Etudiant>> leTrimestre = new HashMap<>();
                lesBulletins.get(lvMatieres.getSelectionModel().getSelectedItem().toString()).put(lvDevoirs.getSelectionModel().getSelectedItem().toString(), leTrimestre);
            } else if (!lesBulletins.get(lvMatieres.getSelectionModel().getSelectedItem().toString()).get(lvDevoirs.getSelectionModel().getSelectedItem().toString()).containsKey(cboTrimestres.getSelectionModel().getSelectedItem().toString())) {
                //si la tout existe mais le trimestre n'est pas creer
                ArrayList<Etudiant> lesEtudiant = new ArrayList<>();
                lesEtudiant.add(etudiant);
                lesBulletins.get(lvMatieres.getSelectionModel().getSelectedItem().toString()).get(lvDevoirs.getSelectionModel().getSelectedItem().toString()).put(cboTrimestres.getSelectionModel().getSelectedItem().toString(), lesEtudiant);
            } else {
                lesBulletins.get(lvMatieres.getSelectionModel().getSelectedItem().toString()).get(lvDevoirs.getSelectionModel().getSelectedItem().toString()).get(cboTrimestres.getSelectionModel().getSelectedItem().toString());
            }
            btnValider.setText("NoteEnregistrer");
        }
       // int bidon = 12;


    }



    @FXML
    public void lvMatieresMoyenneClicked(Event event)
    {

        int numDev = 1;
        int totalDevoir =
        int moyenne =  txtNote/totalDev;

        for(int i ; i<les)
        {
             moyenne =
        }
        // A vous de jouer
        TreeItem root = new TreeItem<>();
        for(String nomMatiere : lesBulletins.keySet())
        {
            TreeItem noeudMatiere = new TreeItem<>(nomMatiere);
            noeudMatiere.setExpanded(true);
            for(String devoir : lesBulletins.get(nomMatiere).keySet())
            {
                TreeItem noeudDevoir =new TreeItem<>(devoir);
                for(Etudiant e : lesBulletins.get()
                {
                    TreeItem noeudNote = new TreeItem<>("Devoir n°" + numDev + ":" + moyenne );
                    noeudDevoir.getChildren().add(noeudNote);
                    noeudMatiere.getChildren().add(noeudDevoir);
                }


            }
            root.getChildren().add(noeudMatiere);
        }
        root.setExpanded(true);




        //en selectionnant une matiere on a les devoirs afficher , note maxi  et major
        TreeItem matiereSelectionner = (TreeItem) tvMoyennesParDevoirs.getSelectionModel().getSelectedItem();

        //si matiere selectionner afficher les devoirs / major et notemaxi
        /*
        if(matiereSelectionner != null)
        {
            for (Etudiant e : lesBulletins.get(matiereSelectionner.getParent().getValue().toString()).stream(
        }

         */

    }
}