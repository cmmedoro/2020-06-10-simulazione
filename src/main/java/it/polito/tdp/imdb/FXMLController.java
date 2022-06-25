/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
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

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	this.txtResult.clear();
    	//controllo sugli input
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Devi prima creare il grafico!");
    		return;
    	}
    	Actor a = this.boxAttore.getValue();
    	if(a == null) {
    		this.txtResult.setText("Devi selezionare un attore dall'apposita tendina");
    		return;
    	}
    	//se sono qui, tutto ok, posso proseguire.
    	this.txtResult.setText("ATTORI SIMILI a: "+a.getFirstName()+" "+a.getLastName()+"\n");
    	if(this.model.getSimili(a).size() == 0) {
    		this.txtResult.setText("Non ce ne sono: vertice isolato\n");
    	}else {
    		for(Actor aa : this.model.getSimili(a)) {
    			this.txtResult.appendText(aa.toString()+"\n");
    		}
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	//controllo input
    	String g = this.boxGenere.getValue();
    	if(g == null) {
    		this.txtResult.setText("Devi selezionare un genere dall'apposita tendina");
    		return;
    	}
    	//se sono qui posso proseguire con la creazione del grafo
    	this.model.creaGrafo(g);
    	this.txtResult.setText("Grafo creato\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    	//popolo la tendina degli attori con tutti quelli che sono nel grafo come vertici
    	this.boxAttore.getItems().clear();
    	for(Actor a : this.model.getVertices()) {
    		this.boxAttore.getItems().add(a);
    	}
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	this.txtResult.clear();
    	//controllo input
    	if(!this.model.isGraphCreated()) {
    		this.txtResult.setText("Devi prima creare il grafico!");
    		return;
    	}
    	int n;
    	try {
    		n = Integer.parseInt(this.txtGiorni.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un valore numerico intero");
    		return;
    	}
    	//se sono qui posso proseguire con la simulazione
    	this.model.simulazione(n);
    	this.txtResult.setText("Attori intervistati: "+this.model.intereviews().size()+"\n");
    	for(Actor a : this.model.intereviews()) {
    		this.txtResult.appendText(""+a+"\n");
    	}
    	this.txtResult.appendText("Numero giorni di pausa: "+this.model.pauses()+"\n");

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().clear();
    	this.boxGenere.getItems().addAll(this.model.getGeneri());
    }
}
