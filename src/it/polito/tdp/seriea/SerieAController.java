/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.PuntiClassifica;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<Team> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleCarica(ActionEvent event) {
    	Season season = boxSeason.getValue();
    	if (season == null) {
    		txtResult.setText("Per favore selezionare una stagione dalla tendina!");
    		return;
    	}
    	this.model.creaGrafo(season);
    	txtResult.setText("Grafo creato!\n");
    	txtResult.appendText("# Vertici : " + this.model.getNumVertici() + "\n");
    	txtResult.appendText("# Archi : " + this.model.getNumArchi() + "\n");
    	
    	List<PuntiClassifica> classifica = this.model.calcolaClassifica();
    	txtResult.appendText("La classifica per la stagione '" + season + "' è: \n");
    	for (PuntiClassifica p : classifica) {
    		txtResult.appendText(p.getTeam() + " - " + p.getPunti() + "\n");
    	}
    }

    @FXML
    void handleDomino(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	boxSeason.getItems().addAll(this.model.getAllSeasons());
    }
}
