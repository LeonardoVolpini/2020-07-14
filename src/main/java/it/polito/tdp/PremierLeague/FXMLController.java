/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.SquadraMigliore;
import it.polito.tdp.PremierLeague.model.SquadraPeggiore;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	this.txtResult.clear();
    	if (!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, creare prima il grafo");
    		return;
    	}
    	Team team = this.cmbSquadra.getValue();
    	if (team==null) {
    		this.txtResult.setText("Errore, selezionare una squadra");
    		return;
    	}
    	List<SquadraPeggiore> r1=model.SquadreBattute(team);
    	List<SquadraMigliore> r2=model.SquadreCheBattono(team);
    	this.txtResult.appendText("Squadre arrivate sotto "+team.toString()+":\n");
    	for (SquadraPeggiore t : r1) {
    		this.txtResult.appendText(t.getT().toString()+" - "+t.getPeso()+"\n");
    	}
    	this.txtResult.appendText("\n");
    	this.txtResult.appendText("Squadre arrivate sopra "+team.toString()+":\n");
    	for (SquadraMigliore t : r2) {
    		this.txtResult.appendText(t.getT().toString()+" - "+t.getPeso()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	model.CreaGrafo();
    	this.txtResult.setText("GRAFO CREATO \n");
    	this.txtResult.appendText("# Vertici: "+model.getNumVertici());
    	this.txtResult.appendText("\n # Archi: "+model.getNumArchi());
    	}

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	if(!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, crea prima il grafo !!");
    		return;
    	}
    	String nString= this.txtN.getText();
    	int n;
    	try {
    		n= Integer.parseInt(nString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Errore inserire un valore numerico di reporter");
    		return;
    	}
    	String xString= this.txtX.getText();
    	int x;
    	try {
    		x= Integer.parseInt(xString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Errore inserire un valore numerico della soglia dei reporter");
    		return;
    	}
    	this.model.simula(n, x);
    	this.txtResult.setText("In media c'erano "+model.reporterForMatch()+" per partita\n");
    	this.txtResult.appendText("E ci sono state "+model.matchUnderSogliaX()+" partite con meno reporter della soglia minima");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbSquadra.getItems().addAll(model.getAllTeams());
    }
}
